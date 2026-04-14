package com.GatherAtDusk.Scenes;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Buttons.ReturnToTitleButton;
import com.GatherAtDusk.Managers.SceneManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameWinScene extends ScreenAdapter {

	private final MainGame game;
	private final SceneManager sceneManager;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private Stage stage;
	private Texture gameWinText;
	private static final float CAM_WIDTH = 800;
	private static final float CAM_HEIGHT = 480;
	private boolean shuttingDown = false;
		
	public GameWinScene(MainGame game) {
			this.game = game;
			this.sceneManager = game.sceneManager; // use the shared SceneManager
	        this.batch = game.batch;
		}
		
	public void show() {
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
	    gameWinText = new Texture("my-Game_Win.png");

	    font = new BitmapFont();
	    font.getData().setScale(2f);
	    stage = new Stage(new ScreenViewport());
	    Gdx.input.setInputProcessor(stage); 


	    ReturnToTitleButton returnTitleButton = new ReturnToTitleButton(sceneManager); //needs to send sceneManger for the button to call title screen

	    returnTitleButton.setPosition(
	    		Gdx.graphics.getWidth() / 2f - returnTitleButton.getWidth() / 2f, 
	            Gdx.graphics.getHeight() / 2f - returnTitleButton.getHeight() / 2f
	        );

	    stage.addActor(returnTitleButton);
	}

	@Override
	public void render(float delta) {
		if(shuttingDown) return;
		//background
	    Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1); //dark blue
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    camera.update();
	    batch.setProjectionMatrix(camera.combined);

	    batch.begin();
	    batch.draw(gameWinText, 270, 300 ,241, 65);
	    //font.draw(batch, "GAMEWIN", 200, 300);
	    batch.end();
	        
	    stage.act(delta);
	    stage.draw();
	}
	    
	    //ADD DISPOSING WHEN SWITCHING TO SCENE
	    @Override
	    public void dispose() { //dispose when switching to different scene
	        font.dispose();
	        stage.dispose();
	        gameWinText.dispose();
	        batch.dispose();
	    }

		public void beginShutdown() {
			shuttingDown = true;
		}

}
