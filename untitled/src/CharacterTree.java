// Students: Nico Rugani, <Partner Name>
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CharacterTree {
    private Node root;

    // ===== Part 1: Load from file using insert(id) =====
    public void readTree(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            // Minimal starter so the game runs even if no file is present.
            root = new Node(1, "real");
            root.left  = new Node(0, "Tom Brady");
            root.right = new Node(2, "Batman");
            return;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 2);
                if (parts.length != 2) continue;
                int id;
                try {
                    id = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException nfe) {
                    continue;
                }
                String text = parts[1].trim();
                insert(new Node(id, text));
            }
        }
        if (root == null) {
            root = new Node(1, "real");
            root.left  = new Node(0, "Tom Brady");
            root.right = new Node(2, "Batman");
        }
    }

    // Standard BST insert by id to reconstruct the intended shape
    public void insert(Node n) {
        if (root == null) { root = n; return; }
        Node cur = root, parent = null;
        while (cur != null) {
            parent = cur;
            cur = (n.id < cur.id) ? cur.left : cur.right;
        }
        if (n.id < parent.id) parent.left = n; else parent.right = n;
    }

    // ===== Part 2: Identify characters (traverse & ask prompts) =====
    public void play(Scanner in) {
        boolean again = true;
        while (again) {
            System.out.print("Think of a character. Ready? (y/n): ");
            if (!yes(in)) break;

            List<String> path = new ArrayList<>();
            Node current = root;

            // Traverse internal nodes
            while (current != null && !current.isLeaf()) {
                String q = formatQuestion(current.text);
                System.out.printf("Is your character %s? (y/n): ", q);
                boolean ans = yes(in);
                path.add(ans ? q : ("not " + q));
                current = ans ? current.left : current.right;
            }

            if (current == null) {
                System.out.println("[Error] Inconsistent tree branch. Ending round.");
                System.out.print("Play again? (y/n): ");
                again = yes(in);
                continue;
            }

            // Guess at leaf
            System.out.printf("Are you thinking of %s? (y/n): ", current.text);
            if (yes(in)) {
                System.out.print("Hooray! I guessed correctly! Play again? (y/n): ");
                again = yes(in);
            } else {
                // Print path per rubric before learning
                printPathMiss(path, current.text);
                learnNewCharacter(in, current);
                System.out.print("Got it. Want to play again? (y/n): ");
                again = yes(in);
            }
        }
    }

    // Learning per assignment:
    // - Replace old leaf text with NEW trait (internal node)
    // - YES/left = NEW character leaf
    // - NO/right = OLD character leaf
    private void learnNewCharacter(Scanner in, Node leaf) {
        String oldName = leaf.text;

        System.out.print("Who were you thinking of? ");
        String newName = promptNonEmpty(in);

        System.out.printf(
                "Give me a yes/no trait that is TRUE for %s but FALSE for %s.\nTrait: ",
                newName, oldName);
        String newTrait = stripEndingQuestionMark(promptNonEmpty(in));

        // Convert leaf to internal question node
        leaf.text = newTrait;
        leaf.left  = new Node(-1, newName);  // YES branch (has the trait)
        leaf.right = new Node(-1, oldName);  // NO branch (previous guess)
    }

    // ===== Part 3: Persistence (relabel inorder, write breadth-first) =====
    public void relabelAndSave(String path) throws IOException {
        int[] counter = new int[]{0};
        relabelInOrder(root, counter);
        List<Node> bfs = breadthFirst(root);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            for (Node n : bfs) {
                bw.write(n.id + ", " + n.text);
                bw.newLine();
            }
        }
    }

    private void relabelInOrder(Node n, int[] counter) {
        if (n == null) return;
        relabelInOrder(n.left, counter);
        n.id = counter[0]++;
        relabelInOrder(n.right, counter);
    }

    private List<Node> breadthFirst(Node r) {
        List<Node> out = new ArrayList<>();
        if (r == null) return out;
        ArrayDeque<Node> q = new ArrayDeque<>();
        q.add(r);
        while (!q.isEmpty()) {
            Node cur = q.removeFirst();
            out.add(cur);
            if (cur.left != null) q.addLast(cur.left);
            if (cur.right != null) q.addLast(cur.right);
        }
        return out;
    }

    // ===== Helpers =====
    private static boolean yes(Scanner in) {
        while (true) {
            String s = in.nextLine().trim().toLowerCase(Locale.ROOT);
            if (s.startsWith("y")) return true;
            if (s.startsWith("n")) return false;
            System.out.print("Please answer y or n: ");
        }
    }

    private static String promptNonEmpty(Scanner in) {
        while (true) {
            String s = in.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.print("Please enter something: ");
        }
    }

    private static String formatQuestion(String s) {
        return stripEndingQuestionMark(s.trim());
    }

    private static String stripEndingQuestionMark(String s) {
        String t = s.trim();
        if (t.endsWith("?")) t = t.substring(0, t.length() - 1).trim();
        return t;
    }

    // Prints: "I don't know any real, not an athlete, singer characters that are not X"
    private static void printPathMiss(List<String> path, String leafName) {
        if (path.isEmpty()) {
            System.out.printf("I don't know any characters that are not %s.%n", leafName);
            return;
        }
        String joined = String.join(", ", path);
        System.out.printf("I don't know any %s characters that are not %s.%n", joined, leafName);
    }

    // Optional: debug the current tree
    public void printBreadthFirst() {
        for (Node n : breadthFirst(root)) {
            System.out.println(n.id + " -> " + n.text + (n.isLeaf() ? " [Leaf]" : " [Q]"));
        }
    }
}
