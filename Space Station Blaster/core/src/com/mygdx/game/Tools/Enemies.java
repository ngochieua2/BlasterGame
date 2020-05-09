package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.GreenUFO;

public class Enemies {
    public static final int MAX_ENEMIES = 5;
    public static final int GREEN_UFO_HEALTH = 3;

    private Sprite greenUfoSprite;
    private World world;
    private PlayScreen playScreen;

    public enum Type {NONE, GREEN_UFO};

    public Type[] type;
    public Sprite[] sprite;
    public Vector2[] velocity;
    public int[] health;


    public Enemies(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;

        type = new Type[MAX_ENEMIES];
        sprite = new Sprite[MAX_ENEMIES];
        velocity = new Vector2[MAX_ENEMIES];
        health = new int[MAX_ENEMIES];
    }

    public void init() {
        for (int i=0; i<MAX_ENEMIES; i++) {
            type[i] = Type.NONE;
            velocity[i] = new Vector2();
        }
    }
    //TODO: spawn enemies
    public int spawn(Type t) {
        if (t == Type.NONE) {
            return -1;
        }

        int freeIndex = -1;
        for (int i=0; i<MAX_ENEMIES; i++) {
            if (type[i] == Type.NONE) {
                freeIndex = i;
            }
        }
        if (freeIndex < 0) {
            return -1;
        }
        type[freeIndex] = t;
        switch (t) {
            case GREEN_UFO:
                sprite[freeIndex] = new GreenUFO(world, playScreen);
                if (t == Type.NONE) {
                    return -1;
                }
                //Randomly generate velocity vector so the ships direction is random
                float xVelocity = MathUtils.random(-20f, 20f);
                //Use pythagoras to produce a yVelocity so that the speed of the ship is always constant
                float yVelocity = (float) Math.sqrt(GreenUFO.GREEN_UFO_SPEED * GreenUFO.GREEN_UFO_SPEED - xVelocity * xVelocity);
                yVelocity = yVelocity * MathUtils.randomSign();
                velocity[freeIndex] = new Vector2(xVelocity, yVelocity);

                health[freeIndex] = GREEN_UFO_HEALTH;

                //TODO: create body

                float spawnX;
                float spawnY;
                //TODO: get camera position to come up with random spawn point
                if (xVelocity < 0) {

                }
                break;
            default:
                break;
        }
        return freeIndex;
    }

    public void update(float deltaTime) {

    }

    public void render(SpriteBatch batch) {

    }
}
