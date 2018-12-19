package com.khanh.sample;

public class Customer {
    private String name;
    private int age;
    private double credit;

    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
        this.credit = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
}
