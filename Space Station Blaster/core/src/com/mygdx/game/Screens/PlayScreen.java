package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Bullets;
import com.mygdx.game.Player;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Tools.Enemies;
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
        bullets = new Bullets(this);
        player = new Player(this);
        enemies = new Enemies(this);
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
        bullets.render(game.spriteBatch);
        enemies.render(game.spriteBatch);
        game.spriteBatch.end();

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

    public OrthographicCamera getCamera() {
        return gameCamera;
    }

}
