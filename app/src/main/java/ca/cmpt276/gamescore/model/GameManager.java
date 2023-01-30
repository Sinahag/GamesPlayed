package ca.cmpt276.gamescore.model;

import java.util.ArrayList;

/*
    GameManager stores all games in the current instance and allows creation, deletion, and search
 */
public class GameManager {
    private ArrayList<Game> Games = new ArrayList<>();
    private static GameManager instance;
    /*
        Singleton Support
     */
    private GameManager() {}

    public static GameManager getInstance(){
        if (instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public ArrayList<Game> getGames(){
        return this.Games;
    }

    public void addGame(Game game){
        this.Games.add(game);
    }

    public Game indexGame(int index){
        return this.Games.get(index);
    }

    public void removeByIndex(int index){
        this.Games.remove(index);
    }

    public boolean isEmpty(){
        return this.Games.isEmpty();
    }

}
