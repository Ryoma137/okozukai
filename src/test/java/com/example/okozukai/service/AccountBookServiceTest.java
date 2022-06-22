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
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountBookServiceTest {

    @Autowired
    AccountBookService accountBookService;

    @Autowired
    AccountRepository accountRepository;


    @Test
    @Sql("/test-schema.sql")
    @DisplayName("DBに既存データが存在する時、引数(accountBookForm)で受け取った情報がAccountインスタンスに収入情報として格納されてレコードに追加される")
    void testRegisterIncomeDataPassedThroughParameterWhenRecordsExistInDB() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");

        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

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
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("DBのテーブル内にデータが存在しない時、引数(accountBookForm)で受け取った情報がAccountインスタンスに収入情報として格納されてレコードに追加される")
    void testRegisterIncomeDataPassedThroughParameterWhenRecordNotExistsInDB() {

        var original = accountRepository.findAll();
        assertEquals(0, original.size(), "DBのテーブル内にデータが存在しない事を確認");

        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(1, actual.size(), "レコード追加後のDBのテーブル内に存在するデータ数を確認");
        assertNotNull(actual.get(0), "DBのテーブル内にデータが存在することの確認");
        assertEquals(accountBookForm.getItemDate(), actual.get(0).getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), actual.get(0).getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), actual.get(0).getIncome(), "引数(accountBookForm)で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), actual.get(0).getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("registerIncome_既存データあり_データが追加され既存データに変更なし")
    void testRegisterIncome1() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");

        Map<Long, Account> originalMap = original.stream().collect(Collectors.toMap(Account::getId, account -> account));

        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");

        Map<Long, Account> actualMap = actual.stream().collect(Collectors.toMap(Account::getId, account -> account));

        assertTrue(originalMap.entrySet().stream().allMatch(e -> e.getValue().equals(actualMap.get(e.getKey()))), "データを与える後に取得したMapにデータを与える前に取得したMapのデータが含まれている");

        var addedRecord = actualMap.entrySet().stream().sorted(Map.Entry.<Long, Account>comparingByKey().reversed()).findFirst().orElseGet(Assertions::fail);

        assertNotNull(addedRecord, "最大値(最後に追加したデータ)のIDを持つデータが存在しているかを確認する");

        assertEquals(accountBookForm.getItemDate(), addedRecord.getValue().getItemDate(), "与えられたデータの日付値とValueの日付の値が一致している");
        assertEquals(accountBookForm.getItem(), addedRecord.getValue().getItem(), "与えられたデータの内容値とValueの内容の値が一致している");
        assertEquals(accountBookForm.getPrice(), addedRecord.getValue().getIncome(), "与えられたデータの値段値とValueの収入の値が一致している");
        assertEquals(accountBookForm.getNote(), addedRecord.getValue().getNote(), "与えられたデータの備考値とValueの備考の値が一致している");
        
    }
}