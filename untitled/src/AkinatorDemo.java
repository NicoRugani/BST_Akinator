
import java.io.IOException;
import java.util.Scanner;

public class AkinatorDemo {
    public static void main(String[] args) {
        String file = (args.length > 0) ? args[0] : "tree.txt";
        CharacterTree tree = new CharacterTree();

        try {
            tree.readTree(file);   //load with insert()
        } catch (IOException e) {
            System.err.println("Failed to read " + file + ": " + e.getMessage());
        }

        try (Scanner in = new Scanner(System.in)) {
            tree.play(in);    // ask prompts & learn on misses
        }

        try {
            tree.relabelAndSave(file); //relabel inorder, write BFS
            System.out.println("Knowledge saved to " + file + ". Goodbye!");
        } catch (IOException e) {
            System.err.println("Failed to save to " + file + ": " + e.getMessage());
        }
    }
}
