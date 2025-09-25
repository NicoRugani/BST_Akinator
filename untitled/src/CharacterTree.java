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
                path.add(answer ? q: (" not " + q));
                current = answer ? current.left : current.right;
            }

            //reached a leaf
            System.out.printf("Are you thinking of %s? (y/n): ", current.text);
            if(yes(in)){
                System.out.println("Horray! I guessed correctly! Would you like to play again? (y/n): ");
                again = yes(in);
            }
            else{
                printPathMiss(path, current.text);
                learnNewCharacter(in, current);
                System.out.println("Darn! next time! Would you like to play again? (y/n): ");
                again = yes(in);
            }
        }
    }

    private static String formatQuestion(String s){
        String t = s.strip();
        if (t.endsWith("?")) t = t.substring(0, t.length() - 1).trim();
        return t;
    }

    private static boolean yes(Scanner in){
        while(true){
            String s = in.nextLine().strip().toLowerCase();
            if ( s.startsWith("y") ) return true;
            if ( s.startsWith("n") ) return false;
            System.out.println("Please answer y or n: ");
        }
    }

    private static void printPathMiss(List<String> path, String leafName){
        if(path.isEmpty()){
            System.out.printf("I don't know any characters that are not %s.%n", leafName);
            return;
        }

        String joined = String.join(", ", path);
        System.out.printf("I don't know any %s characters that are not %s.%n", joined, leafName);
    }

    private void learnNewCharacter(Scanner in, Node leaf){
        String oldName = leaf.text;
        System.out.println("What character were you thinking of?: ");
        String newName = readNonEmpty(in);
    }


    private static String readNonEmpty(Scanner in){
        while(true){
            String s = in.nextLine().strip();
            if(!s.isEmpty()) return s;
            System.out.print("Please enter something; ");
        }
    }

    public void relabelAndSave(String path) throws IOException {
        int [] counter = new int[]{0};

        relabelInOrder(root, counter);

        List<Node> breadthFirst = BreadthFirstList(root);
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))){
            for(Node n : breadthFirst){
                bw.write(n.id + ", " + n.text);
                bw.newLine();
            }
        }
    }

    private void relabelInOrder(Node n, int[] counter){
        if(n == null) return;
        relabelInOrder(n.left, counter);
        n.id = counter[0]++;
        relabelInOrder(n.right, counter);
    }

    private List<Node> BreadthFirstList(Node root){
        List<Node> out = new ArrayList<>();
        if(root == null) return out;
        ArrayDeque<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while(!queue.isEmpty()){
            Node n = queue.remove();
            out.add(n);
            if(n.left != null) queue.add(n.left);
            if(n.right != null) queue.add(n.right);

        }
        return out;
    }

    public void printBreadthFirst(){
        for(Node n : BreadthFirstList(root)){
            System.out.println(n.id + " -> " + n.text + (n.isLeaf() ? " [Leaf]" : "[Q]"));
        }
    }




}
