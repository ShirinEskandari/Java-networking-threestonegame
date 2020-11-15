package business;

/**
 * This is Logic of the game Each player has 15 stone and The role of the play,
 * each player should follow the x and Y move the other score:each player can
 * get 3 stone together in diagonal, horizontal, vertical gets 1 point.
 *
 * @author Shirin, Lin
 */
public class GameEngine {

    private Board board;
    private Move move;
    private Score score;
    private int serverInitialStone = 15;
    private int clientInitialStone = 15;

    /**
     * constructor sets its board
     *
     * @param board
     */
    public GameEngine(Board board, Score score) {
        this.board = board;
        this.score = score;
    }

    /**
     * sets the move on base of the given board
     *
     * @param move
     * @param board
     */
    public GameEngine(Move move, Board board, Score score) {
        this.move = move;
        this.board = board;
        this.score = score;
        this.score.getNewScroe(move);
        this.board.setBoardForOneMove(move);
    }

    /**
     * get the board
     *
     * @return
     */
    private Board getBoard() {
        return board;
    }

    /**
     * set the board
     *
     * @param board
     */
    private void setBoard(Board board) {
        this.board = board;
    }

    /**
     * get move
     *
     * @return
     */
    private Move getMove() {
        return move;
    }

    /**
     * set move
     *
     * @param move
     */
    private void setMove(Move move) {
        this.move = move;
    }

    /**
     * This method checks if the client playes in the same coordinate X,Y of the
     * server
     *
     * @param userx
     * @param usery
     * @param serverx
     * @param servery
     * @return
     */
    public boolean hasClientChosenEmptySpot(int userx, int usery, int serverx, int servery) {
        if (serverx == -1 || servery == -1) {
            return true;
        }
        if (userx == serverx || usery == servery) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get Remaining Stone of the server
     *
     * @return
     */
    private int getRemainingServerStone() {
        return serverInitialStone - countStonesOnBoard(EnumColor.BLACK);
    }

    /**
     * Get Remaining Stone of the client
     *
     * @return
     */
    public int getRemainingClientStone() {
        return clientInitialStone - countStonesOnBoard(EnumColor.WHITE);
    }

    /**
     * Get Remaining Stone of the server and Client
     *
     * @return
     */
    private int countStonesOnBoard(EnumColor color) {
        int count = 0;
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                if (board.getColor(x, y) == color) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * This method finds all available places (horizontal and vertical) on base
     * of the client move, then it gets tootal of the point for each player and
     * predict if for new move each player put his stone in that position which
     * position would be the best for server move. it calculates points for each
     * player for each position.and returns the server move coordinates
     *
     * @return
     */
    public Move chooseTheBestOption() throws Exception {
//        AiMove serverMove = new AiMove(board,score);
//        int[] bestMove = serverMove.thebestMove(this.move);
//        return new Move(bestMove[0],bestMove[1],EnumColor.BLACK);
        
        Move serverMove = new Move(0, 0, EnumColor.BLACK);
        Move anyPossibleMove = new Move(0, 0, EnumColor.BLACK);
        int serverBestPotentialScore = score.getBlackScore();
        int clientBestPotentialScore = score.getWhiteScore();
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                if ((x == move.getX() || y == move.getY()) && board.getColor(x, y) == EnumColor.EMPTY) {
                    anyPossibleMove.setX(x);
                    anyPossibleMove.setY(y);
                    int serverScore = score.getCheckScore(x, y, EnumColor.BLACK);
                    int clientScore = score.getCheckScore(x, y, EnumColor.WHITE);
                    //client will be ahead with this move
                    if (clientScore > serverScore) {
                        //is this move will be better than previous options for client
                        if (clientScore >= clientBestPotentialScore) {
                            clientBestPotentialScore = clientScore;
                            serverMove.setX(x);
                            serverMove.setY(y);
                        }
                        //server will be ahead with this move   
                    } else if (serverScore > clientScore) {
                        //is this move will be better than previous options for server
                        if (serverScore >= serverBestPotentialScore) {
                            serverBestPotentialScore = serverScore;
                            serverMove.setX(x);
                            serverMove.setY(y);
                        }
                    }
                }
            }
        }
        if (serverBestPotentialScore != 0 || clientBestPotentialScore != 0) {
            return serverMove;
        }
        return anyPossibleMove;
    }
    }
     

