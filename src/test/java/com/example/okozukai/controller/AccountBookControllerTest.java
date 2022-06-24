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

        assertEquals(1000, actual.get(0).getIncome(), "収入情報を登録するメソッドが呼ばれ、収入情報が登録されていることを確認");
        assertEquals(0, actual.get(0).getExpense(), "収入情報を登録するメソッドが呼ばれるため、支出情報が登録されていないことを確認");

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

        assertEquals(1000, actual.get(0).getExpense(), "支出情報を登録するメソッドが呼ばれ、支出情報が登録されていることを確認");
        assertEquals(0, actual.get(0).getIncome(), "支出情報を登録するメソッドが呼ばれるため、収入情報が登録されていないことを確認");

    }
}