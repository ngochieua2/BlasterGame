package com.mygdx.game.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * InstructionScreen: is the screen to explain how to play this game
 * including mission, win condition, how to control player ship, get score
 * and use supported items. It can be access from TitleScreen.
 */
public class InstructionScreen implements Screen {

    // constants for instruction banner, button and entities images
    private static final int BANNER_WIDTH = 500;
    private static final int BANNER_HEIGHT = 200;
    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 140;
    private static final float TEXT_BUTTON_WIDTH = 1.75f;
    private static final float TEXT_BUTTON_HEIGHT = 1.75f;
    private static final int BUTTON_SIZE = 50;
    private static final int SCORE_SIZE = 68;
    private static final int POWERUP_SIZE = 15;

    // declare all instruction screen entities
    private SpaceStationBlaster game;
    private Skin skin;
    private Skin textSkin;
    private Stage stage;

    private Texture gameBanner;
    private Image bannerImage;
    private Texture backgroundTexture;
    private Image background;

    // button
    private TextButton Back;

    // label
    private Label instructionTextlabel;
    private Label desktopControlTitleLabel;
    private Label desktopUpLabel;
    private Label desktopLeftLabel;
    private Label desktopRightLabel;
    private Label desktopShotLabel;
    private Label mobileControlTitleLabel;
    private Label mobileUpLabel;
    private Label mobileLeftLabel;
    private Label mobileRightLabel;
    private Label mobileShotLabel;

    private Label scoreLabel;
    private Label largeAsteroidScore;
    private Label mediumAsteroidScore;
    private Label smallAsteroidScore;
    private Label ufoGreenScore;
    private Label ufoRedScore;
    private Label spaceStationScore;
    private Label powerUpsLabel;
    private Label boltLabel;
    private Label shieldLabel;

    //desktop textures and images
    private Texture desktopKeyUp;
    private Image dkuImage;

    private Texture desktopKeyLeft;
    private Image dklImage;

    private Texture desktopKeyRight;
    private Image dkrImage;

    private Texture desktopKeyShot;
    private Image dksImage;

    //mobile textures and images
    private Texture mobileKeyUp;
    private Image mkuImage;

    private Texture mobileKeyLeft;
    private Image mklImage;

    private Texture mobileKeyRight;
    private Image mkrImage;

    private Texture mobileKeyShot;
    private Image mksImage;

    //score texture
    private Texture largeBrownAstTexture;
    private Texture largeGreyAstTexture;
    private Texture mediumBrownAstTexture;
    private Texture mediumGreyAstTexture;
    private Texture smallBrownAstTexture;
    private Texture smallGreyAstTexture;
    private Texture greenUFOTexture;
    private Texture redUFOTexture;
    private Texture spaceStationTexture1;
    private Texture spaceStationTexture2;
    private Texture spaceStationTexture3;

    // score images
    private Image largeBrownImage;
    private Image largeGreyImage;
    private Image mediumBrownImage;
    private Image mediumGreyImage;
    private Image smallBrownImage;
    private Image smallGreyImage;
    private Image greenUFOImage;
    private Image redUFOImage;
    private Image spaceStation1Image;
    private Image spaceStation2Image;
    private Image spaceStation3Image;

    // power up textures and images
    private Texture boltTexture;
    private Image boltImage;
    private Texture shieldTexture;
    private Image shieldImage;

    private Viewport viewport;
    private OrthographicCamera camera;

    /**
     * InstructionScreen constructor: set up all entities will appear on this screen including banner,
     * labels, buttons, images, stage.
     * @param game is the game in generally
     */
    public InstructionScreen(final SpaceStationBlaster game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT, camera);
        camera.setToOrtho(false, SpaceStationBlaster.V_WIDTH / 2, SpaceStationBlaster.V_HEIGHT / 2);

