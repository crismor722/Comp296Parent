package com.GatherAtDusk.Scenes;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Buttons.ReturnToTitleButton;
import com.GatherAtDusk.Managers.SceneManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameWinScene extends ScreenAdapter {

	private final MainGame game;
	private SceneManager sceneManager;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private Stage stage;
	private Texture gameWinText;
	private Texture voiceCredits1;
	private Texture voiceCredits2;
	private Texture startUp;
    private Texture startDown;
    private BitmapFont buttonFont;
    private Sound lineWin;
	private static final float CAM_WIDTH = 800f;
	private static final float CAM_HEIGHT = 480f;
	private static final float VC1WIDTH = 180f;
	private static final float VC1HEIGHT = 41f;
	private static final float VC2WIDTH = 252f;
	private static final float VC2HEIGHT = 122f;
	private static final float GW_WIDTH = 241f;
	private static final float GW_HEIGHT = 65f;
	private static final float CENTER_Y1 = 270f;
	private static final float CENTER_Y2 = 310f;
	private static final float CENTER_X_GW =370f;
	private static final float CENTER_X_VC1 = 310f;
	private static final float CENTER_X_VC2 = 190f;
	private boolean shuttingDown = false;
	
	
	public GameWinScene(MainGame game) {
		this.game = game;
		this.sceneManager = game.sceneManager; // use the shared SceneManager
		this.batch = MainGame.batch;	
	}
		
	public void show() {
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
	    gameWinText = new Texture("my-Game_Win.png");
	    voiceCredits1 = new Texture("my-VoiceCredits1.png");
	    voiceCredits2 = new Texture("my-VoiceCredits2.png");

	    font = new BitmapFont();
	    font.getData().setScale(2f);
	    stage = new Stage(new ScreenViewport());
	    Gdx.input.setInputProcessor(stage); 
	    
	    createButtonStuff();
	    //i need to do this to dispose of it properly
	    ReturnToTitleButton returnTitleButton = new ReturnToTitleButton(sceneManager, startUp, startDown, buttonFont); //needs to send sceneManger for the button to call title screen

	    returnTitleButton.setPosition(
	    		Gdx.graphics.getWidth() / 2f - returnTitleButton.getWidth() / 2f, 
	            Gdx.graphics.getHeight() / 3f - returnTitleButton.getHeight() / 3f
	        );
	    
	    lineWin = Gdx.audio.newSound(Gdx.files.internal("lineWin.wav"));
	    lineWin.play();
	    stage.addActor(returnTitleButton);
	}

	private void createButtonStuff() {
    	startUp = new Texture("menuButtonUp.png");
    	startDown = new Texture("menuButtonDown.png");
    	buttonFont = new BitmapFont();
	}

	@Override
	public void render(float delta) {
		if (shuttingDown) {
		    Gdx.input.setInputProcessor(null);
		    return;
		}
		//background
	    Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1); //dark blue
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    camera.update();
	    batch.setProjectionMatrix(camera.combined);

	    batch.begin();
	    batch.draw(gameWinText, CENTER_Y1, CENTER_X_GW ,GW_WIDTH, GW_HEIGHT);
	    batch.draw(voiceCredits1,CENTER_Y2, CENTER_X_VC1, VC1WIDTH, VC1HEIGHT);
	    batch.draw(voiceCredits2,CENTER_Y1 , CENTER_X_VC2, VC2WIDTH, VC2HEIGHT);
	    //font.draw(batch, "GAMEWIN", 200, 300);
	    batch.end();
	        
	    stage.act(delta);
	    stage.draw();
	}
	    
	    //ADD DISPOSING WHEN SWITCHING TO SCENE
	    @Override
	    public void dispose() { //dispose when switching to different scene
	    	lineWin.dispose();
	        font.dispose();
	        buttonFont.dispose();
	        startUp.dispose();
	        startDown.dispose();
	        voiceCredits1.dispose();
	        voiceCredits2.dispose();
	        gameWinText.dispose();
	        stage.dispose();
	    }

		public void beginShutdown() {
			shuttingDown = true;
		}

}
