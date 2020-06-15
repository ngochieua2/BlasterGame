package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

public class TitleScreen implements Screen {

    private static final int BANNER_WIDTH = 600;
    private static final int BANNER_HEIGHT = 400;

    private SpaceStationBlaster game;
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    private Texture gameBanner;
    private Image image;

    private TextButton Play;
    private TextButton Instruction;
    private TextButton Exit;

    private Table table;

    public TitleScreen(final SpaceStationBlaster game){
        this.game = game;

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));

        // game banner
        gameBanner = new Texture("screen/gameTitle.png");
        image = new Image(gameBanner);

        // play button
        Play = new TextButton("PLAY GAME", skin, "default");
        Play.setWidth(250f);
        Play.setHeight(80f);
        Play.setPosition(Gdx.graphics.getWidth() /2 - Play.getWidth()/2, Gdx.graphics.getHeight()/3 + 50 );
        Play.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        } );

        // instruction button
        Instruction = new TextButton("INSTRUCTION", skin, "default");
        Instruction.setWidth(250f);
        Instruction.setHeight(80f);
        Instruction.setPosition(Gdx.graphics.getWidth() /2 - Instruction.getWidth()/2, Gdx.graphics.getHeight()/3 );
        Instruction.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        } );


        // exit button
        Exit = new TextButton("EXIT GAME", skin, "default");
        Exit.setWidth(250f);
        Exit.setHeight(80f);
        Exit.setPosition(Gdx.graphics.getWidth() /2 - Exit.getWidth()/2, Gdx.graphics.getHeight()/3 - 150);
        Exit.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        } );


        // set up table for game banner
        table = new Table();
        table.setHeight(BANNER_HEIGHT);
        table.setWidth(BANNER_WIDTH);
        table.setPosition(Gdx.graphics.getWidth() /2 - BANNER_WIDTH/2, Gdx.graphics.getHeight() * 3/4 - BANNER_HEIGHT/2);
        table.add(image);


        // set stage and add actor
        stage = new Stage();
        stage.addActor(table);
        stage.addActor(Play);
        stage.addActor(Instruction);
        stage.addActor(Exit);
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
