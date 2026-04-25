package com.GatherAtDusk.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Buttons.LoadFileButton;
import com.GatherAtDusk.Managers.SceneManager;

public class TitleScreen extends ScreenAdapter {

    private SceneManager sceneManager;
    private OrthographicCamera camera;
    private SpriteBatch batch; //dont make static
    private BitmapFont font;
    private Texture background;
    private Texture title;
    private Stage stage;
    private static final float CAM_WIDTH = 800;
    private static final float CAM_HEIGHT = 480;
    private boolean shuttingDown;
    private Texture startUp;
    private Texture startDown;
    private BitmapFont buttonFont;

    public TitleScreen(MainGame game) {
        this.sceneManager = game.sceneManager; // use the shared SceneManager
        //MUST DO IT LIKE THIS TO SAVE MEMORY
        this.batch = MainGame.batch;
    }

    @Override
    public void show() {
    	
    	background = new Texture("Background_space.png");
    	title = new Texture("my-Gather_At_Dusk.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);

        font = new BitmapFont();
        font.getData().setScale(2f); // doubling default font size
        stage = new Stage(new ScreenViewport()); //creating a stage is just easier for handling UI elements, maybe change later
        Gdx.input.setInputProcessor(stage); //allows user to click on button or more specifically the stage
        createButtonStuff();


        LoadFileButton loadFileButton = new LoadFileButton(sceneManager, startUp, startDown, buttonFont); //needs to send sceneManger for the button to call checkpoint

        loadFileButton.setPosition(
                Gdx.graphics.getWidth() / 2f - loadFileButton.getWidth() / 2f, //(center of the screen) - (center of button) = button in center of screen
                Gdx.graphics.getHeight() / 2f - loadFileButton.getHeight() / 2f
        );

        stage.addActor(loadFileButton); //need to add button as an actor so the button can do its thing
    }

    private void createButtonStuff() {
    	startUp = new Texture("startButtonUp.png");
    	startDown = new Texture("startButtonDown.png");
    	
    	buttonFont = new BitmapFont();
	}

	@Override
    public void render(float delta) {
    	if (shuttingDown) {
    	    Gdx.input.setInputProcessor(null);
    	    return;
    	}

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        batch.draw(background, 0, 0, 1024, 600); //this is the width and height of the  actual png

        batch.draw(title, 225, 300 ,368, 65);
        batch.end();
        
        stage.act(delta);
        stage.draw();
    }
    
    //ADD DISPOSING WHEN SWITCHING TO SCENE
    @Override
    public void dispose() { //dispose when switching to different scene
        font.dispose();
        buttonFont.dispose();
        startUp.dispose();
        startDown.dispose();
        stage.dispose();
        background.dispose();
        title.dispose();
    }

	public void beginShutdown() {
		shuttingDown = true;
	}
}
