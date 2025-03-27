package com.mycompany.mavenproject8;

import java.util.*;

public class Mavenproject8 {
public static void main(String[] args) {

    Vector v = new Vector();

        //Add elements to Vector
        v.add("A");
        v.add("B");
        v.add("D");
        v.add("E");
        v.add("F");
        v.add("G");
        v.add("H");

        System.out.println("Vector contains : " + v);

        Enumeration e = v.elements();

        ArrayList aList = Collections.list(e);
        System.out.println("Arraylist contains : " + aList);
    }
}