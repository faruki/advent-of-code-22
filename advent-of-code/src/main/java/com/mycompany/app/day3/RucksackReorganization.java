package com.mycompany.app.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RucksackReorganization {

    private static final String FILE_PATH = "/rucksack_reorganization.txt";

    public static void main(final String[] args) throws IOException {
        final RucksackReorganization rucksackReorganization = new RucksackReorganization();
        System.out.println("Sum of priorities: " + rucksackReorganization.getSumPriorities());
    }

    int getSumPriorities() throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return getSumFromInputStream(inputStream);
    }

    private int getSumFromInputStream(final InputStream inputStream) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int totalScore = 0;
            while ((line = br.readLine()) != null) {
                totalScore += getPriority(line.substring(0, line.length() / 2), line.substring(line.length() / 2));
            }
            return totalScore;
        }
    }

    private int getPriority(final String firstCompartment, final String secondCompartment) {
        Map<Character, Integer> firstCompartmentItemMap = generateItemMap(firstCompartment);
        Map<Character, Integer> secondCompartmentItemMap = generateItemMap(secondCompartment);
        int score = 0;
        for (final Map.Entry<Character, Integer> entry : firstCompartmentItemMap.entrySet()) {
            if (secondCompartmentItemMap.containsKey(entry.getKey())) {
                score += getCharacterPriority(entry.getKey());
            }
        }
        return score;
    }

    private Map<Character, Integer> generateItemMap(final String firstCompartment) {
        Map<Character, Integer> compartmentItemMap = new HashMap<>();
        for (final char c : firstCompartment.toCharArray()) {
            if (compartmentItemMap.containsKey(c)) {
                compartmentItemMap.put(c, compartmentItemMap.get(c) + 1);
            } else {
                compartmentItemMap.put(c, 1);
            }
        }
        return compartmentItemMap;
    }

    private int getCharacterPriority(final char key) {
        if (Character.isLowerCase(key)) {
            return key - 'a' + 1;
        }
        return key - 'A' + 27;
    }

}
