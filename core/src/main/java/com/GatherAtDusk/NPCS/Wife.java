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
	private static float moveSpeed = 0.5f;
	private boolean isLooping;
	private boolean isWalking = true;
	private boolean isSitting;
	private boolean isRunning = false;
	
	private Texture idleSheet;
	private Texture walkSheet;
	private Texture sitSheet;
	private Texture runSheet;
	
	private Animation<TextureRegion> idleAnimation;
	private Animation<TextureRegion> walkAnimation;
	private Animation<TextureRegion> sitAnimation;
	private Animation<TextureRegion> runAnimation;
	private Animation<TextureRegion> currentAnimation;
	
	private int frameSize = 64;
	private int frameCount;
	private float frameDuration = 0.1f;
	private int row;
	
	private static CollisionType type = CollisionType.WIFE_WIN;
	private static boolean wifeWin = true;
	
	

	public Wife(World world, float startX, float startY) {
		if(wifeWin == false) {
			type = CollisionType.WIFE_LOSE;
		}
		else {
			type = CollisionType.WIFE_WIN;
		}
		this.world = world;
		createBody(startX, startY, type);
		loadSprites();
		setFrame(idleAnimation, true);
	}
	
	//CALL GETFRAME AND UPDATE

	private void createBody(float startX, float startY, CollisionType type) {
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
	    wifeFix.setUserData(type);
	    wifeBody.setGravityScale(0f);
	        
	    shape.dispose();
	}
	
	
	private void loadSprites() {
		idleSheet = new Texture("wifeIdle.png");
	    walkSheet = new Texture("wifeWalk.png");
	    sitSheet = new Texture("wifeSit.png");
	    runSheet = new Texture ("wifeRun.png");
	    
	    row = 1; //idle left
	    frameCount = 2;
	    frameDuration = 0.2f;
	    idleAnimation = AnimationHelper.createAnimation(idleSheet, frameSize, frameSize, row, frameCount, frameDuration, false);
	    
	    row = 1; //walk left
	    frameCount = 9;
	    frameDuration = 0.2f;
	    walkAnimation = AnimationHelper.createAnimation(walkSheet, frameSize, frameSize, row, frameCount, frameDuration, false);
	    
	    row = 3; //sit right
	    frameCount = 1;
	    frameDuration = 1f;
	    sitAnimation = AnimationHelper.createAnimation(sitSheet, frameSize, frameSize, row, frameCount, frameDuration, false);
	    
	    row = 1; //run left
	    frameCount = 8;
	    frameDuration = 0.1f;
	    runAnimation = AnimationHelper.createAnimation(runSheet, frameSize, frameSize, row, frameCount, frameDuration, false);
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
		
		if (isSitting) {
			wifeBody.setLinearVelocity(0, wifeVelocity.y);
			setFrame(sitAnimation, true);
			return; //again no need to check anything else
		}
		
		if(isWalking) { //default to true, need to manually set false
			wifeBody.setLinearVelocity(-moveSpeed, wifeVelocity.y);
			setFrame(walkAnimation, true);
		}
		else if(isRunning) { //default to true, need to manually set false
			wifeBody.setLinearVelocity(-moveSpeed, wifeVelocity.y);
			setFrame(runAnimation, true);
		}
		else {
			wifeBody.setLinearVelocity(0, wifeVelocity.y);
			setFrame(idleAnimation, true);
		}
	}
	public CollisionType getType() {
		return type;
	}

	public static void setTypeLose() {
		 wifeWin = false;
	}
	public static boolean isTypeWin() {
		return wifeWin;
	}
	
	public static void setTypeWin() {
		wifeWin = true;
	}
	
	public void setWalking(boolean isWalking){
		this.isWalking = isWalking;
	}

	public boolean isSitting() {
		return isSitting;
	}

	public void setSitting(boolean isSitting) { //called in end scene
		this.isSitting = isSitting;
		isWalking = !isSitting; //if sitting then not walking
		isRunning = !isSitting; // i shouldn't need this but this is for safety
	}

	public boolean isWalking() { //called in contact listener
		return isWalking;
	}

	public int getFrameSize() {
		return frameSize;
	}
	public Vector2 getPosition() {
	    return wifeBody.getPosition();
	}
	public static void setRunningSpeed() {
		moveSpeed = 1.5f;
	}
	
	public static void setWalkingSpeed() {
		moveSpeed = 1.0f;
	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
