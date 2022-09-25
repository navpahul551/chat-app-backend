package com.nav.ChatApi.controllers;

import com.nav.ChatApi.exceptions.AlreadyExistsException;
import com.nav.ChatApi.exceptions.BadRequestException;
import com.nav.ChatApi.exceptions.NotFoundException;
import com.nav.ChatApi.exceptions.UnauthorizedException;
import com.nav.ChatApi.models.EmailDetails;
import com.nav.ChatApi.models.GroupModel;
import com.nav.ChatApi.services.EmailService;
import com.nav.ChatApi.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("groups")
public class GroupsController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GroupModel newGroup){
        try{
            groupService.addGroup(newGroup);
            return ResponseEntity.ok("Group created successfully!");
        }
        catch(NotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{id}/send-invite")
    public ResponseEntity<?> sendInvite(@PathVariable(name="id") Long groupId, @RequestBody EmailDetails recipient){
        try{
            groupService.sendInvite(groupId, recipient.getRecipient());
            return ResponseEntity.ok("Invite sent successfully!");
        }
        catch(NotFoundException | AlreadyExistsException | BadRequestException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (UnauthorizedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            System.out.println("Email exception. Error: " + ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing your request!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
