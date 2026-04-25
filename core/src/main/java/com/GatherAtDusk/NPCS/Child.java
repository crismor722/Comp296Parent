package com.GatherAtDusk.NPCS;

import com.GatherAtDusk.Helpers.AnimationHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.World;

public class Child {
	private Body childBody;
	private World world;
	private static final float CHILD_WIDTH = 64f;
	private static final float CHILD_HEIGHT = 64f;
	private static final float PPM = (float) 100; // 100 pixels per meter
	private float stateTime;
	private boolean isLooping;
	
	private Texture sitSheet;
	
	private Animation<TextureRegion> sitAnimation;
	
	private Animation<TextureRegion> currentAnimation;
	
	private static final int FRAME_SIZE = 64;
	
	public Child(World world, float startX, float startY) {
		this.world = world;
		createBody(startX, startY);
		loadSprites();
		setFrame(sitAnimation, true);
	}
	
	//CALL GETFRAME AND UPDATE

	private void createBody(float startX, float startY) {
		BodyDef bd = new BodyDef();
	    bd.type = BodyDef.BodyType.StaticBody;
	    bd.position.set(startX/PPM, startY/PPM);
	     
	    childBody = world.createBody(bd);
	    childBody.setUserData(this); 

	    //no shape needed
	    //no fixture needed
	    childBody.setGravityScale(0f);    
	}
	
	private void loadSprites() {
	    sitSheet = new Texture("childSit.png");
	    
	    int row = 1; //sit left
	    int frameCount = 2;
	    float frameDuration = 4f;
	    
	    sitAnimation = AnimationHelper.createAnimation(sitSheet, FRAME_SIZE, FRAME_SIZE, row, frameCount, frameDuration, false);
	}
	
	public TextureRegion getFrame() {
	    return currentAnimation.getKeyFrame(stateTime, isLooping);
	}
	private void setFrame(Animation<TextureRegion> currentAnimation, boolean isLooping) {
		this.isLooping = isLooping;
		this.currentAnimation = currentAnimation;
	}
	
	public void update(float delta) {
		stateTime += delta;
		//frame is already set shouldn't need to set it
	}
	
	public static int getFrameSize() {
		return FRAME_SIZE;
	}
	public Vector2 getPosition() {
	    return childBody.getPosition();
	}
	
	public static float getChildWidth() {
		return CHILD_WIDTH;
	}

	public static float getChildHeight() {
		return CHILD_HEIGHT;
	}

	public void dispose() {
		world.destroyBody(childBody);
		sitSheet.dispose();
	}
}
