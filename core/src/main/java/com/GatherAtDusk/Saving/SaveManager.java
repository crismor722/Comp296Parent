package com.GatherAtDusk.Saving;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveManager {
    private static final String SAVE_FILE = "save1.json";

    public static SaveData load() {
        FileHandle localFile = Gdx.files.local(SAVE_FILE);
        Json json = new Json();

        if (!localFile.exists()) {
            System.out.println("Save file not found. Using defaults.");
            return new SaveData(); // checkpointID = 1
        }

        String content = localFile.readString();
        System.out.println("SAVE FILE CONTENT: " + content);

        SaveData data = json.fromJson(SaveData.class, content);
        if (data == null) {
            System.out.println("Save file corrupted. Using defaults.");
            data = new SaveData();
        }

        return data;
    }

    public static void save(SaveData data) {
        FileHandle localFile = Gdx.files.local(SAVE_FILE);
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json); // ensures proper output

        String output = json.toJson(data);
        localFile.writeString(output, false);

        System.out.println("SaveManager.save() called");
        System.out.println("JSON WRITTEN: " + output); //this it to make 100% sure it works
        System.out.println("FILE PATH: " + localFile.file().getAbsolutePath());
    }
}