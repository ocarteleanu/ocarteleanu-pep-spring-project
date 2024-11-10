package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
//holds the code that handles the business logic
@Service
public class AccountService {
    
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService (AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account saveAccount(Account account){
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts(){
        List<Account> accounts = new ArrayList<>();
        if(accountRepository.findAll() != null){
            accounts = accountRepository.findAll();
        }
        return accounts;
    }

    public Account findAccoundById (int id){
        Optional<Account> accountContainer = accountRepository.findById((long) id);
        if(accountContainer.isPresent()){
            return accountContainer.get();
        }
        else{
            return null;
        }
    }
}
