package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;

public class PlayScreen implements Screen {
    private SpaceStationBlaster game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud gameHud;

    // Box2d variables
    private World world;
    // graphical representation of bodies and fixtures in our game world
    private Box2DDebugRenderer box2dDebugRenderer;

    public PlayScreen(SpaceStationBlaster game) {
        this.game = game;
        // gameCamera is used to follow player spaceship through game world
        gameCamera = new OrthographicCamera();
        // maintain virtual aspect ratio despite screen size
        gameViewport = new FitViewport(SpaceStationBlaster.V_WIDTH,
                SpaceStationBlaster.V_HEIGHT, gameCamera);
        // create HUD for score, number of ships and shield level
        gameHud = new Hud(game.spriteBatch);

        // create Box3d world with no gravity and set to sleep objects at rest
        world = new World(new Vector2(0, 0), true);
        box2dDebugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.setProjectionMatrix(gameHud.stage.getCamera().combined);
        gameHud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