        skin = new Skin(Gdx.files.internal("gui/star-soldier-ui.json"));
        stage = new Stage(new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT));
        textSkin = new Skin(Gdx.files.internal("gui/glassy-ui.json"));

        stage.getViewport();
        stage.setViewport(viewport);

        // background
        backgroundTexture = new Texture("screen/star_background.png");
        background = new Image(backgroundTexture);
        background.setSize(SpaceStationBlaster.V_WIDTH,SpaceStationBlaster.V_HEIGHT);
        background.setPosition(0,0);
        stage.addActor(background);

        // game banner
        gameBanner = new Texture("screen/instruction.png");
        bannerImage = new Image(gameBanner);
        bannerImage.setSize(BANNER_WIDTH, BANNER_HEIGHT);
        bannerImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - BANNER_WIDTH/2, SpaceStationBlaster.V_HEIGHT - BANNER_HEIGHT + 30);

        // label instruction text
        instructionTextlabel = new Label("You are required manoeuvre and destroy asteroids and incoming UFOs to rack up \n"+
                "as many point as you can. When you have reached the required number of points, \n" +
                "a space station will enter in a random location. Once you have destroyed the space \n"+
                "station, you will move on to a more challenging stage. During each stage, you can \n" +
                "pick up random Shield and Weapon upgrades to improve you space cruiser", skin, "default");

        instructionTextlabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - instructionTextlabel.getWidth()/2, SpaceStationBlaster.V_HEIGHT - BANNER_HEIGHT - 30);
        instructionTextlabel.setAlignment(Align.center);

        // Show control instruction on desktop or mobile
        switch(Gdx.app.getType()) {
            case iOS:
            case Android:
                // control title
                mobileControlTitleLabel = new Label("Control your Space cruiser on mobile:", skin, "default");
                mobileControlTitleLabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - mobileControlTitleLabel.getWidth()/2,SpaceStationBlaster.V_HEIGHT/2  + 120);
                mobileControlTitleLabel.setAlignment(Align.center);
                mobileControlTitleLabel.setColor(Color.SALMON);

                // mobile key
                mobileUpLabel = new Label(": Thrust", skin,"default");
                mobileUpLabel.setPosition(SpaceStationBlaster.V_WIDTH/2 - mobileUpLabel.getWidth()/2 - 450 ,SpaceStationBlaster.V_HEIGHT/2  + 70);

                mobileLeftLabel = new Label(": Rotate Left", skin,"default");
                mobileLeftLabel.setPosition(SpaceStationBlaster.V_WIDTH/2 - mobileLeftLabel.getWidth()/2 - 160 ,SpaceStationBlaster.V_HEIGHT/2  + 70);

                mobileRightLabel = new Label(": Rotate Right", skin,"default");
                mobileRightLabel.setPosition(SpaceStationBlaster.V_WIDTH/2 - mobileRightLabel.getWidth()/2 + 190 ,SpaceStationBlaster.V_HEIGHT/2 + 70);

                mobileShotLabel = new Label(": Fire", skin,"default");
                mobileShotLabel.setPosition(SpaceStationBlaster.V_WIDTH/2  - mobileShotLabel.getWidth()/2  + 450 ,SpaceStationBlaster.V_HEIGHT/2  + 70);

                // mobile key images
                mobileKeyUp = new Texture("Controls/transparentDark24.png");
                mkuImage = new Image(mobileKeyUp);
                mkuImage.setSize(BUTTON_SIZE, BUTTON_SIZE);
                mkuImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - mobileUpLabel.getWidth()/2 - 460 - mkuImage.getWidth() ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                mobileKeyLeft = new Texture("Controls/transparentDark22.png");
                mklImage = new Image(mobileKeyLeft);
                mklImage.setSize(BUTTON_SIZE,BUTTON_SIZE);
                mklImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - mobileLeftLabel.getWidth()/2 - 185 - mklImage.getWidth() ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                mobileKeyRight = new Texture("Controls/transparentDark23.png");
                mkrImage = new Image(mobileKeyRight);
                mkrImage.setSize(BUTTON_SIZE,BUTTON_SIZE);
                mkrImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - mobileRightLabel.getWidth()/2 + 120 ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                mobileKeyShot = new Texture("Controls/transparentDark47.png");
                mksImage = new Image(mobileKeyShot);
                mksImage.setSize(BUTTON_SIZE ,BUTTON_SIZE);
                mksImage.setPosition(SpaceStationBlaster.V_WIDTH/2  - mobileShotLabel.getWidth()/2  + 440 - mksImage.getWidth() ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                // add key labels and images to stage
                stage.addActor(mobileControlTitleLabel);
                stage.addActor(mobileUpLabel);
                stage.addActor(mkuImage);
                stage.addActor(mobileLeftLabel);
                stage.addActor(mklImage);
                stage.addActor(mobileRightLabel);
                stage.addActor(mkrImage);
                stage.addActor(mobileShotLabel);
                stage.addActor(mksImage);
                break;

            case Desktop:
                // control title
                desktopControlTitleLabel = new Label("Control your Space cruiser on desktop:", skin, "default");
                desktopControlTitleLabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - desktopControlTitleLabel.getWidth()/2,SpaceStationBlaster.V_HEIGHT/2  + 120);
                desktopControlTitleLabel.setAlignment(Align.center);
                desktopControlTitleLabel.setColor(Color.SALMON);

                // desktop key
                desktopUpLabel = new Label(": Thrust", skin,"default");
                desktopUpLabel.setPosition(SpaceStationBlaster.V_WIDTH/2 - desktopUpLabel.getWidth()/2 - 450 ,SpaceStationBlaster.V_HEIGHT/2  + 70);

                desktopLeftLabel = new Label(": Rotate Left", skin,"default");
                desktopLeftLabel.setPosition(SpaceStationBlaster.V_WIDTH/2 - desktopLeftLabel.getWidth()/2 - 175 ,SpaceStationBlaster.V_HEIGHT/2  + 70);

                desktopRightLabel = new Label(": Rotate Right", skin,"default");
                desktopRightLabel.setPosition(SpaceStationBlaster.V_WIDTH/2 - desktopRightLabel.getWidth()/2 + 175 ,SpaceStationBlaster.V_HEIGHT/2  + 70);

                desktopShotLabel = new Label(": Fire", skin,"default");
                desktopShotLabel.setPosition(SpaceStationBlaster.V_WIDTH/2  - desktopShotLabel.getWidth()/2  + 550 ,SpaceStationBlaster.V_HEIGHT/2  + 70);

                // Desktop key images
                desktopKeyUp = new Texture("Controls/upkey.png");
                dkuImage = new Image(desktopKeyUp);
                dkuImage.setSize(BUTTON_SIZE, BUTTON_SIZE);
                dkuImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - desktopUpLabel.getWidth()/2 - 460 - dkuImage.getWidth() ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                desktopKeyLeft = new Texture("Controls/LeftKey.png");
                dklImage = new Image(desktopKeyLeft);
                dklImage.setSize(BUTTON_SIZE,BUTTON_SIZE);
                dklImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - desktopLeftLabel.getWidth()/2 - 185 - dklImage.getWidth() ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                desktopKeyRight = new Texture("Controls/rightkey.png");
                dkrImage = new Image(desktopKeyRight);
                dkrImage.setSize(BUTTON_SIZE,BUTTON_SIZE);
                dkrImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - desktopRightLabel.getWidth()/2 + 165 - dkrImage.getWidth() ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                desktopKeyShot = new Texture("Controls/spacebar.png");
                dksImage = new Image(desktopKeyShot);
                dksImage.setSize(BUTTON_SIZE *4,BUTTON_SIZE);
                dksImage.setPosition(SpaceStationBlaster.V_WIDTH/2  - desktopShotLabel.getWidth()/2  + 540 - dksImage.getWidth() ,SpaceStationBlaster.V_HEIGHT/2  + 60);

                // add key labels and images to stage
                stage.addActor(desktopControlTitleLabel);
                stage.addActor(desktopUpLabel);
                stage.addActor(dkuImage);
                stage.addActor(desktopLeftLabel);
                stage.addActor(dklImage);
                stage.addActor(desktopRightLabel);
                stage.addActor(dkrImage);
                stage.addActor(desktopShotLabel);
                stage.addActor(dksImage);
                break;
        }

        // score information
        scoreLabel = new Label("Scoring:", skin, "default");
        scoreLabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - scoreLabel.getWidth()/2,SpaceStationBlaster.V_HEIGHT/2 + 20 );
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setColor(Color.SALMON);

        largeAsteroidScore = new Label(": x100", skin,"default");
        largeAsteroidScore.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400, SpaceStationBlaster.V_HEIGHT/2 - 20 );

        mediumAsteroidScore = new Label(": x200", skin,"default");
        mediumAsteroidScore.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400, SpaceStationBlaster.V_HEIGHT/ 2 - 80 );

        smallAsteroidScore = new Label(": x300", skin,"default");
        smallAsteroidScore.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400, SpaceStationBlaster.V_HEIGHT/ 2 - 140);

        ufoGreenScore = new Label(": x700", skin, "default");
        ufoGreenScore.setPosition(SpaceStationBlaster.V_WIDTH/2 - ufoGreenScore.getWidth()/2 , SpaceStationBlaster.V_HEIGHT/2 - 40);

        ufoRedScore = new Label(": x1000", skin, "default");
        ufoRedScore.setPosition(SpaceStationBlaster.V_WIDTH/2 - ufoGreenScore.getWidth()/2, SpaceStationBlaster.V_HEIGHT/2 - 100);

        spaceStationScore = new Label(": x2500", skin, "default");
        spaceStationScore.setPosition(SpaceStationBlaster.V_WIDTH/2 - spaceStationScore.getWidth()/2 + 400 , SpaceStationBlaster.V_HEIGHT/2 - 80);

        // add image for score
        largeBrownAstTexture = new Texture("screen/largeBrown.png");
        largeBrownImage = new Image(largeBrownAstTexture);
        largeBrownImage.setSize(SCORE_SIZE,SCORE_SIZE);
        largeBrownImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400 - largeBrownImage.getWidth() * 2 , SpaceStationBlaster.V_HEIGHT/2 - 40 );

        largeGreyAstTexture = new Texture("screen/largeGrey.png");
        largeGreyImage = new Image(largeGreyAstTexture);
        largeGreyImage.setSize(SCORE_SIZE,SCORE_SIZE);
        largeGreyImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400 - largeGreyImage.getWidth() , SpaceStationBlaster.V_HEIGHT/2 - 40);

        mediumBrownAstTexture = new Texture("screen/mediumBrown.png");
        mediumBrownImage = new Image(mediumBrownAstTexture);
        mediumBrownImage.setSize(SCORE_SIZE/2,SCORE_SIZE/2);
        mediumBrownImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400 - mediumBrownImage.getWidth() * 2, SpaceStationBlaster.V_HEIGHT/ 2 - 90);

        mediumGreyAstTexture = new Texture("screen/mediumGrey.png");
        mediumGreyImage = new Image(mediumGreyAstTexture);
        mediumGreyImage.setSize(SCORE_SIZE/2,SCORE_SIZE/2);
        mediumGreyImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400 - mediumGreyImage.getWidth(), SpaceStationBlaster.V_HEIGHT/ 2 - 90);

        smallBrownAstTexture = new Texture("screen/smallBrown.png");
        smallBrownImage = new Image(smallBrownAstTexture);
        smallBrownImage.setSize(SCORE_SIZE/3,SCORE_SIZE/3);
        smallBrownImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400 - smallBrownImage.getWidth() * 2, SpaceStationBlaster.V_HEIGHT/ 2 - 140);

        smallGreyAstTexture = new Texture("screen/smallGrey.png");
        smallGreyImage = new Image(smallGreyAstTexture);
        smallGreyImage.setSize(SCORE_SIZE/3,SCORE_SIZE/3);
        smallGreyImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - largeAsteroidScore.getWidth()/2 - 400 - smallGreyImage.getWidth() , SpaceStationBlaster.V_HEIGHT/ 2 - 140);

        greenUFOTexture = new Texture("screen/greenUFO.png");
        greenUFOImage = new Image(greenUFOTexture);
        greenUFOImage.setSize(SCORE_SIZE,SCORE_SIZE);
        greenUFOImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - ufoGreenScore.getWidth()/2 - greenUFOImage.getWidth() - 5 , SpaceStationBlaster.V_HEIGHT/2 - 60);

        redUFOTexture = new Texture("screen/redUFO.png");
        redUFOImage = new Image(redUFOTexture);
        redUFOImage.setSize(SCORE_SIZE,SCORE_SIZE);
        redUFOImage.setPosition(SpaceStationBlaster.V_WIDTH/2 - ufoGreenScore.getWidth()/2 - redUFOImage.getWidth() - 5, SpaceStationBlaster.V_HEIGHT/2 - 125);

        spaceStationTexture1 = new Texture("screen/SS1.png");
        spaceStation1Image = new Image(spaceStationTexture1);
        spaceStation1Image.setSize(SCORE_SIZE,SCORE_SIZE*2);
        spaceStation1Image.setPosition(SpaceStationBlaster.V_WIDTH/2 - spaceStationScore.getWidth()/2 + 400 - spaceStation1Image.getWidth() * 3 , SpaceStationBlaster.V_HEIGHT/2 - 130);

        spaceStationTexture2 = new Texture("screen/SS2.png");
        spaceStation2Image = new Image(spaceStationTexture2);
        spaceStation2Image.setSize(SCORE_SIZE,SCORE_SIZE*2);
        spaceStation2Image.setPosition(SpaceStationBlaster.V_WIDTH/2 - spaceStationScore.getWidth()/2 + 400 - spaceStation1Image.getWidth() * 2 , SpaceStationBlaster.V_HEIGHT/2 - 130);

        spaceStationTexture3 = new Texture("screen/SS3.png");
        spaceStation3Image = new Image(spaceStationTexture3);
        spaceStation3Image.setSize(SCORE_SIZE,SCORE_SIZE*2);
        spaceStation3Image.setPosition(SpaceStationBlaster.V_WIDTH/2 - spaceStationScore.getWidth()/2 + 400 - spaceStation1Image.getWidth() , SpaceStationBlaster.V_HEIGHT/2 - 130);

        // powerup label
        powerUpsLabel = new Label("Power up: ", skin, "default");
        powerUpsLabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - powerUpsLabel.getWidth()/2,SpaceStationBlaster.V_HEIGHT/2 - 160);
        powerUpsLabel.setAlignment(Align.center);
        powerUpsLabel.setColor(Color.SALMON);

        shieldLabel = new Label(": randomly appears when Green UFOs are destroyed. 20% increase in shield",skin, "default");
        shieldLabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - shieldLabel.getWidth()/2 , SpaceStationBlaster.V_HEIGHT/2 - 200);
        shieldLabel.setAlignment(Align.center);

        boltLabel = new Label(": randomly appears when Red UFOs are destroyed. It increases fire rate",skin, "default");
        boltLabel.setPosition(SpaceStationBlaster.V_WIDTH /2 - boltLabel.getWidth()/2, SpaceStationBlaster.V_HEIGHT/2 - 240);
        boltLabel.setAlignment(Align.center);

        // add images for power up
        shieldTexture = new Texture("screen/shield.png");
        shieldImage = new Image(shieldTexture);
        shieldImage.setSize(POWERUP_SIZE * 2.5f, POWERUP_SIZE * 3);
        shieldImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - shieldLabel.getWidth()/2 - shieldImage.getWidth() - 10 , SpaceStationBlaster.V_HEIGHT/2 - 210);

        boltTexture = new Texture("screen/bolt.png");
        boltImage = new Image(boltTexture);
        boltImage.setSize(POWERUP_SIZE * 2, POWERUP_SIZE * 3);
        boltImage.setPosition(SpaceStationBlaster.V_WIDTH /2 - boltLabel.getWidth()/2 - boltImage.getWidth() - 10 , SpaceStationBlaster.V_HEIGHT/2 - 255);

        // Back button
        Back = new TextButton("Back", skin, "default");
        Back.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        Back.getLabel().setFontScale(TEXT_BUTTON_WIDTH, TEXT_BUTTON_HEIGHT);
        Back.setPosition(SpaceStationBlaster.V_WIDTH /2 - Back.getWidth()/2, 0);
        Back.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.BUTTON_PRESS_SOUND, Sound.class).play();
                game.setScreen(new TitleScreen(game));
            }
        } );

        // set stage and add actor

        stage.addActor(bannerImage);
        stage.addActor(instructionTextlabel);
        stage.addActor(scoreLabel);
        stage.addActor(largeAsteroidScore);
        stage.addActor(mediumAsteroidScore);
        stage.addActor(smallAsteroidScore);
        stage.addActor(ufoGreenScore);
        stage.addActor(ufoRedScore);
        stage.addActor(spaceStationScore );
        stage.addActor(largeBrownImage);
        stage.addActor(largeGreyImage);
        stage.addActor(mediumBrownImage);
        stage.addActor(mediumGreyImage);
        stage.addActor(smallBrownImage);
        stage.addActor(smallGreyImage);
        stage.addActor(greenUFOImage);
        stage.addActor(redUFOImage);
        stage.addActor(spaceStation1Image);
        stage.addActor(spaceStation2Image);
        stage.addActor(spaceStation3Image);
        stage.addActor(powerUpsLabel);
        stage.addActor(shieldLabel);
        stage.addActor(shieldImage);
        stage.addActor(boltLabel);
        stage.addActor(boltImage);
        stage.addActor(Back);
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
