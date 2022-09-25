package com.nav.ChatApi.services;

import com.nav.ChatApi.config.JwtTokenProvider;
import com.nav.ChatApi.entities.Group;
import com.nav.ChatApi.entities.GroupInvite;
import com.nav.ChatApi.entities.Role;
import com.nav.ChatApi.entities.User;
import com.nav.ChatApi.exceptions.BadRequestException;
import com.nav.ChatApi.exceptions.NotFoundException;
import com.nav.ChatApi.exceptions.UnauthorizedException;
import com.nav.ChatApi.exceptions.UserNotFoundException;
import com.nav.ChatApi.models.*;
import com.nav.ChatApi.repositories.GroupInviteRepository;
import com.nav.ChatApi.repositories.GroupRepository;
import com.nav.ChatApi.repositories.RoleRepository;
import com.nav.ChatApi.repositories.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JwtTokenProvider  jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupInviteRepository groupInviteRepository;

    public Authentication authenticateUser(String jwtToken) throws UserNotFoundException, UnauthorizedException {
        Long userId = jwtTokenProvider.getUserIdFromJwtToken(jwtToken);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new UnauthorizedException("User account is disabled!");
        } else if (!user.isAccountNonLocked()) {
            throw new UnauthorizedException("User account is locked!");
        } else if (!user.isAccountNonExpired()) {
            throw new UnauthorizedException("User account is expired!");
        } else if (!user.isCredentialsNonExpired()) {
            throw new UnauthorizedException("User account credentials are expired!");
        }

        return setUserPrincipal(user);
    }

    public Token loginUser(LoginRequestModel loginDetails) throws BadRequestException {
        String email = loginDetails.getEmail();
        String password = loginDetails.getPassword();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new BadRequestException("Invalid username or email!");
        }
        else if(!passwordEncoder.matches(password, optionalUser.get().getPassword())){
            throw new BadRequestException("Invalid password!");
        }

        Authentication authentication = setUserPrincipal(optionalUser.get());
        Token token = new Token(jwtTokenProvider.generateJwtToken(authentication), optionalUser.get());
        LOGGER.info("Token generated successfully!");
        return token;
    }

    public User signUpUser(SignUpModel user) throws NotFoundException, BadRequestException {
        var optionalUser = userRepository.findByEmail(user.getEmail());

        if(optionalUser.isPresent())
            throw new BadRequestException("User with email " + user.getEmail() + " already exists");

        var userRole = roleRepository.findByName(Role.Rolename.ROLE_USER).orElseThrow(NotFoundException::new);

        var newUser = new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), user.getFirstName(),
                user.getLastName(), Collections.singletonList(userRole));

        return userRepository.save(newUser);
    }

    public List<Group> getAllGroups(Long userId) throws NotFoundException {
        System.out.println("fetching user groups******************************");
        var user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        List<Group> groups = new ArrayList<>();

        for(var group : user.getGroups()){
            System.out.println("Group name: " + group.getName());
            groups.add(new Group(group.getId(), group.getName(), null));
        }

        return groups;
    }

    public User getCurrentUser() throws  NotFoundException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return  userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(() ->
                new NotFoundException("Logged in user not found!"));
    }

    public List<GroupInvite> getAllGroupInvites(Long userId) throws NotFoundException, UnauthorizedException {
        var currentUser = getCurrentUser();

        if(!Objects.equals(currentUser.getId(), userId)) throw new UnauthorizedException("Unauthorized access!");

        var groupInvites = groupInviteRepository.findBySenderOrReceiverEmail(currentUser, currentUser.getEmail());
        var result = new ArrayList<GroupInvite>();

        for(var groupInvite : groupInvites){
            var group = new Group(groupInvite.getGroup().getId(), groupInvite.getGroup().getName(), null);
            var sender = new User(groupInvite.getSender().getEmail(), null, groupInvite.getSender().getFirstName(), groupInvite.getSender().getLastName(), null);
            result.add(new GroupInvite(groupInvite.getId(), group, sender, groupInvite.getReceiverEmail(), groupInvite.isActive(), groupInvite.isAccepted()));
        }

        return result;
    }

    public void acceptGroupInvite(Long userId, Long inviteId) throws NotFoundException, UnauthorizedException, BadRequestException {
        var currentUser = getCurrentUser();
        var groupInvite = groupInviteRepository.findById(inviteId).orElseThrow(() -> new NotFoundException("Group invite not found!"));

        if(!Objects.equals(currentUser.getId(), userId)) throw new UnauthorizedException("Unauthorized access!");
        if(!groupInvite.isActive()) throw new BadRequestException("The group invite was declined!");

        var group = groupInvite.getGroup();

        if(group.getUsers().contains(currentUser)) throw new BadRequestException("The user was already added to the group!");

        group.getUsers().add(currentUser);
        groupRepository.save(group);
        groupInvite.setActive(false);
        groupInvite.setAccepted(true);
        groupInviteRepository.save(groupInvite);
        System.out.println("User successfully added to the group!");
    }

    public void declineGroupInvite(Long userId, Long inviteId) throws NotFoundException, UnauthorizedException, BadRequestException {
        var currentUser = getCurrentUser();
        var groupInvite = groupInviteRepository.findById(inviteId).orElseThrow(() -> new NotFoundException("Group invite not found!"));

        if(!Objects.equals(currentUser.getId(), userId)) throw new UnauthorizedException("Unauthorized access!");
        if(!groupInvite.isActive()) throw new BadRequestException("The invite was already declined!");

        groupInvite.setActive(false);
        groupInviteRepository.save(groupInvite);
        System.out.println("Group invitation declined successfully!");
    }

    public void deleteGroupInvite(Long inviteId) throws NotFoundException, UnauthorizedException, BadRequestException {
        var currentUser = getCurrentUser();
        var groupInvite = groupInviteRepository.findById(inviteId).orElseThrow(() -> new NotFoundException("Group invite not found!"));

        if(!Objects.equals(groupInvite.getSender().getId(), currentUser.getId())) throw new UnauthorizedException();
        if(groupInvite.isAccepted()) throw new BadRequestException("The invitation has already been  accepted!");

        groupInviteRepository.delete(groupInvite);
        System.out.println("Group invite deleted successfully!");
    }

    public void deleteUserFromGroup(Long groupId) throws NotFoundException, BadRequestException {
        var currentUser = getCurrentUser();
        var group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found!"));

        if(!group.getUsers().contains(currentUser)) throw new BadRequestException("User not found in the group!");

        if(group.getUsers().size() == 1) groupRepository.delete(group);
        else {
            group.getUsers().remove(currentUser);
            groupRepository.save(group);
        }

        System.out.println(("User deleted from group successfully!"));
    }

    private Authentication setUserPrincipal(User user){
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal.getUsername(),
                userPrincipal.getPassword(),
                userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
