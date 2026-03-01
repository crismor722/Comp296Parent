package com.GatherAtDusk.Blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.GatherAtDusk.Managers.SaveManager;
import com.GatherAtDusk.PlayerStuff.Player;



public class CheckpointBlock {
	private Body body;
    public int checkpointID;
    private float width;
    private float height;

    public CheckpointBlock(World world, float positionX, float positionY, float width, float height, int id) {
        this.checkpointID = id;
        this.width = width;
        this.height = height;

        //static body for checkpoint
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(positionX, positionY);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true; // makes it trigger collisions but not block movement
        body.createFixture(fixtureDef).setUserData(CollisionType.CHECKPOINT);
    }

    public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getIdofCurrentCheckpoint() {
        return checkpointID;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    // called when player touches checkpoint in contactlistener
    public void activateSave(Player player) {
    	int id = getIdofCurrentCheckpoint();
        SaveManager.saveCheckpoint(id);
        System.out.println("Checkpoint activated: " + id);
    }
    
    public Vector2 getSpawnPositionPixels(float PPM) {
        return new Vector2(
            body.getPosition().x * PPM,
            body.getPosition().y * PPM
        );
    }

	public Body getBody() {
		return body;
	}

}
