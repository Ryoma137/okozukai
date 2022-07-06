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

        var recordsWithoutAddedOne = actual.stream().filter(accountValue -> accountValue.getId() != 4).toList();
        assertTrue(original.contains(recordsWithoutAddedOne.get(0)), "1件目のデータが追加前と変わらないこと");
        assertTrue(original.contains(recordsWithoutAddedOne.get(1)), "2件目のデータが追加前と変わらないこと");
        assertTrue(original.contains(recordsWithoutAddedOne.get(2)), "3件目のデータが追加前と変わらないこと");

    }

    @Sql("/test-schema.sql")
    @Test
    @DisplayName("与えられたデータのキーと一致するデータがDBに存在する時、DBにデータが追加されず更新されているかの確認")
    void testUpdateWhenDataContainsSameKeyWithGivenData() {

        var original = accountRepository.findAll();

        var account = new Account();
        account.setId(1L);
        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setItem("testItem");
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");

        accountRepository.save(account);

        var actual = accountRepository.findAll();
        assertEquals(original.size(), actual.size(), "データを与えた後と与える前のDBに保存されているデータ数が同じ事の確認");

        var accountValueWithMaxId = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);
        assertNotEquals(account, accountValueWithMaxId, "与えられたデータでデータが新規に追加されていない事を確認");

        assertNotEquals(original, actual, "データを与えた後と与える前のDBに保存されているデータの中身が異なる事を確認");

        var diffFromActual = original.stream().filter(account1 -> actual.stream().noneMatch(before -> before.equals(account1))).toList();
        var diffFromOriginal = actual.stream().filter(account1 -> original.stream().noneMatch(before -> before.equals(account1))).toList();
        assertNotEquals(diffFromActual, diffFromOriginal, "データを与えた後と与える前で値が異なるデータを比較し、値が同じでない事を確認");
        assertEquals(1, diffFromOriginal.size(),"更新されたデータが1件のみである事の確認");

        assertEquals(Date.valueOf("2022-03-01"), diffFromOriginal.get(0).getItemDate(), "与えられたデータで日付が変更されている事を確認");
        assertEquals("testItem", diffFromOriginal.get(0).getItem(), "与えられたデータで内容が変更されている事を確認");
        assertEquals(1000, diffFromOriginal.get(0).getExpense(), "与えられたデータで支出が変更されている事を確認");
        assertEquals(1500, diffFromOriginal.get(0).getIncome(), "与えられたデータで支出が変更されている事を確認");
        assertEquals("testNote", diffFromOriginal.get(0).getNote(), "与えられたデータで備考が変更されている事を確認");

    }


    @Sql("/test-schema-not-data-exist.sql")
    @Test
    @DisplayName("DBのテーブル内にデータが存在しない時、空の配列を取得することを確認")
    void testFindAllWhenDataNotExistInDB() {

        var actual = accountRepository.findAll();
        assertTrue(actual.isEmpty(), "DBにデータがない時、空の配列を取得する");
    }

    @Sql("/test-schema-not-data-exist.sql")
    @Test
    @DisplayName("DBのテーブル内にデータが存在する時、DBのテーブル内にあるデータが全て取得されることを確認")
    void testFindAllWhenDataExistsInDB() {

        var original = accountRepository.findAll();
        assertTrue(original.isEmpty(), "DBにデータがない時、空のデータを取得する");

        var account = new Account();
        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setItem("testItem");
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");

        var account2 = new Account();
        account2.setItemDate(Date.valueOf("2022-04-01"));
        account2.setItem("testItem2");
        account2.setExpense(2000);
        account2.setIncome(2500);
        account2.setNote("testNote2");

        var account3 = new Account();
        account3.setItemDate(Date.valueOf("2022-05-01"));
        account3.setItem("testItem3");
        account3.setExpense(3000);
        account3.setIncome(3500);
        account3.setNote("testNote3");

        accountRepository.save(account);
        accountRepository.save(account2);
        accountRepository.save(account3);

        var actual = accountRepository.findAll();

        assertEquals(3, actual.size(), "DBに登録されているデータ数の確認");
        assertTrue(actual.contains(account), "DBに保存されているデータが取得できている");
        assertTrue(actual.contains(account2), "DBに保存されているデータが取得できている");
        assertTrue(actual.contains(account3), "DBに保存されているデータが取得できている");

    }


}