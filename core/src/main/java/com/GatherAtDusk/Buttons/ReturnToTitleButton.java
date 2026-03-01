package com.GatherAtDusk.Buttons;

import com.GatherAtDusk.Managers.SceneManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ReturnToTitleButton extends TextButton{
	private static final int BUTTON_WIDTH = 120;
	private static final int BUTTON_HEIGHT = 50;
	
	public ReturnToTitleButton(SceneManager sceneManager) {
		super("Return to Title", createStyle());

        setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float curserLocationX, float curserLocationY) { 
                sceneManager.startTitleScreen();
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
