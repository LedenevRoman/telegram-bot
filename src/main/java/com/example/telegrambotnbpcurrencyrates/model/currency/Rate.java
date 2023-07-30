package com.example.telegrambotnbpcurrencyrates.model.currency;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Rate {
    private String no;
    private LocalDate effectiveDate;
    private Double mid;
}
