package com.mycompany.app.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RockPaperScissors {

    private static final String FILE_PATH = "/rock_paper_scissors.txt";

    public static void main(final String[] args) throws IOException {
        final RockPaperScissors rockPaperScissors = new RockPaperScissors();
        System.out.println("My total score: " + rockPaperScissors.calculateOverallScore());
    }

    private int calculateOverallScore() throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return collectScoreFromInputStream(inputStream);
    }

    private int collectScoreFromInputStream(final InputStream inputStream) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int totalScore = 0;
            while ((line = br.readLine()) != null) {
                final String[] roundAsStringArray = line.split(" ");
                totalScore += getRoundScore(roundAsStringArray[0].charAt(0), roundAsStringArray[1].charAt(0));
            }
            return totalScore;
        }
    }

    private int getRoundScore(final char opponentChoice, final char myChoice) {
        final int shapeScore = getScoreBasedOnShape(myChoice);
        final int outcomeScore = getOutcomeScore(opponentChoice, myChoice);
        return shapeScore + outcomeScore;
    }

    private int getScoreBasedOnShape(final char choice) {
        switch (choice) {
            case 'A':
            case 'X':
                return 1;
            case 'B':
            case 'Y':
                return 2;
            case 'C':
            case 'Z':
            default:
                return 3;
        }
    }

    private int getOutcomeScore(final char opponentChoice, char myChoice) {
        final int myChoiceAsInt = getScoreBasedOnShape(myChoice);
        final int opponentChoiceAsInt = getScoreBasedOnShape(opponentChoice);
        if (myChoiceAsInt == opponentChoiceAsInt) {
            return 3;
        }
        if (myChoiceAsInt == 1 && opponentChoiceAsInt == 3 ||
                myChoiceAsInt == 2 && opponentChoiceAsInt == 1 ||
                myChoiceAsInt == 3 && opponentChoiceAsInt == 2) {
            return 6;
        }
        return 0;
    }
}
