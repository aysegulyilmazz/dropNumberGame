package dropnumbergame;

public class MultiLinkedList {

    // Single entry point of the multi-linked list
    Node head;

    // Board dimensions
    final int MAX_ROWS = 7;
    final int MAX_COLS = 5;

    public MultiLinkedList() {
        head = null;
    }

    // Checks whether the board is empty
    public boolean isEmpty() {
        return head == null;
    }

    // -------------------------------
    // Main traversal and search methods
    // -------------------------------
    // Adds a new node to the main nextAll chain
    public void addNodeToAllNodesList(Node newNode) {
        newNode.nextAll = head;
        head = newNode;
    }

    // Finds a node at the given row and column
    public Node findNode(int row, int col) {
        Node current = head;

        while (current != null) {
            if (current.row == row && current.col == col) {
                return current;
            }
            current = current.nextAll;
        }

        return null;
    }

    // Finds the top node in a specific column
    public Node getTopNodeInColumn(int col) {
        Node current = head;
        Node topNode = null;

        while (current != null) {
            if (current.col == col) {
                if (topNode == null || current.row < topNode.row) {
                    topNode = current;
                }
            }
            current = current.nextAll;
        }

        return topNode;
    }

    // Counts the number of nodes in a column
    public int getColumnSize(int col) {
        Node current = head;
        int count = 0;

        while (current != null) {
            if (current.col == col) {
                count++;
            }
            current = current.nextAll;
        }

        return count;
    }

    // -------------------------------
    // Link update methods
    // -------------------------------
    // Rebuilds left and right links in the given row
    public void updateRowLinks(int row) {
        Node current = head;

        while (current != null) {
            if (current.row == row) {
                current.left = null;
                current.right = null;
            }
            current = current.nextAll;
        }

        Node prev = null;

        for (int col = 0; col < MAX_COLS; col++) {
            Node node = findNode(row, col);

            if (node != null) {
                node.left = prev;

                if (prev != null) {
                    prev.right = node;
                }

                prev = node;
            }
        }
    }

    // Rebuilds up and down links in the given column
    public void rebuildColumnLinks(int col) {
        Node prev = null;

        for (int row = 0; row < MAX_ROWS; row++) {
            Node node = findNode(row, col);

            if (node != null) {
                node.up = prev;

                if (prev != null) {
                    prev.down = node;
                }

                prev = node;
            }
        }

        if (prev != null) {
            prev.down = null;
        }
    }

    // Removes a node from the main chain and neighbor links
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node == head) {
            head = head.nextAll;
        } else {
            Node current = head;

            while (current != null && current.nextAll != node) {
                current = current.nextAll;
            }

            if (current != null) {
                current.nextAll = node.nextAll;
            }
        }

        if (node.up != null) {
            node.up.down = node.down;
        }

        if (node.down != null) {
            node.down.up = node.up;
        }

        if (node.left != null) {
            node.left.right = node.right;
        }

        if (node.right != null) {
            node.right.left = node.left;
        }

        node.up = null;
        node.down = null;
        node.left = null;
        node.right = null;
        node.nextAll = null;
    }

    // -------------------------------
    // Game operation methods
    // -------------------------------
    // Moves nodes down in a column when empty spaces exist below them
    public void dropNodesDownInColumn(int col) {
        boolean moved;

        do {
            moved = false;

            for (int row = MAX_ROWS - 2; row >= 0; row--) {
                Node node = findNode(row, col);

                if (node != null && findNode(row + 1, col) == null) {
                    node.row = row + 1;
                    moved = true;
                }
            }

        } while (moved);

        for (int row = 0; row < MAX_ROWS; row++) {
            updateRowLinks(row);
        }

        rebuildColumnLinks(col);
    }

    // Adds a new tile to the selected column without merging
    public void addTileOnly(int col, int value) {
        if (col < 0 || col >= MAX_COLS) {
            return;
        }
        if (getColumnSize(col) == MAX_ROWS) {
            return;
        }

        Node topNode = getTopNodeInColumn(col);

        if (topNode == null) {
            Node newNode = new Node(value, MAX_ROWS - 1, col);
            addNodeToAllNodesList(newNode);
            updateRowLinks(newNode.row);
            rebuildColumnLinks(col);
        } else {
            Node newNode = new Node(value, topNode.row - 1, col);
            addNodeToAllNodesList(newNode);
            newNode.down = topNode;
            topNode.up = newNode;
            updateRowLinks(newNode.row);
            rebuildColumnLinks(col);
        }
    }

    // Performs all possible merges in the selected column
    public void checkAndMerge(int col) {
        boolean changed;

        do {
            changed = false;

            for (int row = MAX_ROWS - 1; row > 0; row--) {
                Node lower = findNode(row, col);
                Node upper = findNode(row - 1, col);

                if (lower != null && upper != null && lower.value == upper.value) {
                    lower.value *= 2;
                    removeNode(upper);
                    updateRowLinks(row - 1);
                    updateRowLinks(row);
                    rebuildColumnLinks(col);
                    dropNodesDownInColumn(col);
                    changed = true;
                    break;
                }
            }
        } while (changed);
    }

    // Performs only one merge step, mainly used for GUI animation
    public boolean mergeOneStep(int col) {
        for (int row = MAX_ROWS - 1; row > 0; row--) {
            Node lower = findNode(row, col);
            Node upper = findNode(row - 1, col);

            if (lower != null && upper != null && lower.value == upper.value) {
                lower.value *= 2;
                removeNode(upper);

                updateRowLinks(row - 1);
                updateRowLinks(row);
                rebuildColumnLinks(col);
                dropNodesDownInColumn(col);

                return true;
            }
        }

        return false;
    }

    // Drops a tile and then completes the merge process
    public void dropTile(int col, int value) {
        addTileOnly(col, value);
        checkAndMerge(col);
    }

    // -------------------------------
    // Display method
    // -------------------------------
    // Prints the current board state to the console
    public void displayGrid() {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                Node found = findNode(row, col);

                if (found != null) {
                    System.out.printf("%-6d", found.value);
                } else {
                    System.out.printf("%-6s", ".");
                }
            }
            System.out.println();
        }

        System.out.println("--------------------------");
    }
}
