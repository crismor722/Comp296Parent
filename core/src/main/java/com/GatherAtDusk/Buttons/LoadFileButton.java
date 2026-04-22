package com.GatherAtDusk.Buttons;

import com.GatherAtDusk.Managers.SaveManager;
import com.GatherAtDusk.Managers.SceneManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//NOTE: I made these variables vague so it won't be confused with imported objects but just know
// font is for the button's font
// style is for the button's style
// pixmap is for the button's pixmap

public class LoadFileButton extends TextButton {
	private static final int BUTTON_WIDTH = 100; //make sure this is is double the height bc the button png is 32 by 16
	private static final int BUTTON_HEIGHT = 50;

    public LoadFileButton(SceneManager sceneManager, Texture startUp, Texture startDown, BitmapFont font) { //scene manager needs to be included so button can call goToCheckPoint
        super("", createStyle(startUp, startDown, font));

        setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        addListener(new ClickListener() {
            @Override
            /*public void clicked(InputEvent event, float curserLocationX, float curserLocationY) { //when clicked, the location of the cursor is sent, this is default
                sceneManager.goToSceneForCheckpoint(SaveManager.loadCheckpoint()); //NOTE: because the method is static, there is no need to initialize it ex. SceneManager = scenemanager
            }
            */
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttonCode) { //when user touches down
                return true;
            }
            
            public void touchUp(InputEvent event, float x, float y, int pointer, int buttonCode) { //whenev
                //setColor(Color.WHITE); don't need this since user won't see it
                sceneManager.goToSceneForCheckpoint(SaveManager.loadCheckpoint()); // goes to scene on touch up otherwise the user wont see the change of color on the click
            }
        });
    }

    private static TextButtonStyle createStyle(Texture startUp, Texture startDown, BitmapFont font) {

        /*Pixmap pixmap = new Pixmap(BUTTON_WIDTH, BUTTON_HEIGHT, Pixmap.Format.RGBA8888); //setting button height and format
        pixmap.setColor(Color.SLATE); //temp color green and testing to see if color works/looks good
        pixmap.fill();
        
        pixmap.setColor(Color.WHITE); //outline
        pixmap.drawRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());

        Texture texture = new Texture(pixmap); // sending pixmap to texture
        pixmap.dispose(); //don't need pixmap memory anymore

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        */
        TextureRegionDrawable up = new TextureRegionDrawable(new TextureRegion(startUp));
        TextureRegionDrawable down = new TextureRegionDrawable(new TextureRegion(startDown));

        TextButtonStyle style = new TextButtonStyle();
        style.up = up; //libgdx is awesome it already will set the style to up and down whenever the button is clicked
        style.down = down;
        style.font = font;

        return style;
    }
}

