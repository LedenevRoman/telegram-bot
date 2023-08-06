package com.example.telegrambotnbpcurrencyrates.model.currency;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Currency {
    private String name;
    private String code;
    private LocalDate effectiveDate;
    private Double rate;
}
