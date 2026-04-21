package com.GatherAtDusk.Scenes;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Blocks.BossAttackBlock;
import com.GatherAtDusk.Blocks.CheckpointBlock;
import com.GatherAtDusk.Blocks.PlayerAttackBlock;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.GatherAtDusk.ContactListener.GameContactListener;
import com.GatherAtDusk.Managers.DialogueManager;
import com.GatherAtDusk.NPCS.Boss;
import com.GatherAtDusk.NPCS.Wife;
import com.GatherAtDusk.PlayerStuff.Player;
import com.GatherAtDusk.PlayerStuff.HealthUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class BossScene extends ScreenAdapter{
	private final MainGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private static SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    private Wife wife;
    private Boss boss;
    private Texture groundTexture;
    private Texture backgroundDay;
    private Texture swooshTexture;
    private Texture kunaiTexture;
    private Texture rockTexture;
    private Texture tree;
    private DialogueManager dialogueManager;
    private boolean shuttingDown = false;
    
	private static final int HGRAVITY = 0;
    private static final float VGRAVITY = -9.8f; //LibGDX likes floats for decimals
    private static final float WORLD_WIDTH = 800f *3/2f;
    private static final float WORLD_HEIGHT = 480f *3/2f;
    private static final float GROUND_WIDTH_POSITION = WORLD_WIDTH/2;
    private static final float GROUND_HEIGHT_POSITION= 50F; //height center is the position not size
    private static final float GROUND_WIDTH_SIZE =  WORLD_WIDTH;
    private static final float GROUND_HEIGHT_SIZE = GROUND_HEIGHT_POSITION;
    private static final float FRICTION = 0.8f; //friction doesn't really have an effect now, maybe find a way to remove later
    private float playerStartX = 100f ; // center of screen in pixels
    private float playerStartY = GROUND_HEIGHT_POSITION + 60f; // above ground
    private static final float PPM = (float) 100; // 100 pixels per meter 
    private static final float WALL_THICKNESS = 10f / PPM;
    private static final float TILE_SIZE = 16f/ PPM;
    private static final float TREE_SIZE = 32f/PPM;
    private static final float TILE_INDEX_X = GROUND_WIDTH_SIZE / (TILE_SIZE *PPM); // 800 /16 = 50
    private GameContactListener contactListener;
    private HealthUI healthUI;
    
    public BossScene(MainGame game) {
    	this.game = game;
    	BossScene.batch = MainGame.batch;
    }
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
        shapeRenderer = new ShapeRenderer();

        //gravity (downward)
        //NOTE: world is not the same as the scene
        world = new World(new Vector2(HGRAVITY, VGRAVITY), true); //world handles gravity
        debugRenderer = new Box2DDebugRenderer();

        createGround();
        createBoundaries();
        createPlayer(playerStartX, playerStartY);
        createBoss();
        createExtraStuff();
        
        
        contactListener = new GameContactListener(game, player, boss, this, dialogueManager); //sending this boss scene
        world.setContactListener(contactListener); //set contact listener is built into box2d
    }
    private void createBoss() {
		boss = new Boss(world, player, 700f *3/2, GROUND_HEIGHT_POSITION + 60f);
		swooshTexture = new Texture("BossSwoosh.png");
		kunaiTexture = new Texture("kunai.png");
	}

	private void createPlayer(float spawnX, float spawnY) {
		player = new Player(world, spawnX, spawnY, boss);
		rockTexture = new Texture("small-rock.png");
	}
	
	private void createExtraStuff() {
		healthUI = new HealthUI(player, boss);
		groundTexture = new Texture("Platform Tileset/dirtfull.png");
		backgroundDay = new Texture ("DayBackground.png");
		tree = new Texture("oak.png");
		dialogueManager = new DialogueManager(game, player, 1);
	}
	
	private void createWife() {
		if(contactListener.getWifeWin() == false) {
			Wife.setTypeLose();
			Wife.setRunningSpeed();
		}
		else {
			Wife.setWalkingSpeed();
		}
		wife = new Wife(world, 800f *3/2, GROUND_HEIGHT_POSITION + 60f);
		if(contactListener.getWifeWin() == false) {
			wife.setRunning(true); //also sets walking to false
			wife.setWalking(false);
		}
	}

	private void createGround() { //createGround needs to be in this scene and not in MainGame, MainGame only handles scenes
    	BodyDef bodyDef = new BodyDef(); //need to set definitions of where the ground will be
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(GROUND_WIDTH_POSITION/ PPM, GROUND_HEIGHT_POSITION / PPM); //Box2d centers it from middle of the width but 
        Body groundBody = world.createBody(bodyDef);
        groundBody.setUserData(this); 

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(GROUND_WIDTH_SIZE/2 / PPM, GROUND_HEIGHT_SIZE/2 / PPM); //create the shape of the ground
        
        //setting the shape of the ground
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape; //sets the fixtureDef's shape as groundShape
        groundFixtureDef.friction = FRICTION;
        
        Fixture groundFixture = groundBody.createFixture(groundFixtureDef); //creating shape 
        groundFixture.setUserData(CollisionType.GROUND);
        groundShape.dispose(); //groundShape is saved in fixtureDef so it is not needed to be saved in memory
    }
    
    private void createBoundaries() {
        // left and right boundaries

        // left wall
    	BodyDef leftWallDef = new BodyDef();
    	leftWallDef.type = BodyDef.BodyType.StaticBody;
    	leftWallDef.position.set(0 - WALL_THICKNESS/2, WORLD_HEIGHT / 2 / PPM);
    	Body leftWallBody = world.createBody(leftWallDef);

    	PolygonShape leftShape = new PolygonShape();
    	leftShape.setAsBox(WALL_THICKNESS / 2, WORLD_HEIGHT / 2 / PPM);

    	FixtureDef leftWallFixtureDef = new FixtureDef();
    	leftWallFixtureDef.shape = leftShape;
    	leftWallFixtureDef.friction = 0f; // no friction on walls
    	leftWallFixtureDef.restitution = 0f; // optional, prevents bouncing
    	Fixture leftWallFixture = leftWallBody.createFixture(leftWallFixtureDef);
    	leftWallFixture.setUserData(CollisionType.WALL);

    	leftShape.dispose();


        // right wall
        BodyDef rightWallDef = new BodyDef();
        rightWallDef.type = BodyDef.BodyType.StaticBody;
        rightWallDef.position.set(WORLD_WIDTH / PPM + WALL_THICKNESS/2, WORLD_HEIGHT / 2 / PPM);
        Body rightWallBody = world.createBody(rightWallDef);
        
        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsBox(WALL_THICKNESS / 2, WORLD_HEIGHT / 2 / PPM);
        
        FixtureDef rightWallFixtureDef = new FixtureDef();
        rightWallFixtureDef.shape = rightShape;
        rightWallFixtureDef.friction = 0f; // no friction on walls
        rightWallFixtureDef.restitution = 0f; // optional, prevents bouncing
        Fixture rightWallFixture = rightWallBody.createFixture(rightWallFixtureDef);
        rightWallFixture.setUserData(CollisionType.WALL);
        
        rightShape.dispose();
    }
    

    @Override
    public void render(float delta) { 
    	if (shuttingDown) {
    	    Gdx.input.setInputProcessor(null); //stops rendering
    	    return;
    	}
        // sky color
        //Gdx.gl.glClearColor(0.4f, 0.7f, 1f, 1); // color blue
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //CHECK HERE IF COLLISIONS OR OVERLAP IS INCONSISTENT
        world.step(1 / 60f, 8, 2); //(timeStep, velocityIterations, positionIterations) renders at 60 fps, how good collisons, how good overlapping
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        
        batch.begin();
        batch.draw(backgroundDay, 0, GROUND_HEIGHT_POSITION/ PPM, WORLD_WIDTH/ PPM, WORLD_HEIGHT/ PPM);
        batch.end();
        
        Gdx.gl.glEnable(GL20.GL_BLEND); //enables blending the white with the background to make it brighter
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, 0.3f); // slight white glow
        shapeRenderer.rect(0, 0, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        batch.begin();
        batch.draw(tree, WORLD_WIDTH/4/PPM, 64/PPM, TREE_SIZE* 7, TREE_SIZE *7);
        batch.end();
        
        batch.begin();
        for(int i = 0; i < TILE_INDEX_X; i++){
            for(int j = 0; j < 4; j++) {
        		batch.draw(
        			groundTexture,
        			i * TILE_SIZE,
        			TILE_SIZE * j,
        			TILE_SIZE,
        			TILE_SIZE
        		);
        	}
        }
        batch.end();
        
        contactListener.processPendingDestruction(); //need to be called  after world step in order for LibGDX to be happy
        player.update(delta);  //happens before player rendering and coloring
        
        //player color and rendering
        batch.begin();
        batch.draw(
            player.getFrame(),
            player.getPosition().x -64f/PPM,
            player.getPosition().y -40f/PPM, //change here for boss sprire and height pos
            player.getFrameSize()/ PPM *2,
            player.getFrameSize() / PPM *2
        );

        batch.end();
        
        batch.begin();
        
        for (PlayerAttackBlock attack : player.getActiveAttacks()) {
        	if(attack.isDestroyed()){ //this if statement gets rid of the left over color when the block deletes
        		player.getActiveAttacks().removeValue(attack, true);
        	}
        	else {
        		batch.draw(
        		  rockTexture,
                  attack.getPosition().x- PlayerAttackBlock.getBlockWidth() / 2,
                  attack.getPosition().y - PlayerAttackBlock.getBlockHeight() /2, // 0.64 meters
                  PlayerAttackBlock.getBlockWidth() * attack.getBlockSize(), //check to see if critblock
                  PlayerAttackBlock.getBlockHeight() * attack.getBlockSize()
              );
                
        	}
        }
        
        batch.end();
        
        
        batch.begin();
        for(BossAttackBlock attack : boss.getActiveAttacks()) {
        	if(attack.isDestroyed()) {
    			boss.getActiveAttacks().removeValue(attack, true); //clean up array to prevent lingering sprite images
    		}
        	if(attack.isAbove()){ //this if statement gets rid of the left over color when the block deletes
        		batch.draw(
                        kunaiTexture,
                        attack.getPosition().x- attack.getBlockWidth() / 2,
                        attack.getPosition().y - attack.getBlockHeight() /2, // 0.64 meters
                        attack.getBlockWidth(),
                        attack.getBlockHeight()
                    );
        		
        	}
        	else {
        		
        		batch.draw(
                        swooshTexture,
                        attack.getPosition().x- attack.getBlockWidth() / 2,
                        attack.getPosition().y - attack.getBlockHeight() /2, // 0.64 meters
                        attack.getBlockWidth() *3,
                        attack.getBlockHeight() *3
                    );
        	}
            
        }
        batch.end();
        
        batch.begin();

        batch.draw(
            boss.getFrame(),
            boss.getPosition().x - 190f/ PPM,
            boss.getPosition().y - 95f/ PPM, //change here for boss sprire and height pos
            96f / PPM *4,
            96f / PPM *4
        );
        batch.end();
        
        if (contactListener.shouldCreateWife() && wife == null) {
            createWife();
            contactListener.resetCreateWife();
        }
        if(wife !=null) {
        	 batch.begin();
             batch.draw(
                 wife.getFrame(),
                 wife.getPosition().x -64f/PPM,
                 wife.getPosition().y -45f/PPM, 
                 wife.getFrameSize()/ PPM *2,
                 wife.getFrameSize() / PPM *2
             );

             batch.end();
             wife.update(delta);
        }
        
        //making a bit more grass
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //fills in color for ground
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.rect(0, 64f/PPM, GROUND_WIDTH_SIZE / PPM, TILE_SIZE/2 ); // Collision block is 2/3 shape of ground color right now
        shapeRenderer.end();

       
        boss.update(delta);
        
        dialogueManager.update(delta);
        dialogueManager.render();
        
        healthUI.bossAndPlayerUpdate();
        healthUI.render(delta);
        
        //box2d debug
        //debugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose() { //when new scene starts make sure to dispose these elements
    	player.dispose();
        wife.dispose();
        boss.dispose();
        
    	tree.dispose();
    	swooshTexture.dispose();
    	kunaiTexture.dispose();
    	groundTexture.dispose();
        backgroundDay.dispose();
        rockTexture.dispose();
        healthUI.dispose();
        dialogueManager.dispose();
        shapeRenderer.dispose();
        world.dispose();
        
        player = null;
        boss = null;
        wife = null;
    }
	public void beginShutdown() {
		shuttingDown = true;
	}
}
