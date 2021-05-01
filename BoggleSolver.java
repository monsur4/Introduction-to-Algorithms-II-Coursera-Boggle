import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoggleSolver {

    // private final TrieST<Integer> trie = new TrieST<>();
    private final MyTrie trie = new MyTrie();
    private boolean marked[][];
    private SET<String> allValidBoardWords;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            trie.put(dictionary[i], i);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        allValidBoardWords = new SET<>();
        int rows = board.rows();
        int columns = board.cols();
        marked = new boolean[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                char w = board.getLetter(r, c);
                marked[r][c] = true;
                String currentWord = "";
                if (w == 'Q') currentWord = currentWord + "QU";
                else currentWord = currentWord + w;
                if (w == 'Q') checkNext(currentWord, board, r, c, 2); // char Q represents a Qu
                else checkNext(currentWord, board, r, c, 1);
                marked[r][c] = false;
            }
        }
        return allValidBoardWords;
    }


    private void checkNext(String word, BoggleBoard board, int r, int c, int l) {
        // for a one-row and one-column board
        if (board.rows() == 1 && board.cols() == 1) {
            return;
        }

        // for one row board
        if (board.rows() == 1) {
            // the weird case of having only one row 1 * 10
            if (r == 0 && c == 0) {
                checkRight(word, board, r, c, l);
            }
            else if (r == 0 && c == board.cols() - 1) {
                checkLeft(word, board, r, c, l);
            }
            else {
                checkRight(word, board, r, c, l);
                checkLeft(word, board, r, c, l);
            }
        }
        else if (board.cols() == 1) { // only one column
            if (r == 0 && c == 0) {
                checkDown(word, board, r, c, l);
            }
            else if (r == board.rows() - 1 && c == 0) {
                checkUp(word, board, r, c, l);
            }
            else {
                checkDown(word, board, r, c, l);
                checkUp(word, board, r, c, l);
            }
        }
        else { // it is not a weird board
            // case a
            if (r == 0 && c == 0) {
                checkRight(word, board, r, c, l);
                checkDownRight(word, board, r, c, l);
                checkDown(word, board, r, c, l);
            }
            // case c
            else if (r == 0 && c == board.cols() - 1) {
                checkLeft(word, board, r, c, l);
                checkDownLeft(word, board, r, c, l);
                checkDown(word, board, r, c, l);
            }
            // case g
            else if (r == board.rows() - 1 && c == 0) {
                checkUp(word, board, r, c, l);
                checkUpRight(word, board, r, c, l);
                checkRight(word, board, r, c, l);
            }
            // case i
            else if (r == board.rows() - 1 && c == board.cols() - 1) {
                checkUp(word, board, r, c, l);
                checkUpLeft(word, board, r, c, l);
                checkLeft(word, board, r, c, l);
            }
            // case b
            else if (r == 0) {
                checkLeft(word, board, r, c, l);
                checkDownLeft(word, board, r, c, l);
                checkDown(word, board, r, c, l);
                checkDownRight(word, board, r, c, l);
                checkRight(word, board, r, c, l);
            }
            // case h
            else if (r == board.rows() - 1) {
                checkLeft(word, board, r, c, l);
                checkUpLeft(word, board, r, c, l);
                checkUp(word, board, r, c, l);
                checkUpRight(word, board, r, c, l);
                checkRight(word, board, r, c, l);
            }
            // case d
            else if (c == 0) {
                checkUp(word, board, r, c, l);
                checkUpRight(word, board, r, c, l);
                checkRight(word, board, r, c, l);
                checkDownRight(word, board, r, c, l);
                checkDown(word, board, r, c, l);
            }
            // case f
            else if (c == board.cols() - 1) {
                checkUp(word, board, r, c, l);
                checkUpLeft(word, board, r, c, l);
                checkLeft(word, board, r, c, l);
                checkDownLeft(word, board, r, c, l);
                checkDown(word, board, r, c, l);
            }
            // case e -- the ideal case
            else {
                checkUpLeft(word, board, r, c, l);
                checkUp(word, board, r, c, l);
                checkUpRight(word, board, r, c, l);
                checkLeft(word, board, r, c, l);
                checkRight(word, board, r, c, l);
                checkDownLeft(word, board, r, c, l);
                checkDown(word, board, r, c, l);
                checkDownRight(word, board, r, c, l);
            }
        }
    }

    private void checkHelper(String word, BoggleBoard board, int r, int c, int l) {
        if (marked[r][c]) return;
        // Iterable<String> prefs = trie.keysWithPrefix(word);
        // for (String string : prefs) {
        //     if (string == null) return;
        // }
        if (!trie.isPrefix(word)) {
            return;
        }
        marked[r][c] = true;
        char w = board.getLetter(r, c);
        if (w == 'Q') word = word + "QU";
        else word = word + w;
        int len = l;
        if (w == 'Q') len = len + 1;// increase length further by one if the char encountered is a Q
        if (len >= 3 && trie.contains(word)) {
            allValidBoardWords.add(word);
        }
        checkNext(word, board, r, c, len);
        marked[r][c] = false;
    }

    private void checkLeft(String word, BoggleBoard board, int r, int c, int l) {
        // if (marked[r][c - 1]) return;
        // marked[r][c - 1] = true;
        // word = word + board.getLetter(r, c - 1);
        // if (trie.contains(word)) {
        //     allValidBoardWords.add(word);
        // }
        // checkNext(word, board, r, c - 1);
        // marked[r][c - 1] = false;
        checkHelper(word, board, r, c - 1, l + 1);
    }

    private void checkRight(String word, BoggleBoard board, int r, int c, int l) {
        // if (marked[r][c + 1]) return;
        // marked[r][c + 1] = true;
        // word = word + board.getLetter(r, c + 1);
        // if (trie.contains(word)) {
        //     allValidBoardWords.add(word);
        // }
        // checkNext(word, board, r, c + 1);
        // marked[r][c + 1] = false;
        checkHelper(word, board, r, c + 1, l + 1);
    }

    private void checkUp(String word, BoggleBoard board, int r, int c, int l) {
        checkHelper(word, board, r - 1, c, l + 1);
    }

    private void checkDown(String word, BoggleBoard board, int r, int c, int l) {
        checkHelper(word, board, r + 1, c, l + 1);
    }

    private void checkUpLeft(String word, BoggleBoard board, int r, int c, int l) {
        checkHelper(word, board, r - 1, c - 1, l + 1);
    }

    private void checkUpRight(String word, BoggleBoard board, int r, int c, int l) {
        checkHelper(word, board, r - 1, c + 1, l + 1);
    }

    private void checkDownLeft(String word, BoggleBoard board, int r, int c, int l) {
        checkHelper(word, board, r + 1, c - 1, l + 1);
    }

    private void checkDownRight(String word, BoggleBoard board, int r, int c, int l) {
        checkHelper(word, board, r + 1, c + 1, l + 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trie.contains(word)) return 0;
        int wordLength = word.length();

        if (wordLength >= 8) {
            return 11;
        }
        else if (wordLength == 7) {
            return 5;
        }
        else if (wordLength == 6) {
            return 3;
        }
        else if (wordLength == 5) {
            return 2;
        }
        else if (wordLength == 4 || wordLength == 3) {
            return 1;
        }
        return 0;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        // BoggleBoard board = new BoggleBoard(1, 1);
        Stopwatch stopwatch = new Stopwatch();
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        StdOut.println("Time taken = " + stopwatch.elapsedTime());
        // StdOut.println(solver.getAllValidWords(board));
    }
}
