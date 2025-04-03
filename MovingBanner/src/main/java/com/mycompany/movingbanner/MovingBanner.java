package com.mycompany.movingbanner;

import javax.swing.*;
import java.awt.*;

public class MovingBanner extends JFrame implements Runnable {
    private JLabel label;
    private String text = "Your name here! ";
    
    public MovingBanner() {
        setTitle("Banner");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.BLUE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().setBackground(Color.CYAN);
        add(label);

        new Thread(this).start(); // Memulai thread untuk animasi
    }

    @Override
    public void run() {
        while (true) {
            text = text.substring(1) + text.charAt(0); // Menggeser teks
            label.setText(text);
            try {
                Thread.sleep(500); // Jeda agar pergerakan tidak terlalu cepat
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovingBanner().setVisible(true));
    }
}
