package com.example.okozukai.service;

import com.example.okozukai.entity.Account;
import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountBookService {

    @Autowired
    AccountRepository accountRepository;

    public void registerIncome(AccountBookForm accountBookForm) {

        var account = new Account();

        accountRepository.save(account);
    }
}
