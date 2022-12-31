package com.mycompany.app.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * --- Day 8: Treetop Tree House ---
 * <p>
 * The expedition comes across a peculiar patch of tall trees all planted carefully in a grid. The Elves explain that a previous expedition planted these trees as a reforestation effort. Now, they're curious if this would be a good location for a tree house.
 * <p>
 * First, determine whether there is enough tree cover here to keep a tree house hidden. To do this, you need to count the number of trees that are visible from outside the grid when looking directly along a row or column.
 * <p>
 * The Elves have already launched a quadcopter to generate a map with the height of each tree (your puzzle input). For example:
 * <p>
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Each tree is represented as a single digit whose value is its height, where 0 is the shortest and 9 is the tallest.
 * <p>
 * A tree is visible if all of the other trees between it and an edge of the grid are shorter than it. Only consider trees in the same row or column; that is, only look up, down, left, or right from any given tree.
 * <p>
 * All of the trees around the edge of the grid are visible - since they are already on the edge, there are no trees to block the view. In this example, that only leaves the interior nine trees to consider:
 * <p>
 * The top-left 5 is visible from the left and top. (It isn't visible from the right or bottom since other trees of height 5 are in the way.)
 * The top-middle 5 is visible from the top and right.
 * The top-right 1 is not visible from any direction; for it to be visible, there would need to only be trees of height 0 between it and an edge.
 * The left-middle 5 is visible, but only from the right.
 * The center 3 is not visible from any direction; for it to be visible, there would need to be only trees of at most height 2 between it and an edge.
 * The right-middle 3 is visible from the right.
 * In the bottom row, the middle 5 is visible, but the 3 and 4 are not.
 * With 16 trees visible on the edge and another 5 visible in the interior, a total of 21 trees are visible in this arrangement.
 * <p>
 * Consider your map; how many trees are visible from outside the grid?
 * <p>
 * Your puzzle answer was 1803.
 * <p>
 * --- Part Two ---
 * Content with the amount of tree cover available, the Elves just need to know the best spot to build their tree house: they would like to be able to see a lot of trees.
 * <p>
 * To measure the viewing distance from a given tree, look up, down, left, and right from that tree; stop if you reach an edge or at the first tree that is the same height or taller than the tree under consideration. (If a tree is right on the edge, at least one of its viewing distances will be zero.)
 * <p>
 * The Elves don't care about distant trees taller than those found by the rules above; the proposed tree house has large eaves to keep it dry, so they wouldn't be able to see higher than the tree house anyway.
 * <p>
 * In the example above, consider the middle 5 in the second row:
 * <p>
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Looking up, its view is not blocked; it can see 1 tree (of height 3).
 * Looking left, its view is blocked immediately; it can see only 1 tree (of height 5, right next to it).
 * Looking right, its view is not blocked; it can see 2 trees.
 * Looking down, its view is blocked eventually; it can see 2 trees (one of height 3, then the tree of height 5 that blocks its view).
 * A tree's scenic score is found by multiplying together its viewing distance in each of the four directions. For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).
 * <p>
 * However, you can do even better: consider the tree of height 5 in the middle of the fourth row:
 * <p>
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Looking up, its view is blocked at 2 trees (by another tree with a height of 5).
 * Looking left, its view is not blocked; it can see 2 trees.
 * Looking down, its view is also not blocked; it can see 1 tree.
 * Looking right, its view is blocked at 2 trees (by a massive tree of height 9).
 * This tree's scenic score is 8 (2 * 2 * 1 * 2); this is the ideal spot for the tree house.
 * <p>
 * Consider each tree on your map. What is the highest scenic score possible for any tree?
 * <p>
 * Your puzzle answer was 268912.
 */
public class TreetopTreeHouse {

    private static final String FILE_PATH = "/treetop_tree_house.txt";
    private final List<List<Tree>> treeGrid = new ArrayList<>();

    private int topScore;

    public static void main(final String[] args) throws IOException {
        final TreetopTreeHouse treetopTreeHouse = new TreetopTreeHouse();
        System.out.println("Number of visible trees: " + treetopTreeHouse.getVisibleTreeCount());
        System.out.println("Top Viewing Score: " + treetopTreeHouse.topScore);
    }

    int getVisibleTreeCount() throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        generateGrid(inputStream);
        generateTreeGrid();
        return countVisibleTrees();
    }

    private int countVisibleTrees() {
        int count = 0;
        for (final List<Tree> row : treeGrid) {
            for (Tree tree : row) {
                if (tree.isVisible()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void generateGrid(final InputStream inputStream) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    List<Tree> currentRow = new ArrayList<>();
                    for (char c : line.toCharArray()) {
                        currentRow.add(new Tree(Integer.parseInt(String.valueOf(c))));
                    }
                    treeGrid.add(currentRow);
                }
            }
        }
    }

    private void generateTreeGrid() {
        scanRowsLeft();
        scanRowsRight();
        scanColsTop();
        scanColsBottom();
    }

    private void scanRowsLeft() {
        for (final List<Tree> trees : treeGrid) {
            trees.get(0).setVisible(true);
            for (int j = 1; j < trees.size(); j++) {
                final Tree currentTree = trees.get(j);
                boolean isVisible = true;
                for (int k = j - 1; k >= 0; k--) {
                    currentTree.setViewingDistanceLeft(currentTree.getViewingDistanceLeft() + 1);
                    if (trees.get(k).getHeight() >= currentTree.getHeight()) {
                        isVisible = false;
                        break;
                    }
                }
                currentTree.setVisible(isVisible);
            }
        }
    }

    private void scanRowsRight() {
        for (final List<Tree> trees : treeGrid) {
            trees.get(trees.size() - 1).setVisible(true);
            for (int j = trees.size() - 2; j >= 0; j--) {
                final Tree currentTree = trees.get(j);
                boolean isVisible = true;
                for (int k = j + 1; k < trees.size(); k++) {
                    currentTree.setViewingDistanceRight(currentTree.getViewingDistanceRight() + 1);
                    if (trees.get(k).getHeight() >= currentTree.getHeight()) {
                        isVisible = false;
                        break;
                    }
                }
                currentTree.setVisible(currentTree.isVisible() || isVisible);
            }
        }
    }

    private void scanColsTop() {
        for (int i = 0; i < treeGrid.size(); i++) {
            treeGrid.get(0).get(i).setVisible(true);
            for (int j = 1; j < treeGrid.get(i).size(); j++) {
                final Tree currentTree = treeGrid.get(j).get(i);
                boolean isVisible = true;
                for (int k = j - 1; k >= 0; k--) {
                    currentTree.setViewingDistanceTop(currentTree.getViewingDistanceTop() + 1);
                    if (treeGrid.get(k).get(i).getHeight() >= currentTree.getHeight()) {
                        isVisible = false;
                        break;
                    }
                }
                currentTree.setVisible(currentTree.isVisible() || isVisible);
            }
        }
    }

    private void scanColsBottom() {
        for (int i = 0; i < treeGrid.size(); i++) {
            treeGrid.get(treeGrid.size() - 1).get(i).setVisible(true);
            for (int j = treeGrid.size() - 2; j >= 0; j--) {
                final Tree currentTree = treeGrid.get(j).get(i);
                boolean isVisible = true;
                for (int k = j + 1; k < treeGrid.size(); k++) {
                    currentTree.setViewingDistanceBottom(currentTree.getViewingDistanceBottom() + 1);
                    if (treeGrid.get(k).get(i).getHeight() >= currentTree.getHeight()) {
                        isVisible = false;
                        break;
                    }
                }
                currentTree.setVisible(currentTree.isVisible() || isVisible);

                final int scenicScore = currentTree.getViewingDistanceBottom() * currentTree.getViewingDistanceTop() *
                        currentTree.getViewingDistanceLeft() * currentTree.getViewingDistanceRight();
                currentTree.setScenicScore(scenicScore);
                topScore = Math.max(topScore, scenicScore);
            }
        }
    }

    private static class Tree {
        private int height;

        private boolean isVisible;

        private int viewingDistanceLeft;

        private int viewingDistanceRight;

        private int viewingDistanceTop;

        private int viewingDistanceBottom;

        private int scenicScore;

        Tree() {

        }

        Tree(int height, boolean isVisible) {
            this.height = height;
            this.isVisible = isVisible;
        }

        public Tree(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public int getViewingDistanceLeft() {
            return viewingDistanceLeft;
        }

        public int getViewingDistanceRight() {
            return viewingDistanceRight;
        }

        public int getViewingDistanceTop() {
            return viewingDistanceTop;
        }

        public int getViewingDistanceBottom() {
            return viewingDistanceBottom;
        }

        public int getScenicScore() {
            return scenicScore;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setVisible(boolean visible) {
            isVisible = visible;
        }

        public void setViewingDistanceLeft(int viewingDistanceLeft) {
            this.viewingDistanceLeft = viewingDistanceLeft;
        }

        public void setViewingDistanceRight(int viewingDistanceRight) {
            this.viewingDistanceRight = viewingDistanceRight;
        }

        public void setViewingDistanceTop(int viewingDistanceTop) {
            this.viewingDistanceTop = viewingDistanceTop;
        }

        public void setViewingDistanceBottom(int viewingDistanceBottom) {
            this.viewingDistanceBottom = viewingDistanceBottom;
        }

        public void setScenicScore(int scenicScore) {
            this.scenicScore = scenicScore;
        }
    }
}
