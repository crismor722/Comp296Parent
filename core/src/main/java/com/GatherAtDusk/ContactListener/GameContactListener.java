package com.GatherAtDusk.ContactListener;

import com.GatherAtDusk.Blocks.CheckpointBlock;
import com.GatherAtDusk.PlayerStuff.Player;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class GameContactListener implements ContactListener {

    private final Player player;
    private Array<CheckpointBlock> checkpointBlocks;

    public GameContactListener(Player player, Array<CheckpointBlock> checkpointBlocks) {
        this.player = player;
        this.checkpointBlocks = checkpointBlocks;
    }
    //note: i made this class this way so all i need to do is to compare what collisions happen, and what to do with it
    //this class detects all collisions and checks what collisionTypes are involved to determine what to do with them
    @Override
  //whenever contact happens, box2d will call this method
    public void beginContact(Contact contact) { 
    	//need to figure out what contact happened because box2d detects collisions, not collisionTypes
    	//getFixtureA() is built into box2d
    	Fixture fixtureA = contact.getFixtureA(); //contact.getFixture gets one of the fixtures in the contact
    	Fixture fixtureB = contact.getFixtureB();
    	
    	
        CollisionType currentCollisionTypeA = getType(fixtureA); 
        CollisionType currentCollisionTypeB = getType(fixtureB); //sets the collision type after collision type is determined from getType
        //after getType runs, now the program knows what collisionType it is
        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER, CollisionType.GROUND)) {
            player.setOnGround(true); //sends the data to the player class so the player can check if it is on the ground
        }
        
        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER, CollisionType.CHECKPOINT)) {
            Fixture checkpointFixture = (currentCollisionTypeA == CollisionType.CHECKPOINT) ? fixtureA : fixtureB;

            // Loop through all checkpointBlocks to find which one was touched
            for (CheckpointBlock checkpoint : checkpointBlocks) {
                if (checkpoint.getBody().getFixtureList().contains(checkpointFixture, true)) {
                    checkpoint.activateSave(player);
                    break; // only activate the one touched
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) { //whenever two objects stop touching, box2d calls this method
        CollisionType currentCollisionTypeA = getType(contact.getFixtureA());
        CollisionType currentCollisionTypeB = getType(contact.getFixtureB());

        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER, CollisionType.GROUND)) { //box2d can get confused, this helps
            player.setOnGround(false); //setOnground now maybe change to isMakingContact
        }
    }

    @Override public void preSolve(Contact contact, Manifold manifold) {}
    @Override public void postSolve(Contact contact, ContactImpulse impulse) {}

    //need to check if  collision type is in enum for saftey
    private CollisionType getType(Fixture fixture) { 
        Object data = fixture.getUserData(); //collsionType is saved in userData of the object
        //IMPORTANT: userData is being set in player class and in introscene for the ground 
        //make it an object so "intanceof" can be used
        if (data instanceof CollisionType) { //if data object is an instance of collsionType, send the collisionType, if not, then null 
            return (CollisionType) data;
        } else {
            return null;
        } 
    }

    private boolean isPair(CollisionType currentCollisionTypeA, CollisionType currentCollisionTypeB,
    		CollisionType comparedCollisionTypeA, CollisionType comparedCollisionTypeB) {
    	
    	if ((currentCollisionTypeA == comparedCollisionTypeA && currentCollisionTypeB == comparedCollisionTypeB) || 
    			(currentCollisionTypeA == comparedCollisionTypeB && currentCollisionTypeB == comparedCollisionTypeA)) {
            return true; //this is just to say that if either collisionTypes equals the other, return true
        } else {
            return false;
        }
    }
    
    //notes for future me: this method retrieves player because it call playe.setOnGround
    //getContact is called when box2d detects contact between two objects
    //first step, get the first fixture
    //second step get the collsionType from the fixture which is saved in setUserData
    //third step, check what do do with collsionTypes and do it, ex. player.setOnGround(true)
}
