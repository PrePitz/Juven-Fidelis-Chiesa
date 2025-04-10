package com.mycompany.birthdaygui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BirthdayGUI extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BirthdayGUI());
    }

    public JTextField nameField = new JTextField(15);
    public JTextField dateField = new JTextField(10);
    public JButton calculateButton = new JButton("Hitung");
    public JTextArea resultArea = new JTextArea(5, 30);

    public BirthdayGUI() {
        setTitle("Kalkulator Umur & Ulang Tahun");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nama:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Tanggal Lahir (yyyy-mm-dd):"));
        inputPanel.add(dateField);
        inputPanel.add(calculateButton);

        resultArea.setEditable(false);
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Tambahkan ActionListener ke tombol "Hitung"
        calculateButton.addActionListener(e -> calculateAgeAndBirthday());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Metode untuk menghitung usia dan hari ulang tahun berikutnya
    private void calculateAgeAndBirthday() {
        String name = nameField.getText().trim();
        String dateString = dateField.getText().trim();

        // Periksa apakah input kosong
        if (name.isEmpty() || dateString.isEmpty()) {
            resultArea.setText("Mohon isi semua kolom!");
            return;
        }

        try {
            // Parsing tanggal lahir
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(dateString, formatter);
            LocalDate today = LocalDate.now();

            // Hitung usia
            Period age = Period.between(birthDate, today);

            // Hitung ulang tahun berikutnya
            LocalDate nextBirthday = birthDate.withYear(today.getYear());
            if (!nextBirthday.isAfter(today)) {
                nextBirthday = nextBirthday.plusYears(1);
            }
            Period daysUntilNextBirthday = Period.between(today, nextBirthday);

            // Tampilkan hasil
            resultArea.setText(String.format(
                "Halo %s,\n" +
                "Usia Anda saat ini: %d tahun, %d bulan, %d hari.\n" +
                "Hari ulang tahun berikutnya dalam: %d bulan, %d hari.",
                name,
                age.getYears(), age.getMonths(), age.getDays(),
                daysUntilNextBirthday.getMonths(), daysUntilNextBirthday.getDays()
            ));
        } catch (DateTimeParseException ex) {
            resultArea.setText("Format tanggal salah! Gunakan format yyyy-mm-dd.");
        }
    }
}
