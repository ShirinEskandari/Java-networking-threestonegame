package business;

/**
 **Constructor taking in number of rows and number of columns to set up the 2D
 * array of colors (which is a board) It can set any size
 *
 * @author Shirin
 */
public class Board {

    private EnumColor[][] board;
    private EnumColor color;

    /**
     * Constructor taking in number of rows and number of columns to set up the
     * 2D array of Enum colors (which is a board) It can set any size
     *
     * @author Shirin Eskandari sep2019
     * @param start
     * @param end
     */
    public Board(int start, int end) {
        board = new EnumColor[start][end];
        for (int i = 0; i < start; i++) {
            for (int j = 0; j < end; j++) {
                board[i][j] = color.EMPTY;
                // System.out.println("from board1constructori=="+i+ "  j==  "+j);
            }
        }
    }

    /**
     * get the board
     *
     * @author Shirin Eskandari sep2019
     * @return
     */
    private EnumColor[][] getBoard() {
        return board;
    }

    /**
     * get the color for specific place of the board
     *
     * @param row
     * @param col
     * @return
     */
    public EnumColor getColor(int row, int col) {
        //System.out.println("from enumcolor row=="+row+ "  col==  "+col);
        return board[row][col];
    }

    private void setColor(int row, int col, EnumColor color) {
        board[row][col] = color;
    }

    /**
     * it gets move of the client and set the xy place of his move to the board
     * (during the game)
     *
     *
     * @param move
     */
    public void setBoardForOneMove(Move move) {
        this.board[move.getX()][move.getY()] = move.color;
    }
    
    public int getBoardLength(){
        return board.length;
    }
    
    
    /**
     * get the color? I should see if we really need it
     *
     * @return
     */
    private EnumColor getColor() {
        return color;
    }
}
