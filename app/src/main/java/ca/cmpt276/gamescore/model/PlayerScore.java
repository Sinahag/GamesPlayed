package ca.cmpt276.gamescore.model;

/*
    PlayerScore stores the various cards and sum of cards of a player.
    It can calculate the score of the player and implements standard accessors and
    throws exceptions for invalid parameters
 */
public class PlayerScore {

    public static final int NUM_CARDS_BEFORE_20_BOOST = 8;
    private int numWagerCards;
    private int sumPointsCards;
    private int numCards;

    public PlayerScore(int numWagerCards, int sumPoints, int numCards){
        if (numWagerCards<0 || sumPoints<0 || numCards<0){
            throw new IllegalArgumentException("numPlayers must be non-negative");
        }

        if (numCards == 0) {
            this.numWagerCards = 0;
            this.sumPointsCards = 0;
        }else {
            this.numWagerCards = numWagerCards;
            this.sumPointsCards = sumPoints;
        }
        this.numCards=numCards;
    }

    public int getNumWagerCards() {
        return numWagerCards;
    }

    public int getSumPointsCards() {
        return sumPointsCards;
    }

    public int getNumCards() {
        return numCards;
    }

    public int getScore(){
        int score = (sumPointsCards-20)*(numWagerCards+1);
        if (this.numCards >= NUM_CARDS_BEFORE_20_BOOST){
            return score + 20;
        }
        return score;
    }

}

