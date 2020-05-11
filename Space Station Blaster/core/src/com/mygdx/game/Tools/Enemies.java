package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.GreenUFO;

public class Enemies {
    private static final int MAX_ENEMIES = 5;
    private static final int GREEN_UFO_HEALTH = 3;
    private static final float GREEN_UFO_DENSITY = 0.4f;

    private Sprite greenUfoSprite;
    private World world;
    private PlayScreen playScreen;

    public enum Type {NONE, GREEN_UFO};

    private Type[] type;
    private Sprite[] sprite;
    private Vector2[] velocity;
    private int[] health;
    private Body[] body;


    public Enemies(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;

        type = new Type[MAX_ENEMIES];
        sprite = new Sprite[MAX_ENEMIES];
        velocity = new Vector2[MAX_ENEMIES];
        health = new int[MAX_ENEMIES];
        body = new Body[MAX_ENEMIES];
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

                //TODO: get camera position to come up with random spawn point
                float spawnY;
                float spawnX = MathUtils.random(0 - GreenUFO.GREEN_UFO_TEXTURE_WIDTH, Gdx.graphics.getWidth() + GreenUFO.GREEN_UFO_TEXTURE_WIDTH);
                //if it spawns completely left of screen or right of screen, then the Y position can be anywhere within the screen's height
                if (spawnX < 0 + GreenUFO.GREEN_UFO_TEXTURE_WIDTH/2 || spawnX > Gdx.graphics.getWidth() - GreenUFO.GREEN_UFO_TEXTURE_WIDTH/2) {
                    spawnY = MathUtils.random(0 - GreenUFO.GREEN_UFO_TEXTURE_HEIGHT, Gdx.graphics.getHeight() + GreenUFO.GREEN_UFO_TEXTURE_HEIGHT);
                }
                //Otherwise, it must spawn either completely above the screen or completely below it
                else {
                    if (MathUtils.randomBoolean()) {
                        spawnY = 0 - GreenUFO.GREEN_UFO_TEXTURE_HEIGHT;
                    }
                    else {
                        spawnY = Gdx.graphics.getHeight() + GreenUFO.GREEN_UFO_TEXTURE_HEIGHT;
                    }
                }
                Vector2 position = new Vector2(spawnX, spawnY);
                body[freeIndex] = ShapeFactory.createCircle(position, GreenUFO.GREEN_UFO_TEXTURE_WIDTH/2, BodyDef.BodyType.DynamicBody, world, 0.4f);
                sprite[freeIndex].setOrigin(spawnX, spawnY);
                sprite[freeIndex].setCenter(spawnX, spawnY);
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
