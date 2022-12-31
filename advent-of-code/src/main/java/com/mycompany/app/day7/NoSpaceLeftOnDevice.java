package com.mycompany.app.day7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * --- Day 7: No Space Left On Device ---
 * <p>
 * You can hear birds chirping and raindrops hitting leaves as the expedition proceeds. Occasionally, you can even hear much louder sounds in the distance; how big do the animals get out here, anyway?
 * <p>
 * The device the Elves gave you has problems with more than just its communication system. You try to run a system update:
 * <p>
 * $ system-update --please --pretty-please-with-sugar-on-top
 * Error: No space left on device
 * Perhaps you can delete some files to make space for the update?
 * <p>
 * You browse around the filesystem to assess the situation and save the resulting terminal output (your puzzle input). For example:
 * <p>
 * $ cd /
 * $ ls
 * dir a
 * 14848514 b.txt
 * 8504156 c.dat
 * dir d
 * $ cd a
 * $ ls
 * dir e
 * 29116 f
 * 2557 g
 * 62596 h.lst
 * $ cd e
 * $ ls
 * 584 i
 * $ cd ..
 * $ cd ..
 * $ cd d
 * $ ls
 * 4060174 j
 * 8033020 d.log
 * 5626152 d.ext
 * 7214296 k
 * The filesystem consists of a tree of files (plain data) and directories (which can contain other directories or files). The outermost directory is called /. You can navigate around the filesystem, moving into or out of directories and listing the contents of the directory you're currently in.
 * <p>
 * Within the terminal output, lines that begin with $ are commands you executed, very much like some modern computers:
 * <p>
 * cd means change directory. This changes which directory is the current directory, but the specific result depends on the argument:
 * cd x moves in one level: it looks in the current directory for the directory named x and makes it the current directory.
 * cd .. moves out one level: it finds the directory that contains the current directory, then makes that directory the current directory.
 * cd / switches the current directory to the outermost directory, /.
 * ls means list. It prints out all of the files and directories immediately contained by the current directory:
 * 123 abc means that the current directory contains a file named abc with size 123.
 * dir xyz means that the current directory contains a directory named xyz.
 * Given the commands and output in the example above, you can determine that the filesystem looks visually like this:
 * <p>
 * - / (dir)
 * - a (dir)
 * - e (dir)
 * - i (file, size=584)
 * - f (file, size=29116)
 * - g (file, size=2557)
 * - h.lst (file, size=62596)
 * - b.txt (file, size=14848514)
 * - c.dat (file, size=8504156)
 * - d (dir)
 * - j (file, size=4060174)
 * - d.log (file, size=8033020)
 * - d.ext (file, size=5626152)
 * - k (file, size=7214296)
 * Here, there are four directories: / (the outermost directory), a and d (which are in /), and e (which is in a). These directories also contain files of various sizes.
 * <p>
 * Since the disk is full, your first step should probably be to find directories that are good candidates for deletion. To do this, you need to determine the total size of each directory. The total size of a directory is the sum of the sizes of the files it contains, directly or indirectly. (Directories themselves do not count as having any intrinsic size.)
 * <p>
 * The total sizes of the directories above can be found as follows:
 * <p>
 * The total size of directory e is 584 because it contains a single file i of size 584 and no other directories.
 * The directory a has total size 94853 because it contains files f (size 29116), g (size 2557), and h.lst (size 62596), plus file i indirectly (a contains e which contains i).
 * Directory d has total size 24933642.
 * As the outermost directory, / contains every file. Its total size is 48381165, the sum of the size of every file.
 * To begin, find all of the directories with a total size of at most 100000, then calculate the sum of their total sizes. In the example above, these directories are a and e; the sum of their total sizes is 95437 (94853 + 584). (As in this example, this process can count files more than once!)
 * <p>
 * Find all of the directories with a total size of at most 100000. What is the sum of the total sizes of those directories?
 * <p>
 * Your puzzle answer was 1077191.
 * <p>
 * --- Part Two ---
 * <p>
 * Now, you're ready to choose a directory to delete.
 * <p>
 * The total disk space available to the filesystem is 70000000. To run the update, you need unused space of at least 30000000. You need to find a directory you can delete that will free up enough space to run the update.
 * <p>
 * In the example above, the total size of the outermost directory (and thus the total amount of used space) is 48381165; this means that the size of the unused space must currently be 21618835, which isn't quite the 30000000 required by the update. Therefore, the update still requires a directory with total size of at least 8381165 to be deleted before it can run.
 * <p>
 * To achieve this, you have the following options:
 * <p>
 * Delete directory e, which would increase unused space by 584.
 * Delete directory a, which would increase unused space by 94853.
 * Delete directory d, which would increase unused space by 24933642.
 * Delete directory /, which would increase unused space by 48381165.
 * Directories e and a are both too small; deleting them would not free up enough space. However, directories d and / are both big enough! Between these, choose the smallest: d, increasing unused space by 24933642.
 * <p>
 * Find the smallest directory that, if deleted, would free up enough space on the filesystem to run the update. What is the total size of that directory?
 * <p>
 * Your puzzle answer was 5649896.
 */
