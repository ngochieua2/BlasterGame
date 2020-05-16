package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Tools.ShapeFactory;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap tiledMap;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap tiledMap, Rectangle bounds) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.bounds = bounds;

        body = ShapeFactory.createRectangle(
                new Vector2(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2),
                new Vector2(bounds.getWidth() / 2, bounds.getHeight() / 2),
                BodyDef.BodyType.StaticBody, world, 1f);


        // define body
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(bounds.getX() + bounds.getWidth() / 2 / SpaceStationBlaster.PPM,
                bounds.getY() + bounds.getHeight() / 2 / SpaceStationBlaster.PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        final Body body =  world.createBody(bodyDef);

        // define fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.getWidth() / 2 / SpaceStationBlaster.PPM,
                bounds.getHeight() / 2 / SpaceStationBlaster.PPM);
        final FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }



}
