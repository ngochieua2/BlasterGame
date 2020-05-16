package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Tools.ShapeFactory;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap tiledMap;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, TiledMap tiledMap, Rectangle bounds) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.bounds = bounds;

        ShapeFactory.createRectangle(
                new Vector2(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2),
                new Vector2(bounds.getWidth() / 2, bounds.getHeight() / 2),
                BodyDef.BodyType.StaticBody, world, 1f);
    }

}
