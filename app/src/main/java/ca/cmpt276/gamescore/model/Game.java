package ca.cmpt276.gamescore.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/*
    Game class stores the players information, time, and can return the winner(s) of the game
 */
public class Game {
    public static final String TIE_MESSAGE = "Tie game";
    public static final String DATE_TIME_FORMAT = "MMM dd @ hh:mma";
    private final LocalDateTime time;
    private ArrayList<PlayerScore> Players;

    public Game() {
        this.time = LocalDateTime.now();
        this.Players = new ArrayList<>();
    }


    public int getNumPlayers() {
        return this.Players.size();
    }

    public void addPlayer(int playerID, int numWagerCards, int sumPoints, int numCards) {
        PlayerScore player = new PlayerScore(numWagerCards, sumPoints, numCards);
        if (playerID>0) {
            this.Players.set(playerID-1,player);
        }else{
            this.Players.add(player);
        }
    }

    public int getPlayerScore(int player) {
        player = player - 1;  // index of player is off by 1 (arraylist is 0-indexed)
        return this.Players.get(player).getScore();
    }

    public int getPlayerNumCard(int player) {
        player = player - 1;  // index of player is off by 1 (arraylist is 0-indexed)
        return this.Players.get(player).getNumCards();
    }

    public int getPlayerNumWagers(int player) {
        player = player - 1;  // index of player is off by 1 (arraylist is 0-indexed)
        return this.Players.get(player).getNumWagerCards();
    }

    public int getPlayerSumCards(int player) {
        player = player - 1;  // index of player is off by 1 (arraylist is 0-indexed)
        return this.Players.get(player).getSumPointsCards();
    }



    public String getDateTime() {
        // formats the time for the UI
        return this.time.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    public String Winner() {
        // report the player(s) who got the highest player score
        int max_score = -1000000;
        // find the maximum scorer
        for (PlayerScore player : this.Players) {
            if (player.getScore() > max_score) {
                max_score = player.getScore();
            }
        }
        int Player_Number = 1;
        //find all the players who attained this high score
        ArrayList<Integer> topPlayers = new ArrayList<>();
        for (PlayerScore player : this.Players) {
            if (player.getScore() == max_score) {
                topPlayers.add(Player_Number);
            }
            Player_Number++;
        }

        if (topPlayers.size() > 1) {
            return TIE_MESSAGE;
        } else {
            return "Player " + topPlayers.get(0) + " won";
        }
    }

    public String getRecord() {
        if (Objects.equals(this.Winner(), TIE_MESSAGE)) {
            return this.getDateTime() + " - " + this.Winner() + " : " +
                    this.getPlayerScore(1) + " vs " + this.getPlayerScore(2);
        }else{
            return this.getDateTime() + " - " + this.Winner() + ": " +
                    this.getPlayerScore(1) + " vs " + this.getPlayerScore(2);
        }
    }
}
