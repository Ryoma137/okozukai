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

        return "/update";
    }

    @PutMapping("/account-book/update/{id}")
    @ResponseBody
    public String updateInfo(@PathVariable("id") Long id, @ModelAttribute("updateInfo") AccountBookForm accountBookForm) {


        if (accountBookForm.getPriceType().equals("income")) {
            accountBookService.updateIncome(id, accountBookForm);
        } else if (accountBookForm.getPriceType().equals("expense")) {
            accountBookService.updateExpense(id, accountBookForm);
        }
        return "redirect:/account-book";
    }
}