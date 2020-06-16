package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceStationBlaster;

/**
 * CreditScreen: Creates a Screen for displaying the credits
 */
public class CreditsScreen implements Screen {

    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 140;
    private static final float TEXT_BUTTON_WIDTH = 1.75f;
    private static final float TEXT_BUTTON_HEIGHT = 1.75f;

    private Viewport viewport;
    private OrthographicCamera camera;

    private String credits;
    private Label creditsLabel;
    private Texture backgroundTexture;
    private Image background;

    private Skin skin;

    private Stage stage;

    private TextButton closeButton;

    boolean closeButtonClicked;

    SpaceStationBlaster game;

    /**
     * CreditsScreen constructor: sets up the screen to display the credits in a Label and a button
     * for closing the credits screen.
     * @param game this is the main Game class
     */
    public CreditsScreen(final SpaceStationBlaster game) {
        this.game = game;
        closeButtonClicked = false;

        camera = new OrthographicCamera();

        // keep the aspect ratio by scaling the world up to fit the screen, thus letterboxing
        // remaining space
        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT, camera);

        // center the camera
        // camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.setToOrtho(false, SpaceStationBlaster.V_WIDTH / 2, SpaceStationBlaster.V_HEIGHT / 2);

        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));
        stage = new Stage(new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT));
        stage.getViewport();

        credits = Gdx.files.internal("credits.txt").readString();

        // background
        backgroundTexture = new Texture("screen/star_background.png");
        background = new Image(backgroundTexture);
        background.setSize(SpaceStationBlaster.V_WIDTH,SpaceStationBlaster.V_HEIGHT);
        background.setPosition(0,0);


        creditsLabel = new Label(credits, skin);
        creditsLabel.setFontScale(1.15f);
        creditsLabel.setWidth(SpaceStationBlaster.V_WIDTH);
        creditsLabel.setWidth(SpaceStationBlaster.V_WIDTH);
        creditsLabel.setPosition(SpaceStationBlaster.V_WIDTH / 2 - creditsLabel.getWidth() / 2,
                SpaceStationBlaster.V_HEIGHT / 2 - creditsLabel.getHeight() / 2 - 20);
        creditsLabel.setAlignment(Align.center);

        closeButton = new TextButton("CLOSE", skin, "default");
        closeButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        closeButton.getLabel().setFontScale(TEXT_BUTTON_WIDTH, TEXT_BUTTON_HEIGHT);
        closeButton.setPosition(SpaceStationBlaster.V_WIDTH / 2 - closeButton.getWidth() / 2,
                SpaceStationBlaster.V_HEIGHT / 2 - closeButton.getHeight() / 2 - 340);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                game.setScreen(new TitleScreen(game));
            }
        });

        stage.setViewport(viewport);
        stage.addActor(background);
        stage.addActor(creditsLabel);
        stage.addActor(closeButton);

        resize(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    /**
     * render: updates the camera and renders the stage to the screen
     * @param delta is the difference between one frame rendered and the next frame rendered
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.spriteBatch.setProjectionMatrix(camera.combined);

        stage.draw();
    }

    /**
     * resize: calls the resize method of the current state
     * @param width is the width of the screen
     * @param height is the height of the screen
     */
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

    /**
     * dispose: for disposing of any images or media when we are not using them to prevent
     * memory leaks.
     */
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
