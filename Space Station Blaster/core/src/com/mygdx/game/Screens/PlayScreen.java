package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Bullets;
import com.mygdx.game.Effects;
import com.mygdx.game.Player;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Asteroids;
import com.mygdx.game.Enemies;
import com.mygdx.game.Walls;

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
    private Bullets bullets;
    private Enemies enemies;
    private Asteroids asteroids;

    private Effects effects;

    private float elapsedTime;

    private ShapeRenderer shapeRenderer;

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

        walls = new Walls(this);
        effects = new Effects(this);
        bullets = new Bullets(this);
        player = new Player(this);
        enemies = new Enemies(this);
        asteroids = new Asteroids(this);
        gameHud.clearStageNumberDisplay(); // clear the stage number after 4 seconds;
    }

    public void reloadStage() {
        player = new Player(this);
        bullets = new Bullets(this);
        enemies = new Enemies(this);
        asteroids = new Asteroids(this);
        gameHud.clearStageNumberDisplay();
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public TextureAtlas getUITextureAtlas() {
        return uiTextureAtlas;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Bullets getBullets() {
        return bullets;
    }

    public Effects getEffects() {
        return effects;
    }

    @Override
    public void show() {

    }

    private void handleInput(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
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

    @Override
    public void render(float delta) {
        update(delta);
        elapsedTime += delta;
        // clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // gameCamera.update();
        mapRenderer.render();

        // set camera to draw what our main game camera can see
        game.spriteBatch.setProjectionMatrix(gameCamera.combined);
        // draw our sprites onto the screen
        game.spriteBatch.begin();

        player.render(game.spriteBatch);
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
            player.impactCurrentFrame = (TextureRegion) player.impactAnimation.getKeyFrame(player.impactElapsedTime, false);;
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
                } else {
                    Gdx.app.exit();
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

        // set camera to draw what the HUD camera can see
        game.spriteBatch.setProjectionMatrix(gameHud.stage.getCamera().combined);
        gameHud.stage.draw();


        // testing the player bounds
        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.polygon(player.playerBounds.getTransformedVertices());
        shapeRenderer.end();

        //testing enemy bounds
        for (int i=0; i<5; i++) {
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(enemies.circleColliders[i].x, enemies.circleColliders[i].y, enemies.circleColliders[i].radius);
            shapeRenderer.end();
        }

        // testing the wall bounds
        for(int index = 0; index < walls.colliders.size(); index++) {
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(walls.colliders.get(index).getX(), walls.colliders.get(index).getY(),
                    walls.colliders.get(index).getWidth(), walls.colliders.get(index).getHeight());
            shapeRenderer.end();
        }

        // testing the bullet bounds
        if (bullets.refCollider != null) {
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.polygon(bullets.refCollider.getTransformedVertices());
            shapeRenderer.end();
        }

        //testing the Asteroids bounds
//        for (int i=0; i < Asteroids.Asteroids_Max; i++){
//            if (asteroids.type[i] != Asteroids.TYPE.NONE ){
//
//                shapeRenderer.setProjectionMatrix(gameCamera.combined);
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//                shapeRenderer.setColor(Color.YELLOW);
//                shapeRenderer.polygon(asteroids.Astcollider[i].getTransformedVertices());
//                shapeRenderer.end();
//            }
//
//        }
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
        gameHud.dispose();
    }

    public Player getPlayer() {
        return player;
    }

    public Walls getWalls() {
        return walls;
    }

    public Hud getGameHud() {
        return gameHud;
    }

    public Enemies getEnemies() {
        return enemies;
    }

    public OrthographicCamera getCamera() {
        return gameCamera;
    }

    public  Asteroids getAsteroids(){ return asteroids;}
}
