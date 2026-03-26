package com.GatherAtDusk.NPCS;

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
import com.GatherAtDusk.PlayerStuff.Player;

public class Boss {
	private Body bossBody;
	private static final float BOSS_WIDTH = 64f;
	private static final float BOSS_HEIGHT = 64f;
	private static final float PPM = (float) 100; // 100 pixels per meter 
	private World world;
	private Player player;
	private Vector2 blockPos;
	private BossAttackBlock currentAttack;
	private Array<BossAttackBlock> activeAttacks = new Array<>();
	private int health = 100;
	//private float xPos = 400f / PPM;
	private float yPos = 480f / PPM;
	private float velocity = -3f;
	private float stateTime;
	private boolean isAttacking;
	private boolean attackDone;
	private boolean timerStarted = false;
	private int attackFrameIndex;
	
	private Timer.Task attackTask;
	private Timer.Task secondAttackTask;
	private Timer.Task aboveAttackTask;
	private Timer.Task secondAboveAttackTask;
	
	private Texture idleSheet;
	private Texture attackSheet;
	
	private Animation<TextureRegion> idleAnimation;
	private Animation<TextureRegion> attackAnimation;
	
	public Boss(World world, Player player, float startX, float startY) {
		this.world = world;
		this.player = player;
		createBody(world, startX, startY);
		loadSprites();
	}


	private void callTimer() {
		attackTask = new Timer.Task() { // do it this way so I can cancel task
		    @Override
		    public void run() {
		    	isAttacking= true;
		    	attackDone = true; //allows the attack to happen again
		    	stateTime = 0f; //reset statetime is needed or else attack will be set to the wrong frame
		    	attackFrameIndex = attackAnimation.getKeyFrameIndex(stateTime);  
		    }
		};
		
		secondAttackTask = new Timer.Task(){
			public void run() {
				attack();
			}
		};
		
		aboveAttackTask = new Timer.Task() {
		    @Override
		    public void run() { //this attack will attack over the head of the player
		        attackFromAbove();
		    }
		};
		
		secondAboveAttackTask = new Timer.Task() {
		    @Override
		    public void run() { //this attack will attack over the head of the player
		        attackFromAbove(3);
		    }
		};
		
		Timer.schedule(attackTask, 1, 2);// delay, interval
		Timer.schedule(aboveAttackTask, 2, 2);
		Timer.schedule(secondAboveAttackTask, 3, 3);
		Timer.schedule(secondAttackTask, 1.5f, 2);
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


	public void attackFromAbove() { //two methods of attack, first one is normal attack from boss, second attack is from over the players head
		float playerX = player.getPosition().x;

	    blockPos = new Vector2(playerX, yPos);

	    BossAttackBlock newAttack = new BossAttackBlock(world, blockPos, velocity);
	    newAttack.setDamage(10);
	    activeAttacks.add(newAttack);
	}
	
	public void attackFromAbove(int attackAmount) { //two methods of attack, first one is normal attack from boss, second attack is from over the players head
		float xOffset = -10f;
		float playerX;
		
	    for( int i = 0; i < attackAmount; i++) {
	    	
	    	playerX = player.getPosition().x + (xOffset /PPM);
	    	blockPos = new Vector2(playerX, yPos);
	    	
	    	BossAttackBlock newAttack = new BossAttackBlock(world, blockPos, velocity);
		    activeAttacks.add(newAttack);
		    
		    xOffset = xOffset + 10f;
	    }
	    
	}
	
	public Array<BossAttackBlock> getActiveAttacks() {
	    return activeAttacks;
	}

	public void update(float delta) {
	    stateTime += delta; //statetime determines what frame the animation needs to be at
	    if(!timerStarted && player.getCanMove()) {
	        callTimer();
	        timerStarted = true;
	    }
	    if(timerStarted && player.getCanMove() == false) {
	    	attackTask.cancel();
	    	aboveAttackTask.cancel();
	    	secondAttackTask.cancel();
	    	secondAboveAttackTask.cancel();
	    }
	    
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
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void takeDamage(int damage ) {
		if(health !=0) {
			health = health - damage;
		}
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
}
