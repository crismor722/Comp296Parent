package com.GatherAtDusk;

import com.GatherAtDusk.Scenes.*; //imports all scenes

public class SceneManager {
    private MainGame game;
    // private int lastCheckpoint;

    public SceneManager(MainGame game) {
        this.game = game;
       // lastCheckpoint = 0; // default starting checkpoint
    }
    
    public void startTitleScreen() {
    	game.setScreen(new TitleScreen(game));
    	
    }

    public void goToSceneForCheckpoint(int checkpoint) {
       // lastCheckpoint = checkpoint;
        switch(checkpoint) {
            case 0:
                game.setScreen(new IntroScene(game));
                break;
            /*case 1:
                game.setScreen(new GameScene(game, checkpoint));
                break;
            case 2:
                game.setScreen(new BossScene(game, checkpoint));
                break; */
            default:
                game.setScreen(new IntroScene(game));
        }
    }

   /* public void playerReachedCheckpoint(int checkpoint) {
        lastCheckpoint = checkpoint;
    }

    public int getLastCheckpoint() {
        return lastCheckpoint;
    } */
}
