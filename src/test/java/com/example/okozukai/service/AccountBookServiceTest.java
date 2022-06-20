package com.example.okozukai.service;

import com.example.okozukai.entity.Account;
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
@Sql("/test-schema.sql")
class AccountBookServiceTest {

    @Autowired
    AccountBookService accountBookService;

    @Autowired
    AccountRepository accountRepository;


    @Test
    @DisplayName("引数(accountBookForm)で受け取った情報がAccountインスタンスに収入情報として格納されてレコードに追加される")
    void testRegisterIncomeDataPassedThroughParameter() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        var account = new Account();
        account.setId(null);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setIncome(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数(accountBookForm)で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数(accountBookForm)で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, account.getIncome(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの収入値に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数(accountBookForm)で受け取った備考の値がAccountのインスタンスの備考値に格納されているかの確認");


        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");


        var saveAccount = accountRepository.save(account);

        assertEquals(accountBookForm.getItemDate(), saveAccount.getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), saveAccount.getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), saveAccount.getIncome(), "引数(accountBookForm)で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), saveAccount.getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @DisplayName("引数(accountBookForm)で受け取った情報がAccountインスタンスに支出情報として格納されてレコードに追加される")
    void testRegisterExpenseDataPassedThroughParameter() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        var account = new Account();
        account.setId(null);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setExpense(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数(accountBookForm)で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数(accountBookForm)で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, account.getExpense(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの支出値に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数(accountBookForm)で受け取った備考の値がAccountのインスタンスの備考値に格納されているかの確認");


        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");


        var saveAccount = accountRepository.save(account);

        assertEquals(accountBookForm.getItemDate(), saveAccount.getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), saveAccount.getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), saveAccount.getExpense(), "引数(accountBookForm)で受け取った日付の値が支出値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), saveAccount.getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @DisplayName("引数(accountBookForm)で受け取った情報がもAccountインスタンスに支出、収入情報共に格納されてレコードに追加される")
    void testRegisterIncomeAndExpenseDataPassedThroughParameter() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        var account = new Account();
        account.setId(null);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setIncome(accountBookForm.getPrice());
        account.setExpense(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数(accountBookForm)で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数(accountBookForm)で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, account.getIncome(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの収入値に格納されているかの確認");
        assertEquals(1000, account.getExpense(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの収入値に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数(accountBookForm)で受け取った備考の値がAccountのインスタンスの備考値に格納されているかの確認");

        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");

        var saveAccount = accountRepository.save(account);

        assertEquals(accountBookForm.getItemDate(), saveAccount.getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), saveAccount.getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), saveAccount.getIncome(), "引数(accountBookForm)で受け取った価格の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), saveAccount.getExpense(), "引数(accountBookForm)で受け取った価格の値が支出値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), saveAccount.getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }

    @Test
    @DisplayName("IDがnullの時、引数(accountBookForm)で受け取った情報がAccountインスタンスに情報として格納されてレコードに追加される")
    void testRegisterDataWithNullId() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        var account = new Account();
        account.setId(null);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setIncome(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数(accountBookForm)で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数(accountBookForm)で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, account.getIncome(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの収入値に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数(accountBookForm)で受け取った備考の値がAccountのインスタンスの備考値に格納されているかの確認");


        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");


        var saveAccount = accountRepository.save(account);

        assertEquals(accountBookForm.getItemDate(), saveAccount.getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), saveAccount.getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), saveAccount.getIncome(), "引数(accountBookForm)で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), saveAccount.getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }


    @Test
    @DisplayName("IDがnullではない時、引数(accountBookForm)で受け取った情報がAccountインスタンスに情報として格納されてレコードに追加される")
    void testRegisterDataWithNotNullId() {

        var original = accountRepository.findAll();
        assertEquals(3, original.size(), "レコード追加前のDBに保存されているデータ数の確認");


        var accountBookForm = new AccountBookForm();
        accountBookForm.setItemDate(Date.valueOf("2022-03-01"));
        accountBookForm.setItem("testItem");
        accountBookForm.setPrice(1000);
        accountBookForm.setNote("testNote");

        var account = new Account();
        account.setId(5L);
        account.setItemDate(accountBookForm.getItemDate());
        account.setItem(accountBookForm.getItem());
        account.setIncome(accountBookForm.getPrice());
        account.setNote(accountBookForm.getNote());

        assertEquals(Date.valueOf("2022-03-01"), account.getItemDate(), "引数(accountBookForm)で受け取った日付の値がAccountのインスタンスの内容の日付に格納されているかの確認");
        assertEquals("testItem", account.getItem(), "引数(accountBookForm)で受け取った内容の値がAccountのインスタンスの内容に格納されているかの確認");
        assertEquals(1000, account.getIncome(), "引数(accountBookForm)で受け取った値段の値がAccountのインスタンスの収入値に格納されているかの確認");
        assertEquals("testNote", accountBookForm.getNote(), "引数(accountBookForm)で受け取った備考の値がAccountのインスタンスの備考値に格納されているかの確認");


        accountBookService.registerIncome(accountBookForm);

        var actual = accountRepository.findAll();
        assertEquals(4, actual.size(), "レコード追加後のDBに保存されているデータ数の確認");


        var saveAccount = accountRepository.save(account);

        assertEquals(accountBookForm.getItemDate(), saveAccount.getItemDate(), "引数(accountBookForm)で受け取った日付の値が日付値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getItem(), saveAccount.getItem(), "引数(accountBookForm)で受け取った内容の値が内容値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getPrice(), saveAccount.getIncome(), "引数(accountBookForm)で受け取った日付の値が収入値としてDBに保存されているかの確認");
        assertEquals(accountBookForm.getNote(), saveAccount.getNote(), "引数(accountBookForm)で受け取った備考の値が備考値としてDBに保存されているかの確認");

    }
}