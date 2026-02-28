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
	private static final float BLOCK_WIDTH = 30f;
	private static final float BLOCK_HEIGHT = 15f;
	private int damage = 10; //default damage
	private boolean destroyed = false;
	
	public BossAttackBlock(World world, Vector2 blockPos){
		this.world = world;
		
		//the usual create body stuff
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.KinematicBody; //this body type allows it to ignore physics
        bd.position.set(blockPos);

        bossAttackBody = world.createBody(bd);
        bossAttackBody.setUserData(this); 

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BLOCK_WIDTH/2f / PPM, BLOCK_HEIGHT/ 2f / PPM); 

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.isSensor = true;

        Fixture bossAttackFix = bossAttackBody.createFixture(fd);
        bossAttackFix.setUserData(CollisionType.BOSS_ATTACK_BLOCK);
        
        //setVelocity();
        
        shape.dispose();
	}
	
	/*private void setVelocity() { //pretty much the same thing as setting the player's velocity
		float moveSpeed = 3.5f;
		Vector2 blockVector = playerAttackBody.getLinearVelocity();
		playerAttackBody.setLinearVelocity(moveSpeed, blockVector.x);
		
	} */
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public static float getBlockWidth() {
		return BLOCK_WIDTH;
	}

	public static float getBlockHeight() {
		return BLOCK_HEIGHT;
	}
	
	public Vector2 getPosition() {
		return bossAttackBody.getPosition();
	}

	
	public Body getBody() {
		return bossAttackBody;
	}
	
	public void destroy() {
		world.destroyBody(bossAttackBody);
		destroyed = true;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
}
