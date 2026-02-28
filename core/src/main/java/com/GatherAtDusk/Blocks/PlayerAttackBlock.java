package com.GatherAtDusk.Blocks;

import com.GatherAtDusk.ContactListener.CollisionType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PlayerAttackBlock {
	
	private Body playerAttackBody;
	private World world;
	
	private static final float PPM = (float) 100;
	private static final float BLOCK_WIDTH = 10f /PPM;
	private static final float BLOCK_HEIGHT = 6f / PPM;
	private boolean destroyed = false;
	
	
	public PlayerAttackBlock( World world, Vector2 blockPos){
		this.world = world;
		
		//the usual create body stuff
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.KinematicBody; //this body type allows it to ignore physics
        bd.position.set(blockPos);

        playerAttackBody = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BLOCK_WIDTH/2f / PPM, BLOCK_HEIGHT/ 2f / PPM); 

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.isSensor = true;

        Fixture playerAttackFix = playerAttackBody.createFixture(fd);
        playerAttackFix.setUserData(CollisionType.PLAYER_ATTACK_BLOCK);
        
        setVelocity();
        
        shape.dispose();
	}
	
	private void setVelocity() { //pretty much the same thing as setting the player's velocity
		float moveSpeed = 3.5f;
		Vector2 blockVector = playerAttackBody.getLinearVelocity();
		playerAttackBody.setLinearVelocity(moveSpeed, blockVector.x);
		
	}

	public static float getBlockWidth() {
		return BLOCK_WIDTH;
	}

	public static float getBlockHeight() {
		return BLOCK_HEIGHT;
	}
	
	public Vector2 getPosition() {
		return playerAttackBody.getPosition();
	}

	
	public Body getBody() {
		return playerAttackBody;
	}
	
	public void destroy() {
		world.destroyBody(playerAttackBody);
		destroyed = true;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	
	// public void bossTakeDamage()
}

