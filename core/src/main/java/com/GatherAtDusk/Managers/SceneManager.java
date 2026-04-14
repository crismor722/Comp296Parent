package com.GatherAtDusk.Managers;

import com.GatherAtDusk.MainGame;
import com.GatherAtDusk.Scenes.*; //imports all scenes
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class SceneManager {
    private MainGame game;
    private TitleScreen titleScreen;
    private Screen oldScreen;

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
            	disposeOldScreen();
                game.setScreen(new IntroScene(game));
                break;
            case 2:
            	disposeOldScreen();
                game.setScreen(new BossScene(game));
                
                break;
            default:
                game.setScreen(new IntroScene(game));
        }
    }
    
    public void isGameOver() {
    	disposeOldScreen();
    	game.setScreen(new GameOverScene(game));
    }
    
    public void setEndScene() {
    	disposeOldScreen();
    	game.setScreen(new EndScene(game));
    }
    
    public void setGameWin() {
    	disposeOldScreen();
    	game.setScreen(new GameWinScene(game));
    	SaveManager.overrideSave();
    }
    private void disposeOldScreen() {
    	oldScreen = game.getScreen();
    	
    	if (oldScreen instanceof IntroScene) {
    	    ((IntroScene) oldScreen).beginShutdown(); // saftey net
    	}
    	if (oldScreen instanceof BossScene) {
    	    ((BossScene) oldScreen).beginShutdown();
    	}
    	
    	if (oldScreen instanceof EndScene) {
    	    ((EndScene) oldScreen).beginShutdown();
    	}
    	
    	if (oldScreen instanceof GameWinScene) {
    	    ((GameWinScene) oldScreen).beginShutdown();
    	}
    	if (oldScreen instanceof GameOverScene) {
    	    ((GameOverScene) oldScreen).beginShutdown();
    	}
    	
    	if (oldScreen instanceof TitleScreen) {
    	    ((TitleScreen) oldScreen).beginShutdown();
    	}
    	
    	if (oldScreen != null) {
            //oldScreen.dispose(); 
    		Gdx.app.postRunnable(() -> {// this delays the call of disposing after frame is finished otherwise the game crashes
    		    oldScreen.dispose();// allows me to call dispose for any screen since it is in the screen interface and all the scenes extend screenAdapter which is uses the screen interface
    		});
        }
    }
}
