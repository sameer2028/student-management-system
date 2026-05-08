package com.student;

import java.math.BigDecimal;

// This class holds a single line item from the fee structure
public class FeeDetail {

    private String description;
    private BigDecimal amount;

    // Constructor
    public FeeDetail(String description, BigDecimal amount) {
        this.description = description;
        this.amount = amount;
    }

    // Getters
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
}