package com.student;

public class GenerateHash {
    public static void main(String[] args) {
        // You can change "student123" to any password you want
        String password = "teacher123";
        
        // Hash the password
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Print it out
        System.out.println("--- HASH GENERATED ---");
        System.out.println("Password: " + password);
        System.out.println("Copy this hash into your SQL INSERT statement:");
        System.out.println(hashedPassword);
        System.out.println("----------------------");
    }
}