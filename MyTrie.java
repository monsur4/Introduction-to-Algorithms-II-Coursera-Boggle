import edu.princeton.cs.algs4.StdOut;

public class MyTrie {
    private static final int R = 26;
    private Node root = new Node();

    private static class Node {
        private int value = -1;
        private Node[] childNode = new Node[R];
        private int size;
    }

    public void put(String word, int val) {
        root = put(root, word, val, 0);
        // root = put(root, word, val, 0);
    }

    private Node put(Node x, String word, int val, int d) {
        if (x == null) {
            x = new Node();
            // x.childNode[word.charAt(d) - 65] = word.charAt(d)
        }
        if (d == word.length()) {
            x.value = val;
            return x;
        }

        char c = word.charAt(d);
        x.childNode[word.charAt(d) - 65] = put(x.childNode[c - 65], word, val, d + 1);
        x.size = x.childNode[word.charAt(d) - 65] != null ? 1 : 0;
        return x;
    }

    public boolean contains(String word) {
        return contains(root, word, 0);
    }

    private boolean contains(Node x, String word, int d) {
        if (x == null) return false;
        if (d == word.length()) {
            return x.value >= 0;
        }
        char c = word.charAt(d);
        return contains(x.childNode[c - 65], word, d + 1);
    }

    public boolean isPrefix(String word) {
        return isPrefix(root, word, 0);
    }

    private boolean isPrefix(Node x, String word, int d) {
        if (x == null) {
            return false;
        }
        if (d == word.length()) {
            return (x.size
                    == 1); // if the size if 1 then this word is a prefix to other words in the dict
        }
        return isPrefix(x.childNode[word.charAt(d) - 65], word, d + 1);
    }

    public static void main(String[] args) {

        MyTrie myTrie = new MyTrie();
        myTrie.put("AB", 4);
        StdOut.println(myTrie.isPrefix(""));
        StdOut.println(myTrie.isPrefix("A"));
        StdOut.println(myTrie.isPrefix("AB"));
        StdOut.println(myTrie.isPrefix("BA"));
        StdOut.println(myTrie.contains("A"));
        StdOut.println(myTrie.contains("AB"));

    }
}
