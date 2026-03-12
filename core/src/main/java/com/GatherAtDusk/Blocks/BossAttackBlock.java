package com.GatherAtDusk.Blocks;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BossAttackBlock {
	private World world;
	private Body bossAttackBody;
	
	private static final float PPM = (float) 100;
	private float blockWidth = 30f/ PPM;
	private float blockHeight = 15f / PPM;
	private int damage = 10; //default damage
	private boolean destroyed = false;
	private Fixture bossAttackFix;
	private Vector2 blockPos;
	private float velocity = -3.5f;
	private boolean isAbove;
	
	
	public BossAttackBlock(World world, Vector2 blockPos){
		this.world = world;
		this.blockPos = blockPos;
		destroyed = false;
		createBody();
		isAbove = false;
	}
	 // if velocity is  sent, x vel is 0 and y veloc goes down, else x veloc is set and y is zero
	public BossAttackBlock(World world, Vector2 blockPos, float velocity){ //second constructor to allows for attacks from sky, need to edit velocity
		this.world = world;
		this.blockPos = blockPos;
		this.velocity = velocity;
		destroyed = false;
		createBody(velocity);
		isAbove = true;
	}
	
	private void createBody() {
		//the usual create body stuff
		 BodyDef bd = new BodyDef();
	     bd.type = BodyDef.BodyType.DynamicBody; //this body type allows it to ignore physics
	     bd.position.set(blockPos);
	     
	     bossAttackBody = world.createBody(bd);
	     bossAttackBody.setUserData(this); 

	     PolygonShape shape = new PolygonShape();
	     shape.setAsBox(blockWidth/2f / PPM, blockHeight/ 2f / PPM); 

	     FixtureDef fd = new FixtureDef();
	     fd.shape = shape;
	     fd.isSensor = true;

	     bossAttackFix = bossAttackBody.createFixture(fd);
	     bossAttackFix.setUserData(CollisionType.BOSS_ATTACK_BLOCK);
	     bossAttackBody.setGravityScale(0f);
	        
	     setVelocity();
	        
	     shape.dispose();
	}
	
	private void createBody(float velocity) {
		BodyDef bd = new BodyDef();
		float tempVal = blockWidth;
		blockWidth = blockHeight;
		blockHeight = tempVal;
		
	    bd.type = BodyDef.BodyType.DynamicBody; //this body type allows it to ignore physics
	    bd.position.set(blockPos);
	     
	    bossAttackBody = world.createBody(bd);
	    bossAttackBody.setUserData(this); 

	    PolygonShape shape = new PolygonShape();
	    shape.setAsBox(blockWidth/2f / PPM, blockHeight/ 2f / PPM); 

	    FixtureDef fd = new FixtureDef();
	    fd.shape = shape;
	    fd.isSensor = true;

	    bossAttackFix = bossAttackBody.createFixture(fd);
	    bossAttackFix.setUserData(CollisionType.BOSS_ATTACK_BLOCK);
	    bossAttackBody.setGravityScale(0f);
	        
	    setVelocity(velocity);
	        
	    shape.dispose();
	}

	private void setVelocity() { //pretty much the same thing as setting the player's velocity
		Vector2 blockVector = bossAttackBody.getLinearVelocity();
		bossAttackBody.setLinearVelocity(velocity, blockVector.x);
		
	} 
	
	private void setVelocity(float velocity) {
		Vector2 blockVector = bossAttackBody.getLinearVelocity();
		bossAttackBody.setLinearVelocity(blockVector.x, velocity);
		
	} 
	
	public boolean isAbove() {
		return isAbove;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public float getBlockWidth() {
		return blockWidth;
	}

	public float getBlockHeight() {
		return blockHeight;
	}
	
	public Vector2 getPosition() {
		return bossAttackBody.getPosition();
	}

	
	public Body getBody() {
		return bossAttackBody;
	}
	
	public void destroy() {
		bossAttackBody.destroyFixture(bossAttackFix);
		world.destroyBody(bossAttackBody);
		destroyed = true;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
}
