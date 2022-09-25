package com.nav.ChatApi.controllers;

import com.nav.ChatApi.exceptions.AlreadyExistsException;
import com.nav.ChatApi.exceptions.BadRequestException;
import com.nav.ChatApi.exceptions.NotFoundException;
import com.nav.ChatApi.exceptions.UnauthorizedException;
import com.nav.ChatApi.models.GroupModel;
import com.nav.ChatApi.models.LoginRequestModel;
import com.nav.ChatApi.models.SignUpModel;
import com.nav.ChatApi.repositories.GroupRepository;
import com.nav.ChatApi.repositories.UserRepository;
import com.nav.ChatApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestModel loginDetails) throws BadRequestException {
        try{
            var token = userService.loginUser(loginDetails);
            return ResponseEntity.ok(token);
        }
        catch (Exception ex){
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpModel user){
        try{
            return ResponseEntity.ok(userService.signUpUser(user));
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/groups")
    public ResponseEntity<?> getAllGroups(@PathVariable Long id){
        try{
            return ResponseEntity.ok(userService.getAllGroups(id));
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/group-invites")
    public ResponseEntity<?> getAllGroupInvites(@PathVariable Long id){
        try{
            return ResponseEntity.ok(userService.getAllGroupInvites(id));
        }
        catch (NotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(UnauthorizedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("{id}/group-invites/{inviteId}/accept")
    public ResponseEntity<?> acceptGroupInvite(@PathVariable Long id, @PathVariable Long inviteId){
        try{
            userService.acceptGroupInvite(id, inviteId);
            return ResponseEntity.ok("User added to group successfully!");
        }
        catch(NotFoundException | BadRequestException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (UnauthorizedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("{id}/group-invites/{inviteId}/decline")
    public ResponseEntity<?> declineGroupInvite(@PathVariable Long id, @PathVariable Long inviteId){
        try{
            userService.declineGroupInvite(id, inviteId);
            return ResponseEntity.ok("User added to group successfully!");
        }
        catch(NotFoundException | BadRequestException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (UnauthorizedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}/group-invites/{inviteId}")
    public ResponseEntity<?> deleteGroupInvite(@PathVariable Long inviteId){
        try{
            userService.deleteGroupInvite(inviteId);
            return ResponseEntity.ok("Group invitation deleted successfully!");
        }
        catch(NotFoundException | BadRequestException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (UnauthorizedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}/groups/{groupId}/exit")
    public ResponseEntity<?> exitGroup(@PathVariable Long groupId){
        try{
            userService.deleteUserFromGroup(groupId);
            return ResponseEntity.ok("Group exited successfully!");
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
