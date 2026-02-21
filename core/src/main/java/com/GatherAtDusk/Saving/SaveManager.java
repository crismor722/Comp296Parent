package com.GatherAtDusk.Saving;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveManager {

    private static final String SAVE_NAME = "GatherAtDuskSave";

    public static void saveCheckpoint(int newCheckpointID) {
        Preferences prefs = Gdx.app.getPreferences(SAVE_NAME); //automatically opens OR creates a small save file
        
        int currentID = prefs.getInteger("checkpointID", 1); //retrieves current id
        
        if (currentID == newCheckpointID) { //if checkpointid already checkpointid don't call anything
        	return;
        }
        prefs.putInteger("checkpointID", newCheckpointID); //if different checkpoint, set it to the new checkpoint
        prefs.flush(); //this actually writes
        System.out.println("Saved checkpoint: " + newCheckpointID);
    }

    public static int loadCheckpoint() {
    	Preferences prefs = Gdx.app.getPreferences(SAVE_NAME); //retrieves the saved data
    	int loadId = prefs.getInteger("checkpointID", 1);
        System.out.println("Loaded checkpoint: " + loadId);
        return loadId;

    }
}