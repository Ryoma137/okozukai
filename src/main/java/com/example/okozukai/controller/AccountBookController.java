package com.example.okozukai.controller;


import com.example.okozukai.form.AccountBookForm;
import com.example.okozukai.service.AccountBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountBookController {

    @Autowired
    AccountBookService accountBookService;

    @GetMapping("/account-book/new")
    public String getRegisterPage(@ModelAttribute("registerInfo") AccountBookForm accountBookForm) {

        return "new";
    }

    @PostMapping("/account-book/new")
    public String registerIncome(@ModelAttribute("registerInfo") AccountBookForm accountBookForm) {

        accountBookService.registerIncome(accountBookForm);
        return "/new";

    }


}
