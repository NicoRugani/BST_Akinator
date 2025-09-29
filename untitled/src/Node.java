// Students: Nico Rugani, <Partner Name>
public class Node {
    int id;
    String text;
    Node left, right;

    public Node(int id, String text) {
        this.id = id;
        this.text = (text == null) ? "" : text.trim();
    }

    boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public String toString() {
        return id + ", " + text;
    }
}
