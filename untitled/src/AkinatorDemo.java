import java.io.IOException;
import java.util.Scanner;

public class AkinatorDemo {
    public static void main(String[] args) {
        String file = (args.length > 0) ? args[0] : "tree.txt";
        CharacterTree tree = new CharacterTree();

        try{
            tree.readTree(file);
        } catch(IOException e){
            System.err.println("Failed to read  " + file + ": " + e.getMessage());
        }

        try(Scanner in = new Scanner(System.in)){
            tree.play(in);
        }

    }
}
