package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Player;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

/**
 * Hud: for displaying the current score, shield level and number of ships remaining
 */
public class Hud implements Disposable {

    public static final int GREEN_UFO_POINTS = 700;
    public static final int RED_UFO_POINTS = 1000;
    public static final int SPACE_STATION_POINTS = 2500;
    public static final int LARGE_ASTEROID_POINTS = 100;
    public static final int MEDIUM_ASTEROID_POINTS = 200;
    public static final int SMALL_ASTEROID_POINTS = 300;
    public static final int FIRST_EXTRA_SHIP = 20000;
    public static final int SECOND_EXTRA_SHIP = 50000;
    public static final int SCORE_REQUIRED_TO_SPAWN_SPACE_STATION = 10000;
    private static final int FONT_SIZE = 24;
    private static final int FONT_SIZE_STAGE = 48;
    private static final int DEF_SCORE = 0;
    private static final int DEF_SHIELD = 3;
    private static final int DEF_SHIPS = 2;
    private static final int MAX_SHIELD = 5;
    private static final int DEF_STAGE_NUMBER = 1;

    boolean firstExtraShipAwarded;
    boolean secondExtraShipAwarded;

    public Stage stage;
    private Viewport viewport;

    private TextureAtlas textureAtlas;
    private TextureAtlas uiTextureAtlas;

    public int score; // players current score
    public int currentStageScore; // players current stage score, used to spawn space station.
    public int shield; // players number of hit points left
    public float shootingCooldown; // this is the players current cooldown. Rate of fire.
    public int ships; // players number of lives left
    public int stageNumber; // current state number
    public int scoreRequiredToSpawnSpaceStation; // score required to spawn space station of the current stage

    private Label currentScoreLabel;
    private Label shieldLevelLabel;
    private Label shipsCountLabel;
    private Label stageNumberLabel;
    private Label stageCompleteLabel;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bitmapFont;
    private Label.LabelStyle labelStyle;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameterStage;
    private BitmapFont bitmapFontStage;
    private Label.LabelStyle labelStyleStage;

    // variables for the player shield
    private Pixmap pixmap;
    private TextureRegionDrawable drawable;
    private ProgressBar shieldProgressBar;
    private ProgressBar.ProgressBarStyle shieldProgressBaRStyle;

    private Table table;

    private PlayScreen playScreen;

    /**
     * Hud constructor: display a Hud at the top of the screen using displaying the score, Shield
     * level and number of ships in a Table. Display the score, and number of lives in a Label
     * using FreeTrueTypeFontGenerator and display the Shield level using Pixmap on a ProgressBar.
     * Also prepare message displaying Stage number in a Label to be displayed at the start of each
     * stage.
     * @param spriteBatch used to draw textures or sprites onto the screen for the current frame
     * @param playScreen is the screen that the player spaceship lives in as well as asteroids,
     *                   enemies and the walls
     */
    public Hud(SpriteBatch spriteBatch, PlayScreen playScreen) {
        this.playScreen = playScreen;
        this.textureAtlas = playScreen.getTextureAtlas();
        this.uiTextureAtlas = playScreen.getUITextureAtlas();
        shootingCooldown = playScreen.getPlayer().DEF_SHOOTING_COOLDOWN;

        score = DEF_SCORE;
        shield = DEF_SHIELD;
        ships = DEF_SHIPS;
        stageNumber = DEF_STAGE_NUMBER;
        firstExtraShipAwarded = false;
        secondExtraShipAwarded = false;

        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        // add true type font for the HUD
        fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/kenvector_future.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // set the font parameters for HUD
        fontParameter.size = FONT_SIZE;
        fontParameter.color = Color.WHITE;

        // generate the bitmap font for HUD
        bitmapFont = fontGenerator.generateFont(fontParameter);

        // set the label style for HUD
        labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;

        // set the true type font for current stage number
        fontParameterStage = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // set the font parameters for current stage number
        fontParameterStage.size = FONT_SIZE_STAGE;
        fontParameterStage.color = Color.WHITE;

        // generate the bitmap font for current stage number
        bitmapFontStage = fontGenerator.generateFont(fontParameterStage);

        // set the label style for current stage number
        labelStyleStage = new Label.LabelStyle();
        labelStyleStage.font = bitmapFontStage;

        // draw the pixmap for our shield progress bar
        pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        // create our progress bar
        shieldProgressBaRStyle = new ProgressBar.ProgressBarStyle();
        shieldProgressBaRStyle.background = drawable;

        // draw the pixmap knob for our shield progress bar
        pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CYAN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        shieldProgressBaRStyle.knob = drawable;

        // draw the pixmap knobBefore for our shield progress bar
        Pixmap pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CYAN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        shieldProgressBaRStyle.knobBefore = drawable;

        shieldProgressBar = new ProgressBar(0f, 1f, 0.2f, false,
                shieldProgressBaRStyle);

        shieldProgressBar.setValue(shield * 0.2f);
        shieldProgressBar.setBounds(10, 10, 100, 20);

        // set the label text
        currentScoreLabel = new Label(String.format("SCORE: %06d", score), labelStyle);
        shieldLevelLabel = new Label(String.format("SHIELD: "), labelStyle);
        shipsCountLabel = new Label(String.format("SHIPS: %d", ships), labelStyle);
        stageNumberLabel = new Label(String.format("STAGE: %d", stageNumber), labelStyleStage);

        table = new Table();

        table.setHeight(50);
        table.setWidth(SpaceStationBlaster.V_WIDTH);
        table.top();
        table.setBackground(new NinePatchDrawable(getNinePatch()));

        table.add(shieldLevelLabel).left().padLeft(10);
        table.add(shieldProgressBar).width(100).left();

        table.add(currentScoreLabel).expandX();
        table.add(shipsCountLabel).width(100).right().padRight(20);

        table.row();
        table.setOriginX(0);
        table.setOriginY(0);
        table.setX(0);
        table.setY(SpaceStationBlaster.V_HEIGHT - 60);

        stageNumberLabel.setPosition((SpaceStationBlaster.V_WIDTH - stageNumberLabel.getWidth() + 80) / 2,
                (SpaceStationBlaster.V_HEIGHT - stageNumberLabel.getHeight() - 60) / 2);

        stage.addActor(table);
        stage.addActor(stageNumberLabel);
    }

