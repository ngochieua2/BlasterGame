package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

public class InstructionScreen implements Screen {

    private static final int BANNER_WIDTH = 180;
    private static final int BANNER_HEIGHT = 100;

    private SpaceStationBlaster game;
    private SpriteBatch batch;
    private Skin skin;
    private Skin textSkin;
    private Skin titleSkin;
    private Stage stage;

    private Texture gameBanner;
    private Image image;

    //desktop images
    private Texture desktopKeyUp;
    private Image dkuImage;

    private Texture desktopKeyLeft;
    private Image dklImage;

    private Texture desktopKeyRight;
    private Image dkrImage;

    private Texture desktopKeyShot;
    private Image dksImage;

    //mobile images
    private Texture mobileKeyUp;
    private Image mkuImage;

    private Texture mobileKeyLeft;
    private Image mklImage;

    private Texture mobileKeyRight;
    private Image mkrImage;

    private Texture mobileKeyShot;
    private Image mksImage;


    private TextButton Back;

    private Label instructionTextlabel;
    private Label controlTitleLabel;
    private Label desktopLabel;
    private Label desktopUpLabel;
    private Label desktopLeftLabel;
    private Label desktopRightLabel;
    private Label desktopShotLabel;
    private Label mobileLabel;
    private Label mobileUpLabel;
    private Label mobileLeftLabel;
    private Label mobileRightLabel;
    private Label mobileShotLabel;



    private Table table;
    private Table keyTable;

    public InstructionScreen(final SpaceStationBlaster game) {
        this.game = game;

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));
        textSkin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        titleSkin = new Skin(Gdx.files.internal("gui/glassy-ui.json"));


        // game banner
        gameBanner = new Texture("screen/instruction.png");
        image = new Image(gameBanner);

        // Back button
        Back = new TextButton("Back", skin, "default");
        Back.setWidth(160f);
        Back.setHeight(80f);
        Back.setPosition(Gdx.graphics.getWidth() /2 - Back.getWidth()/2, 0);
        Back.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        } );

        // label instruction text
        instructionTextlabel = new Label("  You are required manoeuvre and destroy asteroids and incoming UFO to rack up as\n"+
                "  many point as you can. When you have reached the required number of points a \n" +
                "  space station will enter in a random location. Once you have destroyed the space \n"+
                "  station you move on to a more challenging stage. During each stage you can pick up \n" +
                "  random. Shield and Weapon pickup to improve you space cruiser", textSkin, "default");

        instructionTextlabel.setPosition(10, Gdx.graphics.getHeight() - BANNER_HEIGHT  - 70);

        // control title
        controlTitleLabel = new Label("Control your Space cruiser:", skin, "default");
        controlTitleLabel.setPosition(Gdx.graphics.getWidth() /2 - controlTitleLabel.getWidth()/2,Gdx.graphics.getHeight() - BANNER_HEIGHT - 90);
        controlTitleLabel.setAlignment(Align.center);
        controlTitleLabel.setColor(Color.SALMON);

        // desktop
        desktopLabel = new Label("On desktop", textSkin, "default");
        desktopLabel.setPosition(Gdx.graphics.getWidth() * 1/4 -desktopLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 110);

        // desktop key
        desktopUpLabel = new Label("Thrust", textSkin,"default");
        desktopUpLabel.setPosition(Gdx.graphics.getWidth() * 1/8 -desktopUpLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 130);

        desktopLeftLabel = new Label("Rotate Left", textSkin,"default");
        desktopLeftLabel.setPosition(Gdx.graphics.getWidth() * 1/8 - desktopLeftLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 130);

        desktopRightLabel = new Label("Rotate Right", textSkin,"default");
        desktopRightLabel.setPosition(Gdx.graphics.getWidth() * 3/8 - desktopRightLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 130);

        desktopShotLabel = new Label("Fire", textSkin,"default");
        desktopShotLabel.setPosition(Gdx.graphics.getWidth() * 2/8 - desktopShotLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 150);

        // Destop key images


        // mobile
        mobileLabel = new Label("On mobile", textSkin, "default");
        mobileLabel.setPosition(Gdx.graphics.getWidth() * 3/4 - mobileLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 110);

        // mobile key
        mobileUpLabel = new Label("Thrust", textSkin,"default");
        mobileUpLabel.setPosition(Gdx.graphics.getWidth() * 5/8 -mobileUpLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 130);

        mobileLeftLabel = new Label("Rotate Left", textSkin,"default");
        mobileLeftLabel.setPosition(Gdx.graphics.getWidth() * 5/8 - mobileLeftLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 130);

        mobileRightLabel = new Label("Rotate Right", textSkin,"default");
        mobileRightLabel.setPosition(Gdx.graphics.getWidth() * 7/8 - mobileRightLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 130);

        mobileShotLabel = new Label("Fire", textSkin,"default");
        mobileShotLabel.setPosition(Gdx.graphics.getWidth() * 6/8 - mobileShotLabel.getWidth()/2 ,Gdx.graphics.getHeight() - BANNER_HEIGHT - 150);


        // set up table for game banner
        table = new Table();
        table.setHeight(BANNER_HEIGHT);
        table.setWidth(BANNER_WIDTH);
        table.setPosition(Gdx.graphics.getWidth() /2 - BANNER_WIDTH/2, Gdx.graphics.getHeight() - BANNER_HEIGHT + 10);
        table.add(image);

        // set stage and add actor
        stage = new Stage();
        stage.addActor(table);
        stage.addActor(instructionTextlabel);
        stage.addActor(controlTitleLabel);
        stage.addActor(desktopLabel);
        //stage.addActor(desktopUpLabel);
        stage.addActor(desktopLeftLabel);
        stage.addActor(desktopRightLabel);
        //stage.addActor(desktopShotLabel);
        stage.addActor(mobileLabel);
        //stage.addActor(mobileUpLabel);
        stage.addActor(mobileLeftLabel);
        stage.addActor(mobileRightLabel);
        //stage.addActor(mobileShotLabel);
        stage.addActor(Back);
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

    }
}
