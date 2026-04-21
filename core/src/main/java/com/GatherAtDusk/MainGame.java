package com.GatherAtDusk;

import com.GatherAtDusk.Managers.SceneManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainGame extends Game {
	 public static SpriteBatch batch;
	 public static ShapeRenderer shapeRenderer;
	 public SceneManager sceneManager; //dont do static it can hokd stuff by accident
    @Override
    public void create() {
    	 batch = new SpriteBatch();
    	 shapeRenderer = new ShapeRenderer();
         sceneManager = new SceneManager(this);
         sceneManager.startTitleScreen();; // start at first scene
    }
}
