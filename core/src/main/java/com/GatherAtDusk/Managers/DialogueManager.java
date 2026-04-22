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
    private Texture texture;
    private BitmapFont font;
    private final MainGame game; // game is being used to get the game's scenemanager
    private SceneManager sceneManager;
    private Array<String> lines = new Array<>();
    private Array<Sound> voiceLines = new Array<>();
    private Array<Sound> disposeVoiceLines = new Array<>();
    
    private Sound currentSound;
    private Sound oldSound;
    private Sound line1player;
    private Sound line2player;
    private Sound line3player;
    private Sound line4player;
    private Sound line5player;
    private Sound line6player;
    
    private Sound line1wife;
    private Sound line2wife;
    private Sound line3wife;
    private Sound line4wife;
    
    private Sound line1grandfather;
    
    
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
        
        // making background for text
        Pixmap pixmap = new Pixmap(BORDER_WIDTH, BORDER_HEIGHT, Pixmap.Format.RGBA8888); // setting height and width here doesn't do anything i just do it for the border
        pixmap.setColor(Color.WHITE); 
        pixmap.fill();
        
        //border
        pixmap.setColor(Color.BLACK);
        pixmap.drawRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());

        texture = new Texture(pixmap);
        Drawable whiteDrawable = new TextureRegionDrawable(new TextureRegion(texture));

        pixmap.dispose();
        
        //text
        font = new BitmapFont();
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

    private void setupVoiceLines(int id) {
    	if(id ==1) {
    		line1player = Gdx.audio.newSound(Gdx.files.internal("line1player.wav"));
    		line2player = Gdx.audio.newSound(Gdx.files.internal("line2player.wav"));
    		disposeVoiceLines.addAll(line1player, line2player);
    	}
    	else if(id ==2) {
    		line3player = Gdx.audio.newSound(Gdx.files.internal("line3player.wav"));
    		disposeVoiceLines.add(line3player);
    	}
		
    	else if(id == 3) {
    		line4player = Gdx.audio.newSound(Gdx.files.internal("line4player.wav"));
    		line5player = Gdx.audio.newSound(Gdx.files.internal("line5player.wav"));
    		
    		
    		line1wife = Gdx.audio.newSound(Gdx.files.internal("line1wife.wav"));
    		line2wife = Gdx.audio.newSound(Gdx.files.internal("line2wife.wav"));
    		line3wife = Gdx.audio.newSound(Gdx.files.internal("line3wife.wav"));
    		disposeVoiceLines.addAll(line4player, line5player, line1wife, line2wife, line3wife);
    	}
		
    	else if(id == 4) {
    		line6player = Gdx.audio.newSound(Gdx.files.internal("line6player.wav"));
    		line1grandfather = Gdx.audio.newSound(Gdx.files.internal("line1grandfather.wav"));
    		disposeVoiceLines.addAll(line6player, line1grandfather);
    	}
    	else if (id == 5) {
    		line4wife= Gdx.audio.newSound(Gdx.files.internal("line4wife.wav"));
    		disposeVoiceLines.add(line4wife);
    	}
		
		
    	
		/*disposeVoiceLines.addAll(line1player, line2player, line3player, line4player, line5player, line6player, 
				line1wife, line2wife, line3wife, line4wife,
				line1grandfather);
		*/
	}

	private void startDialogue() {
        if(lines.size == 0) return;
        currentIndex = 0;
        dialogueLabel.setText(lines.get(currentIndex));
        voiceDialogueHelper();
        active = true;
    }
	
	private void voiceDialogueHelper() {
		if(currentIndex!=0){
			oldSound = voiceLines.get(currentIndex-1);
			if(oldSound != null) {
				oldSound.stop();
			}
		}
		currentSound = voiceLines.get(currentIndex);
        if(currentSound != null) {
        	currentSound.play();
        }
	}

    private void createLines(int dialogueID) {
        switch(dialogueID) {

            case 0:
            	setupVoiceLines(1);
            	player.setCanMove(false);
                lines.add("Press enter to read next line"); //0
                voiceLines.add(null);//0
                lines.add("YOU: It has been almost a day in my search."); //1 line 1 player
                voiceLines.add(line1player);//1
                lines.add("YOU: I must find my family..."); //2 line 2 player
                voiceLines.add(line2player);//2
                lines.add("Press WASD to move and SPACE to attack"); //3
                voiceLines.add(null);//3
                break;

            case 1:
            	setupVoiceLines(2);
            	player.setCanMove(false);
                lines.add("YOU: Hey you! You are going to pay for this!"); // 0 line 3 player
                voiceLines.add(line3player);//0
                Wife.setTypeWin(); // reset here because of it being a static method maybe find a better way to reset wife
                break;

            case 2:
            	setupVoiceLines(3);
            	player.setCanMove(false);
            	lines.add("WIFE: Honey! What are you doing here?"); //0 line 1 wife
            	voiceLines.add(line1wife);// 0
            	lines.add("YOU: I'm here to save you of course!"); //1 line 4 player
            	voiceLines.add(line4player);// 1
            	lines.add("WIFE: Save me? This is my father!");// 2 line 2 wife
            	voiceLines.add(line2wife);// 2
            	lines.add("WIFE: I told you last night I was going to bring my father home today"); //3 line 3 wife
            	voiceLines.add(line3wife);// 3
            	lines.add("YOU: Oh yeah um I uh... forgot..."); //4 line 5 player
            	voiceLines.add(line5player); //4
                break;
                
            case 3:
            	setupVoiceLines(4);
            	lines.add("YOU: So... No hard feelings?"); // 0 line 6 player
            	voiceLines.add(line6player);//0
            	lines.add("GRANDFATHER: Mhm"); // 1 line 1 grandpa
            	voiceLines.add(line1grandfather);//1
            	break;
            	
            case 4:
            	setupVoiceLines(5);
            	player.setCanMove(false);
            	lines.add("WIFE: Honey! Nooo! Are you okay???"); // 0 line 4 wife
            	voiceLines.add(line4wife);//0
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
    		voiceLines.get(currentIndex-1).stop();
    		sceneManager.setEndScene();
    		break;
    	case 3:
    		voiceLines.get(currentIndex-1).stop();
    		sceneManager.setGameWin();
    		break;
    	}
	}
    
	public void update(float delta) {

        if(!active) return;

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            currentIndex++; // voice lines and text follow the same index

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
            voiceDialogueHelper(); //check if verbal dialogue
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
    	for (Sound s : disposeVoiceLines) {
            if (s != null) s.dispose();;
        }
    	texture.dispose();
    	font.dispose();
        stage.dispose();
    }
}