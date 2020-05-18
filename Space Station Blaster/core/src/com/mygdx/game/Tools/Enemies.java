package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Sprites.GreenUFO;

public class Enemies {
    private static final int MAX_ENEMIES = 5;
    private static final int GREEN_UFO_HEALTH = 3;

    private World world;
    private PlayScreen playScreen;

    public enum Type {NONE, GREEN_UFO};

    private Type[] type;
    private Sprite[] sprite;
    //private Vector2[] velocity;
    private int[] health;
    private Body[] body;


    public Enemies(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;

        type = new Type[MAX_ENEMIES];
        sprite = new Sprite[MAX_ENEMIES];
        //velocity = new Vector2[MAX_ENEMIES];
        health = new int[MAX_ENEMIES];
        body = new Body[MAX_ENEMIES];
    }

    /*public void init() {
        for (int i=0; i<MAX_ENEMIES; i++) {
            type[i] = Type.NONE;
            velocity[i] = new Vector2();
        }
    }*/

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
                sprite[freeIndex] = new GreenUFO(playScreen);
                if (t == Type.NONE) {
                    return -1;
                }
                //Randomly generate velocity vector so the ships direction is random
                float xVelocity = MathUtils.random(-20f, 20f);
                //Use pythagoras to produce a yVelocity so that the speed of the ship is always constant
                float yVelocity = (float) Math.sqrt(GreenUFO.GREEN_UFO_SPEED * GreenUFO.GREEN_UFO_SPEED - xVelocity * xVelocity);
                yVelocity = yVelocity * MathUtils.randomSign();
                //velocity[freeIndex] = new Vector2(xVelocity, yVelocity);

                health[freeIndex] = GREEN_UFO_HEALTH;

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
                sprite[freeIndex].setOrigin(spawnX, spawnY);
                sprite[freeIndex].setCenter(spawnX, spawnY);

                spawnGreenUFO(spawnX, spawnY, freeIndex);
                body[freeIndex].setLinearVelocity(xVelocity, yVelocity);
                break;
            default:
                break;
        }
        return freeIndex;
    }

    public void spawnGreenUFO(float x, float y, int index) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(x/ SpaceStationBlaster.PPM, y/SpaceStationBlaster.PPM);

        body[index] = world.createBody(bd);

        CircleShape circle = new CircleShape();
        circle.setRadius((GreenUFO.GREEN_UFO_TEXTURE_WIDTH/2)/SpaceStationBlaster.PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = GreenUFO.GREEN_UFO_DENSITY;

        body[index].createFixture(fd);
    }

    public void update(float deltaTime) {
        for (int i=0; i<MAX_ENEMIES; i++) {
            if (type[i] != Type.NONE) {
                sprite[i].translate(body[i].getLinearVelocity().x * deltaTime, body[i].getLinearVelocity().y * deltaTime);
            }
        }
    }

    public void render(SpriteBatch batch) {

    }
}
