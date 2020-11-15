package business;

/**
 * calculate the score depending on user's color it counts scores in 3
 * directions, vertical, horizontal, diagonal for each player
 *
 * @author Lin,Shirin
 */
public class Score {

    private Board board;
    private int newScore = 0;
    private int checkscore = 0;
    private int playerOneTotal = 0;
    private int playertwoTotal = 0;
    private int x;
    private int y;
    private EnumColor color;

    public Score(Board board) {
        this.board = board;
    }

    /**
     * This method calculates the score on base of the coordinates of the board
     * it counts scores in 3 directions, vertical, horizontal, diagonal for each
     * player playerOne is the client (white stone) and playerTwo consider as a
     * server(black stone)
     *
     * @return
     */
    private int newScore(Move move) {
        x = move.getX();
        y = move.getY();
        color = move.getColor();

        newScore = 0;
        if (x - 2 >= 0) {
            if (color == board.getColor(x - 1, y) && color == board.getColor(x - 2, y)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }

        if (x + 2 < 11) {
            if (color == board.getColor(x + 1, y) && color == board.getColor(x + 2, y)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }

        if (y - 2 >= 0) {
            if (color == board.getColor(x, y - 1) && color == board.getColor(x, y - 2)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }

        if (y + 2 < 11) {
            if (color == board.getColor(x, y + 1) && color == board.getColor(x, y + 2)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }

        if (x - 2 >= 0 && y - 2 >= 0) {
            if (color == board.getColor(x - 1, y - 1) && color == board.getColor(x - 2, y - 2)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }

        if (x + 2 < 11 && y + 2 < 11) {
            if (color == board.getColor(x + 1, y + 1) && color == board.getColor(x + 2, y + 2)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }

        if (x - 2 >= 0 && y + 2 < 11) {
            if (color == board.getColor(x - 1, y + 1) && color == board.getColor(x - 2, y + 2)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }

        if (x + 2 < 11 && y - 2 >= 0) {
            if (color == board.getColor(x + 1, y - 1) && color == board.getColor(x + 2, y - 2)) {
                newScore++;
                if (color == color.WHITE) {
                    this.playerOneTotal++;
                } else if (color == color.BLACK) {
                    this.playertwoTotal++;

                }
            }
        }
        return newScore;
    }

    public int getNewScroe(Move move) {
        return newScore(move);
    }

    /**
     * Get total score for client
     */
    public int getWhiteScore() {
        return this.playerOneTotal;
    }

    /**
     * Get total score for server
     */
    public int getBlackScore() {
        return this.playertwoTotal;
    }

    /**
     * This method uses only for prediction of the server then it would not
     * change the real scores of the client or server This method calculates the
     * score on base of the coordinates of the board it counts scores in 3
     * directions, vertical, horizontal, diagonal for each player
     *
     * @return
     */
    private int newScore(int x, int y, EnumColor color) {
        checkscore = 0;
        if (x - 2 >= 0) {
            if (color == board.getColor(x - 1, y) && color == board.getColor(x - 2, y)) {
                checkscore++;
            }
        }

        if (x + 2 < 11) {
            if (color == board.getColor(x + 1, y) && color == board.getColor(x + 2, y)) {
                checkscore++;
            }
        }

        if (x + 1 < 11 && x - 1 >= 0) {
            if (color == board.getColor(x - 1, y) && color == board.getColor(x + 1, y)) {
                checkscore++;
            }
        }

        if (y - 2 >= 0) {
            if (color == board.getColor(x, y - 1) && color == board.getColor(x, y - 2)) {
                checkscore++;
            }
        }

        if (y - 1 >= 0 && y + 1 < 11) {
            if (color == board.getColor(x, y - 1) && color == board.getColor(x, y + 1)) {
                checkscore++;
            }
        }

        if (y + 2 < 11) {
            if (color == board.getColor(x, y + 1) && color == board.getColor(x, y + 2)) {
                checkscore++;
            }
        }

        if (x - 2 >= 0 && y - 2 >= 0) {
            if (color == board.getColor(x - 1, y - 1) && color == board.getColor(x - 2, y - 2)) {
                checkscore++;
            }
        }

        if (x + 2 < 11 && y + 2 < 11) {
            if (color == board.getColor(x + 1, y + 1) && color == board.getColor(x + 2, y + 2)) {
                checkscore++;
            }
        }

        if (x - 2 >= 0 && y + 2 < 11) {
            if (color == board.getColor(x - 1, y + 1) && color == board.getColor(x - 2, y + 2)) {
                checkscore++;
            }
        }

        if (x + 2 < 11 && y - 2 >= 0) {
            if (color == board.getColor(x + 1, y - 1) && color == board.getColor(x + 2, y - 2)) {
                checkscore++;
            }
        }
        return checkscore;
    }

    /**
     * This method uses only for prediction of the server then it would not
     * change the real scores of the client or server and we can get the total
     * score for each player
     */
    public int getCheckScore(int x1, int y1, EnumColor color1) {

        if (color1 == EnumColor.WHITE) {
            return newScore(x1, y1, color1) + playerOneTotal;
        } else {
            return newScore(x1, y1, color1);
        }
    }
}
