package com.mygdx.game.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Bullets;
import com.mygdx.game.Effects;
import com.mygdx.game.Player;
import com.mygdx.game.Powerups;
import com.mygdx.game.Scenes.Controller;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Asteroids;
import com.mygdx.game.Enemies;
import com.mygdx.game.Walls;

/**
 * PlayScreen: the main screen of our game world where the Player, Asteroids and Enemies live in
 */
public class PlayScreen implements Screen {

    private TextureAtlas textureAtlas;
    private TextureAtlas uiTextureAtlas;
    private TiledMap tiledMap;
    private MapRenderer mapRenderer;
    private SpaceStationBlaster game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud gameHud;
    private Walls walls;
    // game sprites
    private Player player;
    private Powerups powerups;
    private Bullets bullets;
    private Enemies enemies;
    private Asteroids asteroids;
    public Controller controller;

    private Effects effects;

    private float elapsedTime;
    public float stageCompleteElapsedTime;

    private ShapeRenderer shapeRenderer;

    /**
     * PlayerScreen constructor: setups up the textureAtlas and tiled map paths. Then sets up
     * camera, game view port and map render. Posisions the view port to the camera. Then creates
     * instances of the Hud, Controller, Walls, Effects, Powerups, Bullets, Player, Enemies and
     * Asteriods.
     * @param game this is the main Game class
     */
    public PlayScreen(SpaceStationBlaster game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();

        textureAtlas = new TextureAtlas(Gdx.files.internal(SpaceStationBlaster.TEXTURE_ATLAS_PATH));
        uiTextureAtlas = new TextureAtlas(Gdx.files.internal(SpaceStationBlaster.UI_TEXTURE_ATLAS_PATH));

        tiledMap = new TmxMapLoader().load(SpaceStationBlaster.TILE_MAP_PATH);

        // gameCamera is used to follow player spaceship through game world
        gameCamera = new OrthographicCamera();
        // maintain virtual aspect ratio despite screen size
        gameViewport = new FitViewport(SpaceStationBlaster.V_WIDTH,
                SpaceStationBlaster.V_HEIGHT, gameCamera);

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        // create HUD for score, number of ships and shield level
        gameHud = new Hud(game.spriteBatch, this);

        // create the onscreen Controller for mobile input
        controller = new Controller(game.spriteBatch);

        walls = new Walls(this);
        effects = new Effects(this);
        powerups = new Powerups(this);
        bullets = new Bullets(this);
        player = new Player(this);
        enemies = new Enemies(this);
        asteroids = new Asteroids(this);
        gameHud.setScoreRequiredToSpawnSpaceStation();
        gameHud.clearStageNumberDisplay(); // clear the stage number after 4 seconds;

        stageCompleteElapsedTime = 0;
    }

    /**
     * reloadStage: reloads the stage of the game by re initialising Powerups, Bullets, Player
     * Enemies, Asteroids. Then clears score required to spawn stace station and clears stage
     * number display
     */
    public void reloadStage() {
        powerups = new Powerups(this);
        bullets = new Bullets(this);
        player = new Player(this);
        enemies = new Enemies(this);
        asteroids = new Asteroids(this);
        gameHud.setScoreRequiredToSpawnSpaceStation();
        gameHud.clearStageNumberDisplay();
    }

    /**
     * getTextureAtlas: gets the textureAtlas
     * @return textureAtlas
     */
    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    /**
     * getUITextureAtlas: gets the UITextureAtlas
     * @return UITextureAtlas
     */
    public TextureAtlas getUITextureAtlas() {
        return uiTextureAtlas;
    }

    /**
     * getTiledMap: gets the tiledMap
     * @return tiledMap
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * getBullets: gets the bullets
     * @return bullets
     */
    public Bullets getBullets() {
        return bullets;
    }

    /**
     * getEffects: gets the effects
     * @return effects
     */
    public Effects getEffects() {
        return effects;
    }

    /**
     * getPowerUps: gets the powerups
     * @return powerups
     */
    public Powerups getPowerups() {
        return powerups;
    }

    @Override
    public void show() {

    }

