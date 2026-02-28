package com.GatherAtDusk.PlayerStuff;
import com.GatherAtDusk.Blocks.PlayerAttackBlock;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;


public class Player {
	private Body playerBody;
	private Boolean isOnGround = false;
	private static final float PLAYER_WIDTH = 40f;
	private static final float PLAYER_HEIGHT = 60f;
	private static final float PPM = (float) 100; // 100 pixels per meter 
	private World world;
	private PlayerAttackBlock currentAttack;
	private Array<PlayerAttackBlock> activeAttacks = new Array<>();
	
	
	public Player(World world, float startX, float startY) {
		this.world = world;
		createBody(world, startX, startY);
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
	
	
	public void attack() { //creates the attack block
		Vector2 playerPos = playerBody.getPosition();
	    float xOffset = 0.8f;
	    Vector2 blockPos = new Vector2(playerPos.x + xOffset, playerPos.y);

	    PlayerAttackBlock newAttack = new PlayerAttackBlock(world, blockPos);
	    activeAttacks.add(newAttack);
	}
	
	public Array<PlayerAttackBlock> getActiveAttacks() {
	    return activeAttacks;
	}

	public void update() { //gravity is established in introscene already

        float moveSpeed = 3f; //base movespeed
        
        Vector2 playerVelocity = playerBody.getLinearVelocity();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerBody.setLinearVelocity(-moveSpeed, playerVelocity.y); //pressing "a" make the player go left
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerBody.setLinearVelocity(moveSpeed, playerVelocity.y); //pressing "d" makes the player go right
        }
        else {
            playerBody.setLinearVelocity(0, playerVelocity.y); //if no buttons pressed, do nothing
        }

        if (isOnGround && Gdx.input.isKeyJustPressed(Input.Keys.W)) { // player needs to be on ground before they could jump
            playerBody.setLinearVelocity(playerVelocity.x, 5f); // jumps straight up
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            attack();
        }
                
    }

	
			
}
