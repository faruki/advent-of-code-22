package com.mycompany.app.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
        Set<Character> firstCompartmentItemSet = generateItemSet(firstCompartment);
        Set<Character> secondCompartmentItemSet = generateItemSet(secondCompartment);
        AtomicInteger score = new AtomicInteger(0);
        firstCompartmentItemSet.stream()
                .filter(secondCompartmentItemSet::contains)
                .forEach(character -> score.getAndAdd(getCharacterPriority(character)));
        return score.get();
    }

    private Set<Character> generateItemSet(final String firstCompartment) {
        Set<Character> compartmentItemSet = new HashSet<>();
        for (final char c : firstCompartment.toCharArray()) {
            compartmentItemSet.add(c);
        }
        return compartmentItemSet;
    }

    private int getCharacterPriority(final char key) {
        if (Character.isLowerCase(key)) {
            return key - 'a' + 1;
        }
        return key - 'A' + 27;
    }

}
