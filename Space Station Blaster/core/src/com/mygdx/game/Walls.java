package com.mygdx.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;

import java.util.ArrayList;

public class Walls {
    private static final String MAP_BOUNDS = "Bounds";
    private static final int MAX_WALLS = 8;

    public static final int TOP_WALL = 4;
    public static final int BOTTOM_WALL = 5;
    public static final int LEFT_WALL = 7;
    public static final int RIGHT_WALL = 6;

    // Entity data
    private Array<RectangleMapObject> rectangleMapObjects;
    public ArrayList<Rectangle> colliders;
    private Vector2[] positions;

    private TiledMap tiledMap;
    public Walls(PlayScreen playScreen) {
        this.tiledMap = playScreen.getTiledMap();
        positions = new Vector2[MAX_WALLS];
        colliders = new ArrayList<Rectangle>();

        // get all the bounds objects in the tile map
        rectangleMapObjects = tiledMap.getLayers().get(MAP_BOUNDS).getObjects().getByType(RectangleMapObject.class);

        // create static bodies and fixtures for all the objects in the tile map
        for (RectangleMapObject rectangleMapObject : rectangleMapObjects) {
            Rectangle rectangle = rectangleMapObject.getRectangle();
            colliders.add(rectangle);
        }
        for (int index = 0; index < MAX_WALLS; index++) {
            positions[index] = new Vector2();
            positions[index].x = colliders.get(index).x;
            positions[index].y = colliders.get(index).y;
        }
    }

}
