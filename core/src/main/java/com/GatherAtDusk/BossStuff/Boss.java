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

public class Boss {
	private Body bossBody;
	private static final float BOSS_WIDTH = 40f;
	private static final float BOSS_HEIGHT = 60f;
	private static final float PPM = (float) 100; // 100 pixels per meter 
	private World world;
	private Vector2 blockPos;
	private BossAttackBlock currentAttack;
	private Array<BossAttackBlock> activeAttacks = new Array<>();
	private int health = 100;
	private float xPos = 400f / PPM;
	private float yPos = 480f / PPM;
	private float velocity = -1.5f;
	

	
	
	public Boss(World world, float startX, float startY) {
		this.world = world;
		createBody(world, startX, startY);
		callTimer();
		
	}

	private void callTimer() {
		Timer.schedule(new Timer.Task() {
		    @Override
		    public void run() {
		        attack();
		    }
		}, 0, 2);
		
		Timer.schedule(new Timer.Task() {
		    @Override
		    public void run() {
		        attack(xPos, yPos);
		    }
		}, 0, 2);
	}

	private void createBody(World world, float startX, float startY) {
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; //dynamic sprite
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
	
	public void attack() { //creates the attack block
		Vector2 bossPos = bossBody.getPosition();
	    float xOffset = 0.5f;
	    blockPos = new Vector2(bossPos.x - xOffset, bossPos.y);

	    BossAttackBlock newAttack = new BossAttackBlock(world, blockPos);
	    activeAttacks.add(newAttack);
	}
	
	public void attack(float xPos, float yPos) {
	    blockPos = new Vector2(xPos, yPos);

	    BossAttackBlock newAttack = new BossAttackBlock(world, blockPos, velocity);
	    activeAttacks.add(newAttack);
	}
	
	public Array<BossAttackBlock> getActiveAttacks() {
	    return activeAttacks;
	}

	//public void update() {    } 
}
