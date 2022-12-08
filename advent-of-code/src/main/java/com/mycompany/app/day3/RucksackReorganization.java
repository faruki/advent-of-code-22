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

/**
 * --- Day 3: Rucksack Reorganization ---
 * <p>
 * One Elf has the important job of loading all of the rucksacks with supplies for the jungle journey. Unfortunately, that Elf didn't quite follow the packing instructions, and so a few items now need to be rearranged.
 * <p>
 * Each rucksack has two large compartments. All items of a given type are meant to go into exactly one of the two compartments. The Elf that did the packing failed to follow this rule for exactly one item type per rucksack.
 * <p>
 * The Elves have made a list of all of the items currently in each rucksack (your puzzle input), but they need your help finding the errors. Every item type is identified by a single lowercase or uppercase letter (that is, a and A refer to different types of items).
 * <p>
 * The list of items for each rucksack is given as characters all on a single line. A given rucksack always has the same number of items in each of its two compartments, so the first half of the characters represent items in the first compartment, while the second half of the characters represent items in the second compartment.
 * <p>
 * For example, suppose you have the following list of contents from six rucksacks:
 * <p>
 * vJrwpWtwJgWrhcsFMMfFFhFp
 * jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
 * PmmdzqPrVvPwwTWBwg
 * wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
 * ttgJtRGJQctTZtZT
 * CrZsJsPPZsGzwwsLwLmpwMDw
 * The first rucksack contains the items vJrwpWtwJgWrhcsFMMfFFhFp, which means its first compartment contains the items vJrwpWtwJgWr, while the second compartment contains the items hcsFMMfFFhFp. The only item type that appears in both compartments is lowercase p.
 * The second rucksack's compartments contain jqHRNqRjqzjGDLGL and rsFMfFZSrLrFZsSL. The only item type that appears in both compartments is uppercase L.
 * The third rucksack's compartments contain PmmdzqPrV and vPwwTWBwg; the only common item type is uppercase P.
 * The fourth rucksack's compartments only share item type v.
 * The fifth rucksack's compartments only share item type t.
 * The sixth rucksack's compartments only share item type s.
 * To help prioritize item rearrangement, every item type can be converted to a priority:
 * <p>
 * Lowercase item types a through z have priorities 1 through 26.
 * Uppercase item types A through Z have priorities 27 through 52.
 * In the above example, the priority of the item type that appears in both compartments of each rucksack is 16 (p), 38 (L), 42 (P), 22 (v), 20 (t), and 19 (s); the sum of these is 157.
 * <p>
 * Find the item type that appears in both compartments of each rucksack. What is the sum of the priorities of those item types?
 * <p>
 * Your puzzle answer was 7568.
 * <p>
 * --- Part Two ---
 * As you finish identifying the misplaced items, the Elves come to you with another issue.
 * <p>
 * For safety, the Elves are divided into groups of three. Every Elf carries a badge that identifies their group. For efficiency, within each group of three Elves, the badge is the only item type carried by all three Elves. That is, if a group's badge is item type B, then all three Elves will have item type B somewhere in their rucksack, and at most two of the Elves will be carrying any other item type.
 * <p>
 * The problem is that someone forgot to put this year's updated authenticity sticker on the badges. All of the badges need to be pulled out of the rucksacks so the new authenticity stickers can be attached.
 * <p>
 * Additionally, nobody wrote down which item type corresponds to each group's badges. The only way to tell which item type is the right one is by finding the one item type that is common between all three Elves in each group.
 * <p>
 * Every set of three lines in your list corresponds to a single group, but each group can have a different badge item type. So, in the above example, the first group's rucksacks are the first three lines:
 * <p>
 * vJrwpWtwJgWrhcsFMMfFFhFp
 * jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
 * PmmdzqPrVvPwwTWBwg
 * And the second group's rucksacks are the next three lines:
 * <p>
 * wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
 * ttgJtRGJQctTZtZT
 * CrZsJsPPZsGzwwsLwLmpwMDw
 * In the first group, the only item type that appears in all three rucksacks is lowercase r; this must be their badges. In the second group, their badge item type must be Z.
 * <p>
 * Priorities for these items must still be found to organize the sticker attachment efforts: here, they are 18 (r) for the first group and 52 (Z) for the second group. The sum of these is 70.
 * <p>
 * Find the item type that corresponds to the badges of each three-Elf group. What is the sum of the priorities of those item types?
 * <p>
 * Your puzzle answer was 2780.
 */
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
