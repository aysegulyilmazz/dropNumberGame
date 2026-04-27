package dropnumbergame;

public class Node {

    // Tile value and its position on the 7x5 board
    int value;
    int row;
    int col;

    /*
     * Directional pointers used for logical neighbor relationships.
     * up/down connect nodes in the same column.
     * left/right connect nodes in the same row.
     */
    Node up;
    Node down;
    Node left;
    Node right;

     /*
     * Main traversal pointer.
     * All nodes can be reached from the head pointer by following nextAll.
     */
    Node nextAll;

     /*
     * Constructor creates a new tile node with its value and position.
     * All pointers are initialized as null because connections are updated later.
     */
    public Node(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;

        this.up = null;
        this.down = null;
        this.left = null;
        this.right = null;
        this.nextAll = null;
    }
}
