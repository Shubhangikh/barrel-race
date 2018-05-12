package com.cwl.barrelrace.barrelracegame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.cwl.barrelrace.barrelracegame.R;

import java.util.Collections;

public class Home extends AppCompatActivity {

    //Request codes for various options
    private static final int REQUEST_CODE_GAME = 1;
    private static final int REQUEST_CODE_HIGH_SCORES = 2;
    private static final int REQUEST_CODE_INFO = 3;
    private static final int REQUEST_CODE_SETTINGS = 4;

    /* Called when the activity is launched */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the layout for the activity
        setContentView(R.layout.activity_home);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);



        // Enabling support for Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Play Game button
        Button btnPlayGame = findViewById(R.id.btnPlayGame);
        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starting the Game Activity
                Intent intent = new Intent(getApplicationContext(), Game.class);
                Home.this.startActivityForResult(intent, REQUEST_CODE_GAME);
            }
        });
        //High Scores button
        Button btnHighScores = findViewById(R.id.btnHighScores);
        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starting the HighScores Activity
                Intent intent = new Intent(getApplicationContext(), HighScores.class);
                Home.this.startActivityForResult(intent, REQUEST_CODE_HIGH_SCORES);
            }
        });

        //Settings button
        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starting the Settings Activity
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Home.this.startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /* Method for inflating the menu on action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    /* Method for doing required stuff based on the option selected from the action bar menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.game_exit) {
            finish();
            System.exit(0);
            return true;
        }
        if(id == R.id.game_info) {
            Intent intent = new Intent(this, Instructions.class);
            startActivityForResult(intent, REQUEST_CODE_INFO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
