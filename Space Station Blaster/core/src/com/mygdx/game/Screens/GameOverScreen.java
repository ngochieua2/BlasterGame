package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.SpaceStationBlaster;

public class GameOverScreen implements Screen {

    private static final int BANNER_WIDTH = 400;
    private static final int BANNER_HEIGHT = 300;


    private SpaceStationBlaster game;
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    private Texture gameoverBanner;
    private Image image;
    private Label scoreLabel;

    private TextButton tryAgain;
    private TextButton exit;

    private int score;

    private Table table;


    public GameOverScreen (final SpaceStationBlaster game, int score){
        this.game = game;
        this.score = score;

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));

        // gameover banner
        gameoverBanner = new Texture("screen/gameover.png");
        image = new Image(gameoverBanner);

        // score label
        scoreLabel = new Label(String.format("SCORE: %d", score), skin ,"default");
        scoreLabel.setPosition(Gdx.graphics.getWidth() /2 - scoreLabel.getWidth()/2, Gdx.graphics.getHeight()/2 + 20);
        scoreLabel.setAlignment(Align.center);

        // try again button
        tryAgain = new TextButton("TRY AGAIN", skin, "default");
        tryAgain.setWidth(220f);
        tryAgain.setHeight(80f);
        tryAgain.setPosition(Gdx.graphics.getWidth() /2 - tryAgain.getWidth()/2, Gdx.graphics.getHeight()/3 - 30 );
        tryAgain.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        } );

        // exit button
        exit = new TextButton("EXIT GAME", skin, "default");
        exit.setWidth(220f);
        exit.setHeight(80f);
        exit.setPosition(Gdx.graphics.getWidth() /2 - exit.getWidth()/2, Gdx.graphics.getHeight()/3 - 100);
        exit.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        } );

        // set up table for gameover banner
        table = new Table();
        table.setHeight(BANNER_HEIGHT);
        table.setWidth(BANNER_WIDTH);
        table.setPosition(Gdx.graphics.getWidth() /2 - BANNER_WIDTH/2, Gdx.graphics.getHeight() * 2/3 - BANNER_HEIGHT/2);
        table.add(image);


        // set stage and add actor
        stage = new Stage();
        stage.addActor(table);
        stage.addActor(scoreLabel);
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
