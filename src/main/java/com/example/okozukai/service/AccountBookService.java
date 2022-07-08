package com.example.okozukai.service;

import com.example.okozukai.entity.Account;
import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AccountBookService {

    @Autowired
    AccountRepository accountRepository;

    public void registerInfo(AccountBookForm accountBookForm) {

        var account = new Account();
        account.setId(null);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        if (accountBookForm.getPriceType().equals("income")) {
            account.setIncome(accountBookForm.getPrice());
        } else if (accountBookForm.getPriceType().equals("expense")) {
            account.setExpense(accountBookForm.getPrice());
        }
        account.setNote(accountBookForm.getNote());
        accountRepository.save(account);
    }

    public List<Account> getFindAll() {
        var dataFromDB = accountRepository.findAll();
        dataFromDB.sort(Comparator.comparing(Account::getItemDate).thenComparing(Account::getId).reversed());
        return dataFromDB;
    }

    public int getTotalPrice() {

        var dataFromDB = accountRepository.findAll();
        int totalExpense = dataFromDB.stream().mapToInt(Account::getExpense).sum();
        int totalIncome = dataFromDB.stream().mapToInt(Account::getIncome).sum();
        int netWorth = totalIncome - totalExpense;

        return netWorth;
    }

    public void updateInfo(long id, AccountBookForm accountBookForm) {

        var data = getBySpecifiedId(id);
        accountBookForm.setId(data.getId());

        var account = new Account();
        account.setId(data.getId());
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());

        if (accountBookForm.getPriceType().equals("income")) {
            account.setIncome(accountBookForm.getPrice());
            account.setExpense(0);
        } else if (accountBookForm.getPriceType().equals("expense")) {
            account.setIncome(0);
            account.setExpense(accountBookForm.getPrice());
        }
        account.setNote(accountBookForm.getNote());
        accountRepository.save(account);

    }

    public Account getBySpecifiedId(long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public void deleteBySpecifiedId(long id) {
        accountRepository.deleteById(id);
    }

    public void getPageInfo(long id, AccountBookForm accountBookForm) {

        var recordData = getBySpecifiedId(id);

        if (recordData.getExpense() == 0) {
            accountBookForm.setPrice(recordData.getIncome());
            accountBookForm.setPriceType("income");
        } else if (recordData.getIncome() == 0) {
            accountBookForm.setPrice(recordData.getExpense());
            accountBookForm.setPriceType("expense");
        }

        accountBookForm.setItem(recordData.getItem());
        accountBookForm.setItemDate(recordData.getItemDate());
        accountBookForm.setNote(recordData.getNote());
    }
}
