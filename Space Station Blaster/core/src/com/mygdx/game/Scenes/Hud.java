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
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameAssetManager;
import com.mygdx.game.SpaceStationBlaster;

public class Hud implements Disposable {

    private static final int FONT_SIZE = 24;
    private static final int DEF_SCORE = 0;
    private static final int DEF_SHIELD = 3;
    private static final int DEF_SHIPS = 3;
    private static final int MAX_SHIELD = 5;
    private static final int MAX_SHIPS = 4;

    public Stage stage;
    private Viewport viewport;

    private GameAssetManager gameAssetManager;
    private TextureAtlas mainTextureAtlas;
    private TextureAtlas uiTextureAtlas;

    private Image numeralXImage;
    private Image[] playerLifeImages;
    private Image[] shieldImages;
    private Image squareWhiteImage;

    private Integer score;
    private Integer shield;
    private Integer ships;

    private Label currentScoreLabel;
    private Label shieldLevelLabel;
    private Label shipsCountLabel;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bitmapFont;
    private Label.LabelStyle labelStyle;

    public Hud(SpriteBatch spriteBatch) {
        score = DEF_SCORE;
        shield = DEF_SHIELD;
        ships = DEF_SHIPS;

        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        gameAssetManager = new GameAssetManager();
        gameAssetManager.loadImages();
        gameAssetManager.assetManager.finishLoading();

        // add true type font
        fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/kenvector_future.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // set the font parameters
        fontParameter.size = FONT_SIZE;
        fontParameter.color = Color.WHITE;

        // generate the bitmap font
        bitmapFont = fontGenerator.generateFont(fontParameter);

        // set the label style
        labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;

        mainTextureAtlas = gameAssetManager.assetManager.get(gameAssetManager.spriteSheetPack);
        uiTextureAtlas = gameAssetManager.assetManager.get(
                gameAssetManager.uiSpaceExpansionSpriteSheetPack);

        numeralXImage = new Image(mainTextureAtlas.findRegion("numeralX"));


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
                playerLifeImages[index] = new Image(mainTextureAtlas.findRegion("playerLife1_blue"));
            }

        // set the label text
        currentScoreLabel = new Label(String.format("SCORE: %06d", score), labelStyle);
        shieldLevelLabel = new Label(String.format("SHIELD: "), labelStyle);
        shipsCountLabel = new Label(String.format("SHIPS: ", ships), labelStyle);

        Table table = new Table();

        table.setHeight(50);
        table.setWidth(SpaceStationBlaster.V_WIDTH);
        table.top();
        // table.setFillParent(true);
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

        stage.addActor(table);
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

    }
}