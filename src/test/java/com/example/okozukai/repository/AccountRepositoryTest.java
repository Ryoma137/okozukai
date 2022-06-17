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
    @DisplayName("DBのテーブル内にデータが存在しない時、与えられたデータがDBに追加されていること")
    void testAddDataWhenDataNotExistInDB() {

        var account = new Account();
    //    String aaa = "2022-03-01";

        account.setIncome(1500);
        account.setExpense(1000);
        account.setItem("testItem");
        account.setItemDate(Date.valueOf("2022-03-01"));
        account.setNote("testNote");


        accountRepository.save(account);
        var actual = accountRepository.findAll();

        assertEquals(1, actual.size(), "追加したデータのみがDBに保存されている");
       // assertEquals(account, actual.get(0), "追加したデータがレコードの1列目に保存されている");

    }


}