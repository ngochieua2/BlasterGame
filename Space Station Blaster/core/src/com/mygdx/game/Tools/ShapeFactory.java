package com.mygdx.game.Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.SpaceStationBlaster;

public class ShapeFactory {



    private ShapeFactory() {}

    public static Body createRectangle(final Vector2 position, final Vector2 size,
                                       final BodyDef.BodyType bodyType, final World world,
                                       float density) {

        // define body
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x / SpaceStationBlaster.PPM,
                position.y / SpaceStationBlaster.PPM);
        bodyDef.type = bodyType;
        final Body body =  world.createBody(bodyDef);

        // define fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / SpaceStationBlaster.PPM, size.y / SpaceStationBlaster.PPM);
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }
}
