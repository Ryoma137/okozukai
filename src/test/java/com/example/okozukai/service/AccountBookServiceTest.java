package com.example.okozukai.service;

import com.example.okozukai.entity.Account;
import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.security.Key;
import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/test-schema.sql")
class AccountBookServiceTest {

    @Autowired
    AccountBookService accountBookService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("与えられた収入データ0の時、レコードに追加される")
    void testRegisterIncomeWithZeroValueExpense() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(0);
        accountBookForm.setNote("testNote");

        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");
    }

    @Test
    @DisplayName("与えられた収入データが0以外の時、レコードに追加される")
    void testRegisterIncomeWithNonZeroValueExpense() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        var account = new Account();
        account.setId(null);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setIncome(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, accountBookForm.getPrice(), "引数で受け取った値段の値がAccountのインスタンスの収入値に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数で受け取った備考の値がAccountのインスタンスの備考値に格納されているかの確認");


        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();


        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");


    }

}