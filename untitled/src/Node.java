import java.util.*;

public class Node {
    int id;
    String text;
    Node left, right;

    public Node(int id, String text) {
        this.id = id;
        this.text = text.trim();
    }

    boolean isLeaf(){
        return left == null && right ==null;
    }

    @Override
    public String toString(){
        return id + ", " + text;
    }

}



