package com.mycompany.bookform;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private final String[] columnNames = {"ID", "Title", "Author"};
    private JTextField titleField, authorField, idField;

    public BookForm() {
        setTitle("Book Manager GUI");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize table model and table
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Add table with scroll pane to the center
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create input panel for adding/editing books
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));
        JPanel inputPanel = new JPanel(new FlowLayout());
        idField = new JTextField(5);
        idField.setEditable(false); // ID is read-only for editing
        titleField = new JTextField(15);
        authorField = new JTextField(15);

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);

        // Create button panel for "Add", "Edit", "Delete", and "Refresh"
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        JButton editButton = new JButton("Edit Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addBookViaAPI());
        editButton.addActionListener(e -> editBookViaAPI());
        deleteButton.addActionListener(e -> deleteBookViaAPI());
        refreshButton.addActionListener(e -> loadDataFromAPI());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Add panels to the control panel
        controlPanel.add(inputPanel);
        controlPanel.add(buttonPanel);

        // Add control panel to the bottom
        add(controlPanel, BorderLayout.SOUTH);

        // Load data initially
        loadDataFromAPI();

        // Add row selection listener for the table
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                titleField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                authorField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });
    }

    private void loadDataFromAPI() {
        try {
            // Fetch data from the API
            URL url = new URL("http://localhost:4567/api/books");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String json = in.lines().collect(Collectors.joining());
            in.close();

            // Parse JSON response to a list of Book objects
            List<Book> books = new Gson().fromJson(json, new TypeToken<List<Book>>() {}.getType());

            // Clear existing data in the table
            tableModel.setRowCount(0);

            // Add new data to the table model
            for (Book book : books) {
                Object[] row = {book.getId(), book.getTitle(), book.getAuthor()};
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data:\n" + e.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBookViaAPI() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both Title and Author fields must be filled!", 
                                          "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            URL url = new URL("http://localhost:4567/api/books");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create JSON body for the new book
            String jsonBody = new Gson().toJson(new Book(0, title, author));
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                JOptionPane.showMessageDialog(this, "Book successfully added!");
                titleField.setText("");
                authorField.setText("");
                loadDataFromAPI();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book. HTTP Code: " + responseCode,
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editBookViaAPI() {
        String id = idField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", 
                                          "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            URL url = new URL("http://localhost:4567/api/books/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create JSON body for the updated book
            String jsonBody = new Gson().toJson(new Book(Integer.parseInt(id), title, author));
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Book successfully updated!");
                loadDataFromAPI();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update book. HTTP Code: " + responseCode,
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBookViaAPI() {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete!", 
                                          "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this book?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            URL url = new URL("http://localhost:4567/api/books/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                JOptionPane.showMessageDialog(this, "Book successfully deleted!");
                // Clear fields after deletion
                idField.setText("");
                titleField.setText("");
                authorField.setText("");
                loadDataFromAPI();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete book. HTTP Code: " + responseCode,
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BookForm gui = new BookForm();
                gui.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(BookForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}

class Book {
    private int id;
    private String title;
    private String author;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}