package com.example.okozukai.form;

import lombok.Data;

import java.sql.Date;

@Data
public class AccountBookForm {

    private Date itemDate;

    private String item;

    private int income;

    private int expense;

    private String note;

}
