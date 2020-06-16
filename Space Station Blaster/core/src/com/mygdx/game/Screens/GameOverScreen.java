package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceStationBlaster;

/**
 * GameOverScreen: is the screen when player lose the game
 */

public class GameOverScreen implements Screen {

    // constants for game over banner and button
    private static final int BANNER_WIDTH = 400;
    private static final int BANNER_HEIGHT = 300;
    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 80;

    // declare all game over screen entities
    private SpaceStationBlaster game;
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    private Texture gameoverBanner;
    private Image image;
    private Texture backgroundTexture;
    private Image background;

    private Label scoreLabel;

    private TextButton tryAgain;
    private TextButton mainMenu;
    private TextButton exit;

    private int score;

    private Viewport viewport;
    private OrthographicCamera camera;

    /**
     * GameOverScreen constructor: set up all entities will appear on this screen including banner,
     * labels, buttons, stage.
     * @param game is the game in generally
     * @param score is current score before player lose. It is got from PlayScreen
     */
    public GameOverScreen (final SpaceStationBlaster game, int score){
        this.game = game;
        this.score = score;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));

        // background
        backgroundTexture = new Texture("screen/star_background.png");
        background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setPosition(0,0);

        // gameover banner
        gameoverBanner = new Texture("screen/gameover.png");
        image = new Image(gameoverBanner);
        image.setSize(BANNER_WIDTH, BANNER_HEIGHT);
        image.setPosition(Gdx.graphics.getWidth() /2 - BANNER_WIDTH/2, Gdx.graphics.getHeight() * 2/3 - BANNER_HEIGHT/2);

        // score label
        scoreLabel = new Label(String.format("SCORE: %d", score), skin ,"default");
        scoreLabel.setPosition(Gdx.graphics.getWidth() /2 - scoreLabel.getWidth()/2, Gdx.graphics.getHeight()/2 + 20);
        scoreLabel.setAlignment(Align.center);

        // try again button
        tryAgain = new TextButton("TRY AGAIN", skin, "default");
        tryAgain.setWidth(BUTTON_WIDTH);
        tryAgain.setHeight(BUTTON_HEIGHT);
        tryAgain.setPosition(Gdx.graphics.getWidth() /2 - tryAgain.getWidth()/2, Gdx.graphics.getHeight()/2 - 70 );
        tryAgain.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                game.setScreen(new PlayScreen(game));
            }
        } );

        // main menu button
        mainMenu = new TextButton("Main Menu", skin, "default");
        mainMenu.setWidth(BUTTON_WIDTH);
        mainMenu.setHeight(BUTTON_HEIGHT);
        mainMenu.setPosition(Gdx.graphics.getWidth() /2 - mainMenu.getWidth()/2, Gdx.graphics.getHeight()/2 - 140 );
        mainMenu.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                game.setScreen(new TitleScreen(game));
            }
        } );

        // exit button
        exit = new TextButton("EXIT GAME", skin, "default");
        exit.setWidth(BUTTON_WIDTH);
        exit.setHeight(BUTTON_HEIGHT);
        exit.setPosition(Gdx.graphics.getWidth() /2 - exit.getWidth()/2, Gdx.graphics.getHeight()/2 - 240);
        exit.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                Gdx.app.exit();
            }
        } );

        // add actor
        stage.addActor(background);
        stage.addActor(image);
        stage.addActor(scoreLabel);
        stage.addActor(tryAgain);
        stage.addActor(mainMenu);
        stage.addActor(exit);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);


    }


    @Override
    public void show() {

    }

    /**
     * render: render current stage with all actors in it.
     * @param delta is the time passed since the last frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
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
