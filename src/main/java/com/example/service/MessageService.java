package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
//holds the code that handles the business logic
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> retrieveAllMessages(){
        return messageRepository.findAll();
    }

    public Message retrieveMessageById(int id){
        if(!messageRepository.findByMessageId(id).isEmpty()){
            return messageRepository.findByMessageId(id).get();
        }
        else{
            return null;
        }     
    }

    public int deleteMessage(int id){
        Message message = retrieveMessageById(id);
        if(message != null){
            messageRepository.delete(message);
            return 1;
        }
        else{
            return 0;
        }
    }

    public int updateMessage(int id, String text){
        Message message = retrieveMessageById(id);
        if(message != null && text.length() <= 255){
            message.setMessageText(text);
            messageRepository.save(message);
            return 1;
        }
        else {
            return 0;
        }
    }

    public List<Message> getMessagesByAccountId(int id){
        List<Message> messages = new ArrayList<>();
        messages = messageRepository.findByPostedBy(id);
        return messages;
    }
}
