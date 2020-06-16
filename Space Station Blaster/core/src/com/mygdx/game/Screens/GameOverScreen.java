package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
    private static final int BANNER_WIDTH = 1000;
    private static final int BANNER_HEIGHT = 500;
    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 140;
    private static final float TEXT_BUTTON_WIDTH = 1.75f;
    private static final float TEXT_BUTTON_HEIGHT = 1.75f;
    // declare all game over screen entities
    private SpaceStationBlaster game;
    private Skin skin;
    private Skin textSkin;
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
        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT, camera);
        camera.setToOrtho(false, SpaceStationBlaster.V_WIDTH / 2, SpaceStationBlaster.V_HEIGHT / 2);

        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));
        textSkin = new Skin(Gdx.files.internal("gui/glassy-ui.json"));
        stage = new Stage(new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT));
        stage.getViewport();



        // background
        backgroundTexture = new Texture("screen/star_background.png");
        background = new Image(backgroundTexture);
        background.setSize(SpaceStationBlaster.V_WIDTH,SpaceStationBlaster.V_HEIGHT);
        background.setPosition(0,0);

        // gameover banner
        gameoverBanner = new Texture("screen/gameover.png");
        image = new Image(gameoverBanner);
        image.setSize(BANNER_WIDTH, BANNER_HEIGHT);
        image.setPosition(SpaceStationBlaster.V_WIDTH /2 - BANNER_WIDTH/2, SpaceStationBlaster.V_HEIGHT - BANNER_HEIGHT * 3/4);

        // score label
        scoreLabel = new Label(String.format("SCORE: %d", score), textSkin, "big" );
        scoreLabel.setSize(50,50);
        scoreLabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - scoreLabel.getWidth()/2, SpaceStationBlaster.V_HEIGHT/2 + 140);
        scoreLabel.setAlignment(Align.center);

        // try again button
        tryAgain = new TextButton("TRY AGAIN", skin, "default");
        tryAgain.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        tryAgain.getLabel().setFontScale(TEXT_BUTTON_WIDTH, TEXT_BUTTON_HEIGHT);
        tryAgain.setPosition(SpaceStationBlaster.V_WIDTH /2 - tryAgain.getWidth()/2, SpaceStationBlaster.V_HEIGHT/2 - tryAgain.getHeight()/2);
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
        mainMenu.setPosition(SpaceStationBlaster.V_WIDTH /2 - mainMenu.getWidth()/2, SpaceStationBlaster.V_HEIGHT/2 - 140 );
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
        exit.setPosition(SpaceStationBlaster.V_WIDTH /2 - exit.getWidth()/2, SpaceStationBlaster.V_HEIGHT/2 - 240);
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
        //stage.addActor(tryAgain);
        //stage.addActor(mainMenu);
        //stage.addActor(exit);
        resize(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT);
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
        game.spriteBatch.setProjectionMatrix(camera.combined);

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
