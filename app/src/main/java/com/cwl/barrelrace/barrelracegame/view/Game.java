package com.cwl.barrelrace.barrelracegame.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cwl.barrelrace.barrelracegame.R;
import com.cwl.barrelrace.barrelracegame.filemanager.HighScoresFileManager;
import com.cwl.barrelrace.barrelracegame.model.Barrel;
import com.cwl.barrelrace.barrelracegame.model.Player;
import com.cwl.barrelrace.barrelracegame.model.UserPreferences;

import org.w3c.dom.Text;

import java.util.List;

public class Game extends AppCompatActivity implements SensorEventListener {

    // Request codes for various options that the player can select
    private static final int REQUEST_CODE_GAME = 1;
    private static final int REQUEST_CODE_HIGH_SCORES = 2;
    private static final int RESULT_CODE_INFO = 3;
    private static final int RESULT_CODE_SETTINGS = 4;

    // Sensor Manager and Acelerometer objects
    private SensorManager sensorManager;
    private Sensor accelerometer;

    // High Scores file, player details
    private HighScoresFileManager fileManager;
    private EditText playerName;
    private TextView timeElapsed;

    // Position coordinates for the racer
    public static float LOCATION_X;
    public static float LOCATION_Y;

    //Counter for keeping track of time and the timer thread for incrementing the counter
    public static int TIMER_COUNTER;
    TimerThread timerThread;

    // boolean for game over status
    private boolean gameOver = false;
    private boolean penalty = false;

    // ALPHA for setting the speed of the accelerometer
    private static float ALPHA = 1.5f;

    //Different dialogs for game over and when high score is made
    private Dialog gameOverDialog;
    private Dialog highScoresDialog;

    //Vibrator for vibrating on hitting the barrel & fence
    Vibrator vibrator;

    Barrel barrelObj1;
    Barrel barrelObj2;
    Barrel barrelObj3;

    ImageView barrel1;
    ImageView barrel2;
    ImageView barrel3;
    ImageView racer;

    Boolean barrel2Flag1 = false;
    Boolean barrel2Flag2 = false;
    Boolean barrel2Flag3 = false;
    Boolean barrel2Flag4 = false;

    Boolean barrel3Flag1 = false;
    Boolean barrel3Flag2 = false;
    Boolean barrel3Flag3 = false;
    Boolean barrel3Flag4 = false;

    Boolean barrel1Flag1 = false;
    Boolean barrel1Flag2 = false;
    Boolean barrel1Flag3 = false;
    Boolean barrel1Flag4 = false;

    /* OnCreate method gets called when the Game activity starts */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the orientation of the Game Screen to Landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Setting the layout for the activity
        setContentView(R.layout.activity_game);

        // Enabling support for Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_game);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
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

        //Initializing the High Scores file manager object
        fileManager = new HighScoresFileManager();

        //Accessing the Sensor Manager and getting the Accelerometer sensor using the Sensor Manager object
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Initializing the vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Initializing the timer counter to 0
        TIMER_COUNTER = 0;

        //Setting the initial position for the racer
        LOCATION_Y = 1000;
        LOCATION_X = 650;

        // Initializing the the timer thread and starting the thread
        timerThread = new TimerThread();
        timerThread.start();
        timeElapsed = (TextView) findViewById(R.id.game_time_elapsed);

        // Game over dialog with try again and back to home buttons
        gameOverDialog = new Dialog(this, R.style.GameDialog);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gameOverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOverDialog.setContentView(R.layout.game_dialog);

