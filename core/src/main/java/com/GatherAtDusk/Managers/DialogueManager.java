package com.GatherAtDusk.Managers;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.NPCS.Boss;
import com.GatherAtDusk.PlayerStuff.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DialogueManager {
    private Player player;
    private Stage stage;
    private Label dialogueLabel;
    private MainGame game; // game is being used to get the game's scenemanager
    private SceneManager sceneManager;

    private Array<String> lines = new Array<>();
    private int currentIndex = 0;
    private int dialogueID;

   

    private boolean active = false;
  //right now i have 2 constructors because i may want the dialogue above the player or boss
    
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

        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.LIGHT_GRAY);
        style.font.getData().setScale(2f);

        dialogueLabel = new Label("", style);

        dialogueLabel.setPosition(Gdx.graphics.getWidth() * 1/3f, Gdx.graphics.getHeight() * 1/20f);
        dialogueLabel.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 7f);

        stage.addActor(dialogueLabel);
    }

    private void startDialogue() {
        if(lines.size == 0) return;
        currentIndex = 0;
        dialogueLabel.setText(lines.get(currentIndex));
        active = true;
    }

    private void createLines(int dialogueID) {
        switch(dialogueID) {

            case 0:
            	player.setCanMove(false);
                lines.add("Press enter to read next line"); //0
                lines.add("YOU: It has been almost a day in my search."); //1
                lines.add("I must find my family..."); //2
                lines.add("Press WASD to move and SPACE to attack"); //3
                break;

            case 1:
            	player.setCanMove(false);
                lines.add("YOU: Hey you! You are going to pay for this!"); //
                break;

            case 2:
            	player.setCanMove(false);
            	lines.add("WIFE: Honey! What are you doing here?");
            	lines.add("YOU: I'm here to save you of course!");
            	lines.add("WIFE: Save me? This is my father!");
            	lines.add("WIFE: I told you last night I was going to bring my father home today");
            	lines.add("YOU: Oh yeah um I uh... forgot...");
                break;
            case 3:
            	lines.add("YOU: So... No hard feelings?");
            	lines.add("GRANDFATHER: Mhm");
            	break;
        }
    }
    
    public void isSetNewLine(int health, int dialogueID) {
    	this.dialogueID = dialogueID;
    	if(health <=0) {
    		lines.clear();
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
                if(player.isSitting() == false) player.setCanMove(true); //don't set player can move if sitting
                updateScene(dialogueID); //check if scene needs to be updated after dialogue is finished
                return;
            }

            dialogueLabel.setText(lines.get(currentIndex));
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

    public void dispose() {
        stage.dispose();
    }
}