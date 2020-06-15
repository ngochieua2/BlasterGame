package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.SpaceStationBlaster;

public class InstructionScreen implements Screen {

    private static final int BANNER_WIDTH = 600;
    private static final int BANNER_HEIGHT = 400;

    private SpaceStationBlaster game;
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    private Texture gameBanner;
    private Image image;

    private TextButton Back;

    private Table table;

    public InstructionScreen(final SpaceStationBlaster game) {
        this.game = game;

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));

        // game banner
        gameBanner = new Texture("screen/instruction.png");
        image = new Image(gameBanner);

        // Back button
        Back = new TextButton("Back", skin, "default");
        Back.setWidth(250f);
        Back.setHeight(80f);
        Back.setPosition(Gdx.graphics.getWidth() /2 - Back.getWidth()/2, Back.getHeight() + 10 );
        Back.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        } );





    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
