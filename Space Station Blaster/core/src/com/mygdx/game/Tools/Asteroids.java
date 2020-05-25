package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.LargeBrownAsteroids;

public class AsteroidTools {
    private static final int Asteroids_Max = 20;

    private static final float Asteroids_Density = 0.4f;


    private World world;
    private PlayScreen playScreen;
    private Sprite LBrown, MBrown, SBrown, LGrey, MGrey, SGrey;

    private float x,y;
    private final Sprite type;
    private float radians;
    private float VectorX;
    private float VectorY;
    private Vector2 position;
    private int width;
    private int height;

    private float health;
    private Body body;

    private int speed;


    public AsteroidTools(World world, PlayScreen playScreen, float x, float y, Sprite type){
        this.world = world;
        this.playScreen = playScreen;
        this.x = x;
        this.y = y;
        this.type = type;

        LBrown = new LargeBrownAsteroids(world,playScreen);
        LGrey = new LargeBrownAsteroids(world,playScreen);

        if (type == LBrown || type == LGrey){

            width = 101; health = 84;
            speed = 32;
        }

        radians = MathUtils.random(2 * 3.14f);
        VectorX = MathUtils.cos(radians) * speed;
        VectorY = MathUtils.sin(radians) * speed;
        position = new Vector2(x,y);
        body = ShapeFactory.createCircle(position, width / 2, BodyDef.BodyType.DynamicBody, world, 0.4f);
    }

    public void update(float dt){
        x += VectorX * dt;
        y += VectorY * dt;
        Vector2 position = new Vector2(x,y);

    }


}
