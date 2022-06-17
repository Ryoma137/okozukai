package com.example.okozukai.entity;


import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.util.Timer;

@Entity
@Data
@NoArgsConstructor
public class Account {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date itemDate;

    private String item;

    @NotNull
    private int income;

    @NotNull
    private int expense;

    private String note;

}
