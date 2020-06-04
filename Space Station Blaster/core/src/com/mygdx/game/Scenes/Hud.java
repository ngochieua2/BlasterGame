package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

public class Hud implements Disposable {

    private static final int FONT_SIZE = 24;
    private static final int FONT_SIZE_STAGE = 48;
    private static final int DEF_SCORE = 0;
    private static final int DEF_SHIELD = 3;
    private static final int DEF_SHIPS = 2;
    private static final int MAX_SHIELD = 5;
    private static final int MAX_SHIPS = 4;
    private static final int DEF_STAGE_NUMBER = 1;

    public Stage stage;
    private Viewport viewport;

    private TextureAtlas textureAtlas;
    private TextureAtlas uiTextureAtlas;

    private Image numeralXImage;
    private Image[] playerLifeImages;
    private Image[] shieldImages;

    public int score; // players current score
    public int shield; // players number of hit points left
    public int ships; // players number of lives left
    public int stageNumber; // current state number

    private Label currentScoreLabel;
    private Label shieldLevelLabel;
    private Label shipsCountLabel;
    private Label stageNumberLabel;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bitmapFont;
    private Label.LabelStyle labelStyle;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameterStage;
    private BitmapFont bitmapFontStage;
    private Label.LabelStyle labelStyleStage;

    public Hud(SpriteBatch spriteBatch, PlayScreen playScreen) {
        this.textureAtlas = playScreen.getTextureAtlas();
        this.uiTextureAtlas = playScreen.getUITextureAtlas();

        score = DEF_SCORE;
        shield = DEF_SHIELD;
        ships = DEF_SHIPS;
        stageNumber = DEF_STAGE_NUMBER;

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

        numeralXImage = new Image(textureAtlas.findRegion("numeralX"));

        shieldImages = new Image[MAX_SHIELD];
        for (int index = 0; index < shieldImages.length; index++) {
            if (index < shield) {
                shieldImages[index] = new Image(uiTextureAtlas.findRegion("squareBlue"));
            } else {
                shieldImages[index] = new Image(uiTextureAtlas.findRegion("squareWhite"));
            }
        }

        playerLifeImages = new Image[MAX_SHIPS];
        for (int index = 0; index < playerLifeImages.length; index++)
            if (index < ships) {
                playerLifeImages[index] = new Image(textureAtlas.findRegion("playerLife1_blue"));
            }

        // set the label text
        currentScoreLabel = new Label(String.format("SCORE: %06d", score), labelStyle);
        shieldLevelLabel = new Label(String.format("SHIELD: "), labelStyle);
        shipsCountLabel = new Label(String.format("SHIPS: ", ships), labelStyle);
        stageNumberLabel = new Label(String.format("STAGE: %d", stageNumber), labelStyleStage);

        Table table = new Table();

        table.setHeight(50);
        table.setWidth(SpaceStationBlaster.V_WIDTH);
        table.top();
        table.setBackground(new NinePatchDrawable(getNinePatch()));

        table.add(shieldLevelLabel).left().padLeft(10);
        for (int index = 0; index < MAX_SHIELD; index++) {
            table.add(shieldImages[index]).width(20).left();
        }

        table.add(currentScoreLabel).expandX();
        table.add(shipsCountLabel).width(100).right();
        for (int index = 0; index < ships; index++) {
            if (index == ships - 1) {
                table.add(playerLifeImages[index]).width(30).right().padRight(10);
            } else {
                table.add(playerLifeImages[index]).width(30).right();
            }
        }
        table.row();
        table.setOriginX(0);
        table.setOriginY(0);
        table.setX(0);
        table.setY(SpaceStationBlaster.V_HEIGHT - 60);

        stageNumberLabel.setPosition(560,300f);

        stage.addActor(table);
        stage.addActor(stageNumberLabel);
    }

    public void updateScore(int scoreIncrease) {
        score += scoreIncrease;
        currentScoreLabel.setText(String.format("SCORE: %06d", score));
    }

    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
        stageNumberLabel.setText(String.format("STAGE: %d", stageNumber));
    }

    public void clearStageNumberDisplay() {
        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                stageNumberLabel.setText("");
            }
        }, 4);
    }

    private NinePatch getNinePatch() {
        // get the image
        final TextureAtlas.AtlasRegion region = uiTextureAtlas.findRegion("glassPanel");

        return new NinePatch(new TextureRegion(region, 0, 0, region.getRegionWidth(), region.getRegionHeight()), 10, 10, 10, 10);
    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
        stage.dispose();
        textureAtlas.dispose();
        uiTextureAtlas.dispose();
    }
}