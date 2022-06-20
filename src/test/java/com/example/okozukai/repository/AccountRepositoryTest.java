package com.example.okozukai.repository;

import com.example.okozukai.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;
import java.util.Arrays;
import java.util.stream.LongStream;

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
        var maxIdValue = LongStream.of(actual.get(0).getId(), actual.get(1).getId(), actual.get(2).getId(), actual.get(3).getId()).max().getAsLong();
        assertNotNull(maxIdValue, "追加されたデータが存在している");

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

        var maxIdValue = LongStream.of(actual.get(0).getId(), actual.get(1).getId(), actual.get(2).getId(), actual.get(3).getId()).max().getAsLong();
        assertNotNull(maxIdValue, "追加されたデータにIDの最大値が付与されて存在することの確認");

        assertTrue(Arrays.asList(actual.get(0)).contains(original.get(0)), "既存のデータが更新されていない事を確認");
        assertTrue(Arrays.asList(actual.get(1)).contains(original.get(1)), "既存のデータが更新されていない事を確認");
        assertTrue(Arrays.asList(actual.get(2)).contains(original.get(2)), "既存のデータが更新されていない事を確認");

    }

}