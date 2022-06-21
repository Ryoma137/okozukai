package com.example.okozukai.repository;

import com.example.okozukai.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;


    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("IDがNullの時に@GeneratedValueでIDが正しく生成されること")
    void testGeneratedValue() {

        var account = new Account();

        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");


        assertNull(account.getId(), "DBに保存される前のIDはNullであるかの確認");

        accountRepository.save(account);

        var actual = accountRepository.findAll();

        assertEquals(1, actual.size(), "データを与えた後のDBに保存されているデータ数を確認");
        assertNotNull(actual.get(0).getId(), "DBに保存されたIDがnullではなく、IDが生成されている");
    }

    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("DBのテーブル内にデータが存在しない時、与えられたデータがDBに追加されていること")
    void testAddDataWhenDataNotExistInDB() {

        var account = new Account();

        account.setId(null);
        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");

        accountRepository.save(account);
        var actual = accountRepository.findAll();

        assertEquals(1, actual.size(), "データを与えた後のDBに保存されているデータ数を確認");
        assertNotNull(actual.get(0), "DBにデータが存在することの確認");
        assertEquals(account, actual.get(0), "DBに存在してるデータが登録したデータであるかの確認");

    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("DBのテーブル内にデータが存在する時、DBにデータが追加されていること")
    void testAddDataWhenDataExistInDB() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "データを与える前のDBに保存されているデータ数を確認");

        var account = new Account();

        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setItem("testItem");
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");

        accountRepository.save(account);
        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "データを与えた後のDBに保存されているデータ数を確認");
        var maxIdValue = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);

        assertNotNull(maxIdValue, "追加されたデータが存在することの確認");
        assertEquals(account, maxIdValue, "追加したデータがDBに登録されているかの確認");

    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("与えられたデータのキーと一致するデータがDBに存在しない時、DBが更新されず新たなデータとして登録されているかの確認")
    void testUpdateWhenDataNotContainsSameKeyWithGivenData() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "データを与える前のDBに保存されているデータ数を確認");

        var account = new Account();

        account.setId(5L);
        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setItem("testItem");
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");

        accountRepository.save(account);
        var actual = accountRepository.findAll();

        assertEquals(4, actual.size(), "データを与えた後のDBに保存されているデータ数を確認");

        var maxIdValue = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);

        assertNotNull(maxIdValue, "追加されたデータが存在することの確認");
        assertEquals(4, maxIdValue.getId(), "追加したデータのキーがDBのID列を利用したIDに変更されている事を確認");
        assertEquals(account.getItemDate(), maxIdValue.getItemDate(), "追加したデータの日付が登録されている事を確認");
        assertEquals(account.getItem(), maxIdValue.getItem(), "追加したデータの内容が登録されている事を確認");
        assertEquals(account.getExpense(), maxIdValue.getExpense(), "追加したデータの支出が登録されている事を確認");
        assertEquals(account.getIncome(), maxIdValue.getIncome(), "追加したデータの収入が登録されている事を確認");
        assertEquals(account.getNote(), maxIdValue.getNote(), "追加したデータの内容が登録されている事を確認");

        var recordsWithoutAddedOne = actual.stream().limit(3L).toList();

        assertEquals(original.stream().map(Account::getItemDate).toList(), recordsWithoutAddedOne.stream().map(Account::getItemDate).collect(Collectors.toList()), "既存データのitemDateの値が変更されていない事を確認");
        assertEquals(original.stream().map(Account::getItem).collect(Collectors.toList()), recordsWithoutAddedOne.stream().map(Account::getItem).collect(Collectors.toList()), "既存データのitemの値が変更されていない事を確認");
        assertEquals(original.stream().map(Account::getIncome).toList(), recordsWithoutAddedOne.stream().map(Account::getIncome).toList(), "既存データのincomeの値が変更されていない事を確認");
        assertEquals(original.stream().map(Account::getExpense).toList(), recordsWithoutAddedOne.stream().map(Account::getExpense).toList(), "既存データのexpenseの値が変更されていない事を確認");
        assertEquals(original.stream().map(Account::getNote).toList(), recordsWithoutAddedOne.stream().map(Account::getNote).toList(), "既存データのnoteの値が変更されていない事を確認");

    }

}