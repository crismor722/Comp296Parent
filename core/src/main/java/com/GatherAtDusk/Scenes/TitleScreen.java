package com.GatherAtDusk.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.SceneManager;
import com.GatherAtDusk.Buttons.NewFileButton;

public class TitleScreen extends ScreenAdapter {

    private final MainGame game;
    private final SceneManager sceneManager;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private static final float CAM_WIDTH = 800;
    private static final float CAM_HEIGHT = 480;

    public TitleScreen(MainGame game) {
        this.game = game;
        this.sceneManager = game.sceneManager; // use the shared SceneManager
        this.batch = game.batch;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);

        font = new BitmapFont();
        font.getData().setScale(2f);
        // add cursor hover effect for nice effects future me please
        stage = new Stage(new ScreenViewport()); //creating a stage is just easier for handling UI elements, maybe change later
        Gdx.input.setInputProcessor(stage); //allows user to click on button


        NewFileButton newFileButton = new NewFileButton(sceneManager); //needs to send sceneManger for the button to call checkpoint

        newFileButton.setPosition(
                Gdx.graphics.getWidth() / 2f - newFileButton.getWidth() / 2f, //(center of the screen) - (center of button) = button in center of screen
                Gdx.graphics.getHeight() / 2f - newFileButton.getHeight() / 2f
        );

        stage.addActor(newFileButton); //need to add button as an actor so it can do its thing
    }

    @Override
    public void render(float delta) {
    	//background
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1); //dark blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "GATHER AT DUSK", 200, 300);
        //font.draw(batch, "Press ENTER to Start", 230, 200);
        batch.end();
        
        stage.act(delta);
        stage.draw();

        
        // if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // Ask SceneManager which scene to load based on the last checkpoint
            //int checkpoint = sceneManager.getLastCheckpoint();
            //sceneManager.goToSceneForCheckpoint(0); //zero sets it to intro scene
        //}
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
        // don't dispose batch
    }
}
