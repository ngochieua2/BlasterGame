package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameAssetManager;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Sprites.PlayerSpaceship;

public class PlayScreen implements Screen {
    private GameAssetManager gameAssetManager;
    public TextureAtlas textureAtlas;
    private SpaceStationBlaster game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud gameHud;

    // Box2d variables
    private World world;
    // graphical representation of bodies and fixtures in our game world
    private Box2DDebugRenderer box2dDebugRenderer;


    // game sprites
    private PlayerSpaceship player;

    public PlayScreen(SpaceStationBlaster game) {
        this.game = game;

        gameAssetManager = new GameAssetManager();
        gameAssetManager.loadImages();
        gameAssetManager.assetManager.finishLoading();

        textureAtlas = gameAssetManager.assetManager.get(gameAssetManager.spriteSheetPack);

        // gameCamera is used to follow player spaceship through game world
        gameCamera = new OrthographicCamera();
        // maintain virtual aspect ratio despite screen size
        gameViewport = new FitViewport(SpaceStationBlaster.V_WIDTH / SpaceStationBlaster.PPM,
                SpaceStationBlaster.V_HEIGHT / SpaceStationBlaster.PPM, gameCamera);
        // create HUD for score, number of ships and shield level
        gameHud = new Hud(game.spriteBatch);

        // create Box3d world with no gravity and set to sleep objects at rest
        world = new World(new Vector2(0, 0), true);
        box2dDebugRenderer = new Box2DDebugRenderer();

        player = new PlayerSpaceship(world, this);
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyForce(new Vector2(1f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x <= -2) {
            player.body.applyForce(new Vector2(-1f, 0), player.body.getWorldCenter(), true);
        }

    }

    public void update(float deltaTime) {
        // handle user input first
        handleInput(deltaTime);

        // set how many time to calculate per second
        world.step(1 / 60f, 5, 2);

        player.update(deltaTime);

        // attach game camera x position to players x position
        gameCamera.position.x = player.body.getPosition().x;

        // update our Gamecamera with the correct coordinates
        gameCamera.update();

        //Todo: tell our renderer to draw only what it can see in our game world

    }

    @Override
    public void render(float delta) {
        update(delta);

        // clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render Box2DDebugLines
        box2dDebugRenderer.render(world, gameCamera.combined);

        // set camera to draw what our main game camera can see
        game.spriteBatch.setProjectionMatrix(gameCamera.combined);

        // draw our sprites onto the screen
        game.spriteBatch.begin();
        player.draw(game.spriteBatch);
        game.spriteBatch.end();

        // set camera to draw what the HUD camera can see
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
        world.dispose();
        box2dDebugRenderer.dispose();
        gameHud.dispose();
    }
}
