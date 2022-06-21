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
        var accountValueWithMaxId = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);

        assertNotNull(accountValueWithMaxId, "追加されたデータが存在することの確認");
        assertEquals(account, accountValueWithMaxId, "追加したデータがDBに登録されているかの確認");

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

        var accountValueWithMaxId = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);

        assertNotNull(accountValueWithMaxId, "追加されたデータが存在することの確認");
        assertEquals(4, accountValueWithMaxId.getId(), "追加したデータのキーがDBのID列を利用したIDに変更されている事を確認");
        assertEquals(account.getItemDate(), accountValueWithMaxId.getItemDate(), "追加したデータの日付が登録されている事を確認");
        assertEquals(account.getItem(), accountValueWithMaxId.getItem(), "追加したデータの内容が登録されている事を確認");
        assertEquals(account.getExpense(), accountValueWithMaxId.getExpense(), "追加したデータの支出が登録されている事を確認");
        assertEquals(account.getIncome(), accountValueWithMaxId.getIncome(), "追加したデータの収入が登録されている事を確認");
        assertEquals(account.getNote(), accountValueWithMaxId.getNote(), "追加したデータの内容が登録されている事を確認");

        assertEquals(3, actual.stream().filter(accountValue -> accountValue.getId() != 4).toList().size(), "追加したデータを除いたデータ数が３である事の確認");
        assertTrue(original.contains(actual.get(0)), "ID値が1であるデータはデータ追加前にも存在していることの確認");
        assertTrue(original.contains(actual.get(1)), "ID値が2であるデータはデータ追加前にも存在していることの確認");
        assertTrue(original.contains(actual.get(2)), "ID値が3であるデータはデータ追加前にも存在していることの確認");

    }
}