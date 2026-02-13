package com.GatherAtDusk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {
	 public SpriteBatch batch;
	 public SceneManager sceneManager;
    @Override
    public void create() {
    	 batch = new SpriteBatch();
         sceneManager = new SceneManager(this);
         sceneManager.startTitleScreen();; // start at first scene
    }
}
