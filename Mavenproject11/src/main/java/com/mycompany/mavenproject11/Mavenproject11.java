package com.mycompany.mavenproject11;

import java.util.PriorityQueue;

public class Mavenproject11 {

    // Declare the PriorityQueue as an instance variable
    private PriorityQueue<String> stringQueue;

    public Mavenproject11() {
        // Initialize the PriorityQueue in the constructor
        stringQueue = new PriorityQueue<>();
    }

    public void processQueue() {
        // Add elements to the PriorityQueue
        stringQueue.add("ab");
        stringQueue.add("abcd");
        stringQueue.add("abc");
        stringQueue.add("a");

        // Use a loop to remove and print elements in order
        while (!stringQueue.isEmpty()) {
            System.out.println(stringQueue.remove());
        }
    }

    public static void main(String[] args) {
        // Create an instance of the class and call the method to process the queue
        Mavenproject11 app = new Mavenproject11();
        app.processQueue();
    }
}