        Button btnTryAgain = (Button) gameOverDialog.findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver = true;
                gameOverDialog.dismiss();
                Intent intent = new Intent(Game.this, Game.class);
                startActivity(intent);
                finish();
            }
        });
        Button btnGoToHome = gameOverDialog.findViewById(R.id.btnGoToHome);
        btnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameOverDialog.dismiss();
                Intent intent = new Intent();
                setResult(REQUEST_CODE_GAME, intent);
                finish();
            }
        });
        gameOverDialog.hide();

        // High scores dialog with EditText for player name, button for saving the score, try again button and go back to home button
        highScoresDialog = new Dialog(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        highScoresDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        highScoresDialog.setContentView(R.layout.high_score_dialog);

        Button btnTryAgain1 = (Button) highScoresDialog.findViewById(R.id.btnTryAgain1);
        btnTryAgain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver = true;
                highScoresDialog.dismiss();
                Intent intent = new Intent(Game.this, Game.class);
                startActivity(intent);
                finish();
            }
        });
        Button btnGoToHome1 = highScoresDialog.findViewById(R.id.btnGoToHome1);
        btnGoToHome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highScoresDialog.dismiss();
                Intent intent = new Intent();
                setResult(REQUEST_CODE_GAME, intent);
                finish();
            }
        });

        Button btnSaveScore = highScoresDialog.findViewById(R.id.btnSave);
        btnSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerName = (EditText) highScoresDialog.findViewById(R.id.txt_player_name);
                if(playerName.getText().toString().trim().length() == 0) {
                    Toast.makeText(Game.this, "Please enter the username", Toast.LENGTH_SHORT).show();
                    return;
                }
                Player player = new Player(playerName.getText().toString().trim(), timeElapsed.getText().toString().trim());
                Intent intent = new Intent(Game.this, HighScores.class);
                intent.putExtra("NEW_HIGH_SCORE", player);
                startActivity(intent);
                finish();
            }
        });
        highScoresDialog.hide();

        barrel3 = (ImageView) findViewById(R.id.barrel3);
        barrel2 = (ImageView) findViewById(R.id.barrel2);
        barrel1 = (ImageView) findViewById(R.id.barrel1);
        racer = (ImageView) findViewById(R.id.racer);

        barrelObj1 = new Barrel(barrel1, false, false, false, false);
        barrelObj2 = new Barrel(barrel2, false, false, false, false);
        barrelObj3 = new Barrel(barrel3, false, false, false, false);

        if (UserPreferences.getGameLevel()!= null && UserPreferences.getGameLevel().equals("difficult")) {
            ALPHA = 2.5f;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.game_replay) {
            Intent intent = new Intent(Game.this, Game.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /* Called when the Game Activity is resumed */
    @Override
    protected void onResume() {
        super.onResume();

        //Register the sensor on resume
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /* Called when the Game Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        //Unregister the sensor on pause
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    /* Called when a new Sensor Event occurs*/
    @Override
    public void onSensorChanged(SensorEvent event) {

        // Check if the sensor is Accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            //Call onSensorEvent method for changing the position of the racer
            this.onSensorEvent(event);
        }
    }

    /* Called by the onSensorChanged method, checks the Game Over condition, Game Success Condition, Updates the location of the racer
       and ensures that the racer doesn't go beyond the screen  */
    private void onSensorEvent(SensorEvent event) {
        // Setting X & Y locations of the racer image based on the sensor events X,Y values and ALPHA;
        LOCATION_X = LOCATION_X + ALPHA * event.values[0];
        LOCATION_Y = LOCATION_Y + ALPHA * event.values[1];

        if (racer != null) {
            if(barrelObj1.isEncircled() && barrelObj2.isEncircled() && barrelObj3.isEncircled()){
                gameOver = true;
                sensorManager.unregisterListener(this);
                checkHighScore();
            }
            checkBarrelEncircled(barrelObj1);
            checkBarrelEncircled(barrelObj2);
            checkBarrelEncircled(barrelObj3);

            // calling the checkBoundary method to make sure that the racer doesn't move beyond the screen
            checkBoundary(racer);

            //changing the location of the racer image with updated values of LOCATION_Y, LOCATION_X
            moveRacer(racer);
        }
    }

    private void checkBarrelEncircled(Barrel barrel) {
        /* Check if racer has crossed point1
        If yes then set the flag to true*/
        if (LOCATION_Y >= (barrel.getBarrel().getX() + barrel.getBarrel().getWidth() + 5)
                && LOCATION_X >= (barrel.getBarrel().getY() + barrel.getBarrel().getWidth() + 5) && !barrel.isPoint1()) {
            barrel.setPoint1(true);
        }
        /* Check if racer has crossed point2
        If yes then set the flag to true*/
        if (LOCATION_Y >= (barrel.getBarrel().getX() + barrel.getBarrel().getWidth() + 5)
                && LOCATION_X <= (barrel.getBarrel().getY() - barrel.getBarrel().getHeight() - 5) && !barrel.isPoint2()) {
            barrel.setPoint2(true);
        }
        /* Check if racer has crossed point3
        If yes then set the flag to true*/
        if (LOCATION_Y <= (barrel.getBarrel().getX() - barrel.getBarrel().getWidth() - 5)
                && LOCATION_X <= (barrel.getBarrel().getY() - barrel.getBarrel().getHeight() - 5) & !barrel.isPoint3()) {
            barrel.setPoint3(true);
        }
        /* Check if racer has crossed point4
        If yes then set the flag to true*/
        if (LOCATION_Y <= (barrel.getBarrel().getX() - barrel.getBarrel().getWidth() - 5)
                && LOCATION_X >= (barrel.getBarrel().getY() + barrel.getBarrel().getHeight() + 5) && !barrel.isPoint4()) {
            barrel.setPoint4(true);
        }
        if (barrel.isPoint1() && barrel.isPoint2() && barrel.isPoint3() && barrel.isPoint4()) {
            barrel.getBarrel().setImageResource(R.drawable.done);
        }

    }

    /* Method to check if the racer is within the game boundary and repositioning the racer image when it goes beyond the screen*/
    private void checkBoundary(ImageView racer) {

        // get the height and width of the racer image
        int racerWidth = racer.getWidth();
        int racerHeight = racer.getHeight();

        // get the current scren width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        /*** Conditions for checking the boundary in Landscape orientation ***/

        // if LOCATION_X is above the screen shift it below
        if (LOCATION_X <= racerHeight / 2) {
            vibrator.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE));
            penalty = true;
            LOCATION_X = racerHeight / 2;
        }
        // if LOCATION_Y is on extreme left beyond the screen shift it to right
        if (LOCATION_Y <= racerWidth / 8) {
            vibrator.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE));
            penalty = true;
            LOCATION_Y = racerWidth / 8;
        }

        // if LOCATION_X is below the screen shift it above
        if (LOCATION_X >= screenHeight - 4 * racerHeight) {
            vibrator.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE));
            penalty = true;
            LOCATION_X = screenHeight - 4 * racerHeight;
        }
        // if LOCATION_Y is on extreme right beyond the screen shift it to left
        if (LOCATION_Y >= screenWidth - racerWidth) {
            vibrator.vibrate(VibrationEffect.createOneShot(50,VibrationEffect.DEFAULT_AMPLITUDE));
            penalty = true;
            LOCATION_Y = screenWidth - racerWidth;
        }
    }

    private void moveRacer(ImageView racer) {
        // calculating bounding rectangles around the racer the barrels & the fence boundaries for checking the GameOver
        // condition & penalty condition respectively
        int[] racerLocation = new int[2];
        racer.getLocationOnScreen(racerLocation);
        Rect racerRectangle = new Rect(racerLocation[0], racerLocation[1], racerLocation[0] + racer.getWidth() / 2, racerLocation[1] + racer.getHeight() / 2);

        int[] barrel1Location = new int[2];
        barrel1.getLocationOnScreen(barrel1Location);

        int[] barrel2Location = new int[2];
        barrel2.getLocationOnScreen(barrel2Location);

        int[] barrel3Location = new int[2];
        barrel3.getLocationOnScreen(barrel3Location);

        Rect barrel1Rectangle = new Rect(barrel1Location[0], barrel1Location[1], barrel1Location[0] + barrel1.getWidth() / 2, barrel1Location[1] + barrel1.getHeight() / 2);
        Rect barrel2Rectangle = new Rect(barrel2Location[0], barrel2Location[1], barrel2Location[0] + barrel2.getWidth() / 2, barrel2Location[1] + barrel2.getHeight() / 2);
        Rect barrel3Rectangle = new Rect(barrel3Location[0], barrel3Location[1], barrel3Location[0] + barrel3.getWidth() / 2, barrel3Location[1] + barrel3.getHeight() / 2);

        /*** If the racer's bounding rectangle intersects with any of the barrel's bounding rectangle then the game over condition is met
         else if the it intersects with the fence boundary the penalty condition is met else the racer position is updated
         ***/

        if (barrel1Rectangle.intersect(racerRectangle) || barrel2Rectangle.intersect(racerRectangle) || barrel3Rectangle.intersect(racerRectangle)) {
            //Vibrate
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));

            // Unregister the sensor
            sensorManager.unregisterListener(this);
            //Set the gameOver to true, this would interrupt the timerThread;
            gameOver = true;
            //display the game over dialog
            gameOverDialog.show();
        } else {
            //Update racer location
            racer.setX(LOCATION_Y);
            racer.setY(LOCATION_X);
        }
    }

    // Method for checking if the player has made a high score after successfully finishing the game
    private void checkHighScore() {
        //Read contents of the high scores file
        List<Player> players = fileManager.readFromScoresFile(Game.this);

        //Initialize the current player passing name & time taken to complete
        Player currentPlayer = new Player("user0", timeElapsed.getText().toString().trim());

        //If the players list size is less than 10, add current player to the high scores list else check if the player has made a high score or not
        if (players.size() < 10) {
            highScoresDialog.show();
        } else {
            boolean isAHighScore = false;
            for (Player player : players) {
                if (currentPlayer.compareTo(player) <= 0) {
                    isAHighScore = true;
                    break;
                }
            }
            if (isAHighScore) {
                highScoresDialog.show();
            } else {
                gameOverDialog.show();
            }

        }
    }

    /*** this is the TimerThread class for updating the timer counter while playing the game ***/
    private class TimerThread extends Thread {
        TextView text;

        public TimerThread() {
            text = (TextView) findViewById(R.id.game_time_elapsed);
        }

        public void run() {
            while (!isInterrupted()) {
                try {
                    //calculate after every one second
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String timeText = "";
                            //if gameOver is true return else keep incrementing the TIMER_COUNTER
                            if (gameOver) return;
                            else if (penalty) {
                                TIMER_COUNTER = TIMER_COUNTER + 5;
                                penalty = false;
                            } else TIMER_COUNTER++;
                            //formatting time to 00:00
                            int minutes, seconds;
                            String separator = ":";
                            minutes = TIMER_COUNTER / 60;
                            seconds = TIMER_COUNTER % 60;
                            if (minutes < 10) {
                                timeText = "0" + minutes + separator;
                            } else {
                                timeText = minutes + separator;
                            }
                            if (seconds < 10) {
                                timeText = timeText + "0" + seconds;
                            } else {
                                timeText = timeText + seconds;
                            }
                            text.setText(timeText);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
