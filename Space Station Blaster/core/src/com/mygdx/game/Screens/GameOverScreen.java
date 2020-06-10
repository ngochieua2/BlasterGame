package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;

public class GameOverScreen implements Screen {

    private static final int FONT_SIZE = 24;
    private static final int FONT_SIZE_STAGE = 48;

    private SpaceStationBlaster game;
    private SpriteBatch batch;
    private Skin skin;
    private Skin skin2;
    private Stage stage;



    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bitmapFont;
    private Label.LabelStyle labelStyle;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameterStage;
    private BitmapFont bitmapFontStage;
    private Label.LabelStyle labelStyleStage;

    private Table table;

    // set button
    TextButton tryAgain;
    TextButton exit;

    public GameOverScreen (SpaceStationBlaster game){
        this.game = game;

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        //skin2 = new Skin(Gdx.files.internal("fonts/Gameover.fnt"));

        Label gameoverTitle = new Label("GAMEOVER", skin, "default");
        gameoverTitle.setSize(200, 60);
        gameoverTitle.setPosition(Gdx.graphics.getWidth() /2 - gameoverTitle.getWidth()/2, Gdx.graphics.getHeight() * 2/3);
        gameoverTitle.setAlignment(Align.center);

        tryAgain = new TextButton("TRY AGAIN", skin, "default");
        tryAgain.setWidth(100f);
        tryAgain.setHeight(40f);
        tryAgain.setPosition(Gdx.graphics.getWidth() /2 - tryAgain.getWidth()/2, Gdx.graphics.getHeight()/2 );

        exit = new TextButton("EXIT GAME", skin, "default");
        exit.setWidth(100f);
        exit.setHeight(40f);
        exit.setPosition(Gdx.graphics.getWidth() /2 - exit.getWidth()/2, Gdx.graphics.getHeight()/2 - 50);

        stage = new Stage();
        stage.addActor(gameoverTitle);
        stage.addActor(tryAgain);
        stage.addActor(exit);
        Gdx.input.setInputProcessor(stage);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        stage.draw();
        batch.end();
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
        game.spriteBatch.dispose();
    }
}
