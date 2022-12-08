package com.mycompany.app.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RucksackReorganization {

    private static final String FILE_PATH = "/rucksack_reorganization.txt";
    private static final int GROUP_SIZE = 3;

    public static void main(final String[] args) throws IOException {
        final RucksackReorganization rucksackReorganization = new RucksackReorganization();
        System.out.println("Sum of priorities: " + rucksackReorganization.getSumPriorities(false));
        System.out.println("Sum of priorities as groups of 3: " + rucksackReorganization.getSumPriorities(true));
    }

    int getSumPriorities(final boolean isGroup) throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return getSumFromInputStream(inputStream, isGroup);
    }

    private int getSumFromInputStream(final InputStream inputStream, final boolean isGroup) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int totalScore = 0;
            if (!isGroup) {
                while ((line = br.readLine()) != null) {
                    final String firstHalf = line.substring(0, line.length() / 2);
                    final String secondHalf = line.substring(line.length() / 2);
                    totalScore += getPriority(Arrays.asList(firstHalf, secondHalf));
                }
            } else {
                final List<String> rucksackList = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    rucksackList.add(line);
                    if (rucksackList.size() == GROUP_SIZE) {
                        totalScore += getPriority(rucksackList);
                        rucksackList.clear();
                    }
                }
            }
            return totalScore;
        }
    }

    private int getPriority(final List<String> compartmentList) {
        String commonChars = getCommonChars(compartmentList.get(0), compartmentList.get(1));
        for (int i = 2; i < compartmentList.size(); i++) {
            commonChars = getCommonChars(commonChars, compartmentList.get(i));
        }
        int score = 0;
        for (char c : commonChars.toCharArray()) {
            score += getCharacterPriority(c);
        }
        return score;
    }

    private String getCommonChars(final String s1, final String s2) {
        final Set<Character> set1 = generateItemSet(s1);
        final Set<Character> set2 = generateItemSet(s2);
        return set1.stream().filter(set2::contains).map(Object::toString).collect(Collectors.joining());
    }

    private Set<Character> generateItemSet(final String firstCompartment) {
        final Set<Character> compartmentItemSet = new HashSet<>();
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
