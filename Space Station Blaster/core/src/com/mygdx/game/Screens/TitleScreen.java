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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceStationBlaster;

/**
 * TitleScreen: is the first screen appearing when player turn on the game.
 * That will help player play game, read instruction or extra information or exit.
 */

public class TitleScreen implements Screen {

    // constants for title screen banner, button
    private static final int BANNER_WIDTH = 660;
    private static final int BANNER_HEIGHT = 400;
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 80;

    // declare all title screen entities
    private SpaceStationBlaster game;
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    private Texture gameBanner;
    private Image bannerImage;
    private Texture backgroundTexture;
    private Image background;

    private TextButton Play;
    private TextButton Instruction;
    private TextButton Credits;
    private TextButton Exit;

    private Viewport viewport;
    private OrthographicCamera camera;


    /**
     * TitleScreen constructor: set up all entities will appear on this screen including banner,
     * labels, buttons, images, stage.
     * @param game is the game in generally
     */
    public TitleScreen(final SpaceStationBlaster game){
        this.game = game;

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

        // game banner
        gameBanner = new Texture("screen/gameTitle.png");
        bannerImage = new Image(gameBanner);
        bannerImage.setSize(BANNER_WIDTH,BANNER_HEIGHT);
        bannerImage.setPosition(Gdx.graphics.getWidth() /2 - BANNER_WIDTH/2, Gdx.graphics.getHeight() - BANNER_HEIGHT * 4/5 );

        // play button
        Play = new TextButton("PLAY GAME", skin, "default");
        Play.setWidth(BUTTON_WIDTH);
        Play.setHeight(BUTTON_HEIGHT);
        Play.setPosition(Gdx.graphics.getWidth() /2 - Play.getWidth()/2, Gdx.graphics.getHeight()/2  );
        Play.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                game.setScreen(new PlayScreen(game));
            }
        } );

        // instruction button
        Instruction = new TextButton("INSTRUCTIONS", skin, "default");
        Instruction.setWidth(BUTTON_WIDTH);
        Instruction.setHeight(BUTTON_HEIGHT);
        Instruction.setPosition(Gdx.graphics.getWidth() /2 - Instruction.getWidth()/2, Gdx.graphics.getHeight()/2 - 80 );
        Instruction.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                game.setScreen(new InstructionScreen(game));
            }
        } );

        // credit button
        Credits = new TextButton("CREDITS", skin, "default");
        Credits.setWidth(BUTTON_WIDTH);
        Credits.setHeight(BUTTON_HEIGHT);
        Credits.setPosition(Gdx.graphics.getWidth() /2 - Credits.getWidth()/2, Gdx.graphics.getHeight()/2 - 160 );
        Credits.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                //TODO Sandy new CreditsScreen
                //game.setScreen(new ......);
            }
        } );

        // exit button
        Exit = new TextButton("EXIT GAME", skin, "default");
        Exit.setWidth(BUTTON_WIDTH);
        Exit.setHeight(BUTTON_HEIGHT);
        Exit.setPosition(Gdx.graphics.getWidth() /2 - Exit.getWidth()/2, Gdx.graphics.getHeight()/2 - 240);
        Exit.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                Gdx.app.exit();
            }
        } );

        // set stage and add actor
        stage = new Stage();
        stage.addActor(background);
        stage.addActor(bannerImage);
        stage.addActor(Play);
        stage.addActor(Instruction);
        stage.addActor(Credits);
        stage.addActor(Exit);
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
