package com.colak.springtutorial.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeadlockController {

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    // http://localhost:8080/create-deadlock
    @GetMapping("/create-deadlock")
    public String createDeadlock() {
        Thread thread1 = new Thread(this::task1);
        Thread thread2 = new Thread(this::task2);

        thread1.start();
        thread2.start();

        return "Deadlock created! Check the application logs to observe the behavior.";
    }

    private void task1() {
        synchronized (lock1) {
            System.out.println("Thread 1: Holding lock 1...");

            try {
                Thread.sleep(100); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Thread 1: Waiting for lock 2...");
            synchronized (lock2) {
                System.out.println("Thread 1: Acquired lock 2.");
            }
        }
    }

    private void task2() {
        synchronized (lock2) {
            System.out.println("Thread 2: Holding lock 2...");

            try {
                Thread.sleep(100); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Thread 2: Waiting for lock 1...");
            synchronized (lock1) {
                System.out.println("Thread 2: Acquired lock 1.");
            }
        }
    }
}

