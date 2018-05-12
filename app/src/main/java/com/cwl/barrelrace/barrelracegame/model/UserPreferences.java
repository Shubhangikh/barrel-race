package com.cwl.barrelrace.barrelracegame.model;

public class UserPreferences {
    public static String GAME_LEVEL;

    public static void setGameLevel(String level){
        GAME_LEVEL = level;
    }
    public static String getGameLevel(){
        return  GAME_LEVEL;
    }

}
