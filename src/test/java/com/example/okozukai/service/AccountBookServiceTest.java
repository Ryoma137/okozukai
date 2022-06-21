package com.example.okozukai.service;

import com.example.okozukai.entity.Account;
import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/test-schema.sql")
class AccountBookServiceTest {

    @Autowired
    AccountBookService accountBookService;

    @Autowired
    AccountRepository accountRepository;


    @Test
    @DisplayName("IDがNullの時、引数(accountBookForm)で受け取った情報がAccountインスタンスに収入情報として格納されてレコードに追加される")
    void testRegisterIncomeDataPassedThroughParameterWithNullId() {

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

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数(accountBookForm)で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数(accountBookForm)で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, account.getIncome(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの収入に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数(accountBookForm)で受け取った備考の値がAccountのインスタンスの備考に格納されているかの確認");

        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");

        var accountValueWithMaxId = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);
        assertNotNull(accountValueWithMaxId, "追加したデータが存在しているかの確認");
        assertEquals(4, accountValueWithMaxId.getId(), "追加データのID値がDBのID列の数値に変更されてDBに登録されているかの確認");

        assertEquals(accountBookForm.getItemDate(), accountValueWithMaxId.getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), accountValueWithMaxId.getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), accountValueWithMaxId.getIncome(), "引数(accountBookForm)で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), accountValueWithMaxId.getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @DisplayName("IDがnullではない時、引数(accountBookForm)で受け取った情報がAccountインスタンスに収入情報として格納されてレコードに追加される")
    void testRegisterIncomeDataPassedThroughParameterWithNotNullId() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        var account = new Account();
        account.setId(5L);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setIncome(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数(accountBookForm)で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数(accountBookForm)で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, account.getIncome(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの収入値に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数(accountBookForm)で受け取った備考の値がAccountのインスタンスの備考値に格納されているかの確認");

        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");

        var accountValueWithMaxId = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);
        assertNotNull(accountValueWithMaxId, "追加したデータが存在しているかの確認");
        assertEquals(4, accountValueWithMaxId.getId(), "追加データのID値がDBのID列の数値に変更されてDBに登録されているかの確認");

        assertEquals(accountBookForm.getItemDate(), accountValueWithMaxId.getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), accountValueWithMaxId.getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), accountValueWithMaxId.getIncome(), "引数(accountBookForm)で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), accountValueWithMaxId.getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }
}