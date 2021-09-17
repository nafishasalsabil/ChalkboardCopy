package com.example.chalkboard_copy;

public class IncomeClass {
    int id;
    String name,payment;

    public IncomeClass() {
    }

    public IncomeClass(int id, String name, String payment) {
        this.id = id;
        this.name = name;
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
