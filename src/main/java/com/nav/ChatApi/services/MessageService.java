package com.nav.ChatApi.services;

import com.nav.ChatApi.entities.Message;
import com.nav.ChatApi.entities.User;
import com.nav.ChatApi.exceptions.NotFoundException;
import com.nav.ChatApi.exceptions.UnauthorizedException;
import com.nav.ChatApi.models.MessageModel;
import com.nav.ChatApi.repositories.GroupRepository;
import com.nav.ChatApi.repositories.MessageRepository;
import com.nav.ChatApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    public void sendMessage(MessageModel message) throws NotFoundException, UnauthorizedException {
        System.out.println("sender email: " + message.getSenderEmail());

        var sender = userRepository.findByEmail(message.getSenderEmail())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        System.out.println("Group id: " + message.getGroupId());

        var group = groupRepository.findById(message.getGroupId())
                .orElseThrow(() -> new NotFoundException("Group not found!"));

        // check if the sender is inside the group
        if(!group.getUsers().contains(sender)){
            throw new UnauthorizedException("User is unauthorized to send messages to this group!");
        }

        var newMessage = new Message();
        newMessage.setContent(message.getContent());
        newMessage.setUser(sender);
        newMessage.setGroup(group);
        messageRepository.save(newMessage);
    }

    public List<Message> findAllByGroupId(Long groupId) throws NotFoundException {
        var group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found!"));

        List<Message> messages = new ArrayList<>();

        for(var message : group.getMessages()){
            var user = new User(message.getUser().getEmail(), null, message.getUser().getFirstName(), message.getUser().getLastName(), null);
            messages.add(new Message(message.getId(), message.getContent(), user, null));
        }

        return messages;
    }
}
