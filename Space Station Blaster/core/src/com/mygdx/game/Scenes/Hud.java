package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceStationBlaster;

public class Hud {

    private static final int FONT_SIZE = 24;

    public Stage stage;
    private Viewport viewport;

    private Integer score;
    private Integer shield;
    private Integer ships;

//    private Label scoreLabel;
//    private Label shieldLabel;
//    private Label shipsLabel;

    private Label currentScoreLabel;
    private Label shieldLevelLabel;
    private Label shipsCountLabel;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont bitmapFont;
    private Label.LabelStyle labelStyle;

    public Hud(SpriteBatch spriteBatch) {
        score = 0;
        shield = 3;
        ships = 3;

        viewport = new FitViewport(SpaceStationBlaster.V_WIDTH, SpaceStationBlaster.V_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/kenvector_future.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // set the font parameters
        fontParameter.size = FONT_SIZE;
        fontParameter.color = Color.WHITE;
        bitmapFont = fontGenerator.generateFont(fontParameter);

        // set the label style
        labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;

        // set the label text
        currentScoreLabel = new Label(String.format("SCORE:%06d", score), labelStyle);
        shieldLevelLabel = new Label(String.format("SHIELD:%01d", shield), labelStyle);
        shipsCountLabel = new Label(String.format("SHIPS:%01d", ships), labelStyle);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(currentScoreLabel).expandX().padTop(10);
        table.add(shieldLevelLabel).expandX().padTop(10);
        table.add(shipsCountLabel).expandX().padTop(10);
        table.row();
        stage.addActor(table);
    }
}