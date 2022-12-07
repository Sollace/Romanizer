package com.sollace.romanizer;

import java.io.*;

import com.sollace.romanizer.api.Romanizer;

public class Test {
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print("> ");
                String line = reader.readLine();
                if (line.startsWith("assert ")) {
                    double d = Double.valueOf(line.replace("assert ", ""));

                    for (int i = 0; i < d; i++) {
                        String romanized = Romanizer.romanize(i);
                        int dr = Romanizer.deromanize(romanized).intValue();
                        if (dr != i) {
                            throw new RuntimeException("Assertion failed! " + i + " became " + romanized + " aka " + dr);
                        }
                    }
                    System.out.println("Assertion passed!");
                } else {
                    double d = Double.valueOf(line);
                    String romanized = Romanizer.romanize(d);
                    System.out.println("I see: " + romanized + " " + Romanizer.deromanize(romanized));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.flush();
                System.out.println("> ");
            }
        }

    }
}
