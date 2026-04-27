package dropnumbergame;

public class DropNumberGame {

    public static void main(String[] args) {

        // Create the multi-linked list structure that represents the game board
        MultiLinkedList grid = new MultiLinkedList();

        
        int[][] moves = {
            {2, 0}, {2, 3}, {4, 1}, {2, 2}, {4, 4},
            {2, 1}, {4, 4}, {8, 0}, {8, 0},
            {32, 1}, {2, 2}, {64, 2}, {16, 3},
            {64, 1}, {32, 2}, {16, 0}, {16, 4}, {32, 2},
            {64, 1}, {8, 3}, {4, 3},
            {2, 3}, {2, 3}, {2, 1}, {64, 2}, {32, 2}, {16, 2}, {8, 2},
            {8, 2}, {4, 1}, {8, 1}
        };

        // Controls whether the game should stop
        boolean gameOver = false;

        // Process all predefined moves step by step
        for (int i = 0; i < moves.length; i++) {
            if (gameOver) {
                break;
            }

            /*
             * Get the current move information.
             * moves[i][0] is the tile value to be dropped.
             * moves[i][1] is the column where the tile will be placed.
             */
            int value = moves[i][0];
            int col = moves[i][1];

            // Print step information before applying the move
            System.out.println("\n======================================");
            System.out.println("STEP " + (i + 1));
            System.out.println("In this step, a tile with value " + value + " will be dropped.");
            System.out.println("The tile will be placed in column " + col + ".");
            System.out.println("======================================");

            // If the selected column is full, the game ends
            if (grid.getColumnSize(col) == grid.MAX_ROWS) {
                System.out.println("GAME OVER!");
                gameOver = true;
                break;
            }

            // Drop the tile, apply merge operations, and display the updated board
            grid.dropTile(col, value);
            grid.displayGrid();
            System.out.println();
        }
    }
}