package com.GatherAtDusk.Helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


//i made this class to make animations for me, pretty self explanatory just converting texture sheets to texture regions then to animations
public abstract class AnimationHelper {
	
	public static Animation<TextureRegion> createAnimation(Texture sheet, int frameWidth, int frameHeight, int frameCount, float frameDuration, boolean flip){
		
		TextureRegion[][] temp = TextureRegion.split(sheet, frameWidth, frameHeight);

	    TextureRegion[] frames = new TextureRegion[frameCount];

	    for (int i = 0; i < frameCount; i++) {
	        frames[i] = temp[0][i];
	        if(flip) {
	        	frames[i].flip(true, false); // flip horizontally
	        }
	    }

	    return new Animation<>(frameDuration, frames);
	}
	
	public static Animation<TextureRegion> createAnimation(Texture sheet, int frameWidth, int frameHeight, int row, int frameCount, float frameDuration, boolean flip){
		
		TextureRegion[][] temp = TextureRegion.split(sheet, frameWidth, frameHeight);

	    TextureRegion[] frames = new TextureRegion[frameCount];

	    for (int i = 0; i < frameCount; i++) {
	        frames[i] = temp[row][i];

	        if (flip) {
	            frames[i].flip(true, false);
	        }
	    }

	    return new Animation<>(frameDuration, frames);
	}
	
}
