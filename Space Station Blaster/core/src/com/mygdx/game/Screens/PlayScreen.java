package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameAssetManager;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Sprites.PlayerSpaceship;
import com.mygdx.game.Tools.MapLoader;
import com.mygdx.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private GameAssetManager gameAssetManager;
    public TextureAtlas textureAtlas;
    public MapRenderer mapRenderer;
    private SpaceStationBlaster game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud gameHud;

    // Box2d variables
    private World world;
    // graphical representation of bodies and fixtures in our game world
    private Box2DDebugRenderer box2dDebugRenderer;

    private MapLoader mapLoader;

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

        // create Box3d world with no gravity and set to sleep objects at rest
        world = new World(new Vector2(0, 0), true);

        // load tiled map and set up tiled map renderer
        mapLoader = new MapLoader(world, gameAssetManager);
        mapRenderer = new OrthogonalTiledMapRenderer(mapLoader.getTiledMap(),1 / SpaceStationBlaster.PPM);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        // create HUD for score, number of ships and shield level
        gameHud = new Hud(game.spriteBatch);

        box2dDebugRenderer = new Box2DDebugRenderer();

        player = new PlayerSpaceship(world, this);

        world.setContactListener(new WorldContactListener());
    }

    public MapLoader getMapLoader() {
        return mapLoader;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void show() {

    }

    public void handleInput(float deltaTime) {
        Vector2 baseVector = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.body.setAngularVelocity(-2f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.body.setAngularVelocity(2f);
        } else if (player.body.getAngularVelocity() != 0) {
            player.body.setAngularVelocity(0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            //TODO fire laser
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            baseVector.set(0, 2f);
        }

        if (!baseVector.isZero()) {
            player.body.applyForceToCenter(player.body.getWorldVector(baseVector), true);
        }
    }

    public void update(float deltaTime) {
        // handle user input first
        handleInput(deltaTime);

        // set how many time to calculate per second
        world.step(1 / 60f, 6, 2);

        player.update(deltaTime);

        player.setRotation(player.body.getAngle() * MathUtils.radiansToDegrees);

        // attach game camera x and y position to players x and y position
        gameCamera.position.x = player.body.getPosition().x;
        gameCamera.position.y = player.body.getPosition().y;

        // update our Gamecamera with the correct coordinates
        gameCamera.update();
        // tell our renderer to draw only what it can see in our game world
        mapRenderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        // clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // gameCamera.update();
        mapRenderer.render();

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
        mapLoader.dispose();
    }
}
