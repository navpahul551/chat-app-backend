package com.nav.ChatApi.services;

import com.nav.ChatApi.entities.Group;
import com.nav.ChatApi.entities.GroupInvite;
import com.nav.ChatApi.exceptions.AlreadyExistsException;
import com.nav.ChatApi.exceptions.BadRequestException;
import com.nav.ChatApi.exceptions.NotFoundException;
import com.nav.ChatApi.exceptions.UnauthorizedException;
import com.nav.ChatApi.models.EmailDetails;
import com.nav.ChatApi.models.GroupModel;
import com.nav.ChatApi.repositories.GroupInviteRepository;
import com.nav.ChatApi.repositories.GroupRepository;
import com.nav.ChatApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupInviteRepository groupInviteRepository;

    public Group addGroup(GroupModel group) throws NotFoundException {
        var user = userService.getCurrentUser();
        var newGroup = new Group(group.getGroupName(), Collections.singletonList(user));
        return groupRepository.save(newGroup);
    }

    public void sendInvite(Long groupId, String recipient) throws NotFoundException, UnauthorizedException, AlreadyExistsException, BadRequestException {
        System.out.println("Recipient email: " + recipient);

        // checking if the user is present in the group
        var group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found!"));
        var currentUser = userService.getCurrentUser();

        if(!group.getUsers().contains(currentUser))
            throw new UnauthorizedException("You are not authorized to send messages in this group");

        // checking if the invitation has already been sent
        var optionalGroupInvite = groupInviteRepository.findByGroupAndSenderAndReceiverEmailAndActive(group, currentUser, recipient, true);

        if(optionalGroupInvite.isPresent() && optionalGroupInvite.get().isActive())
            throw new AlreadyExistsException("The invitation has already been sent!");

        var receiver = userRepository.findByEmail(recipient);

        // sending email for registration to the receiver they are not registered
        if(receiver.isEmpty()){
            String messageBody = currentUser.getFirstName() + " wants you to join their group '"+group.getName()+"'. Please register on our app at http://localhost:3000/sign-up if you wish to do so.";
            emailService.sendSimpleMail(new EmailDetails(recipient, messageBody, currentUser.getFirstName() + " has sent " +
                "you an invitation to join the "+ group.getName() +" group!", null));
        }
        else if(group.getUsers().contains(receiver))
            throw new BadRequestException("User already added to the group!");

        // saving group invite
        var newGroupInvite = new GroupInvite(group, currentUser, recipient, true, false);
        groupInviteRepository.save(newGroupInvite);
        System.out.println("Invitation sent successfully!");
    }
}
