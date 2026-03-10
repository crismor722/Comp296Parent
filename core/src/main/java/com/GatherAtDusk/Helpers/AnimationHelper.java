package com.GatherAtDusk.Helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



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
	
}
