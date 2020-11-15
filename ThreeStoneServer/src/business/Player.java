
package business;

/**
 * This class will handle the Player/Machine with what color they are,
 * the current score, and the amount of stones they have left to play with.
 * 
 * @author Michael Mishin 1612993
 */
public class Player {
    private final EnumColor playerColor;
    private int currentScore;
    private int stonesLeft;
    
    /**
     * Constructor for the Class Player.
     * 
     * @param color The color of the player
     */
    private Player(EnumColor color)
    {
        if(color != EnumColor.BLACK || color != EnumColor.WHITE)
        {
            throw new IllegalArgumentException("Color given is invalid."
                    + " You need BLACK or RED but you used: "+ color);
        }
        this.playerColor = color;
        this.currentScore = 0;
        this.stonesLeft = 15; //every player starts with 15 stones.
    }
    
    /**
     * This method returns the current score of the player.
     * @return Current Score
     */
    private int getCurrentScore()
    {
        return this.currentScore;
    }
    
    /**
     * This method will set the new score of the player.
     * It will run exceptions if the score given is less than the last score of the player.
     * @param newScore 
     */
    private void setCurrentScore(int newScore)
    {
        //Validation of the argument.
        if(this.currentScore > newScore)
        {
            throw new IllegalArgumentException("The new score should be greater than the previous score.");
        }
        
        this.currentScore = newScore;
    }
    
    /**
     * This method returns the current stones the player has left to play.
     * @return Current Stones.
     */
    private int getStonesLeft()
    {
        return this.stonesLeft;
    }
    
    /**
     * This method will subtract 1 stone from the total stones the player has left to play.
     * This method should be called after the player/machine have made a move.
     * It will check if the amount of stones left is greater than 0 to insure the player does not play more than he should.
     */
    private void subtractStone()
    {
        this.stonesLeft--; // subtracting a stone 
        if(this.stonesLeft < 0)
        {
            throw new IllegalArgumentException("You did more moves than what you are allowed.");
        }
    }
}
