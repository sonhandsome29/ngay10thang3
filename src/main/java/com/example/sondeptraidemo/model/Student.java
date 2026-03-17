package com.example.sondeptraidemo.model;
public class Student {
    private String name;
    private String message;

    // Constructor
    public Student() {
    }

    public Student(String name, String message) {
        this.name = name;
        this.message = message;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}