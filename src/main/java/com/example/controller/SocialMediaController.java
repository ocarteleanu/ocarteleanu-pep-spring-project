package com.example.controller;

import java.util.List;

import java.util.ArrayList;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
//Sets up the endpoints for users to be able to access the API
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping(value = "/register")
    private ResponseEntity<Account> postAccount(@RequestBody Account account){
        if(!account.getUsername().isBlank() && (account.getPassword().length() >= 4) &&
        !duplicateAccount(accountService.getAllAccounts(), account)){
        return ResponseEntity.ok().body(accountService.saveAccount(account));
        }
        else if(duplicateAccount(accountService.getAllAccounts(), account)){
            return ResponseEntity.status(409).body(new Account());
        }
        else {
            return ResponseEntity.status(400).body(new Account());
        }
    }
    
    public boolean duplicateAccount(List<Account> accounts, Account theAccount){
        boolean isDuplicate = false;
        for(Account account : accounts){
            if(theAccount.getUsername().equals(account.getUsername())){
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }

    @PostMapping(value = "/login")
    private ResponseEntity<Account> loginIntoAccount(@RequestBody Account account){
        List<Account> accounts = accountService.getAllAccounts();
        boolean isTheAccountRegistered = false;
        Account targetAccount = new Account();
        for(Account oneAccount : accounts){
            if(account.getUsername().equals(oneAccount.getUsername()) &&
            account.getPassword().equals(oneAccount.getPassword())){
                isTheAccountRegistered = true;
                targetAccount = oneAccount;
                break;
            }
        }
        if(isTheAccountRegistered){
            return ResponseEntity.ok().body(targetAccount);
        }
        else{
            return ResponseEntity.status(401).body(new Account());
        }
    }

    @PostMapping("/messages")
    private ResponseEntity<Message> postMessage(@RequestBody Message message){
        List<Account> accounts = accountService.getAllAccounts();
        List<Integer> idList = new ArrayList<>();
        for(Account oneAccount : accounts){
            idList.add(oneAccount.getAccountId());
        }
        if(!message.getMessageText().isBlank() && message.getMessageText().length() <= 255 &&
        idList.contains(message.getPostedBy())){
            return ResponseEntity.status(200).body(messageService.saveMessage(message));
        }
        else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/messages")
    private ResponseEntity<List<Message>> retriveAllMessages(){
        List<Message> allMessages = messageService.retrieveAllMessages();
        if(allMessages.size() > 0){
            return ResponseEntity.ok().body(allMessages);
        }
        else{
            return ResponseEntity.ok().body(new ArrayList<>());
        }
    }

    @GetMapping("/messages/{messageId}")
    private ResponseEntity<Message> getMessageById(@PathVariable int messageId){
        Message message = messageService.retrieveMessageById(messageId);
        if(message != null){
            return ResponseEntity.ok().body(message);
        }
        else{
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/messages/{messageId}")
    private ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId){
        int numberOfRowsUpdated = messageService.deleteMessage(messageId);
        if(numberOfRowsUpdated == 1){
            return ResponseEntity.ok().body((Integer)(numberOfRowsUpdated));
        }
        else{
            return ResponseEntity.ok().build();
        }
    }
    @PatchMapping("/messages/{messageId}")
    private ResponseEntity<Integer> updateMessageById(@PathVariable int messageId,
     @RequestBody Message message){
        if(messageService.updateMessage(messageId, message.getMessageText()) == 1
         && !message.getMessageText().isEmpty()){
            return ResponseEntity.ok().body((Integer)(1));
        }
        else {
            return ResponseEntity.status(400).build();
        }
     }

     @GetMapping("/accounts/{accountId}/messages")
     private ResponseEntity<List<Message>> retrieveAllMessagesByUser(@PathVariable int accountId){
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        if(messages.isEmpty()){
            return ResponseEntity.ok().body(new ArrayList<>());
        }
        else{
            return ResponseEntity.ok().body(messages);
        }
     }
}
