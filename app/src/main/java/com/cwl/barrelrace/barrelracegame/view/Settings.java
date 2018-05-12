package com.cwl.barrelrace.barrelracegame.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

import com.cwl.barrelrace.barrelracegame.R;
import com.cwl.barrelrace.barrelracegame.model.UserPreferences;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Switch switchLevelDifficult = (Switch) findViewById(R.id.switchDifficult);

        if(UserPreferences.getGameLevel() != null && UserPreferences.getGameLevel().equals("difficult")){
            switchLevelDifficult.setChecked(true);
        }
        if(switchLevelDifficult.isChecked()) {
            UserPreferences.setGameLevel("difficult");
        } else{

            UserPreferences.setGameLevel("easy");
        }
    }

}
