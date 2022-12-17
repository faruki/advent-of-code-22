package com.mycompany.app.day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * --- Day 6: Tuning Trouble ---
 * <p>
 * The preparations are finally complete; you and the Elves leave camp on foot and begin to make your way toward the star fruit grove.
 * <p>
 * As you move through the dense undergrowth, one of the Elves gives you a handheld device. He says that it has many fancy features, but the most important one to set up right now is the communication system.
 * <p>
 * However, because he's heard you have significant experience dealing with signal-based systems, he convinced the other Elves that it would be okay to give you their one malfunctioning device - surely you'll have no problem fixing it.
 * <p>
 * As if inspired by comedic timing, the device emits a few colorful sparks.
 * <p>
 * To be able to communicate with the Elves, the device needs to lock on to their signal. The signal is a series of seemingly-random characters that the device receives one at a time.
 * <p>
 * To fix the communication system, you need to add a subroutine to the device that detects a start-of-packet marker in the datastream. In the protocol being used by the Elves, the start of a packet is indicated by a sequence of four characters that are all different.
 * <p>
 * The device will send your subroutine a datastream buffer (your puzzle input); your subroutine needs to identify the first position where the four most recently received characters were all different. Specifically, it needs to report the number of characters from the beginning of the buffer to the end of the first such four-character marker.
 * <p>
 * For example, suppose you receive the following datastream buffer:
 * <p>
 * mjqjpqmgbljsphdztnvjfqwrcgsmlb
 * After the first three characters (mjq) have been received, there haven't been enough characters received yet to find the marker. The first time a marker could occur is after the fourth character is received, making the most recent four characters mjqj. Because j is repeated, this isn't a marker.
 * <p>
 * The first time a marker appears is after the seventh character arrives. Once it does, the last four characters received are jpqm, which are all different. In this case, your subroutine should report the value 7, because the first start-of-packet marker is complete after 7 characters have been processed.
 * <p>
 * Here are a few more examples:
 * <p>
 * bvwbjplbgvbhsrlpgdmjqwftvncz: first marker after character 5
 * nppdvjthqldpwncqszvftbrmjlhg: first marker after character 6
 * nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg: first marker after character 10
 * zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw: first marker after character 11
 * How many characters need to be processed before the first start-of-packet marker is detected?
 * <p>
 * Your puzzle answer was 1804.
 * <p>
 * --- Part Two ---
 * Your device's communication system is correctly detecting packets, but still isn't working. It looks like it also needs to look for messages.
 * <p>
 * A start-of-message marker is just like a start-of-packet marker, except it consists of 14 distinct characters rather than 4.
 * <p>
 * Here are the first positions of start-of-message markers for all of the above examples:
 * <p>
 * mjqjpqmgbljsphdztnvjfqwrcgsmlb: first marker after character 19
 * bvwbjplbgvbhsrlpgdmjqwftvncz: first marker after character 23
 * nppdvjthqldpwncqszvftbrmjlhg: first marker after character 23
 * nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg: first marker after character 29
 * zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw: first marker after character 26
 * How many characters need to be processed before the first start-of-message marker is detected?
 * <p>
 * Your puzzle answer was 2508.
 */
public class TuningTrouble {

    private static final String FILE_PATH = "/tuning_trouble.txt";

    public static void main(final String[] args) throws IOException {
        final TuningTrouble tuningTrouble = new TuningTrouble();
        System.out.println("Characters to start of packet of size 4: " + tuningTrouble.getNumberOfCharsToUniqueSubstring(4));
        System.out.println("Characters to start of packet of size 14: " + tuningTrouble.getNumberOfCharsToUniqueSubstring(14));
    }

    int getNumberOfCharsToUniqueSubstring(final int windowSize) throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return getNumberOfCharsToUniqueSubstring(inputStream, windowSize);
    }

    private int getNumberOfCharsToUniqueSubstring(final InputStream inputStream, final int windowSize) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            final String dataStream = br.readLine();
            int currentIndex = windowSize;
            while (currentIndex < dataStream.length()) {
                final String currentString = dataStream.substring(currentIndex - windowSize, currentIndex);
                if (hasUniqueChars(currentString)) {
                    return currentIndex;
                }
                currentIndex++;
            }
        }
        return 0;
    }

    private boolean hasUniqueChars(final String s1) {
        final boolean[] letterArray = new boolean[26];
        for (char c : s1.toCharArray()) {
            if (letterArray[c - 'a']) {
                return false;
            }
            letterArray[c - 'a'] = true;
        }
        return true;
    }
}
