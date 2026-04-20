package com.GatherAtDusk.Managers;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.NPCS.Wife;
import com.GatherAtDusk.PlayerStuff.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DialogueManager {
    private Player player;
    private Stage stage;
    private Label dialogueLabel;
    private MainGame game; // game is being used to get the game's scenemanager
    private SceneManager sceneManager;
    private Array<String> lines = new Array<>();
    private Array<Sound> voiceLines = new Array<>();
    private Array<Sound> disposeVoiceLines = new Array<>();
    
    private Sound currentSound;
    private Sound line1;
    
    
    private int currentIndex = 0;
    private int dialogueID;
    private static final int BORDER_HEIGHT = 150;
    private static final int BORDER_WIDTH = 600;
    private boolean gameOver = false;
	private boolean active = false;
    
    public DialogueManager(MainGame game,Player player, int dialogueID) { 
    	this.game = game;
    	this.sceneManager = game.sceneManager;
        this.player = player;
        this.dialogueID = dialogueID;
        setupUI();
        createLines(dialogueID);
        startDialogue();
    }

    private void setupUI() {
    	
        stage = new Stage(new ScreenViewport());
        setupVoiceLines();
        
        // making background for text
        Pixmap pixmap = new Pixmap(BORDER_WIDTH, BORDER_HEIGHT, Pixmap.Format.RGBA8888); // setting height and width here doesn't do anything i just do it for the border
        pixmap.setColor(Color.WHITE); 
        pixmap.fill();
        
        //border
        pixmap.setColor(Color.BLACK);
        pixmap.drawRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());

        Texture texture = new Texture(pixmap);
        Drawable whiteDrawable = new TextureRegionDrawable(new TextureRegion(texture));

        pixmap.dispose();
        
        //text
        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.DARK_GRAY);
        style.font.getData().setScale(3f);

        dialogueLabel = new Label("", style);
        Table textBox = new Table();
        
        textBox.setBackground(whiteDrawable);
        dialogueLabel.setWrap(true);

        textBox.add(dialogueLabel).width(BORDER_WIDTH).height(BORDER_HEIGHT).pad(20); //this actually edits width and height
        textBox.pack();   

        //dialogueLabel.setPosition(Gdx.graphics.getWidth() * 1/4f, Gdx.graphics.getHeight() *1/2f); //used to be 1/20
        //dialogueLabel.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 7f);
        
        textBox.setPosition( // i like this format better
                Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f
            );
        
        stage.addActor(textBox);
    }

    private void setupVoiceLines() {
		line1 = Gdx.audio.newSound(Gdx.files.internal("line1.wav"));
		
	}

	private void startDialogue() {
        if(lines.size == 0) return;
        currentIndex = 0;
        dialogueLabel.setText(lines.get(currentIndex));
        voiceDialogueHelper();
        active = true;
    }
	
	private void voiceDialogueHelper() {
		currentSound = voiceLines.get(currentIndex);
        if(currentSound != null) {
        	currentSound.play();
        }
	}

    private void createLines(int dialogueID) {
        switch(dialogueID) {

            case 0:
            	player.setCanMove(false);
                lines.add("Press enter to read next line"); //0
                voiceLines.add(null);//0
                lines.add("YOU: It has been almost a day in my search."); //1
                voiceLines.add(line1);//1
                lines.add("I must find my family..."); //2
                voiceLines.add(null);//2
                lines.add("Press WASD to move and SPACE to attack"); //3
                voiceLines.add(null);//3
                break;

            case 1:
            	player.setCanMove(false);
                lines.add("YOU: Hey you! You are going to pay for this!"); //
                voiceLines.add(null);
                Wife.setTypeWin(); // reset here because of it being a static method maybe find a better way to reset wife
                break;

            case 2:
            	player.setCanMove(false);
            	voiceLines.add(null);
            	lines.add("WIFE: Honey! What are you doing here?");
            	voiceLines.add(null);
            	lines.add("YOU: I'm here to save you of course!");
            	voiceLines.add(null);
            	lines.add("WIFE: Save me? This is my father!");
            	voiceLines.add(null);
            	lines.add("WIFE: I told you last night I was going to bring my father home today");
            	voiceLines.add(null);
            	lines.add("YOU: Oh yeah um I uh... forgot...");
            	voiceLines.add(null);
                break;
            case 3:
            	lines.add("YOU: So... No hard feelings?");
            	voiceLines.add(null);
            	lines.add("GRANDFATHER: Mhm");
            	voiceLines.add(null);
            	break;
            case 4:
            	player.setCanMove(false);
            	lines.add("WIFE: Honey! Nooo! Are you okay???");
            	voiceLines.add(null);
            	gameOver = true;
            	
        }
    }
    
    public void isSetNewLine(int health, int dialogueID) {
    	this.dialogueID = dialogueID;
    	if(health <=0) {
    		lines.clear();
    		voiceLines.clear();
    		createLines(dialogueID);
    		startDialogue();
    		
    	}
    }
    
    private void updateScene(int dialogueID) { //not all cases will need to switch to scene so just skip to 2
    	switch(dialogueID) {
    	case 2:
    		sceneManager.setEndScene();
    		break;
    	case 3:
    		sceneManager.setGameWin();
    		break;
    	}
	}
    
	public void update(float delta) {

        if(!active) return;

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            currentIndex++;

            if(currentIndex >= lines.size) { //when dialogue is finished...
                active = false;
                if(player.isSitting() == false && Wife.isTypeWin()) player.setCanMove(true); //don't set player can move if sitting or if lost
                //IMPORTAN NOTE WIFEWIN IS DEFAULT TO WIN DO NOT CHNAGE OR THIS WILL NOT WORK
                updateScene(dialogueID); //check if scene needs to be updated after dialogue is finished
                if(gameOver) {
                	sceneManager.isGameOver();
                }
                return;
            }
            
            

            dialogueLabel.setText(lines.get(currentIndex));
            voiceDialogueHelper();
        }

        stage.act(delta);
    }

	public void render() {

        if(!active) return;

        stage.draw();
    }

    public boolean isActive() {
        return active;
    }
    
    public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

    public void dispose() {
        stage.dispose();
    }
}