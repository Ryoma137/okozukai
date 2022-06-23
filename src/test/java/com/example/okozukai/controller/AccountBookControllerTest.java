package com.example.okozukai.controller;

import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.service.AccountBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.security.auth.login.AccountException;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AccountBookControllerTest {

    @Autowired
    AccountBookController accountBookController;

    @Autowired
    AccountBookService accountBookService;

    @Test
    @DisplayName("radio buttonで支出を選択している時、収入情報を登録するメソッドが呼ばれる")
    void testCallRegisterIncomeFunctionWhenSelectedIncome() {

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceRadio("income");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.registerInfo(accountBookForm);

        assertEquals("income", accountBookForm.getPriceRadio(), "priceRadioの値がincomeであること確認");

    }

    @Test
    @DisplayName("radio buttonで支出を選択している時、支出情報を登録するメソッドが呼ばれる")
    void testCallRegisterExpenseFunctionWhenSelectedIncome() {

        var accountBookForm = new AccountBookForm();
        accountBookForm.setPriceRadio("expense");
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        accountBookController.registerInfo(accountBookForm);

        assertEquals("expense", accountBookForm.getPriceRadio(), "priceRadioの値がincomeであること確認");

    }
}