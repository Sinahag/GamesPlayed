package ca.cmpt276.gamescore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.gamescore.model.Game;
import ca.cmpt276.gamescore.model.GameManager;

public class ViewEditGame extends AppCompatActivity {
    public static final int NUM_PLAYERS = 2;
    public static final String EXTRA_POS = "Position";
    private GameManager Games;
    private Game Game;
    int gameID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Edit Game Score");

        Games = GameManager.getInstance();
        setContentView(R.layout.activity_view_edit_game);
        extractIDFromIntent();
        Game = Games.indexGame(gameID);
        populateView();
        setupTimeCreated();
        setupScoreCallOnFill();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        //menu inflator
        getMenuInflater().inflate(R.menu.edit_game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_update_edit:
                onSubmit();
                return true;
            case R.id.action_delete:
                attemptDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attemptDelete(){ // popup message to confirm deletion of game
        Builder Message = new Builder(this); // create the builder for the popup box

        Message.setTitle("Delete Game");
        Message.setMessage("Please Confirm Delete");
        Message.setCancelable(true);
        Message.setPositiveButton("Delete",         // for confirming delete
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Games.removeByIndex(gameID);
                        finish();
                    }
                });
        Message.setNegativeButton(android.R.string.cancel, // for cancelling delete
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog popup = Message.create();
        popup.show();
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
                updateP1();
                TextView tv = findViewById(R.id.winnerTextBox);
                tv.setText(String.valueOf(Game.Winner()));
            }
        });
        et = findViewById(R.id.p2NumWagerEntry);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateP2();
                TextView tv = findViewById(R.id.winnerTextBox);
                tv.setText(String.valueOf(Game.Winner()));
            }
        });
    }

    private void playerUpdate(int player, String playerNumCardStr, String playerSumCardStr, String playerNumWagerStr) {
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
        Game.addPlayer(player,NumWagerCards,SumOfCards,NumberCards);
    }

    private void updateP1(){
        EditText playerNumCards = findViewById(R.id.p1NumCardsEntry);
        String playerNumCardStr = playerNumCards.getText().toString();

        EditText playerSumCards = findViewById(R.id.p1SumPointsEntry);
        String playerSumCardStr = playerSumCards.getText().toString();

        EditText playerNumWager = findViewById(R.id.p1NumWagerEntry);
        String playerNumWagerStr = playerNumWager.getText().toString();

        playerUpdate(1,playerNumCardStr,playerSumCardStr,playerNumWagerStr);

        try{
            TextView tv = findViewById(R.id.p1ScoreBox);
            tv.setText(String.valueOf(Game.getPlayerScore(1)));
        }catch(Exception e){
            Toast.makeText(ViewEditGame.this, R.string.incomplete_score,Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void updateP2() {
        EditText playerNumCards = findViewById(R.id.p2NumCardsEntry);
        String playerNumCardStr = playerNumCards.getText().toString();

        EditText playerSumCards = findViewById(R.id.p2SumPointsEntry);
        String playerSumCardStr = playerSumCards.getText().toString();

        EditText playerNumWager = findViewById(R.id.p2NumWagerEntry);
        String playerNumWagerStr = playerNumWager.getText().toString();

        playerUpdate(2, playerNumCardStr, playerSumCardStr, playerNumWagerStr);
        try{
            TextView tv = findViewById(R.id.p2ScoreBox);
            tv.setText(String.valueOf(Game.getPlayerScore(2)));
        }catch(Exception e){
            Toast.makeText(ViewEditGame.this, R.string.incomplete_score,Toast.LENGTH_SHORT).show();
        }
    }

    private void onSubmit(){
        updateP1();
        updateP2();
        TextView tv = findViewById(R.id.winnerTextBox);
        tv.setText(String.valueOf(Game.Winner()));
        if (Game.getNumPlayers()==NUM_PLAYERS) {
            Toast.makeText(this, Game.Winner(), Toast.LENGTH_SHORT)
                    .show();
            finish();
        }else{
            Toast.makeText(this,"Cannot Create invalid Game",Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private void extractIDFromIntent() {
        Intent intent = getIntent();
        gameID = intent.getIntExtra(EXTRA_POS,0);
    }

    private void setupTimeCreated() {
        TextView tv = findViewById(R.id.dateText);
        tv.setText(Game.getDateTime());
    }

    public static Intent makeIntent(Context context, int position){
        Intent intent = new Intent(context, ViewEditGame.class);
        intent.putExtra(EXTRA_POS,position);
        return intent;
    }

    private void populateView(){
//        Player 1

        EditText playerNumCards = findViewById(R.id.p1NumCardsEntry);
        playerNumCards.setText(String.valueOf(Game.getPlayerNumCard(1)));


        EditText playerSumCards = findViewById(R.id.p1SumPointsEntry);
        playerSumCards.setText(String.valueOf(Game.getPlayerSumCards(1)));

        EditText playerNumWager = findViewById(R.id.p1NumWagerEntry);
        playerNumWager.setText(String.valueOf(Game.getPlayerNumWagers(1)));

        TextView tv = findViewById(R.id.p1ScoreBox);
        tv.setText(String.valueOf(Game.getPlayerScore(1)));
        // Player 2

        playerNumCards = findViewById(R.id.p2NumCardsEntry);
        playerNumCards.setText(String.valueOf(Game.getPlayerNumCard(2)));


        playerSumCards = findViewById(R.id.p2SumPointsEntry);
        playerSumCards.setText(String.valueOf(Game.getPlayerSumCards(2)));

        playerNumWager = findViewById(R.id.p2NumWagerEntry);
        playerNumWager.setText(String.valueOf(Game.getPlayerNumWagers(2)));

        tv = findViewById(R.id.p2ScoreBox);
        tv.setText(String.valueOf(Game.getPlayerScore(2)));

        tv = findViewById(R.id.winnerTextBox);
        tv.setText(String.valueOf(Game.Winner()));
    }


}