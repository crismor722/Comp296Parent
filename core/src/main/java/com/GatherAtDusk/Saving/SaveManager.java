package com.GatherAtDusk.Saving;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveManager {

    private static final String SAVE_NAME = "GatherAtDuskSave";

    public static void saveCheckpoint(int newCheckpointID) {
        Preferences prefs = Gdx.app.getPreferences(SAVE_NAME);
        
        int currentID = prefs.getInteger("checkpointID", 1);
        
        if (currentID == newCheckpointID) { //if checkpoint already checkpoint don't call anything
        	return;
        }
        prefs.putInteger("checkpointID", newCheckpointID);
        prefs.flush();
        System.out.println("Saved checkpoint: " + newCheckpointID);
    }

    public static int loadCheckpoint() {
    	Preferences prefs = Gdx.app.getPreferences(SAVE_NAME);
    	int loadId = prefs.getInteger("checkpointID", 1);
        System.out.println("Loaded checkpoint: " + loadId);
        return loadId;

    }
}