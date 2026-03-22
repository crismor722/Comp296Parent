package com.GatherAtDusk.NPCS;

import com.GatherAtDusk.ContactListener.CollisionType;
import com.GatherAtDusk.Helpers.AnimationHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wife {
	private Body wifeBody;
	private World world;
	private Fixture wifeFix;
	private static final float WIFE_WIDTH = 64f;
	private static final float WIFE_HEIGHT = 64f;
	private static final float PPM = (float) 100; // 100 pixels per meter
	private float stateTime;
	private float moveSpeed = 0.5f;
	private boolean isLooping;
	private boolean isWalking = true;
	private boolean isSitting;
	
	private Texture idleSheet;
	private Texture walkSheet;
	private Texture sitSheet;
	
	private Animation<TextureRegion> idleAnimation;
	private Animation<TextureRegion> walkAnimation;
	private Animation<TextureRegion> sitAnimation;
	
	private Animation<TextureRegion> currentAnimation;
	
	private int frameSize = 64;
	private int frameCount;
	private float frameDuration = 0.1f;
	private int row;
	
	public Wife(World world, float startX, float startY) {
		this.world = world;
		createBody(startX, startY);
		loadSprites();
		setFrame(idleAnimation, true);
	}
	
	//CALL GETFRAME AND UPDATE

	private void createBody(float startX, float startY) {
		BodyDef bd = new BodyDef();
	    bd.type = BodyDef.BodyType.DynamicBody;
	    bd.position.set(startX/PPM, startY/PPM);
	     
	    wifeBody = world.createBody(bd);
	    wifeBody.setUserData(this); 

	    PolygonShape shape = new PolygonShape();
	    shape.setAsBox(WIFE_WIDTH/2f / PPM, WIFE_HEIGHT/ 2f / PPM); 

	    FixtureDef fd = new FixtureDef();
	    fd.shape = shape;
	    fd.isSensor = true;

	    wifeFix = wifeBody.createFixture(fd);
	    wifeFix.setUserData(CollisionType.WIFE);
	    wifeBody.setGravityScale(0f);
	        
	    shape.dispose();
	}
	
	private void loadSprites() {
		idleSheet = new Texture("wifeIdle.png");
	    walkSheet = new Texture("wifeWalk.png");
	    sitSheet = new Texture("wifeSit.png");
	    
	    row = 1; //idle left
	    frameCount = 2;
	    frameDuration = 0.2f;
	    idleAnimation = AnimationHelper.createAnimation(idleSheet, frameSize, frameSize, row, frameCount, frameDuration, false);
	    
	    row = 1; //walk left
	    frameCount = 9;
	    frameDuration = 0.2f;
	    walkAnimation = AnimationHelper.createAnimation(walkSheet, frameSize, frameSize, row, frameCount, frameDuration, false);
	    
	    row = 3; //sit right
	    frameCount = 2;
	    frameDuration = 1f;
	    sitAnimation = AnimationHelper.createAnimation(sitSheet, frameSize, frameSize, row, frameCount, frameDuration, false);
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
		
		Vector2 wifeVelocity = wifeBody.getLinearVelocity();
		if(isWalking) {
			wifeBody.setLinearVelocity(-moveSpeed, wifeVelocity.y);
			setFrame(walkAnimation, true);
		}
		else if (isSitting) {
			wifeBody.setLinearVelocity(0, wifeVelocity.y);
			setFrame(sitAnimation, true);
		}
		else {
			wifeBody.setLinearVelocity(0, wifeVelocity.y);
			setFrame(idleAnimation, true);
		}
	}
	
	public void setWalking(boolean isWalking){
		this.isWalking = isWalking;
	}

	public boolean isSitting() {
		return isSitting;
	}

	public void setSitting(boolean isSitting) {
		this.isSitting = isSitting;
	}

	public boolean isWalking() {
		return isWalking;
	}

	public int getFrameSize() {
		return frameSize;
	}
	public Vector2 getPosition() {
	    return wifeBody.getPosition();
	}
}
