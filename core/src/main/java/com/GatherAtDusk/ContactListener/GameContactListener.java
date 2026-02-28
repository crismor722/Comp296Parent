package com.GatherAtDusk.ContactListener;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.SceneManager;
import com.GatherAtDusk.Blocks.BossAttackBlock;
import com.GatherAtDusk.Blocks.CheckpointBlock;
import com.GatherAtDusk.Blocks.PlayerAttackBlock;
import com.GatherAtDusk.PlayerStuff.Player;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class GameContactListener implements ContactListener {

    private final Player player;
    private final MainGame game;
    private final SceneManager sceneManager;
    private Array<CheckpointBlock> checkpointBlocks; //array list makes life easier
    private Array<PlayerAttackBlock> toDestroy = new Array<>();

    public GameContactListener(MainGame game, Player player, Array<CheckpointBlock> checkpointBlocks ) {
    	this.game = game;
    	this.sceneManager = game.sceneManager;
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
    	
    	
        CollisionType currentCollisionTypeA = getCollisionType(fixtureA); 
        CollisionType currentCollisionTypeB = getCollisionType(fixtureB); //sets the collision type after collision type is determined from getType
        //after getType runs, now the program knows what collisionType it is
        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER, CollisionType.GROUND)) {
            player.setOnGround(true); //sends the data to the player class so the player can check if it is on the ground
        }
        
        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER, CollisionType.CHECKPOINT)) {
            Fixture checkpointFixture = (currentCollisionTypeA == CollisionType.CHECKPOINT) ? fixtureA : fixtureB;
            //the game doesn't know which one is which so i need to tell it with the if statement above^ : if collison type is same as checkpoint, its the checkpoint
            //loop through all checkpointBlocks to find which one was touched
            for (CheckpointBlock currentCheckpoint : checkpointBlocks) {
                if (currentCheckpoint.getBody().getFixtureList().contains(checkpointFixture, true)) { //goes through each checkpoint and matches it to the checkpoint in the collison instance
                    currentCheckpoint.activateSave(player); 
                    //long story short, this compares the unique memory reference of all the created checkpoints and compares it to the checkpoint that is involved with the collision
                    
                    //getBody of current chkpnt , get the fixtures of it, figures out which fixture is the same in memmory reference and then determines that that checkpoint calls activateSave
                    //LibGDX arraylists are different then normal arraylists, LibGDX allows me to compare the fixture's unique memory reference to determine which fixture is which
                    break; // only activate the one touched : side note this one break statement saved me from so much debugging
                }
            }
        }
        
        
        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER_ATTACK_BLOCK, CollisionType.WALL)) {
            Fixture playerAttackFixture =  (currentCollisionTypeA == CollisionType.PLAYER_ATTACK_BLOCK) ? fixtureA : fixtureB;

            PlayerAttackBlock playerBlock = (PlayerAttackBlock) playerAttackFixture.getBody().getUserData();
            toDestroy.add(playerBlock);
        }
        
        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER, CollisionType.BOSS_ATTACK_BLOCK)) {
            Fixture bossAttackFixture =  (currentCollisionTypeA == CollisionType.BOSS_ATTACK_BLOCK) ? fixtureA : fixtureB;
            
            BossAttackBlock bossBlock = (BossAttackBlock) bossAttackFixture.getBody().getUserData();
            player.takeDamage(bossBlock.getDamage());
            sceneManager.isGameOver(player.getHealth());
            //toDestroy.add(block);
        }
    }

    @Override
    public void endContact(Contact contact) { //whenever two objects stop touching, box2d calls this method
        CollisionType currentCollisionTypeA = getCollisionType(contact.getFixtureA());
        CollisionType currentCollisionTypeB = getCollisionType(contact.getFixtureB());

        if (isPair(currentCollisionTypeA, currentCollisionTypeB, CollisionType.PLAYER, CollisionType.GROUND)) {
            player.setOnGround(false); 
        }
    }

    @Override public void preSolve(Contact contact, Manifold manifold) {}
    @Override public void postSolve(Contact contact, ContactImpulse impulse) {}

    //need to check if  collision type is in enum for saftey
    private CollisionType getCollisionType(Fixture fixture) { 
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
    
    public void processPendingDestruction() {
        for (PlayerAttackBlock block : toDestroy) { //array to destroy blocks
        	block.destroy();
        	if (player.getAttackBlock() == block) {
                player.clearCurrentAttack(); // let player attack again
            }
        }
        toDestroy.clear();
    }
    
    
    //notes for future me: this method retrieves player because it call playe.setOnGround
    //getContact is called when box2d detects contact between two objects
    //first step, get the first fixture
    //second step get the collsionType from the fixture which is saved in setUserData
    //third step, check what do do with collsionTypes and do it, ex. player.setOnGround(true)
}
