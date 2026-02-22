package com.GatherAtDusk.Buttons;

import com.GatherAtDusk.SceneManager;
import com.GatherAtDusk.Saving.SaveManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
	private static final int BUTTON_WIDTH = 250;
	private static final int BUTTON_HEIGHT = 70;

    public LoadFileButton(SceneManager sceneManager) { //scene manager needs to be included so button can call goToCheckPoint
        super("NEW FILE", createStyle());

        setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float curserLocationX, float curserLocationY) { //when clicked, the location of the cursor is sent, this is default
                sceneManager.goToSceneForCheckpoint(SaveManager.loadCheckpoint()); //NOTE: because the method is static, there is no need to initialize it ex. SceneManager = scenemanager
            }
        });
    }

    private static TextButtonStyle createStyle() {

        Pixmap pixmap = new Pixmap(BUTTON_WIDTH, BUTTON_HEIGHT, Pixmap.Format.RGBA8888); //setting button height and format
        pixmap.setColor(Color.FOREST); //temp color green and testing to see if color works/looks good
        pixmap.fill();

        Texture texture = new Texture(pixmap); // sending pixmap to texture
        pixmap.dispose(); //don't need pixmap memory anymore

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        BitmapFont font = new BitmapFont(); 

        TextButtonStyle style = new TextButtonStyle();
        style.up = drawable;
        style.down = drawable;
        style.font = font;

        return style;
    }
}

