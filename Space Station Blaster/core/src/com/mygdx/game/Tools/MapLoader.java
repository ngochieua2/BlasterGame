package com.mygdx.game.Tools;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.GameAssetManager;
import com.mygdx.game.SpaceStationBlaster;

/**
 * MapLoader: used to load the tile map for the game world. Load Bounds objects and playerShip
 * object that is used in the game world. Create bodies and fixtures for objects in the game world
 */
public class MapLoader implements Disposable {
    private static final String TILE_MAP_NAME = "game-map.tmx";
    private static final String MAP_BOUNDS = "Bounds";
    private static final String MAP_PLAYER = "PlayerShip";

    private World world;
    private TiledMap tiledMap;

    short categoryBits;
    short maskBits;

    /**
     * tileMapLoader: creates an array of all the Bounds objects in the tile map and then creates
     * static bodies and fixtures of all the objects
     * @param world
     */
    public MapLoader(World world, GameAssetManager gameAssetManager) {
        this.world = world;

        categoryBits = SpaceStationBlaster.WALL_BIT;
        maskBits = SpaceStationBlaster.PLAYER_BIT | SpaceStationBlaster.ASTEROID_BIT |
                SpaceStationBlaster.UFO_BIT | SpaceStationBlaster.ENEMY_SHIP_BIT |
                SpaceStationBlaster.PLAYER_SHOT_BIT | SpaceStationBlaster.ENEMY_SHOT_BIT |
                SpaceStationBlaster.SPACE_STATION_BIT | SpaceStationBlaster.MISSILE_BIT;

        tiledMap = gameAssetManager.assetManager.get(gameAssetManager.tiledMap,  TiledMap.class);

        // get all the bounds objects in the tile map
        Array<RectangleMapObject> bounds = tiledMap.getLayers().get(MAP_BOUNDS).getObjects().getByType(RectangleMapObject.class);

        // create static bodies and fixtures for all the objects in the tile map
        for (RectangleMapObject rectangleMapObject : bounds) {
            Rectangle boundRectangle = rectangleMapObject.getRectangle();
            ShapeFactory.createRectangle(
                    new Vector2(boundRectangle.getX() + boundRectangle.getWidth() / 2, boundRectangle.getY() + boundRectangle.getHeight() / 2),
                    new Vector2(boundRectangle.getWidth() / 2, boundRectangle.getHeight() / 2),
                    BodyDef.BodyType.StaticBody, world, 1f, false, categoryBits, maskBits);
        }
    }

    /**
     * getPlayer: creates a playerShip bounds object in the tile map and then creates dynamic
     * body and fixture of the playerShip
     * @return created playerShip Body
     */
    public Body getPlayer() {
        categoryBits = SpaceStationBlaster.PLAYER_BIT;
        maskBits = SpaceStationBlaster.ASTEROID_BIT |
                SpaceStationBlaster.UFO_BIT | SpaceStationBlaster.ENEMY_SHIP_BIT | SpaceStationBlaster.ENEMY_SHOT_BIT |
                SpaceStationBlaster.SPACE_STATION_BIT | SpaceStationBlaster.MISSILE_BIT | SpaceStationBlaster.WALL_BIT;

        // get the playerShip bounds object in the tile map
        Rectangle playerRectangle = tiledMap.getLayers().get(MAP_PLAYER).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();

        // create dynamic body and fixture for the playerShip object in the tile map and then returns it
        return ShapeFactory.createRectangle(
                new Vector2(playerRectangle.getX() + playerRectangle.getWidth() / 2, playerRectangle.getY() + playerRectangle.getHeight() / 2),
                new Vector2(playerRectangle.getWidth() / 2, playerRectangle.getHeight() / 2),
                BodyDef.BodyType.DynamicBody, world, 0.4f, true, categoryBits, maskBits);
    }

    /**
     * getTiledMap: gets the tiled map
     * @return tiledMap
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * disposes of assets
     */
    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}
