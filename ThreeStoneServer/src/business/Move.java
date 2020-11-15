package business;

/**
 * This class has X,Y coordinate of the move and Color for recognizing the
 * player, white is the client, black is the server
 *
 * @author Shirin
 */
public class Move {

    private int x;
    private int y;
    public EnumColor color;

    /**
     * set the y position of the move
     *
     * @param y
     */
    public  void setY(int y) {
        this.y = y;
    }

    /**
     * This constructor, gets the special place of the board and the color which
     * shows it is move for client or server
     *
     * @param x
     * @param y
     * @param colour
     */
    public Move(int x, int y, EnumColor color) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /**
     * get the x position of the move
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * set the x position of the move
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * get y position of the move
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * get the color of the special place
     *
     * @return
     */
    public EnumColor getColor() {
        return color;
    }

    /**
     * set the color
     *
     * @param colour
     */
    private void setColour(EnumColor color) {
        this.color = color;
    }
}