    /**
     * handleInput: exits the game of the Player presses escape key
     * @param deltaTime is the time passed since the last frame of animation
     */
    private void handleInput(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    /**
     * update: checks to see if the score required to spawn space station and is space station is
     * not spawned. If it has spawns a space station. updates handleInput, powerups, player, bullets
     * enemies and asteroids. Sets the camera position to be same as the Player position. Update
     * the game camera and set map render to draw only what it can see in our game world
     * @param deltaTime is the time passed since the last frame of animation
     */
    public void update(float deltaTime) {
        if (gameHud.currentStageScore >= getGameHud().scoreRequiredToSpawnSpaceStation && !enemies.spaceStationSpawned()) {
            enemies.spawnSpaceStation();
        }
        handleInput(deltaTime);
        powerups.update(deltaTime);
        player.update(deltaTime);
        bullets.update(deltaTime);
        enemies.update(deltaTime);
        asteroids.update(deltaTime);

        // attach game camera x and y position to players x and y position
        gameCamera.position.x = player.getSprite().getX();
        gameCamera.position.y = player.getSprite().getY();

        // update our Gamecamera with the correct coordinates
        gameCamera.update();
        // tell our renderer to draw only what it can see in our game world
        mapRenderer.setView(gameCamera);
    }

    /**
     * render: clears the screen and renders the mapRenderer, player, powerups, bullets, enemies and
     * asteroids. render Bullet fire, Bullet hit, Player destroyed explosion and trail current
     * animation frame. When Player destroyed explosion is on its last frame reset stage, remove
     * ship, reset shield and reset player shooting cooldown speed. Draw Hud and Controller overlay
     * @param delta is the time passed since the last frame of animation
     */
    @Override
    public void render(float delta) {
        update(delta);
        elapsedTime += delta;
        // clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        // set camera to draw what our main game camera can see
        game.spriteBatch.setProjectionMatrix(gameCamera.combined);
        // draw our sprites onto the screen
        game.spriteBatch.begin();

        player.render(game.spriteBatch);
        powerups.render(game.spriteBatch, delta);
        bullets.render(game.spriteBatch, delta);
        enemies.render(game.spriteBatch, delta);
        asteroids.render(game.spriteBatch, delta);
        game.spriteBatch.end();

        if (player.bulletFired && !player.fireAnimation.isAnimationFinished(player.fireElapsedTime)) {
            game.spriteBatch.begin();
            player.fireCurrentFrame = (TextureRegion) player.fireAnimation.getKeyFrame(player.fireElapsedTime, false);
            game.spriteBatch.draw(player.fireCurrentFrame, player.firePosition.x, player.firePosition.y,
                    player.fireCurrentFrame.getRegionWidth() / 2,
                    player.fireCurrentFrame.getRegionHeight() / 2,
                    player.fireCurrentFrame.getRegionWidth(),
                    player.fireCurrentFrame.getRegionHeight(), 1, 1,
                    (float) (player.fireRadians + Math.PI / 2) * MathUtils.radiansToDegrees);
            game.spriteBatch.end();
            player.fireElapsedTime += delta;
        } else {
            player.bulletFired = false;
            player.fireElapsedTime = 0;
        }

        if (player.bulletHit && !player.impactAnimation.isAnimationFinished(player.impactElapsedTime)) {
            game.spriteBatch.begin();
            player.impactCurrentFrame = (TextureRegion) player.impactAnimation.getKeyFrame(player.impactElapsedTime, false);
            ;
            game.spriteBatch.draw(player.impactCurrentFrame, player.impactPosition.x, player.impactPosition.y,
                    player.impactCurrentFrame.getRegionWidth() / 2,
                    player.impactCurrentFrame.getRegionHeight() / 2,
                    player.impactCurrentFrame.getRegionWidth(),
                    player.impactCurrentFrame.getRegionHeight(), 1, 1,
                    (float) (player.impactRadians + Math.PI / 2) * MathUtils.radiansToDegrees);
            game.spriteBatch.end();
            player.impactElapsedTime += delta;
        } else {
            player.bulletHit = false;
            player.impactElapsedTime = 0;
        }

        if (player.playerState == Player.PlayerState.DESTROYED && !player.explosionAnimation.isAnimationFinished(player.explosionElapsedTime)) {
            if (player.explosionElapsedTime == 0) {
                player.playerBounds.setPosition(0, 0);
            }
            game.spriteBatch.begin();
            player.explosionCurrentFrame = (TextureRegion) player.explosionAnimation.getKeyFrame(player.explosionElapsedTime, false);
            player.playerSprite.setRegion(player.explosionCurrentFrame);
            game.spriteBatch.draw(player.explosionCurrentFrame, player.position.x - player.explosionCurrentFrame.getRegionWidth() / 2 +
                            player.playerSprite.getWidth() / 2,
                    player.position.y - player.explosionCurrentFrame.getRegionHeight() / 2 +
                            player.playerSprite.getHeight() / 2,
                    player.explosionCurrentFrame.getRegionWidth() / 2,
                    player.explosionCurrentFrame.getRegionHeight() / 2,
                    player.explosionCurrentFrame.getRegionWidth(),
                    player.explosionCurrentFrame.getRegionHeight(), 1, 1,
                    (float) (player.explosionRadians + Math.PI / 2) * MathUtils.radiansToDegrees);
            game.spriteBatch.end();
            player.explosionElapsedTime += delta;
            if (player.explosionAnimation.isAnimationFinished(player.explosionElapsedTime)) {
                reloadStage();
                if (gameHud.ships > 0) {
                    gameHud.removeShip();
                    gameHud.resetShield();
                    gameHud.shootingCooldown = player.shootingCooldownSpeed;
                } else {
                    game.setScreen(new GameOverScreen(game, gameHud.score));
                }
            }
        } else {
            player.explosionElapsedTime = 0;
        }

        if (player.playerState == Player.PlayerState.NORMAL) {
            game.spriteBatch.begin();
            player.trailCurrentFrame = (TextureRegion) player.trailAnimation.getKeyFrame(player.elapsedTime, true);
            game.spriteBatch.draw(player.trailCurrentFrame, player.trailPosition.x, player.trailPosition.y,
                    player.trailCurrentFrame.getRegionWidth() / 2,
                    player.trailCurrentFrame.getRegionHeight() / 2,
                    player.trailCurrentFrame.getRegionWidth(),
                    player.trailCurrentFrame.getRegionHeight(), 1, 1,
                    (float) (player.trailRadians + 3 * Math.PI / 2) * MathUtils.radiansToDegrees);
            game.spriteBatch.end();
            player.elapsedTime += delta;
        }

        if (player.playerState == Player.PlayerState.STAGE_COMPLETE) {
            getGameHud().displayStageComplete();
            getGameHud().clearStageCompleteDisplay();
            stageCompleteElapsedTime += delta;
            if (stageCompleteElapsedTime > 3) {
                getGameHud().nextStage();
                reloadStage();
                player.shootingCooldownSpeed = getGameHud().shootingCooldown;
                getGameHud().clearStageNumberDisplay();
                stageCompleteElapsedTime = 0;
            }
        }

        // set camera to draw what the HUD camera can see
        game.spriteBatch.setProjectionMatrix(gameHud.stage.getCamera().combined);
        gameHud.stage.draw();

        // set camera to draw what the Controller camera can see
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            game.spriteBatch.setProjectionMatrix((controller.stage.getCamera().combined));
            controller.stage.draw();
        }
    }

    /**
     * resize: updates the game view port width and height to allow for updates when the window
     * is resized.
     * @param width of the screen
     * @param height of the screen
     */
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

    /**
     * dispose: dispose of assets to prevent memory leaks
     */
    @Override
    public void dispose() {
        gameHud.dispose();
        effects.dispose();
    }

    /**
     * getPlayer: gets a player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * getsWalls: gets walls
     * @return walls
     */
    public Walls getWalls() {
        return walls;
    }

    /**
     * getGameHud: gets the gameHud
     * @return gameHud
     */
    public Hud getGameHud() {
        return gameHud;
    }

    /**
     * getEnemies: gets the enemies
     * @return enemies
     */
    public Enemies getEnemies() {
        return enemies;
    }

    /**
     * getCamera: gets the gameCamera
     * @return gameCamera
     */
    public OrthographicCamera getCamera() {
        return gameCamera;
    }

    /**
     * getAsteroids: gets the asteroids
     * @return asteroids
     */
    public Asteroids getAsteroids() {
        return asteroids;
    }
}
