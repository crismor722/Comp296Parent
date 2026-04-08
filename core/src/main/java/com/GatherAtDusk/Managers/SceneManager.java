package com.GatherAtDusk.Managers;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Scenes.*; //imports all scenes

public class SceneManager {
    private MainGame game;
    private TitleScreen titleScreen;

    public SceneManager(MainGame game) {
        this.game = game;
    }
    
    public void startTitleScreen() {
    	this.titleScreen = new TitleScreen(game);
    	game.setScreen(titleScreen);
    	
    }

    public void goToSceneForCheckpoint(int checkpointId) {
        switch(checkpointId) {
            case 0:
                game.setScreen(new IntroScene(game));
                break;
            case 1:
                game.setScreen(new IntroScene(game));
                break;
            case 2:
                game.setScreen(new BossScene(game));
                break;
            default:
                game.setScreen(new IntroScene(game));
        }
    }
    
    public void isGameOver() {
    	game.setScreen(new GameOverScene(game));
    }
    
    public void setEndScene() {
    	game.setScreen(new EndScene(game));
    }
    
    public void setGameWin() {
    	game.setScreen(new GameWinScene(game));
    	SaveManager.overrideSave();
    }
    
    public void disposeTitleScreen() {
    	titleScreen.dispose();
    }
}
