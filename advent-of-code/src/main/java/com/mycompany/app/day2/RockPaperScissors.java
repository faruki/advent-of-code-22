package com.mycompany.app.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RockPaperScissors {

    private static final String FILE_PATH = "/rock_paper_scissors.txt";

    public static void main(final String[] args) throws IOException {
        final RockPaperScissors rockPaperScissors = new RockPaperScissors();
        System.out.println("My total score: " + rockPaperScissors.calculateOverallScore(false));
        System.out.println("My total score following strategy: " + rockPaperScissors.calculateOverallScore(true));
    }

    int calculateOverallScore(final boolean hasStrategy) throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return collectScoreFromInputStream(inputStream, hasStrategy);
    }

    private int collectScoreFromInputStream(final InputStream inputStream, final boolean hasStrategy) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int totalScore = 0;
            while ((line = br.readLine()) != null) {
                final String[] roundAsStringArray = line.split(" ");
                totalScore += getRoundScore(roundAsStringArray[0].charAt(0), roundAsStringArray[1].charAt(0), hasStrategy);
            }
            return totalScore;
        }
    }

    private int getRoundScore(final char opponentChoice, final char myChoice, final boolean hasStrategy) {
        final char myRealChoice = hasStrategy ? getMyChoiceBasedOnOpponent(opponentChoice, myChoice) : myChoice;
        final int shapeScore = getScoreBasedOnShape(myRealChoice);
        final int outcomeScore = getOutcomeScore(opponentChoice, myRealChoice);
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

    private char getMyChoiceBasedOnOpponent(final char opponentChoice, final char strategy) {
        switch (strategy) {
            case 'X':
                return getLosingChoice(opponentChoice);
            case 'Y':
                return opponentChoice;
            case 'Z':
            default:
                return getWinningChoice(opponentChoice);
        }
    }

    private char getLosingChoice(final char opponentChoice) {
        switch (opponentChoice) {
            case 'A':
                return 'C';
            case 'B':
                return 'A';
            case 'C':
            default:
                return 'B';
        }
    }

    private char getWinningChoice(final char opponentChoice) {
        switch (opponentChoice) {
            case 'A':
                return 'B';
            case 'B':
                return 'C';
            case 'C':
            default:
                return 'A';
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
