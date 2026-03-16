package com.GatherAtDusk.Managers;

import com.GatherAtDusk.BossStuff.Boss;
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
    private Boss boss;
    private Stage stage;
    private Label dialogueLabel;

    private Array<String> lines = new Array<>();
    private int currentIndex = 0;

   

    private boolean active = false;
  //right now i have 2 constructors because i may want the dialogue above the player or boss
    
    public DialogueManager(Player player, int dialogueID) { 
        this.player = player;
        setupUI();
        createLines(dialogueID);
        startDialogue();
    }

    public DialogueManager(Player player, Boss boss, int dialogueID) {
        this.player = player;
        this.boss = boss;
        setupUI();
        createLines(dialogueID);
        startDialogue();
    }

    private void setupUI() {

        stage = new Stage(new ScreenViewport());

        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.LIGHT_GRAY);

        dialogueLabel = new Label("", style);

        dialogueLabel.setPosition(50, 5);
        dialogueLabel.setSize(700, 100);

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
                lines.add("It has been almost a day in my search."); //1
                lines.add("I must find my family..."); //2
                lines.add("Press WASD to move and SPACE to attack"); //3
                break;

            case 1:
            	player.setCanMove(false);
                lines.add("Hey you! You are going to pay for this!"); //
                break;

            case 2:
            	lines.add("Wife: Honey! What are you doing here?");
            	lines.add("Player: I'm here to save you of course!");
            	lines.add("Wife: Save me? This is my father!");
            	lines.add("I told you last night I was going to bring my father home today");
            	lines.add("Player: Oh yeah um I uh... forgot...");
                break;
            case 3:
            	lines.add("Player: So... No hard feelings?");
            	lines.add("Grandfather: Mhm");
        }
    }


	public void update(float delta) {

        if(!active) return;

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            currentIndex++;

            if(currentIndex >= lines.size) {
                active = false;
                player.setCanMove(true);
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