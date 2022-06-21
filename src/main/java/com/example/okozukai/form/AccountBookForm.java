package com.example.okozukai.form;

import lombok.Data;

import java.sql.Date;

@Data
public class AccountBookForm {

    private boolean income;

    private boolean expense;

    private Date itemDate;

    private String item;

    private int price; //No.4 のタスクで追加済

    private String note;

}
