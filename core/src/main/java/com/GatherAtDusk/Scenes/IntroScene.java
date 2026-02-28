package com.GatherAtDusk.Scenes;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Blocks.BossAttackBlock;
import com.GatherAtDusk.Blocks.CheckpointBlock;
import com.GatherAtDusk.Blocks.PlayerAttackBlock;
import com.GatherAtDusk.ContactListener.*;
import com.GatherAtDusk.PlayerStuff.Player;
import com.GatherAtDusk.PlayerStuff.PlayerHealthUI;
import com.GatherAtDusk.Saving.SaveManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2; // Vector2 makes it so it defines left right up and down like vectors in physics
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class IntroScene extends ScreenAdapter {
	
    private final MainGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    private CheckpointBlock checkpoint1;
    private CheckpointBlock checkpoint2;
    
    private static final int HGRAVITY = 0;
    private static final float VGRAVITY = -9.8f; //LibGDX likes floats for decimals
    private static final float WORLD_WIDTH = 800f;
    private static final float WORLD_HEIGHT = 480f;
    private static final float GROUND_WIDTH_POSITION = WORLD_WIDTH/2;
    private static final float GROUND_HEIGHT_POSITION= 50F; //height center is the position not size
    private static final float GROUND_WIDTH_SIZE =  WORLD_WIDTH;
    private static final float GROUND_HEIGHT_SIZE = GROUND_HEIGHT_POSITION;
    private static final float FRICTION = 0.8f; //friction doesn't really have an effect now, maybe find a way to remove later
    private float playerStartX = WORLD_WIDTH / 2 ; // center of screen in pixels
    private float playerStartY = GROUND_HEIGHT_POSITION + 60f / 2; // above ground
    private static final float PPM = (float) 100; // 100 pixels per meter 
    private static final float WALL_THICKNESS = 10f / PPM;
    private Array<CheckpointBlock> checkpointsArray = new Array<>();
    private GameContactListener contactListener;
    private PlayerHealthUI playerHealthUI;
    
    private Vector2 tempVector;
    private BossAttackBlock tempDamageBlock;
    
    public IntroScene(MainGame game) {
        this.game = game;
    }

    @Override
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
        createCheckpoints(); //checkpoints need to be before player
        createPlayerHealthUI();
        
        
        tempDamageBlock();
        
        contactListener = new GameContactListener(player, checkpointsArray);
        world.setContactListener(contactListener); //set contact listener is built into box2d
    }


	private void tempDamageBlock() {
		tempVector = new Vector2(800f /2 /PPM, 100f / PPM);
		tempDamageBlock = new BossAttackBlock(world, tempVector);
	}

	private void createPlayer(float spawnX, float spawnY) {
		player = new Player(world, spawnX, spawnY);
	}
	
	private void createPlayerHealthUI() {
		playerHealthUI = new PlayerHealthUI(player);
	}

	private void createGround() { //createGround needs to be in this scene and not in MainGame, MainGame only handles scenes
    	BodyDef bodyDef = new BodyDef(); //need to set definitions of where the ground will be
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(GROUND_WIDTH_POSITION/ PPM, GROUND_HEIGHT_POSITION / PPM); //Box2d centers it from middle of the width but 
        Body groundBody = world.createBody(bodyDef);

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
    
    private void createCheckpoints(){
    	checkpoint1 = new CheckpointBlock(world, 100f/ PPM, 100f/ PPM, 10f/ PPM, 10f/ PPM, 0);
    	checkpoint2 = new CheckpointBlock(world, 200f/ PPM, 100f/ PPM, 10f/ PPM, 10f/ PPM, 1);
    	
    	checkpointsArray.add(checkpoint1);
    	checkpointsArray.add(checkpoint2);

    	int checkpointID = SaveManager.loadCheckpoint();
    	Vector2 spawn = new Vector2(playerStartX, playerStartY); // default spawn
    	

    	switch (checkpointID) {
        case 0:
            spawn = checkpoint1.getSpawnPositionPixels(PPM); //Quality of life thing im trying out
            break;
        case 1:
            spawn = checkpoint2.getSpawnPositionPixels(PPM);
            break;
        default:
            spawn.set(playerStartX, playerStartY); // fallback spawn
    }
    	float spawnX = spawn.x;
    	float spawnY = spawn.y;
    	createPlayer(spawnX, spawnY); //create player at wherever the checkpoint is
    }

    @Override
    public void render(float delta) { 
        // sky color
        Gdx.gl.glClearColor(0.4f, 0.7f, 1f, 1); // color blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //CHECK HERE IF COLLISIONS OR OVERLAP IS INCONSISTENT
        world.step(1 / 60f, 6, 2); //(timeStep, velocityIterations, positionIterations) renders at 60 fps, how good collisons, how good overlapping
        camera.update();

        //ground color and rendering
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //fills in color for ground
        shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1); //color green
        shapeRenderer.rect(0, 0, GROUND_WIDTH_SIZE / PPM, GROUND_HEIGHT_SIZE *3/2 / PPM); // Collision block is 2/3 shape of ground color right now
        shapeRenderer.end();
        
        contactListener.processPendingDestruction(); //need to be called  after world step in order for LibGDX to be happy
        player.update();  //happens before player rendering and coloring
        
        //player color and rendering
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 0f, 0f, 1); // red player
        shapeRenderer.rect(
            player.getPosition().x - Player.getPlayerWidth() /2 / PPM, //sets start of x render to the left edge of the player body
            player.getPosition().y - Player.getPlayerHeight() /2 / PPM, //sets start of y render to the bottom of the player body
            //rendering a rect needs to start at bottom left corner of the player this formula does that
            Player.getPlayerWidth() / PPM,
            Player.getPlayerHeight() / PPM
        );
        shapeRenderer.end();
        
        
        //NEED TO MAKE THIS LOOP AND COLOR ALL CHECKPOINTS IN ARRAY FUTURE ME PLEASE
        
        
        //checkpoint color and rendering 
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 0f, 1); // yellow checkpoint
        shapeRenderer.rect(
            checkpoint1.getPosition().x - checkpoint1.getWidth() / 2 / PPM,
            checkpoint1.getPosition().y - checkpoint1.getHeight() / 2 / PPM,
            checkpoint1.getWidth() / PPM,
            checkpoint1.getHeight() / PPM
        );
        shapeRenderer.end();
        
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 0f, 0f, 1); // red for attacks
        
        //sensors don't get color fill so i trick the game with + 0.001f to make it think i am not coloring the sensor
        for (PlayerAttackBlock attack : player.getActiveAttacks()) {
        	shapeRenderer.rect(
        		attack.getPosition().x + 0.001f - PlayerAttackBlock.getBlockWidth() + 0.001f / 2f,
                attack.getPosition().y + 0.001f- PlayerAttackBlock.getBlockHeight() + 0.001f / 2f,
                PlayerAttackBlock.getBlockWidth() + 0.001f / PPM,
                PlayerAttackBlock.getBlockHeight() + 0.001f /PPM
                );
        }
        shapeRenderer.end();
        
        //temp damage block
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 0f, 0f, 1);
        shapeRenderer.rect(
            tempDamageBlock.getPosition().x - BossAttackBlock.getBlockWidth() /2 / PPM, 
            tempDamageBlock.getPosition().y - BossAttackBlock.getBlockHeight() /2 / PPM, 
            BossAttackBlock.getBlockWidth() / PPM,
            BossAttackBlock.getBlockHeight() / PPM
        );
        shapeRenderer.end();
        
        playerHealthUI.update();
        playerHealthUI.render(delta);
        //box2d debug
        debugRenderer.render(world, camera.combined);
        
        
    }

    @Override
    public void dispose() { //when new scene starts make sure to dispose these elements
        world.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();
    }
}
