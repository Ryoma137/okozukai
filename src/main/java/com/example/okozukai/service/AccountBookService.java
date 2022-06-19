package com.example.okozukai.service;

import com.example.okozukai.entity.Account;
import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class AccountBookService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    AccountRepository accountRepository;

    public AccountBookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerIncome(AccountBookForm accountBookForm) {

        var account = new Account();

        account.setId(null);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setIncome(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        accountRepository.save(account);
    }
}