    /**
     * updateScore: increment the current score and the current stage score and update the label
     * with the new score.
     * @param scoreIncrease is the score to be added to the current score and current stage score
     */
    public void updateScore(int scoreIncrease) {
        score += scoreIncrease;
        currentStageScore += scoreIncrease;
        currentScoreLabel.setText(String.format("SCORE: %06d", score));
    }

    /**
     * nextStage: for updating and displaying the stage number. Increments the stage number and
     * updates the Label with the new stage number
     */
    public void nextStage() {
        this.stageNumber++;
        stageNumberLabel.setText(String.format("STAGE: %d", stageNumber));
    }

    /**
     * addShip: for giving the player an extra ship. increments ships amd updates the Label with
     * the new amount of ships
     */
    public void addShip() {
        ships++;
        shipsCountLabel.setText(String.format("SHIPS: %d", ships));
    }

    /**
     * extraShipAwarded: for checking and to see if the Player has not been rewarded the first ship or
     * second ship.
     * @return true if first extra ship has been awarded or second extra ship has been awarded
     * otherwise returns false
     */
    public boolean extraShipAwarded() {
        boolean shipAwarded = false;
        if (score >= FIRST_EXTRA_SHIP && !firstExtraShipAwarded) {
            firstExtraShipAwarded = true;
            shipAwarded = true;
        } else if (score >= SECOND_EXTRA_SHIP && !secondExtraShipAwarded) {
            secondExtraShipAwarded = true;
            shipAwarded = true;
        }
        return shipAwarded;
    }

    /**
     * increaseShield: for increasing the shield on the shild ProgressBar provided it is not
     * full.
     */
    public void increaseShield() {
        if (shield < MAX_SHIELD) {
            shield++;
            shieldProgressBar.setValue(shield * 0.2f);
        }
    }

    /**
     * decreaseShield: for decreasing the shied on the shield ProgressBar provided it is not empty.
     * Updates the shield ProgressBar with the new shield level. If the shield is empty sets the
     * player state to destroyed
     */
    public void decreaseShield() {
        if (shield > 0) {
            shield--;
            shieldProgressBar.setValue(shield * 0.2f);
            // check if player has no shield
            if (shield == 0) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                playScreen.getPlayer().playerState = Player.PlayerState.DESTROYED;
            }
        }
    }

    /**
     * resetShield: resets the shield to its initial default level and updates the shield ProgressBar
     * with the shield level.
     */
    public void resetShield() {
        shield = DEF_SHIELD;
        shieldProgressBar.setValue(shield * 0.2f);
    }

    /**
     * removeShip: for removing a ship when the Player is destroyed. decrements ships and updates
     * Label to display the new number of ships
     */
    public void removeShip() {
        ships--;
        shipsCountLabel.setText(String.format("SHIPS: %d", ships));
    }

    /**
     * setScoreRequiredToSpawnSpaceStation: sets the currentStageScore to zero and resets the
     * score required to spawn spaceStation based on the stage number.
     */
    public void setScoreRequiredToSpawnSpaceStation() {
        currentStageScore = 0;
        scoreRequiredToSpawnSpaceStation = SCORE_REQUIRED_TO_SPAWN_SPACE_STATION * stageNumber;
    }

    /**
     * clearStageNumberDisplay: clears the stage number Label after for 4 seconds.
     */
    public void clearStageNumberDisplay() {
        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                stageNumberLabel.setText("");
            }
        }, 4);
    }

    /**
     * clearStageCompleteDisplay: clears the stage complete display after 4 seconds.
     */
    public void clearStageCompleteDisplay() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                stageCompleteLabel.setText("");
            }
        }, 3);
    }

    /**
     * displayStageComplete: for preparing the stage complete Label to displaying the STAGE COMPLETE
     * message on the screen when the Player destroys the space station.
     */
    public void displayStageComplete() {
        if (stageCompleteLabel == null) {
            stageCompleteLabel = new Label("STAGE COMPLETE", labelStyleStage);
            stageCompleteLabel.setPosition((SpaceStationBlaster.V_WIDTH - stageNumberLabel.getWidth() - 100) / 2,
                    (SpaceStationBlaster.V_HEIGHT - stageNumberLabel.getHeight() - 60) / 2);
            stage.addActor(stageCompleteLabel);
        } else {
            stageCompleteLabel.setText("STAGE COMPLETE");
        }
    }

    /**
     * getNinePatch: for creating the glass panel display for the Hud. Creates a NinePatch from the
     * TexureAtlas regoin glassPanel.
     * @return a new NinePatch
     */
    private NinePatch getNinePatch() {
        // get the image
        final TextureAtlas.AtlasRegion region = uiTextureAtlas.findRegion("glassPanel");

        return new NinePatch(new TextureRegion(region, 0, 0, region.getRegionWidth(), region.getRegionHeight()), 10, 10, 10, 10);
    }

    /**
     * dispose: disposes of assets to prevent memory leaks
     */
    @Override
    public void dispose() {
        bitmapFont.dispose();
        stage.dispose();
        textureAtlas.dispose();
        uiTextureAtlas.dispose();
    }
}