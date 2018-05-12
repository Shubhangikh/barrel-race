package com.cwl.barrelrace.barrelracegame.filemanager;

import android.content.Context;

import com.cwl.barrelrace.barrelracegame.model.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HighScoresFileManager {
    /*Method for writing scores to the high_scores.txt file*/
    public void writeScoresToFile(List<Player> players, Context context) {
        File contactsFile = new File(context.getFilesDir(), "high_scores.txt");
        try {
            if(!contactsFile.exists()) {
                contactsFile.createNewFile();
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("high_scores.txt", Context.MODE_PRIVATE));
            PrintWriter writer = new PrintWriter(outputStreamWriter);
            int size = 0;
            if(players.size() == 0) {
                writer.close();
            } else {
                while (size < 10 && players.size() > size) {
                    Player player = players.get(size);
                    String name = player.getName();
                    String score = player.getTime();
                    writer.println(name + "\t" + score);
                    size++;
                }
                writer.flush();
                writer.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /*Method for reading contents of the high_scores.txt file*/
    public List<Player> readFromScoresFile(Context context) {
        List<Player> players = new ArrayList<>();
        try {
            File contactsFile = new File(context.getFilesDir(), "high_scores.txt");
            if(contactsFile.exists()) {
                InputStream inputStream = context.openFileInput("high_scores.txt");
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String playerDetails[] = line.split("\t");
                        Player player = new Player(
                                playerDetails[0],
                                playerDetails[1]
                        );
                        players.add(player);
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return players;
    }
}
