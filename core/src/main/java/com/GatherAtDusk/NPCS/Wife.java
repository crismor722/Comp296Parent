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
	
	private static final int FRAME_SIZE = 64;
	
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
	    
	    int row1 = 1; 
	    int row3 = 3;
	    //idle left
	    int frameCount_i = 2;
	    float frameDuration_i = 0.2f;
	    idleAnimation = AnimationHelper.createAnimation(idleSheet, FRAME_SIZE, FRAME_SIZE, row1, frameCount_i, frameDuration_i, false);
	    
	    //walk left
	    int frameCount_w = 9;
	    float frameDuration_w = 0.2f;
	    walkAnimation = AnimationHelper.createAnimation(walkSheet, FRAME_SIZE, FRAME_SIZE, row1, frameCount_w, frameDuration_w, false);
	    
	    //sit right
	    int frameCount_s = 1;
	    float frameDuration_s = 1f;
	    sitAnimation = AnimationHelper.createAnimation(sitSheet, FRAME_SIZE, FRAME_SIZE, row3, frameCount_s, frameDuration_s, false);
	    
	    //run left
	    int frameCount_r = 8;
	    float frameDuration_r = 0.1f;
	    runAnimation = AnimationHelper.createAnimation(runSheet, FRAME_SIZE, FRAME_SIZE, row1, frameCount_r, frameDuration_r, false);
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

	public static int getFrameSize() {
		return FRAME_SIZE;
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
	
	public static float getWifeWidth() {
		return WIFE_WIDTH;
	}

	public static float getWifeHeight() {
		return WIFE_HEIGHT;
	}

	public void dispose() {
		world.destroyBody(wifeBody);
		idleSheet.dispose();
		walkSheet.dispose();
		sitSheet.dispose();
		runSheet.dispose();
	}
}
