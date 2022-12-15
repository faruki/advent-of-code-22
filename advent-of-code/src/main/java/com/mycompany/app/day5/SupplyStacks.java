package com.mycompany.app.day5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * --- Day 5: Supply Stacks ---
 * <p>
 * The expedition can depart as soon as the final supplies have been unloaded from the ships. Supplies are stored in stacks of marked crates, but because the needed supplies are buried under many other crates, the crates need to be rearranged.
 * <p>
 * The ship has a giant cargo crane capable of moving crates between stacks. To ensure none of the crates get crushed or fall over, the crane operator will rearrange them in a series of carefully-planned steps. After the crates are rearranged, the desired crates will be at the top of each stack.
 * <p>
 * The Elves don't want to interrupt the crane operator during this delicate procedure, but they forgot to ask her which crate will end up where, and they want to be ready to unload them as soon as possible so they can embark.
 * <p>
 * They do, however, have a drawing of the starting stacks of crates and the rearrangement procedure (your puzzle input). For example:
 * <p>
 * [D]
 * [N] [C]
 * [Z] [M] [P]
 * 1   2   3
 * <p>
 * move 1 from 2 to 1
 * move 3 from 1 to 3
 * move 2 from 2 to 1
 * move 1 from 1 to 2
 * In this example, there are three stacks of crates. Stack 1 contains two crates: crate Z is on the bottom, and crate N is on top. Stack 2 contains three crates; from bottom to top, they are crates M, C, and D. Finally, stack 3 contains a single crate, P.
 * <p>
 * Then, the rearrangement procedure is given. In each step of the procedure, a quantity of crates is moved from one stack to a different stack. In the first step of the above rearrangement procedure, one crate is moved from stack 2 to stack 1, resulting in this configuration:
 * <p>
 * [D]
 * [N] [C]
 * [Z] [M] [P]
 * 1   2   3
 * In the second step, three crates are moved from stack 1 to stack 3. Crates are moved one at a time, so the first crate to be moved (D) ends up below the second and third crates:
 * <p>
 * [Z]
 * [N]
 * [C] [D]
 * [M] [P]
 * 1   2   3
 * Then, both crates are moved from stack 2 to stack 1. Again, because crates are moved one at a time, crate C ends up below crate M:
 * <p>
 * [Z]
 * [N]
 * [M]     [D]
 * [C]     [P]
 * 1   2   3
 * Finally, one crate is moved from stack 1 to stack 2:
 * <p>
 * [Z]
 * [N]
 * [D]
 * [C] [M] [P]
 * 1   2   3
 * The Elves just need to know which crate will end up on top of each stack; in this example, the top crates are C in stack 1, M in stack 2, and Z in stack 3, so you should combine these together and give the Elves the message CMZ.
 * <p>
 * After the rearrangement procedure completes, what crate ends up on top of each stack?
 * <p>
 * Your puzzle answer was QNNTGTPFN.
 * <p>
 * --- Part Two ---
 * As you watch the crane operator expertly rearrange the crates, you notice the process isn't following your prediction.
 * <p>
 * Some mud was covering the writing on the side of the crane, and you quickly wipe it away. The crane isn't a CrateMover 9000 - it's a CrateMover 9001.
 * <p>
 * The CrateMover 9001 is notable for many new and exciting features: air conditioning, leather seats, an extra cup holder, and the ability to pick up and move multiple crates at once.
 * <p>
 * Again considering the example above, the crates begin in the same configuration:
 * <p>
 * [D]
 * [N] [C]
 * [Z] [M] [P]
 * 1   2   3
 * Moving a single crate from stack 2 to stack 1 behaves the same as before:
 * <p>
 * [D]
 * [N] [C]
 * [Z] [M] [P]
 * 1   2   3
 * However, the action of moving three crates from stack 1 to stack 3 means that those three moved crates stay in the same order, resulting in this new configuration:
 * <p>
 * [D]
 * [N]
 * [C] [Z]
 * [M] [P]
 * 1   2   3
 * Next, as both crates are moved from stack 2 to stack 1, they retain their order as well:
 * <p>
 * [D]
 * [N]
 * [C]     [Z]
 * [M]     [P]
 * 1   2   3
 * Finally, a single crate is still moved from stack 1 to stack 2, but now it's crate C that gets moved:
 * <p>
 * [D]
 * [N]
 * [Z]
 * [M] [C] [P]
 * 1   2   3
 * In this example, the CrateMover 9001 has put the crates in a totally different order: MCD.
 * <p>
 * Before the rearrangement process finishes, update your simulation so that the Elves know where they should stand to be ready to unload the final supplies. After the rearrangement procedure completes, what crate ends up on top of each stack?
 * <p>
 * Your puzzle answer was GGNPJBTTR.
 */
public class SupplyStacks {

    private static final String FILE_PATH = "/supply_stacks.txt";
    private static final String MOVE = "move";

    public static void main(final String[] args) throws IOException {
        final SupplyStacks supplyStacks = new SupplyStacks();
        System.out.println("Combination of top crates using crane 9000: " + supplyStacks.getTopCrates(true));
        System.out.println("Combination of top crates using crane 9001: " + supplyStacks.getTopCrates(false));
    }

    String getTopCrates(final boolean moveSingleCrates) throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        return getTopCrates(inputStream, moveSingleCrates);
    }

    private String getTopCrates(final InputStream inputStream, final boolean moveSingleCrates) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            Deque<Character>[] stackArray = new ArrayDeque[0];
            boolean firstLine = true;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    if (firstLine) {
                        final int numberOfStacks = (line.length() + 1) / 4;
                        stackArray = initStackArray(numberOfStacks);
                        firstLine = false;
                    }
                    if (line.startsWith("[")) {
                        insertIntoStack(stackArray, line);
                    } else {
                        final String[] inputArray = line.split(" ");
                        if (MOVE.equals(inputArray[0])) {
                            move(stackArray, moveSingleCrates, Integer.parseInt(inputArray[1]), Integer.parseInt(inputArray[3]), Integer.parseInt(inputArray[5]));
                        }
                    }
                }
            }
            return topCratesToString(stackArray);
        }
    }

    private Deque<Character>[] initStackArray(final int size) {
        final Deque<Character>[] stackArray = new ArrayDeque[size];
        for (int i = 0; i < size; i++) {
            stackArray[i] = new ArrayDeque<>();
        }
        return stackArray;
    }

    private String topCratesToString(final Deque<Character>[] stackArray) {
        StringBuilder result = new StringBuilder();
        for (Deque<Character> stack : stackArray) {
            result.append(stack.pop());
        }
        return result.toString();
    }

    private void insertIntoStack(final Deque<Character>[] stackArray, final String line) {
        for (int i = 1; i < line.length(); i += 4) {
            char currentChar = line.charAt(i);
            if (currentChar != ' ') {
                stackArray[i / 4].addLast(currentChar);
            }
        }
    }

    private void move(final Deque<Character>[] stackArray, final boolean moveSingleCrates, final int amount, final int fromStack, final int toStack) {
        if (moveSingleCrates) {
            for (int i = 0; i < amount; i++) {
                stackArray[toStack - 1].push(stackArray[fromStack - 1].pop());
            }
        } else {
            final Deque<Character> tempStack = new ArrayDeque<>();
            for (int i = 0; i < amount; i++) {
                tempStack.push(stackArray[fromStack - 1].pop());
            }
            for (int i = 0; i < amount; i++) {
                stackArray[toStack - 1].push(tempStack.pop());
            }
        }
    }
}
