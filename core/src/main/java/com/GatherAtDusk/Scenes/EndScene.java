package com.GatherAtDusk.Scenes;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Blocks.BossAttackBlock;
import com.GatherAtDusk.Blocks.PlayerAttackBlock;
import com.GatherAtDusk.Buttons.EasterEggButton;
import com.GatherAtDusk.ContactListener.CollisionType;
import com.GatherAtDusk.ContactListener.GameContactListener;
import com.GatherAtDusk.Helpers.AnimationHelper;
import com.GatherAtDusk.Managers.DialogueManager;
import com.GatherAtDusk.NPCS.Boss;
import com.GatherAtDusk.NPCS.Child;
import com.GatherAtDusk.NPCS.Wife;
import com.GatherAtDusk.PlayerStuff.HealthUI;
import com.GatherAtDusk.PlayerStuff.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EndScene extends ScreenAdapter{
	
	private final MainGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private World world;
    //private Box2DDebugRenderer debugRenderer;
    private Player player;
    private Boss boss;
    private Texture grassTexture; //note: top of grassTexture is 37.5% transparent
    private Texture groundTexture;
    private Texture backgroundDusk;
    private Texture campfireSheet;
    private GameContactListener contactListener;
    private DialogueManager dialogueManager;
    private Wife wife;
    private Child child;
    private Animation<TextureRegion> campfireAnimation;
    private EasterEggButton easterEggButton;
    private Stage stage;
    private boolean shuttingDown = false;
    
	private static final int HGRAVITY = 0;
    private static final float VGRAVITY = -9.8f; //LibGDX likes floats for decimals
    private static final float WORLD_WIDTH = 800f;
    private static final float WORLD_HEIGHT = 480f;
    private static final float GROUND_WIDTH_POSITION = WORLD_WIDTH/2;
    private static final float GROUND_HEIGHT_POSITION= 50F; //height center is the position not size
    private static final float GROUND_WIDTH_SIZE =  WORLD_WIDTH;
    private static final float GROUND_HEIGHT_SIZE = GROUND_HEIGHT_POSITION;
    private static final float FRICTION = 0.8f; //friction doesn't really have an effect now, maybe find a way to remove later
    private static final float PLAYER_START_X = WORLD_WIDTH / 2  - 60f; // center of screen in pixels
    private static final float START_Y = GROUND_HEIGHT_POSITION + 60f; // above ground
    private static final float PPM = (float) 100; // 100 pixels per meter 
    private static final float WALL_THICKNESS = 10f / PPM;
    private static final float TILE_SIZE = 16f/ PPM;
    private static final float TILE_INDEX_X = GROUND_WIDTH_SIZE / (TILE_SIZE *PPM); // 800 /16 = 50
    private float stateTime;
    private static final int CAMPFIRE_SIZE = 32;
    private static final int CAMPFIRE_FRAMES = 4;
    
    private static final float CHILD_SPAWN_OFFSET = 200f;
    private static final float WIFE_SPAWN_OFFSET = 30f;
    private static final float PLAYER_X_OFFSET = Player.getPlayerWidth();
    private static final float PLAYER_Y_OFFSET = 45f;
    private static final float WIFE_X_OFFSET = Wife.getWifeWidth();
    private static final float WIFE_Y_OFFSET = 45f;
    private static final float CHILD_X_OFFSET = Child.getChildWidth();
    private static final float CHILD_Y_OFFSET = 40f;
    private static final float BOSS_X_OFFSET = 190f;
    private static final float BOSS_Y_OFFSET = 95f;
    private static final float CAMPFIRE_OFFSET = 10f;
    private static final float DK_OFFSET = 80f;
    private static final float BOSS_SPAWN = 500f;
    
    private Texture texture;
    private Texture dkSheet;
    private BitmapFont buttonFont;
    private Pixmap pixmap;
    
    
    public EndScene(MainGame game) {
    	this.game = game;
    	this.batch = MainGame.batch;
    	this.shapeRenderer = MainGame.shapeRenderer;
    }
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);

        //gravity (downward)
        //NOTE: world is not the same as the scene
        world = new World(new Vector2(HGRAVITY, VGRAVITY), true); //world handles gravity
        //debugRenderer = new Box2DDebugRenderer();

        createGround();
        createBoundaries();
        createPlayer(PLAYER_START_X, START_Y);
        createWifeAndChild();
        createBoss(); //boss is needed later
        createExtraStuff();
        
        //contactListener = new GameContactListener(game, player, boss);
        contactListener = new GameContactListener(game, player);
        world.setContactListener(contactListener); //set contact listener is built into box2d
    }
    
	private void createBoss() {
		boss = new Boss(world, player, BOSS_SPAWN, START_Y);
	}

	private void createPlayer(float spawnX, float spawnY) {
		player = new Player(world, spawnX, spawnY);
		player.setSitting(true);
	}
	private void createWifeAndChild() {
		wife = new Wife(world, PLAYER_START_X - WIFE_SPAWN_OFFSET, START_Y);
		wife.setSitting(true);
		
		child = new Child(world, PLAYER_START_X + CHILD_SPAWN_OFFSET, START_Y);
	}
	private void createExtraStuff() {
		grassTexture = new Texture("Platform Tileset/grasstop.png");
		groundTexture = new Texture("Platform Tileset/dirtfull.png");
		backgroundDusk = new Texture("DuskBackground.png");
		campfireSheet = new Texture("Campfire.png");
		campfireAnimation = AnimationHelper.createAnimation(campfireSheet, CAMPFIRE_SIZE, CAMPFIRE_SIZE, CAMPFIRE_FRAMES, 0.1f, false);
		dialogueManager = new DialogueManager(game, player, 3);
		createButtonStuff();
		dkSheet = new Texture("dk1.png"); //need to make button stuff here to dispose of everything properly
		easterEggButton = new EasterEggButton(texture, dkSheet, buttonFont);
		
		stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); 
		
		 easterEggButton.setPosition(
				 Gdx.graphics.getWidth() / 2f - EasterEggButton.getButtonWidth() / 2f, 
	             Gdx.graphics.getHeight() / 5f - EasterEggButton.getButtonHeight() / 2f
	     );
		 stage.addActor(easterEggButton);
	}

	private void createButtonStuff() {
		int buttonWidth = 120;
		int buttonHeight = 120;
		pixmap = new Pixmap(buttonWidth, buttonHeight, Pixmap.Format.RGBA8888); //setting button height and format
        pixmap.setColor(Color.CLEAR); //temp color green and testing to see if color works/looks good
        pixmap.fill();

        texture = new Texture(pixmap); // sending pixmap to texture
        pixmap.dispose(); //don't need pixmap memory anymore
        buttonFont = new BitmapFont(); 
		
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
    	    Gdx.input.setInputProcessor(null);
    	    return;
    	}
    	stateTime += delta;
        // sky color
    	//Gdx.gl.glClearColor(0.05f, 0.02f, 0.08f, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //CHECK HERE IF COLLISIONS OR OVERLAP IS INCONSISTENT
        world.step(1 / 60f, 8, 2); //(timeStep, velocityIterations, positionIterations) renders at 60 fps, how good collisons, how good overlapping
        camera.update();
        
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundDusk, 0, GROUND_HEIGHT_POSITION/ PPM, WORLD_WIDTH/ PPM, WORLD_HEIGHT/ PPM);
        for(int i = 0; i < TILE_INDEX_X; i++){
            batch.draw(
                grassTexture,
                i * TILE_SIZE,
                TILE_SIZE * 4, // 0.64 meters
                TILE_SIZE,
                TILE_SIZE
            );
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
        
        player.update(delta);  //happens before player rendering and coloring
        
        //player color and rendering
        batch.draw(
            player.getFrame(),
            player.getPosition().x -PLAYER_X_OFFSET/PPM,
            player.getPosition().y -PLAYER_Y_OFFSET/PPM, //change here for player sprite and height pos
            Player.getFrameSize()/ PPM *2,
            Player.getFrameSize() / PPM *2
        );

        batch.draw(
            boss.getFrame(),
            boss.getPosition().x - BOSS_X_OFFSET/ PPM,
            boss.getPosition().y - BOSS_Y_OFFSET/ PPM, //change here for boss sprite and height pos
            Boss.getFrameSize() / PPM *4,
            Boss.getFrameSize() / PPM *4
        );
        batch.draw(
         
        		campfireAnimation.getKeyFrame(stateTime, true),
                (WORLD_WIDTH/2 - (CAMPFIRE_SIZE +CAMPFIRE_OFFSET))/ PPM,
                (GROUND_HEIGHT_POSITION + CAMPFIRE_OFFSET) / PPM, //change here for position
                (CAMPFIRE_SIZE +0f) / PPM *3,
                (CAMPFIRE_SIZE +0f) / PPM  *3 
            );
       
       batch.draw(
           wife.getFrame(),
           wife.getPosition().x -WIFE_X_OFFSET/PPM,
           wife.getPosition().y -WIFE_Y_OFFSET/PPM, 
           Wife.getFrameSize()/ PPM *2,
           Wife.getFrameSize() / PPM *2
       );
       
       batch.draw(
           child.getFrame(),
           child.getPosition().x -CHILD_X_OFFSET/PPM,
           child.getPosition().y -CHILD_Y_OFFSET/PPM, 
           Child.getFrameSize()/ PPM *3/2,
           Child.getFrameSize() / PPM *3/2
       );

       batch.end();
       
       if(easterEggButton.isAddAnimation()) {
    	   batch.begin();
    	   batch.draw(
    			   easterEggButton.getFrame(),
    	           (WORLD_WIDTH/2 - (CAMPFIRE_SIZE +DK_OFFSET))/ PPM,
                   (GROUND_HEIGHT_POSITION + DK_OFFSET) / PPM, //change here for position
                   (CAMPFIRE_SIZE +0f) / PPM *3,
                   (CAMPFIRE_SIZE +0f) / PPM  *3 
    	   );
    	   batch.end();
       }
       
       child.update(delta);
       wife.update(delta);
       boss.update(delta);
       dialogueManager.update(delta);
       dialogueManager.render();
       stage.act(delta);
       easterEggButton.update(delta);
       stage.draw();
       
                
        //box2d debug
        //debugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose() { //when new scene starts make sure to dispose these elements
    	player.dispose();
        wife.dispose();
        child.dispose();
        boss.dispose();
        
        texture.dispose();
        dkSheet.dispose();
        buttonFont.dispose();
        grassTexture.dispose();
        campfireSheet.dispose();
    	groundTexture.dispose();
        backgroundDusk.dispose();
        stage.dispose();
        dialogueManager.dispose();
        world.dispose();
        
        player = null;
        boss = null;
        child = null;
        wife = null;
    }
	public void beginShutdown() {
		shuttingDown = true;
	}
}
