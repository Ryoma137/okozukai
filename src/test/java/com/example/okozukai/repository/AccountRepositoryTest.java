package com.example.okozukai.repository;

import com.example.okozukai.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;


    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("IDがNullの時に@IDがNullの時に@GeneratedValueでIDが正しく生成されること")
    void testGeneratedValue() {

        var account = new Account();

        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");


        assertNull(account.getId(), "DBに保存される前のIDはNullであるかの確認");

        accountRepository.save(account);

        var actual = accountRepository.findAll();

        assertEquals(1, actual.size(), "追加したデータのみがDBに保存されている");
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

        assertEquals(1, actual.size(), "追加したデータのみがDBに保存されている");
        assertEquals(account, actual.get(0), "追加したデータがレコードの1列目に保存されている");
    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("DBのテーブル内にデータが存在する時、DBのID列の最後尾にデータが追加されていること")
    void testAddDataWhenDataExistInDB() {

        var account = new Account();

        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");

        accountRepository.save(account);
        var actual = accountRepository.findAll();

        assertNotNull(actual.get(3).getId(), "与えられたデータがDBのID列の最後尾に追加されていることを確認");
        assertEquals(4, actual.size(), "既存のDBに保存されているデータ数に追加したデータ数を合わせたデータ数が取得される事を確認");
    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("与えられたデータのキーと一致するデータがDBに存在しない時、DBが更新されず新たなデータとして登録されているかの確認")
    void testUpdateWhenDataNotContainsSameKeyWithGivenData() {

        var account = new Account();

        account.setId(5L);
        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setExpense(1000);
        account.setIncome(1500);
        account.setNote("testNote");

        accountRepository.save(account);
        var actual = accountRepository.findAll();

        assertNotNull(actual.get(3).getId(), "与えられたデータのキーが一致するデータがDBに存在しない時、与えられたデータでDBの更新ではなく、与えられたデータのキーで新たなデータが登録されているかの確認");
        assertEquals(4, actual.size(), "データが与えられた後のDBに保存されているデータ数を確認");
    }

}