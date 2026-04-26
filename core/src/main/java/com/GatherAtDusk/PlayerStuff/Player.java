package com.GatherAtDusk.PlayerStuff;
import com.GatherAtDusk.Blocks.PlayerAttackBlock;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.GatherAtDusk.Helpers.AnimationHelper;
import com.GatherAtDusk.NPCS.Boss;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;


public class Player {
	private Body playerBody;
	private Boolean isOnGround = false;
	private static final float PLAYER_WIDTH = 64f;
	private static final float PLAYER_HEIGHT = 64f;
	private static final float PPM = (float) 100; // 100 pixels per meter 
	private World world;
	private PlayerAttackBlock currentAttack;
	private Array<PlayerAttackBlock> activeAttacks = new Array<>();
	private int health = 100;
	
	private float stateTime;
	private boolean isAttacking;
	private boolean isLooping;
	private boolean isSitting = false;
	private boolean canAttack = true;
	private boolean isDead = false;
	private boolean shouldResetStatetime = true;
	
	private boolean canMove = true;
	private float moveSpeed = 3f; //base movespeed
	
	private static final int FRAME_SIZE = 64;
	
	//textures
	private Texture idleSheet;
	private Texture attackSheet;
	private Texture runSheet;
	private Texture sitSheet;
	private Texture hurtSheet;
	
	//animations
	private Animation<TextureRegion> idleAnimation;
	
	private Animation<TextureRegion> sittingAnimation;
	
	private Animation<TextureRegion> runRightAnimation;
	private Animation<TextureRegion> runLeftAnimation;
	
	private Animation<TextureRegion> attackRightAnimation;
	
	private Animation<TextureRegion> hurtAnimation;
	
	private Animation<TextureRegion> currentAnimation;
	
	
	public Player(World world, float startX, float startY) {
		this.world = world;
		createBody(world, startX, startY);
		loadSprites();
	}
	
	public Player(World world, float startX, float startY, Boss boss) {
		this.world = world;
		createBody(world, startX, startY);
		loadSprites();
	}

	private void createBody(World world, float startX, float startY) {
		BodyDef bodyDef = new BodyDef(); // defining the body of player
        bodyDef.type = BodyDef.BodyType.DynamicBody; //dynamic sprite
        bodyDef.position.set(startX/ PPM, startY/ PPM);
        
        playerBody = world.createBody(bodyDef); //creates the body
        
        PolygonShape shape = new PolygonShape(); //make the shape of player
        shape.setAsBox((PLAYER_WIDTH / 2f) / PPM, (PLAYER_HEIGHT / 2f) / PPM); //center of player width and height
        
        // need to make it a fixture for collisions
        FixtureDef playerFixtureDef = new FixtureDef();  //define the fixture
        playerFixtureDef.shape = shape;
        playerFixtureDef.density = 1f; // base density
        playerFixtureDef.friction = 0.2f;
        
        Fixture playerFixture = playerBody.createFixture(playerFixtureDef);
        playerFixture.setUserData(CollisionType.PLAYER);
        
        shape.dispose();
	}
	
	private void loadSprites() { // i thought about doing a animation helper method but i like this better so i can see everything

	    idleSheet = new Texture("mainIdle.png");
	    attackSheet = new Texture("mainShoot.png");
	    runSheet = new Texture("mainRun.png");
	    sitSheet = new Texture("mainSit.png");
	    hurtSheet = new Texture("mainHurt.png");
	    int row0 = 0;
	    int row1 = 1;
	    int row2 = 2;
	    int row3 = 3;
	    
	    int frameCount_i = 2;
	    float frameDuration_i = 0.2f;
	    idleAnimation = AnimationHelper.createAnimation(idleSheet, FRAME_SIZE, FRAME_SIZE, row2, frameCount_i, frameDuration_i, false);
	    
	    int frameCount_r = 8;
	    float frameDuration_r = 0.1f;
	    runRightAnimation = AnimationHelper.createAnimation(runSheet, FRAME_SIZE, FRAME_SIZE, row3, frameCount_r, frameDuration_r, false);
	    
	    //same frame count and frame duration
	    runLeftAnimation = AnimationHelper.createAnimation(runSheet, FRAME_SIZE, FRAME_SIZE, row1, frameCount_r, frameDuration_r, false);
	    
	    int frameCount_a = 13;
	    float frameDuration_a = 0.02f;
	    attackRightAnimation = AnimationHelper.createAnimation(attackSheet, FRAME_SIZE, FRAME_SIZE, row3, frameCount_a, frameDuration_a, false);
	    
	    int frameCount_s = 1;
	    float frameDuration_s = 1f; //doesn't really matter bc it will only be one frame;
	    sittingAnimation = AnimationHelper.createAnimation(sitSheet, FRAME_SIZE, FRAME_SIZE, row3, frameCount_s, frameDuration_s, false);
	    
	    int frameCount_h = 4;
	    float frameDuration_h = 0.15f;
	    hurtAnimation = AnimationHelper.createAnimation(hurtSheet, FRAME_SIZE, FRAME_SIZE, row0, frameCount_h, frameDuration_h, false);
	}
	
