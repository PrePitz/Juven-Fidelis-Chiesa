package com.mycompany.mavenproject14;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Mavenproject14 {
    private static ArrayList<Product> products = new ArrayList<>();
    private static DefaultTableModel tableModel;
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Daftar Produk");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        String[] columnNames = {"Nama Produk", "Harga"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        
        JTextField nameField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JButton addButton = new JButton("Tambah");
        JButton deleteButton = new JButton("Hapus");
        JButton updateButton = new JButton("Ubah");
        
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            try {
                double price = Double.parseDouble(priceField.getText());
                Product product = new Product(name, price);
                products.add(product);
                tableModel.addRow(new Object[]{name, price});
                nameField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Masukkan harga yang valid!");
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                products.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                nameField.setText("");
                priceField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Tidak ada yang dipilih");
            }
        });
        
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String newName = nameField.getText();
                try {
                    double newPrice = Double.parseDouble(priceField.getText());
                    products.get(selectedRow).setName(newName);
                    products.get(selectedRow).setPrice(newPrice);
                    tableModel.setValueAt(newName, selectedRow, 0);
                    tableModel.setValueAt(newPrice, selectedRow, 1);
                    nameField.setText("");
                    priceField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Masukkan harga yang valid!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Tidak ada yang dipilih");
            }
        });
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nama:"));
        panel.add(nameField);
        panel.add(new JLabel("Harga:"));
        panel.add(priceField);
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(updateButton);
        
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
