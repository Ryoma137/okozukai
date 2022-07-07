package com.example.okozukai.controller;


import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.service.AccountBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountBookController {

    @Autowired
    AccountBookService accountBookService;

    @GetMapping("/account-book/new")
    public String getRegisterPage(@ModelAttribute("registerInfo") AccountBookForm accountBookForm) {

        return "new";
    }

    @PostMapping("/account-book/new")
    public String registerInfo(@ModelAttribute("registerInfo") AccountBookForm accountBookForm) {

        if (accountBookForm.getPriceType().equals("income")) {
            accountBookService.registerIncome(accountBookForm);
        } else if (accountBookForm.getPriceType().equals("expense")) {
            accountBookService.registerExpense(accountBookForm);
        }
        return "redirect:/account-book";
    }

    @GetMapping("/account-book")
    public String getTopPage(Model model) {

        var dataFromDB = accountBookService.getFindAll();
        var netWorth = accountBookService.getTotalPrice();
        model.addAttribute("dbData", dataFromDB);
        model.addAttribute("netWorth", netWorth);
        return "/index";
    }

    @GetMapping("/account-book/update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id, @ModelAttribute("updateInfo") AccountBookForm accountBookForm) {

        var recordData = accountBookService.getById(id);

        if (recordData.getExpense() == 0) {
            accountBookForm.setPrice(recordData.getIncome());
            accountBookForm.setPriceType("income");
        } else if (recordData.getIncome() == 0) {
            accountBookForm.setPrice(recordData.getExpense());
            accountBookForm.setPriceType("expense");
        }

        accountBookForm.setItem(recordData.getItem());
        accountBookForm.setItemDate(recordData.getItemDate());
        accountBookForm.setNote(recordData.getNote());
        return "/update";
    }

    @PutMapping("/account-book/update/{id}")
    public String updateInfo(@PathVariable("id") Long id, @ModelAttribute("updateInfo") AccountBookForm accountBookForm) {

        var data = accountBookService.getById(id);
        accountBookForm.setId(data.getId());


        if (accountBookForm.getPriceType().equals("income")) {
            accountBookService.updateIncome(accountBookForm);
        } else if (accountBookForm.getPriceType().equals("expense")) {
            accountBookService.updateExpense(accountBookForm);
        }
        return "redirect:/account-book";
    }

    @GetMapping("/account-book/delete/{id}")
    public String getDeletePage(@PathVariable("id") Long id, @ModelAttribute("deleteInfo") AccountBookForm accountBookForm) {

        var recordData = accountBookService.getById(id);

        if (recordData.getExpense() == 0) {
            accountBookForm.setPrice(recordData.getIncome());
            accountBookForm.setPriceType("income");
        } else if (recordData.getIncome() == 0) {
            accountBookForm.setPrice(recordData.getExpense());
            accountBookForm.setPriceType("expense");
        }

        accountBookForm.setItem(recordData.getItem());
        accountBookForm.setItemDate(recordData.getItemDate());
        accountBookForm.setNote(recordData.getNote());

        return "/delete";
    }

    @DeleteMapping("/account-book/delete/{id}")
    public String deleteInfo(@PathVariable("id") Long id, @ModelAttribute("updateInfo") AccountBookForm accountBookForm) {

        accountBookService.deleteBySpecifiedId(id);
        return "redirect:/account-book";
    }


}