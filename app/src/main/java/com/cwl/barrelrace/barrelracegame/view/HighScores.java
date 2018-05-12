package com.cwl.barrelrace.barrelracegame.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.cwl.barrelrace.barrelracegame.R;
import com.cwl.barrelrace.barrelracegame.adapter.HighScoresAdapter;
import com.cwl.barrelrace.barrelracegame.filemanager.HighScoresFileManager;
import com.cwl.barrelrace.barrelracegame.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScores extends AppCompatActivity {
    private ListView highScores;
    private HighScoresAdapter adapter;
    private List<Player> players;
    private HighScoresFileManager fileManager;

    /* Called when the HighScores activity starts*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the layout for the activity
        setContentView(R.layout.activity_high_scores);

        // Enabling support for Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_high_scores);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Setting the navigation icon & adding onClickListener
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Setting the content for the high scores list
        highScores = findViewById(R.id.high_scores_list);
        fileManager = new HighScoresFileManager();
        players = fileManager.readFromScoresFile(this);
        adapter = new HighScoresAdapter(this, R.layout.high_scores_chip, players);

        highScores.setAdapter(adapter);
        Player player = null;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("NEW_HIGH_SCORE")) {
            player = getIntent().getExtras().getParcelable("NEW_HIGH_SCORE");
        }
        if(player != null) {
            players.add(player);
            Collections.sort(players);
            fileManager.writeScoresToFile(players, HighScores.this);
            players = fileManager.readFromScoresFile(HighScores.this);

            adapter.notifyDataSetChanged();
        }

    }

}
