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
import java.util.*;
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


        // 1. DBに入っているデータを {Key: Id, Value: Account} のMap形式で保持する
        // 2. テスト対象メソッドにわたす値の生成
        // 3. テスト対象実行
        // 4. DBに入っているデータを {Key: Id, Value: Account} のMap形式で保持する
        // 5. 1.で取得したMapのデータが5.で取得したMapに存在していて、Valueの値も一致していること
        // 6. 4.で取得したMapに新しいIDをもつ値が存在していて、Valueの値が2.で生成した値と一致していること


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

    }
}