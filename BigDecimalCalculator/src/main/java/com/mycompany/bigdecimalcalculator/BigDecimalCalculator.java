package com.mycompany.bigdecimalcalculator;

import java.math.BigDecimal;
import java.util.Scanner;

public class BigDecimalCalculator {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        
        System.out.print("Masukkan angka pertama: ");
        BigDecimal num1 = new BigDecimal(input.nextLine());
        
        System.out.print("Masukkan angka kedua: ");
        BigDecimal num2 = new BigDecimal(input.nextLine());
        
        System.out.println("Hasil: ");
        System.out.println("Penjumlahan: " + num1.add(num2));
        System.out.println("Pengulangan: " + num1.subtract(num2));
        System.out.println("Perkalian: " + num1.multiply(num2));
        System.out.println("Pembagian: " + num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP));
    }
}