	public TextureRegion getFrame() {
	    return currentAnimation.getKeyFrame(stateTime, isLooping); //when using loop libgdx knows what frame to call based on the duration that has passed
	    //if 0.1 seconds has passed then the next frame is called. this is initialized when i created the animation and set the frame duration
	}
	
	private void setFrame(Animation<TextureRegion> currentAnimation, boolean isLooping) {
		this.isLooping = isLooping;
		this.currentAnimation = currentAnimation;
	}
	
	public void attack() { //creates the attack block
		stateTime = 0;
		Timer.schedule(new Timer.Task() {
		    @Override
		    public void run() {  
				Vector2 playerPos = playerBody.getPosition();
			    float Offset = 0.3f;
			    Vector2 blockPos = new Vector2(playerPos.x + Offset, playerPos.y + Offset);

			    PlayerAttackBlock newAttack = new PlayerAttackBlock(world, blockPos);
			    activeAttacks.add(newAttack);
			    
		    }
		}, 0.2f, 0f, 0);// delay, interval, how many times to run the task. Delay here is to match the attack animation so it attacks on the actual release of the slingshot
		
		Timer.schedule(new Timer.Task() { //attack delay so not too much spamming
		    @Override
		    public void run() {  
		    	canAttack = true;
		    }
		}, 0.2f, 0f, 0); //only runs once
	}
	
	public Array<PlayerAttackBlock> getActiveAttacks() {
	    return activeAttacks;
	}
	

	public void update(float delta) { //gravity is established in introscene already
		stateTime += delta;
		
        Vector2 playerVelocity = playerBody.getLinearVelocity(); //needs to be here because it changes based on inputs
        
        if(isDead) {
        	playerBody.setLinearVelocity(0, playerVelocity.y);
        	setFrame(hurtAnimation, false);
        	if(shouldResetStatetime) { //for non-looping animations i need to do this or else animation will not catch the correct stateTime
        		shouldResetStatetime = false;
        		stateTime = 0;
        	}
        	return;
        }
        
        if(isSitting) {
        	playerBody.setLinearVelocity(0, playerVelocity.y);
        	setFrame(sittingAnimation, true);
        	return; //if sitting nothing else needs to be checked or processed
        }
        
        if(canMove) {
        	if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        		playerBody.setLinearVelocity(-moveSpeed, playerVelocity.y); //pressing "a" make the player go left
        		setFrame(runLeftAnimation, true);
        	}
        	else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        		playerBody.setLinearVelocity(moveSpeed, playerVelocity.y); //pressing "d" makes the player go right
        		setFrame(runRightAnimation, true);
        	}
        	else if(!isAttacking){
        		playerBody.setLinearVelocity(0, playerVelocity.y); //if no buttons pressed, do nothing
        		setFrame(idleAnimation, true);
        	}
        }
        else {
             playerBody.setLinearVelocity(0, playerVelocity.y); //if cant move just idle
             setFrame(idleAnimation, true);
        }
        
        if(canMove) {
        	if (isOnGround && Gdx.input.isKeyJustPressed(Input.Keys.W)) { // player needs to be on ground before they could jump
        		playerBody.setLinearVelocity(playerVelocity.x, 5f); // jumps straight up
        	}
        	if (canAttack && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        		attack();
        		setFrame(attackRightAnimation, false);
        		isAttacking = true;
        		canAttack = false;
        	}
        	
        }
        
        if (attackRightAnimation.isAnimationFinished(stateTime)) {
			isAttacking= false;
		}
    }
	
	public boolean isSitting() {
		return isSitting;
	}

	public void setSitting(boolean isSitting) {
		this.isSitting = isSitting;
		canMove = !isSitting; //if sitting than cannot move, if not sitting then can move this is for the boss class
		//boss class checks if player can move so i need to set this just for boss to not attack
	}

	public static int getFrameSize() {
		return FRAME_SIZE;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void takeDamage(int damage ) {
		health = health - damage;
	}
	
	public void setOnGround(boolean onGround) {
	    this.isOnGround = onGround;
	}

	public static float getPlayerWidth() {
		return PLAYER_WIDTH;
	}

	public static float getPlayerHeight() {
		return PLAYER_HEIGHT;
	}
	
	public Vector2 getPosition() {
	    return playerBody.getPosition();
	}
	
	public Body getBody() {
		return playerBody;
	}
	
	public PlayerAttackBlock getAttackBlock() {
		return currentAttack;
	}
	
	public void clearCurrentAttack() {
		currentAttack = null;
	}
	public void setCanMove(boolean canMove){
		this.canMove = canMove;
	}
	
	public boolean getCanMove(){
		return canMove;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public void dispose() {
		world.destroyBody(playerBody);
		idleSheet.dispose();
		attackSheet.dispose();
		runSheet.dispose();
		sitSheet.dispose();
		hurtSheet.dispose();
	}
	
}
