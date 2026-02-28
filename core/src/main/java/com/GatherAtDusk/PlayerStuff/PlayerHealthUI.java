package com.GatherAtDusk.PlayerStuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PlayerHealthUI {
	private Player player;
	private Stage stage;
	private Label healthLabel; //label is in the LibGDX library
	private int health;
	
	//NOTE: must call playerHealthUI.update; and playerHealthUI.render(delta); in every scene with player
	
	public PlayerHealthUI(Player player){
		this.player = player;
		stage = new Stage( new ScreenViewport());
		
		Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();

        healthLabel = new Label("HP: 100", style);
        healthLabel.setPosition(10, Gdx.graphics.getHeight() - 30);
        stage.addActor(healthLabel);
	}
	
	public void update() { 
		health = player.getHealth();
        healthLabel.setText("Health: " + health);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
