package com.example.okozukai.controller;

import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.service.AccountBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class AccountBookControllerTest {

    @Autowired
    AccountBookController accountBookController;

    @Test
    @DisplayName("radio buttonで支出を選択している時、収入情報を登録するメソッドが呼ばれる")
    void testCallRegisterIncomeMethodWhenSelectedIncome() {

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceType("income");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.registerInfo(accountBookForm);

        assertEquals("income", accountBookForm.getPriceType(), "priceRadioの値がincomeであること確認");

    }

    @Test
    @DisplayName("radio buttonで支出を選択している時、支出情報を登録するメソッドが呼ばれる")
    void testCallRegisterExpenseMethodWhenSelectedIncome() {

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceType("expense");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.registerInfo(accountBookForm);

        assertEquals("expense", accountBookForm.getPriceType(), "priceTypeの値がincomeであること確認");

    }
}