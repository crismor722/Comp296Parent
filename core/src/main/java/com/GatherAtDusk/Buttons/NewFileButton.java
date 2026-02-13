package com.GatherAtDusk.Buttons;

import com.GatherAtDusk.SceneManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class NewFileButton extends TextButton {

    public NewFileButton(SceneManager sceneManager) {
        super("NEW FILE", createStyle());

        setSize(250, 70);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.goToSceneForCheckpoint(0); //CHANGE LATER
            }
        });
    }

    private static TextButtonStyle createStyle() {

        Pixmap pixmap = new Pixmap(250, 70, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.FOREST);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        TextureRegionDrawable drawable =
                new TextureRegionDrawable(new TextureRegion(texture));

        BitmapFont font = new BitmapFont();

        TextButtonStyle style = new TextButtonStyle();
        style.up = drawable;
        style.down = drawable;
        style.font = font;

        return style;
    }
}

