package com.mycompany.testthread;

class PrintNameThread extends Thread {
    PrintNameThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        String name = getName();
        for (int i = 0; i < 100; i++) {
            System.out.print(name);
        }
    }
}

class TestThread {
    public static void main(String args[]) {
        PrintNameThread pnt1 = new PrintNameThread("A");
        PrintNameThread pnt2 = new PrintNameThread("B");
        PrintNameThread pnt3 = new PrintNameThread("C");
        PrintNameThread pnt4 = new PrintNameThread("D");

        // Start the threads explicitly
        pnt1.start();
        pnt2.start();
        pnt3.start();
        pnt4.start();
    }
}
