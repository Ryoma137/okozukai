package com.example.okozukai.controller;

import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class AccountBookControllerTest {

    @Autowired
    AccountBookController accountBookController;

    @Autowired
    AccountRepository accountRepository;

    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("PriceTypeの値がincomeの時、収入情報を登録するメソッドが呼ばれる")
    void testCallRegisterIncomeFunctionWhenSelectedIncome() {

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceType("income");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.registerInfo(accountBookForm);

        var actual = accountRepository.findAll();

        assertEquals(1, actual.size(), "データ追加後のデータを確認");
        assertEquals(Date.valueOf("2022-03-01"), actual.get(0).getItemDate(), "収入情報を登録するメソッドが呼ばれ、日付情報が登録されている事を確認");
        assertEquals("testItem", actual.get(0).getItem(), "収入情報を登録するメソッドが呼ばれ、内容情報が登録されている事を確認");
        assertEquals(1000, actual.get(0).getIncome(), "収入情報を登録するメソッドが呼ばれ、収入情報が登録されていることを確認");
        assertEquals(0, actual.get(0).getExpense(), "収入情報を登録するメソッドが呼ばれるため、支出情報が登録されていないことを確認");
        assertEquals("testNote", actual.get(0).getNote(), "収入情報を登録するメソッドが呼ばれ、備考情報が登録されている事を確認");

    }

    @Test
    @Sql("/test-schema-not-data-exist.sql")
    @DisplayName("PriceTypeの値がexpenseの時、支出情報を登録するメソッドが呼ばれる")
    void testCallRegisterExpenseFunctionWhenSelectedExpense() {

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceType("expense");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.registerInfo(accountBookForm);
        var actual = accountRepository.findAll();

        assertEquals(1, actual.size(), "データ追加後のデータを確認");
        assertEquals(Date.valueOf("2022-03-01"), actual.get(0).getItemDate(), "支出情報を登録するメソッドが呼ばれ、日付情報が登録されている事を確認");
        assertEquals("testItem", actual.get(0).getItem(), "支出情報を登録するメソッドが呼ばれ、内容情報が登録されている事を確認");
        assertEquals(1000, actual.get(0).getExpense(), "支出情報を登録するメソッドが呼ばれ、支出情報が登録されていることを確認");
        assertEquals(0, actual.get(0).getIncome(), "支出情報を登録するメソッドが呼ばれるため、収入情報が登録されていないことを確認");
        assertEquals("testNote", actual.get(0).getNote(), "支出情報を登録するメソッドが呼ばれ、備考情報が登録されている事を確認");

    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("PriceTypeの値がexpenseの時、支出情報を登録するメソッドが呼ばれる")
    void testCallUpdateExpenseFunctionWhenSelectedExpense() {

        var original = accountRepository.findAll();

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceType("expense");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.updateInfo(2L, accountBookForm);
        var actual = accountRepository.findAll();

        var diffFromOriginal = actual.stream().filter(account1 -> original.stream().noneMatch(before -> before.equals(account1))).toList();

        assertEquals(1, diffFromOriginal.size(), "更新されたデータが1件のみである事の確認");

        assertEquals(2L, diffFromOriginal.get(0).getId());
        assertEquals(Date.valueOf("2022-03-01"), diffFromOriginal.get(0).getItemDate(), "支出情報を登録するメソッドが呼ばれ、日付情報が登録されている事を確認");
        assertEquals("testItem", diffFromOriginal.get(0).getItem(), "支出情報を登録するメソッドが呼ばれ、内容情報が登録されている事を確認");
        assertEquals(1000, diffFromOriginal.get(0).getExpense(), "支出情報を登録するメソッドが呼ばれ、支出情報が登録されていることを確認");
        assertEquals(0, diffFromOriginal.get(0).getIncome(), "支出情報を登録するメソッドが呼ばれるため、収入情報が登録されていないことを確認");
        assertEquals("testNote", diffFromOriginal.get(0).getNote(), "支出情報を登録するメソッドが呼ばれ、備考情報が登録されている事を確認");

    }

    @Test
    @Sql("/test-schema.sql")
    @DisplayName("PriceTypeの値がincomeの時、収入情報を登録するメソッドが呼ばれる")
    void testCallUpdateIncomeFunctionWhenSelectedIncome() {

        var original = accountRepository.findAll();

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceType("income");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.updateInfo(2L, accountBookForm);
        var actual = accountRepository.findAll();

        var diffFromOriginal = actual.stream().filter(account1 -> original.stream().noneMatch(before -> before.equals(account1))).toList();

        assertEquals(1, diffFromOriginal.size(), "更新されたデータが1件のみである事の確認");

        assertEquals(2L, diffFromOriginal.get(0).getId());
        assertEquals(Date.valueOf("2022-03-01"), diffFromOriginal.get(0).getItemDate(), "収入情報を登録するメソッドが呼ばれ、日付情報が登録されている事を確認");
        assertEquals("testItem", diffFromOriginal.get(0).getItem(), "収入情報を登録するメソッドが呼ばれ、内容情報が登録されている事を確認");
        assertEquals(0, diffFromOriginal.get(0).getExpense(), "収入情報を登録するメソッドが呼ばれるため、支出情報が登録されていないことを確認");
        assertEquals(1000, diffFromOriginal.get(0).getIncome(), "収入情報を登録するメソッドが呼ばれるため、支出情報が登録されている事を確認");
        assertEquals("testNote", diffFromOriginal.get(0).getNote(), "収入情報を登録するメソッドが呼ばれ、備考情報が登録されている事を確認");

    }
}