package com.GatherAtDusk.Scenes;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.ContactListener.*;
import com.GatherAtDusk.PlayerStuff.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2; // Vector2 makes it so it defines left right up and down like vectors in physics
import com.badlogic.gdx.physics.box2d.*;

public class IntroScene extends ScreenAdapter {
	
    private final MainGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    
    private static final int HGRAVITY = 0;
    private static final float VGRAVITY = -9.8f; //LibGDX likes floats for decimals
    private static final float WORLD_WIDTH = 800f;
    private static final float WORLD_HEIGHT = 480f;
    private static final float GROUND_WIDTH_POSITION = WORLD_WIDTH/2;
    private static final float GROUND_HEIGHT_POSITION= 50F; //height center is the position not size
    private static final float GROUND_WIDTH_SIZE =  WORLD_WIDTH;
    private static final float GROUND_HEIGHT_SIZE = GROUND_HEIGHT_POSITION;
    private static final float FRICTION = 0.8f; //friction doesn't really have an effect now, maybe find a way to remove later
    private static final float START_X = WORLD_WIDTH / 2 ; // center of screen in pixels
    private static final float START_Y = GROUND_HEIGHT_POSITION + 60f / 2; // above ground
    private static final float PPM = (float) 100; // 100 pixels per meter 
    private static final float WALL_THICKNESS = 10f / PPM;
    
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
        createPlayer();
        world.setContactListener(new GameContactListener(player)); //set contact listener is built into box2d
    }

    private void createPlayer() {
		player = new Player(world, START_X, START_Y);
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
        // left, right, top boundaries

        // left wall
    	BodyDef leftWallDef = new BodyDef();
    	leftWallDef.type = BodyDef.BodyType.StaticBody;
    	leftWallDef.position.set(0 - WALL_THICKNESS/2, WORLD_HEIGHT / 2 / PPM);
    	Body left = world.createBody(leftWallDef);

    	PolygonShape leftShape = new PolygonShape();
    	leftShape.setAsBox(WALL_THICKNESS / 2, WORLD_HEIGHT / 2 / PPM);

    	FixtureDef leftWallFixture = new FixtureDef();
    	leftWallFixture.shape = leftShape;
    	leftWallFixture.friction = 0f; // no friction on walls
    	leftWallFixture.restitution = 0f; // optional, prevents bouncing
    	left.createFixture(leftWallFixture).setUserData(CollisionType.WALL);

    	leftShape.dispose();


        // right wall
        BodyDef rightWallDef = new BodyDef();
        rightWallDef.type = BodyDef.BodyType.StaticBody;
        rightWallDef.position.set(WORLD_WIDTH / PPM + WALL_THICKNESS/2, WORLD_HEIGHT / 2 / PPM);
        Body right = world.createBody(rightWallDef);
        
        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsBox(WALL_THICKNESS / 2, WORLD_HEIGHT / 2 / PPM);
        
        FixtureDef rightWallFixture = new FixtureDef();
        rightWallFixture.shape = rightShape;
        rightWallFixture.friction = 0f; // no friction on walls
        rightWallFixture.restitution = 0f; // optional, prevents bouncing
        right.createFixture(rightWallFixture).setUserData(CollisionType.WALL);
        
        rightShape.dispose();
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
         
        player.update();
        
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
