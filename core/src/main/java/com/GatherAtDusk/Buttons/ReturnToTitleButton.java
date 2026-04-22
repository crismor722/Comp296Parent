package com.GatherAtDusk.Buttons;

import com.GatherAtDusk.Managers.SceneManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ReturnToTitleButton extends TextButton{
	private static final int BUTTON_WIDTH = 100; //make sure this is is double the height bc the button png is 32 by 16
	private static final int BUTTON_HEIGHT = 50;
	
	public ReturnToTitleButton(SceneManager sceneManager,Texture menuUp, Texture menuDown, BitmapFont font) { //had to add all these arguments because in order to make global i needed static vars and i cant dispose of static vars
		super("", createStyle(menuUp, menuDown, font));

        setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttonCode) { //when user touches down
                return true;
            }
            
            public void touchUp(InputEvent event, float x, float y, int pointer, int buttonCode) { //whenev
                sceneManager.startTitleScreen();
            }
        });
	}
	
	private static TextButtonStyle createStyle(Texture menuUp, Texture menuDown, BitmapFont font) {
    	
    	TextureRegionDrawable up = new TextureRegionDrawable(new TextureRegion(menuUp));
    	TextureRegionDrawable down = new TextureRegionDrawable(new TextureRegion(menuDown));

        TextButtonStyle style = new TextButtonStyle();
        style.up = up;
        style.down = down;
        style.font = font;

        return style;
    }
}
