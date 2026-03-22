package com.GatherAtDusk.PlayerStuff;

import com.GatherAtDusk.BossStuff.Boss;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HealthUI {
	private Stage stage;
	private Label playerLabel; //label is in the LibGDX library
	private Label bossLabel;
	private int playerHealth;
	private int bossHealth;
	private float uiXPlayer = 10f;
	private float uiXBoss = Gdx.graphics.getWidth() *3/4f;
	private Player player;
	private Boss boss;
	
	//NOTE: must call healthUI.update; and healthUI.render(delta); in every scene with player
	
	public HealthUI(Player player) {
		this.player = player;
		
		
		stage = new Stage( new ScreenViewport());
		Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(2.5f);
        
        playerLabel = new Label("HP: 100", style);
        bossLabel = new Label("HP: 100", style);
		
		createUI(uiXPlayer, playerLabel);
	}
	
	public HealthUI(Player player, Boss boss){
		this.player = player;
		this.boss = boss;
		
		
		stage = new Stage( new ScreenViewport());
		Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(2.5f);
        
        playerLabel = new Label("HP: 100", style);
        bossLabel = new Label("HP: 100", style);
		
		createUI(uiXPlayer, playerLabel);
		createUI(uiXBoss, bossLabel);
	}
	
	private void createUI(float uiX, Label label) {
		label.setPosition(uiX, Gdx.graphics.getHeight() * 9/10F); //x was 10
        label.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 7f);
        stage.addActor(label);
		
	}

	public void playerUpdate() { 
		playerHealth = player.getHealth();
        playerLabel.setText("Health: " + playerHealth);
    }
	
	public void bossAndPlayerUpdate() {
		bossHealth = boss.getHealth();
		bossLabel.setText("Health: " + bossHealth);
		
		playerHealth = player.getHealth();
        playerLabel.setText("Health: " + playerHealth);
	}

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
