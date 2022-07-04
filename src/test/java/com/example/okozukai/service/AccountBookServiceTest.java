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
    @DisplayName("DBに既存データが存在する時、引数で受け取った情報が収入情報としてレコードに追加される")
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

        assertEquals(accountBookForm.getItemDate(), accountValueWithMaxId.getItemDate(), "引数で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), accountValueWithMaxId.getItem(), "引数で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), accountValueWithMaxId.getIncome(), "引数で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), accountValueWithMaxId.getNote(), "引数で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("DBのテーブル内にデータが存在しない時、引数で受け取った情報が収入情報としてレコードに追加される")
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
        assertEquals(accountBookForm.getItemDate(), actual.get(0).getItemDate(), "引数で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), actual.get(0).getItem(), "引数で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), actual.get(0).getIncome(), "引数で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), actual.get(0).getNote(), "引数で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("DBに既存データが存在する時、引数で受け取った情報が支出情報としてレコードに追加される")
    void testRegisterExpenseDataPassedThroughParameterWhenRecordsExistInDB() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");

        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookService.registerExpense(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");

        var accountValueWithMaxId = actual.stream().max(Comparator.comparing(Account::getId)).orElseGet(Assertions::fail);
        assertNotNull(accountValueWithMaxId, "追加したデータが存在しているかの確認");
        assertEquals(4, accountValueWithMaxId.getId(), "追加データのID値がDBのID列の数値に変更されてDBに登録されているかの確認");

        assertEquals(accountBookForm.getItemDate(), accountValueWithMaxId.getItemDate(), "引数で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), accountValueWithMaxId.getItem(), "引数で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), accountValueWithMaxId.getExpense(), "引数で受け取った日付の値が支出値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), accountValueWithMaxId.getNote(), "引数で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("DBのテーブル内にデータが存在しない時、引数で受け取った情報が支出情報として格納されてレコードに追加される")
    void testRegisterExpenseDataPassedThroughParameterWhenRecordNotExistsInDB() {

        var original = accountRepository.findAll();
        assertEquals(0, original.size(), "DBのテーブル内にデータが存在しない事を確認");

        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookService.registerExpense(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(1, actual.size(), "レコード追加後のDBのテーブル内に存在するデータ数を確認");
        assertNotNull(actual.get(0), "DBのテーブル内にデータが存在することの確認");
        assertEquals(accountBookForm.getItemDate(), actual.get(0).getItemDate(), "引数で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), actual.get(0).getItem(), "引数で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), actual.get(0).getExpense(), "引数で受け取った日付の値が支出値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), actual.get(0).getNote(), "引数で受け取った備考の値が備考値としてDBに保存されているかの確認");

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

    @Test
    @Sql("/testCallGetFindAllFunction.sql")
    @DisplayName("登録された収支一覧を日付の降順（日付が重なる場合は登録順）で表示するかの確認")
    void testCallGetFindAllFunction() {

        var actual = accountBookService.getFindAll();

        assertNotNull(actual, "取得したデータがnullではないことの確認");
        assertEquals(6, actual.size(), "DBに保存されているデータ数のデータが取得できていることの確認");

        assertEquals(5, actual.get(0).getId(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(Date.valueOf("2022-10-08"), actual.get(0).getItemDate(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals("Sushi", actual.get(0).getItem(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(1000, actual.get(0).getIncome(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(700, actual.get(0).getExpense(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals("Tuna", actual.get(0).getNote(), "取得したデータが日付の降順に並んでいるかを確認");

        assertEquals(3, actual.get(1).getId(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(Date.valueOf("2022-07-20"), actual.get(1).getItemDate(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals("Green Curry", actual.get(1).getItem(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(500, actual.get(1).getIncome(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(900, actual.get(1).getExpense(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals("Thai Cuisine", actual.get(1).getNote(), "取得したデータが日付の降順に並んでいるかを確認");

        assertEquals(6, actual.get(2).getId(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(Date.valueOf("2022-05-03"), actual.get(2).getItemDate(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals("iPad", actual.get(2).getItem(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(0, actual.get(2).getIncome(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(140000, actual.get(2).getExpense(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals("iPad Pro", actual.get(2).getNote(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");

        assertEquals(4, actual.get(3).getId(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(Date.valueOf("2022-05-03"), actual.get(3).getItemDate(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals("MacBook", actual.get(3).getItem(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(0, actual.get(3).getIncome(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(180000, actual.get(3).getExpense(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals("MacBook Pro", actual.get(3).getNote(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");

        assertEquals(2, actual.get(4).getId(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(Date.valueOf("2022-05-03"), actual.get(4).getItemDate(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals("iPhone", actual.get(4).getItem(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(130000, actual.get(4).getIncome(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals(140000, actual.get(4).getExpense(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");
        assertEquals("iPhone 13 Pro", actual.get(4).getNote(), "取得したデータの日付が重複している時、登録順(idの降順)に並んでいるかを確認");

        assertEquals(1, actual.get(5).getId(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(Date.valueOf("2022-01-20"), actual.get(5).getItemDate(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals("T-Shirts", actual.get(5).getItem(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(1000, actual.get(5).getIncome(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals(1500, actual.get(5).getExpense(), "取得したデータが日付の降順に並んでいるかを確認");
        assertEquals("Uniqlo T-Shirt", actual.get(5).getNote(), "取得したデータが日付の降順に並んでいるかを確認");

    }

    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("DBにデータが存在しない時、残高の値が0になる")
    void testGetTotalPriceWhenDataNotExistInDB() {

        var actual =  accountBookService.getTotalPrice();
        assertEquals(0, actual, "DBに保存されている収出の合計金額が計算されていることの確認");
    }

    @Test
    @Sql("/test-getTotalPrice.sql")
    @DisplayName("収出の計算結果がプラスの時、残高がプラスの値になる")
    void testGetTotalPriceWhenNetWorthIsPlus() {

        var actual = accountBookService.getTotalPrice();
        assertEquals(1, actual, "DBに保存されている収出の合計金額がプラスであることの確認");
    }

    @Test
    @Sql("/test-getTotalPriceMinus.sql")
    @DisplayName("収出の計算結果がマイナスの時、残高がマイナスの値になる")
    void testGetTotalPriceWhenNetWorthIsMinus() {

        var actual = accountBookService.getTotalPrice();
        assertEquals(-1, actual, "DBに保存されている収出の合計金額がマイナスであることの確認");
    }

    @Test
    @Sql("/test-getTotalPriceZero.sql")
    @DisplayName("収出の計算結果が0の時、残高の値が0になる")
    void testGetTotalPriceWhenNetWorthIsZero() {

        var actual = accountBookService.getTotalPrice();
        assertEquals(0, actual, "DBに保存されている収出の合計金額が0であることの確認");
    }

}