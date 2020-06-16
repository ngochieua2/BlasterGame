package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceStationBlaster;

/**
 * CreditScreen: Creates a Screen for displaying the credits
 */
public class CreditsScreen implements Screen {

    private Viewport viewport;
    private OrthographicCamera camera;

    String credits;
    Label creditsLabel;

    private Skin skin;

    private Stage stage;

    private Button closeButton;

    boolean closeButtonClicked;

    SpaceStationBlaster game;

    /**
     * CreditsScreen constructor: sets up the screen to display the credits in a Label and a button
     * for closing the credits screen
     * @param game this is the main Game class
     */
    public CreditsScreen(SpaceStationBlaster game) {
        this.game = game;
        closeButtonClicked = false;

        camera = new OrthographicCamera();

        // keep the aspect ratio by scaling the world up to fit the screen, thus letterboxing
        // remaining space
        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT, camera);

        // center the camera
        // camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.setToOrtho(false, SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT);

        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));
        stage = new Stage(new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT));
        stage.getViewport();

        credits = Gdx.files.internal("credits.txt").readString();

        creditsLabel = new Label(credits, skin);
        creditsLabel.setWidth(SpaceStationBlaster.V_WIDTH);
        creditsLabel.setWidth(SpaceStationBlaster.V_WIDTH);
        creditsLabel.setPosition(SpaceStationBlaster.V_WIDTH - creditsLabel.getWidth() / 2,
                SpaceStationBlaster.V_HEIGHT - creditsLabel.getHeight() / 2);

        closeButton = new TextButton("EXIT", skin, "default");
        closeButton.setWidth(200f);
        closeButton.setHeight(60f);
        closeButton.setPosition((SpaceStationBlaster.V_WIDTH - closeButton.getWidth()) / 2,
                (SpaceStationBlaster.V_HEIGHT - closeButton.getHeight() / 2) - 420);

        stage.setViewport(viewport);
        stage.addActor(creditsLabel);
        stage.addActor(closeButton);

        resize(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    /**
     * HandleInput: for handing input for closeButton and playing button sound effect when clicked.
     */
    public void handleInput() {
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                closeButtonClicked = true;
                //buttonSound.play();
            }
        });
    }

    /**
     * update: checks to see if the close button has been clicked. returns to the TitleScreen if
     * closeButton has been clicked.
     * @param deltaTime is the difference between one frame rendered and the next frame rendered
     */
    public void update(float deltaTime) {
        handleInput();
        if (closeButtonClicked) {
            this.dispose();
        }
    }

    /**
     * render: updates the camera and renders the stage to the screen
     * @param delta is the difference between one frame rendered and the next frame rendered
     */
    @Override
    public void render(float delta) {
        camera.update();

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
