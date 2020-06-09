package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceStationBlaster;

public class Controller {
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 80;

    private Viewport viewport;
    public Stage stage;
    private boolean upPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean shootPressed;

    public Image upImage;
    public Image upPressedImage;

    OrthographicCamera camera;

    public Controller(SpriteBatch spriteBatch) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT, camera);
        stage = new Stage(viewport, spriteBatch);

        // allow us to get input from the stage
        Gdx.input.setInputProcessor(stage);

        // put our textures for our controls in a table
        Table table = new Table();
        table.left().bottom();

        Image upImage = new Image(new Texture("Controls/transparentDark24.png"));
        Image downImage = new Image(new Texture("Controls/transparentDark25.png"));
        Image leftImage = new Image(new Texture("Controls/transparentDark22.png"));
        Image rightImage = new Image(new Texture("Controls/transparentDark23.png"));
        Image shootImage = new Image(new Texture("Controls/transparentDark47.png"));

        upImage.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        downImage.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        leftImage.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        rightImage.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        shootImage.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        upImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        downImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        leftImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        rightImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        shootImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shootPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shootPressed = false;
            }
        });

        shootImage.setPosition(SpaceStationBlaster.V_WIDTH - 100, 100);

        table.add();
        table.add(upImage).size(upImage.getWidth(), upImage.getHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(leftImage).size(leftImage.getWidth(), leftImage.getHeight());
        table.add();
        table.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImage).size(downImage.getWidth(), downImage.getHeight());
        table.add();

        stage.addActor(table);
        stage.addActor(shootImage);
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isShootPressed() {
        return shootPressed;
    }
}
