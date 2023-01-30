package ca.cmpt276.gamescore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.gamescore.model.Game;
import ca.cmpt276.gamescore.model.GameManager;

public class NewGame extends AppCompatActivity {
    public static final int NUM_PLAYERS = 2;
    private final Game newGame = new Game();
    private GameManager Games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Games = GameManager.getInstance();
        newGame.addPlayer(0,0,0,0);
        newGame.addPlayer(0,0,0,0);
        setContentView(R.layout.activity_new_game);
        setupTimeCreated();
        setupScoreCallOnFill();

        // Enable action "up"
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("New Game Score");
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        //menu inflator
        getMenuInflater().inflate(R.menu.new_game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_save) {
            onSubmit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupScoreCallOnFill() {
        EditText et = findViewById(R.id.p1NumWagerEntry);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                createP1();
                TextView tv = findViewById(R.id.winnerTextBox);
                tv.setText(String.valueOf(newGame.Winner()));
            }
        });
        et = findViewById(R.id.p2NumWagerEntry);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                createP2();
                TextView tv = findViewById(R.id.winnerTextBox);
                tv.setText(String.valueOf(newGame.Winner()));
            }
        });
    }

    private void setupTimeCreated() {
        TextView tv = findViewById(R.id.dateText);
        tv.setText(newGame.getDateTime());
    }

    private void playerCreate(int player, String playerNumCardStr, String playerSumCardStr, String playerNumWagerStr) {
        // when the last field is entered for each player
        int NumberCards;
        int SumOfCards;
        int NumWagerCards;

        try {
            NumberCards = Integer.parseInt(playerNumCardStr);
        }catch(NumberFormatException except){
            Toast.makeText(this, R.string.empty_numCards_toast, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        try {
            SumOfCards = Integer.parseInt(playerSumCardStr);
        }catch(NumberFormatException except){
            Toast.makeText(this, R.string.empty_sumCards_toast, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        try {
            NumWagerCards = Integer.parseInt(playerNumWagerStr);
        }catch(NumberFormatException except){
            Toast.makeText(this, R.string.empty_numWager_toast, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //Validate inputs
        if(NumberCards==0 && (NumWagerCards >0 || SumOfCards>0)){
            Toast.makeText(this, R.string.invalid_num_cards_toast, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Add Player
        newGame.addPlayer(player,NumWagerCards,SumOfCards,NumberCards);
    }

    private void createP1(){
        EditText playerNumCards = findViewById(R.id.p1NumCardsEntry);
        String playerNumCardStr = playerNumCards.getText().toString();

        EditText playerSumCards = findViewById(R.id.p1SumPointsEntry);
        String playerSumCardStr = playerSumCards.getText().toString();

        EditText playerNumWager = findViewById(R.id.p1NumWagerEntry);
        String playerNumWagerStr = playerNumWager.getText().toString();

        playerCreate(1,playerNumCardStr,playerSumCardStr,playerNumWagerStr);

        try{
            TextView tv = findViewById(R.id.p1ScoreBox);
            tv.setText(String.valueOf(newGame.getPlayerScore(1)));
        }catch(Exception e){
            Toast.makeText(NewGame.this, R.string.incomplete_score,Toast.LENGTH_SHORT)
                    .show();
        }
    }
    private void createP2() {
        EditText playerNumCards = findViewById(R.id.p2NumCardsEntry);
        String playerNumCardStr = playerNumCards.getText().toString();

        EditText playerSumCards = findViewById(R.id.p2SumPointsEntry);
        String playerSumCardStr = playerSumCards.getText().toString();

        EditText playerNumWager = findViewById(R.id.p2NumWagerEntry);
        String playerNumWagerStr = playerNumWager.getText().toString();

        playerCreate(2, playerNumCardStr, playerSumCardStr, playerNumWagerStr);
        try{
            TextView tv = findViewById(R.id.p2ScoreBox);
            tv.setText(String.valueOf(newGame.getPlayerScore(2)));
        }catch(Exception e){
            Toast.makeText(NewGame.this, R.string.incomplete_score,Toast.LENGTH_SHORT).show();
        }
    }

    private void onSubmit(){
        createP1();
        createP2();
        TextView tv = findViewById(R.id.winnerTextBox);
        tv.setText(String.valueOf(newGame.Winner()));
        if (newGame.getNumPlayers()==NUM_PLAYERS) {
            Toast.makeText(this, newGame.Winner(), Toast.LENGTH_SHORT)
                    .show();
            Games.addGame(newGame);
            finish();

        }else{
            Toast.makeText(this,"Cannot Create invalid Game",Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public static Intent makeIntent(Context context){
        return new Intent(context, NewGame.class);
    }
}