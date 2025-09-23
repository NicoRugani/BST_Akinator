import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class CharacterTree {
    private Node root;

//builds tree from file
    public void readTree(String filename) throws IOException{
        File f = new File(filename);
        if(!f.exists()){
            //if the file doesnt exist, start with a small sample tree
            root = new Node(1, "real");
            root.left = new Node(0, "Tom Brady");
            root.right = new Node(2, "Batman");
            return;
        }
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))){
            String line;
            while((line = br.readLine()) != null){
                line = line.strip();
                if(line.isEmpty()){
                    continue;
                }

                String[] parts = line.split(",", 2);

                if(parts.length !=2){
                    continue;
                }
                int id = Integer.parseInt(parts[0].trim());
                String text = parts[1].trim();
                insert(id,text);
            }
        }
    }

    public void insert(int id, String text){
        Node n = new Node(id, text);
        if(root == null){
            root = n;
            return;
        }
        Node current = root, parent = null;
        while(current != null){
            parent = current;
            if(id < current.id){
                current = current.left;
            }
            else{
                current = current.right;
            }
        }
        if(id < parent.id){
            parent.left = n;
        }
        else{
            parent.right = n;
        }
    }

    public void play(Scanner in){
        boolean again = true;
        while(again){
            System.out.println("Think of a character. Ready? (y/n): ");
            if(!yes(in)) break;

            List<String> path = new ArrayList<>();
            Node current = root;

            while(!current.isLeaf()){
                String q =formatQuestion(current.text);
                System.out.printf("Is your character %s? (y/n): ", q);
                boolean answer = yes(in);
            }
        }
    }

    private static boolean yes(Scanner in){
        while(true){
            String s = in.nextLine().strip().toLowerCase();
            if ( s.startsWith("y") ) return true;
            if ( s.startsWith("n") ) return false;
            System.out.println("Please answer y or n: ");
        }
    }




}
