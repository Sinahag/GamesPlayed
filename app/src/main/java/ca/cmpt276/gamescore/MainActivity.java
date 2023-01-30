package ca.cmpt276.gamescore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ca.cmpt276.gamescore.model.Game;
import ca.cmpt276.gamescore.model.GameManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private GameManager Games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Games = GameManager.getInstance();
        setFloatingButton();
        populateListView();
        registerClickCallback();
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Games Played");
    }
    @Override
    public void onRestart() {
        super.onRestart();
        populateListView();
    }

    private void setFloatingButton() {
        //find the button
        FloatingActionButton btn = findViewById(R.id.addGameBtn);
        // set button behaviour
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.i(TAG, "Clicked the add game button");
                Toast.makeText(MainActivity.this, "Launch Game Add Activity", Toast.LENGTH_SHORT)
                        .show();

                Intent intent = NewGame.makeIntent(MainActivity.this);
                startActivity(intent);
                populateListView();
            }
        });
    }


    private void populateListView() {
        String[] listData = new String[Games.getGames().size()];
//         Create list of items
        for(int i=0;i<Games.getGames().size();i++) {
            listData[i] = Games.getGames().get(i).getRecord();
        }

        // Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,   // activity context
                R.layout.items, // layout to use
                listData);      // data

        // Configure the list view
        ListView list = findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.listViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                Intent intent = ViewEditGame.makeIntent(MainActivity.this, position);
                startActivity(intent);
            }
        });
    }


}