public class NoSpaceLeftOnDevice {

    private static final String FILE_PATH = "/no_space_left_on_device.txt";
    private static final int TOTAL_DISK_SPACE = 70000000;
    private static final int REQUIRED_DISK_SPACE = 30000000;

    public static void main(final String[] args) throws IOException {
        final NoSpaceLeftOnDevice noSpaceLeftOnDevice = new NoSpaceLeftOnDevice();
        System.out.println("Total used storage with limit 100000: " + noSpaceLeftOnDevice.getTotalSizeWithLimit());
        System.out.println("Smallest directory to delete: " + noSpaceLeftOnDevice.getSmallestPossibleDir());
    }

    private int getSmallestPossibleDir() throws IOException {
        final Node root = initFileSystem();
        final int availableDiskSpace = TOTAL_DISK_SPACE - root.getSize();
        final int requiredDirectorySize = REQUIRED_DISK_SPACE - availableDiskSpace;
        return getSmallestPossibleDir(root, requiredDirectorySize);
    }

    private int getSmallestPossibleDir(final Node root, final int requiredDirSize) {
        if (root == null || root.isFile() || root.getSize() < requiredDirSize) {
            return TOTAL_DISK_SPACE;
        }
        int smallestPossibleDirSize = root.getSize();
        for (Map.Entry<String, Node> entry : root.getChildren().entrySet()) {
            final Node currentChildNode = entry.getValue();
            smallestPossibleDirSize = Math.min(smallestPossibleDirSize, getSmallestPossibleDir(currentChildNode, requiredDirSize));
        }
        return smallestPossibleDirSize;
    }

    int getTotalSizeWithLimit() throws IOException {
        final Node root = initFileSystem();
        return getTotalSizeOfDirectoriesLessThan(root, 100000);
    }

    private Node initFileSystem() throws IOException {
        final InputStream inputStream = this.getClass().getResourceAsStream(FILE_PATH);
        final Node root = populateFileSystem(inputStream);
        calculateTreeNodeSizes(root);
        return root;
    }

    private int getTotalSizeOfDirectoriesLessThan(final Node root, final int limit) {
        if (root == null || root.isFile()) {
            return 0;
        }
        int total = root.getSize() <= limit ? root.getSize() : 0;
        for (Map.Entry<String, Node> entry : root.getChildren().entrySet()) {
            final Node currentChildNode = entry.getValue();
            total += getTotalSizeOfDirectoriesLessThan(currentChildNode, limit);
        }
        return total;
    }

    private int calculateTreeNodeSizes(final Node root) {
        if (root == null) {
            return 0;
        }
        int totalSize = root.getSize();
        for (Map.Entry<String, Node> entry : root.getChildren().entrySet()) {
            final Node currentChildNode = entry.getValue();
            if (currentChildNode.isDir()) {
                totalSize += calculateTreeNodeSizes(entry.getValue());
            } else {
                totalSize += currentChildNode.getSize();
            }
        }
        root.setSize(totalSize);
        return totalSize;
    }

    private Node populateFileSystem(final InputStream inputStream) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            final Node root = new Node("/", 0, true, false, null);
            Node currentNode = root;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    if (line.startsWith("$ ls")) {
                        continue;
                    }
                    if (line.startsWith("$ cd")) {
                        currentNode = changeDirectory(currentNode, line.substring(5));
                    } else if (line.startsWith("dir")) {
                        createDirectory(currentNode, line.substring(4));
                    } else {
                        final String[] splittedString = line.split(" ");
                        createFile(currentNode, splittedString[0], splittedString[1]);
                    }
                }
            }
            return root;
        }
    }

    private void createFile(final Node currentNode, final String fileSize, final String fileName) {
        currentNode.getChildren().put(fileName, new Node(fileName, Integer.parseInt(fileSize), false, true, currentNode));
    }

    private void createDirectory(final Node currentNode, final String directoryName) {
        currentNode.getChildren().put(directoryName, new Node(directoryName, 0, true, false, currentNode));
    }

    private Node changeDirectory(final Node currentNode, final String destinationDirectory) {
        if ("..".equals(destinationDirectory)) {
            return currentNode.getParent();
        }
        return currentNode.getChildren().get(destinationDirectory);
    }

    private static class Node {
        private String name;
        private int size;
        private boolean isDir;
        private boolean isFile;
        private Map<String, Node> children;
        private Node parent;

        public Node(String name, int size, boolean isDir, boolean isFile, Node parent) {
            this.name = name;
            this.size = size;
            this.isDir = isDir;
            this.isFile = isFile;
            this.children = new HashMap<>();
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public boolean isDir() {
            return isDir;
        }

        public boolean isFile() {
            return isFile;
        }

        public Map<String, Node> getChildren() {
            return children;
        }

        public Node getParent() {
            return parent;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setSize(final int size) {
            this.size = size;
        }

        public void setDir(final boolean dir) {
            isDir = dir;
        }

        public void setFile(final boolean file) {
            isFile = file;
        }

        public void setChildren(final Map<String, Node> children) {
            this.children = children;
        }

        public void setParent(final Node parent) {
            this.parent = parent;
        }
    }
}
