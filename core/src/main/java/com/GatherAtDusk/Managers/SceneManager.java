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
            	setDisposeOldScreen();
                game.setScreen(new IntroScene(game));
                disposeOldScreen();
                break;
            case 2:
            	setDisposeOldScreen();
                game.setScreen(new BossScene(game));
                disposeOldScreen();
                
                break;
            default:
                game.setScreen(new IntroScene(game));
        }
    }
    
    public void isGameOver() {
    	setDisposeOldScreen();
    	game.setScreen(new GameOverScene(game));
    	disposeOldScreen();
    }
    
    public void setEndScene() {
    	setDisposeOldScreen();
    	game.setScreen(new EndScene(game));
    	disposeOldScreen();
    }
    
    public void setGameWin() {
    	setDisposeOldScreen();
    	game.setScreen(new GameWinScene(game));
    	SaveManager.overrideSave();
    	disposeOldScreen();
    	
    }
    private void setDisposeOldScreen() {
    	oldScreen = game.getScreen();
    	
    	if (oldScreen instanceof IntroScene) {
    	    ((IntroScene) oldScreen).beginShutdown(); // saftey net
    	}
    	else if (oldScreen instanceof BossScene) {
    	    ((BossScene) oldScreen).beginShutdown();
    	}
    	
    	else if (oldScreen instanceof EndScene) {
    	    ((EndScene) oldScreen).beginShutdown();
    	}
    	
    	else if (oldScreen instanceof GameWinScene) {
    	    ((GameWinScene) oldScreen).beginShutdown();
    	}
    	else if (oldScreen instanceof GameOverScene) {
    	    ((GameOverScene) oldScreen).beginShutdown();
    	}
    	
    	else if (oldScreen instanceof TitleScreen) {
    	    ((TitleScreen) oldScreen).beginShutdown();
    	}
    }
    
    private void disposeOldScreen(){
    	if (oldScreen != null) {
            //oldScreen.dispose(); 
    		Gdx.app.postRunnable(() ->{ //try taking 1-2 frames to sit a little bit before disposing
    			Gdx.app.postRunnable(() -> { 
    				oldScreen.dispose();// allows me to call dispose for any screen since it is in the screen interface and all the scenes extend screenAdapter which is uses the screen interface
    			});// this delays the call of disposing after frame is finished otherwise the game crashes
    		});   
        }
    }
}
