package com.student;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Run the Swing UI on the Event Dispatch Thread (standard practice)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // This creates and shows your login window
                new LoginUI();
            }
        });
    }
}