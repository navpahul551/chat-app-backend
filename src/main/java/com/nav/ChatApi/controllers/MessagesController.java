package com.nav.ChatApi.controllers;

import com.nav.ChatApi.entities.Message;
import com.nav.ChatApi.exceptions.NotFoundException;
import com.nav.ChatApi.exceptions.UnauthorizedException;
import com.nav.ChatApi.models.MessageModel;
import com.nav.ChatApi.repositories.MessageRepository;
import com.nav.ChatApi.services.MessageService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("groups/{groupId}/messages")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageModel newMessage){
        try{
            System.out.println("********** message content: " + newMessage.getContent() + " *******************");

            var sender = SecurityContextHolder.getContext().getAuthentication();
            newMessage.setSenderEmail((String) sender.getPrincipal());
            messageService.sendMessage(newMessage);
            return ResponseEntity.ok("Message sent successfully!");
        }
        catch(NotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(UnauthorizedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>("Unable to send message to the group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> get(@PathVariable(name="groupId") Long groupId){
        try{
            return ResponseEntity.ok(messageService.findAllByGroupId(groupId));
//            return ResponseEntity.ok("not found!");
        }
        catch(Exception ex){
            return new ResponseEntity<>("Unable to fetch messages!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
