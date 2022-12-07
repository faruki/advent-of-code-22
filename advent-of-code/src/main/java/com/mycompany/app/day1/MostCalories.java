package com.mycompany.app.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MostCalories {

    private static final String FILE_PATH = "/calories.txt";

    public static void main(String[] args) throws IOException {
        final MostCalories mostCalories = new MostCalories();
        System.out.println(mostCalories.getMaxCalories());
    }

    private int getMaxCalories() throws IOException {
        return calculateDataFromFile();
    }

    private int calculateDataFromFile() throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return readFromInputStream(inputStream);
    }

    private int readFromInputStream(final InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int maxCalories = 0;
            int currentCalories = 0;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    currentCalories += Integer.parseInt(line);
                } else {
                    maxCalories = Math.max(currentCalories, maxCalories);
                    currentCalories = 0;
                }
            }
            return Math.max(currentCalories, maxCalories);
        }
    }
}
