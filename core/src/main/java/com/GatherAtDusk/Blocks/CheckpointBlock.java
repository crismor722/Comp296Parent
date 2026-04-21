package com.GatherAtDusk.Blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.GatherAtDusk.Managers.SaveManager;
import com.GatherAtDusk.PlayerStuff.Player;



public class CheckpointBlock {
	private Body body;
    public int checkpointID;
    private static final float PPM = 100f;
    private static final float WIDTH = 10f/ PPM;
    private static final float HEIGHT = 10f/ PPM;

    public CheckpointBlock(World world, float positionX, float positionY,  int id) {
        this.checkpointID = id;

        //static body for checkpoint
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(positionX, positionY);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, HEIGHT / 2);
        

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true; // makes it trigger collisions but not block movement
        body.createFixture(fixtureDef).setUserData(CollisionType.CHECKPOINT);
    }

    public static float getWidth() {
		return WIDTH;
	}

	public static float getHeight() {
		return HEIGHT;
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
