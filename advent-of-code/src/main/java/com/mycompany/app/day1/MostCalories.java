package com.mycompany.app.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

public class MostCalories {

    private static final String FILE_PATH = "/calories.txt";

    public static void main(final String[] args) throws IOException {
        final MostCalories mostCalories = new MostCalories();
        System.out.println("Top Elf: " + mostCalories.getMaxCalories(1));
        System.out.println("Top 3 Elves: " + mostCalories.getMaxCalories(3));
    }

    int getMaxCalories(final int numberOfElves) throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return getMostCaloriesFromInputStream(inputStream, numberOfElves);
    }

    private int getMostCaloriesFromInputStream(final InputStream inputStream, final int numberOfElves) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            final PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
            int currentCalories = 0;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    currentCalories += Integer.parseInt(line);
                } else {
                    priorityQueue.add(currentCalories);
                    if (priorityQueue.size() > numberOfElves) {
                        priorityQueue.poll();
                    }
                    currentCalories = 0;
                }
            }
            priorityQueue.add(currentCalories);
            if (priorityQueue.size() > numberOfElves) {
                priorityQueue.poll();
            }

            int totalCalories = 0;
            while (!priorityQueue.isEmpty()) {
                totalCalories += priorityQueue.poll();
            }
            return totalCalories;
        }
    }
}
