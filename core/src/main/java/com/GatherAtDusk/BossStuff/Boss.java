package com.GatherAtDusk.BossStuff;

import com.GatherAtDusk.Blocks.BossAttackBlock;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.GatherAtDusk.Helpers.*;

public class Boss {
	private Body bossBody;
	private static final float BOSS_WIDTH = 64f;
	private static final float BOSS_HEIGHT = 64f;
	private static final float PPM = (float) 100; // 100 pixels per meter 
	private World world;
	private Vector2 blockPos;
	private BossAttackBlock currentAttack;
	private Array<BossAttackBlock> activeAttacks = new Array<>();
	private int health = 100;
	private float xPos = 400f / PPM;
	private float yPos = 480f / PPM;
	private float velocity = -1.5f;
	private float stateTime;
	private boolean isAttacking;
	private boolean attackDone;
	private int attackFrameIndex;
	
	private Texture idleSheet;
	private Texture attackSheet;
	
	private Animation<TextureRegion> idleAnimation;
	private Animation<TextureRegion> attackAnimation;
	
	public Boss(World world, float startX, float startY) {
		this.world = world;
		createBody(world, startX, startY);
		callTimer();
		loadSprites();
	}

	

	private void callTimer() {
		Timer.schedule(new Timer.Task() {
		    @Override
		    public void run() {
		    	isAttacking= true;
		    	attackDone = true; //allows the attack to happen again
		        stateTime = 0f; //reset statetime is needed or else attack will be set to the wrong frame
		        attackFrameIndex = attackAnimation.getKeyFrameIndex(stateTime);  
		    }
		}, 1, 2);// delay, interval
		
		Timer.schedule(new Timer.Task() {
		    @Override
		    public void run() { //this attack will attack over the head of the player
		        attack(xPos, yPos);
		    }
		}, 1, 2);
	}

	private void createBody(World world, float startX, float startY) {
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; 
        bodyDef.position.set(startX/ PPM, startY/ PPM);
        
        bossBody = world.createBody(bodyDef); 
        
        PolygonShape shape = new PolygonShape(); 
        shape.setAsBox((BOSS_WIDTH / 2f) / PPM, (BOSS_HEIGHT / 2f) / PPM); 
        
        
        FixtureDef fd = new FixtureDef();  
        fd.shape = shape;
        fd.density = 1f; // base density
        fd.friction = 0.2f;
        
        Fixture bossFixture = bossBody.createFixture(fd);
        bossFixture.setUserData(CollisionType.BOSS);
        
        shape.dispose();
	}
	
	private void loadSprites() {

	    idleSheet = new Texture("BossIdle.png");
	    attackSheet = new Texture("BossAttack.png");

	    idleAnimation = AnimationHelper.createAnimation(idleSheet, 96, 96, 10, 0.1f, true);
	    attackAnimation = AnimationHelper.createAnimation(attackSheet, 96, 96, 6, 0.08f, true);
	}
	
	//helper method  that makes animations
	/*private Animation<TextureRegion> createAnimation(Texture sheet, int frameWidth, int frameHeight, int frameCount, float frameDuration) {

	    TextureRegion[][] temp = TextureRegion.split(sheet, frameWidth, frameHeight);

	    TextureRegion[] frames = new TextureRegion[frameCount];

	    for (int i = 0; i < frameCount; i++) {
	        frames[i] = temp[0][i];
	        frames[i].flip(true, false); // flip horizontally
	    }

	    return new Animation<>(frameDuration, frames);
	}
	*/
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void takeDamage(int damage ) {
		health = health - damage;
	}

	public static float getBossWidth() {
		return BOSS_WIDTH;
	}

	public static float getBossHeight() {
		return BOSS_HEIGHT;
	}
	
	public Vector2 getPosition() {
	    return bossBody.getPosition();
	}
	
	public Body getBody() {
		return bossBody;
	}
	
	public BossAttackBlock getAttackBlock() {
		return currentAttack;
	}
	
	public void clearCurrentAttack() {
		currentAttack = null;
	}
	
	public void setBossAttackPosX(float xPos){
		this.xPos = xPos;
	}
	
	public TextureRegion getFrame() {

	    if(isAttacking) { //pretty self- expanitory
	        return attackAnimation.getKeyFrame(stateTime, false); //(statetime , is looping?) 
	        
	    }
	    return idleAnimation.getKeyFrame(stateTime, true); //when using loop libgdx knows what frame to call based on the duration that has passed
	    //if 0.1 seconds has passed then the next frame is called. this is initialized when i created the animation and set the frame duration
	}
	
	
	public void attack() { //creates the attack block
		Vector2 bossPos = bossBody.getPosition();
	    float xOffset = 1.6f;
	    blockPos = new Vector2(bossPos.x - xOffset, bossPos.y);

	    BossAttackBlock newAttack = new BossAttackBlock(world, blockPos);
	    activeAttacks.add(newAttack);
	}
	
	public void attack(float xPos, float yPos) { //two methods of attack, first one is normal attack from boss, second attack is from over the players head
	    blockPos = new Vector2(xPos, yPos);

	    BossAttackBlock newAttack = new BossAttackBlock(world, blockPos, velocity);
	    activeAttacks.add(newAttack);
	}
	
	public Array<BossAttackBlock> getActiveAttacks() {
	    return activeAttacks;
	}

	public void update(float delta) {
	    stateTime += delta; //statetime determines what frame the animation needs to be at
	    if (attackAnimation.isAnimationFinished(stateTime)) {
			isAttacking= false;
		}
	    if(isAttacking) {
	    	attackFrameIndex = attackAnimation.getKeyFrameIndex(stateTime); //only get frame when attacking
	    }
	    if(attackFrameIndex == 4) { // only attack when sword swipe
	    	if (attackDone) { //without this if statement like 10 attacks will be sent;
	    		attack();
	    		attackDone = false; // only send one attack
	    	}
        }
	}
}
