package com.GatherAtDusk.Buttons;

import com.GatherAtDusk.Helpers.AnimationHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class EasterEggButton extends TextButton {
	
	private static final int BUTTON_WIDTH = 120;
	private static final int BUTTON_HEIGHT = 120;
	private Texture dkSheet;
	
	private boolean addAnimation = false;
	private boolean isLooping = true;
	
	private int frameSize = 48;
	private int frameCount = 4;
	private float frameDuration = 0.25f;
	private float stateTime;
	
	
	private Animation<TextureRegion> currentAnimation;
	
	public EasterEggButton(Texture texture, Texture dkSheet, BitmapFont font) {
		super("", createStyle(texture, font));
		this.dkSheet = dkSheet;
		loadAnimation();
	    setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

	    addListener(new ClickListener() {
	        @Override
	        public void clicked(InputEvent event, float curserLocationX, float curserLocationY) { 
	        	addAnimation = true;
	        }
	    });
	}

	private static TextButtonStyle createStyle(Texture texture, BitmapFont font) {

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        TextButtonStyle style = new TextButtonStyle();
        style.up = drawable;
        style.down = drawable;
        style.font = font;

        return style;
	}

	private void loadAnimation() {
		
		currentAnimation = AnimationHelper.createAnimation(dkSheet, frameSize, frameSize, frameCount, frameDuration, false);
	}
	
	public void update(float delta) {
		stateTime += delta;
	}
	
	public TextureRegion getFrame() {
	    return currentAnimation.getKeyFrame(stateTime, isLooping);
	}
	
	public static int getButtonWidth() {
		return BUTTON_WIDTH;
	}

	public static int getButtonHeight() {
		return BUTTON_HEIGHT;
	}
	
	public boolean isAddAnimation() {
		return addAnimation;
	}

	public void setAddAnimation(boolean addAnimation) {
		this.addAnimation = addAnimation;
	}
	
}
