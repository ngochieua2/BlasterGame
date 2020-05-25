package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.PlayScreen;

public class Enemies {
    private static final int MAX_ENEMIES = 5;
    private static final int GREEN_UFO_HEALTH = 3;
    private static final float GREEN_UFO_SPEED = 20f;
    private static final float ENEMY_SPAWN_INTERVAL = 5000f;

    private PlayScreen playScreen;
    private float timeInterval;

    public enum Type {NONE, GREEN_UFO};
    private TextureRegion greenUFOTexture;

    private Type[] type;
    private Sprite[] sprite;
    private Vector2[] velocity;
    private Circle[] circleColliders;
    private Rectangle[] rectangleColliders;
    private int[] health;

    public Enemies(PlayScreen playScreen) {
        this.playScreen = playScreen;
        timeInterval = 0f;
        greenUFOTexture = playScreen.getTextureAtlas().findRegion("ufoGreen");

        type = new Type[MAX_ENEMIES];
        sprite = new Sprite[MAX_ENEMIES];
        velocity = new Vector2[MAX_ENEMIES];
        circleColliders = new Circle[MAX_ENEMIES];
        rectangleColliders = new Rectangle[MAX_ENEMIES];
        health = new int[MAX_ENEMIES];

        init();
    }

    public void init() {
        for (int i=0; i<MAX_ENEMIES; i++) {
            type[i] = Type.NONE;
            velocity[i] = new Vector2();
            circleColliders[i] = new Circle();
            circleColliders[i].setRadius(greenUFOTexture.getRegionWidth() / 2);
            rectangleColliders[i] = new Rectangle();
        }
    }

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
                sprite[freeIndex] = new Sprite(greenUFOTexture);
                if (t == Type.NONE) {
                    return -1;
                }
                //Randomly generate velocity vector so the ships direction is random
                float xVelocity = MathUtils.random(-20f, 20f);
                //Use pythagoras to produce a yVelocity so that the speed of the ship is always constant
                float yVelocity = (float) Math.sqrt(GREEN_UFO_SPEED * GREEN_UFO_SPEED - xVelocity * xVelocity);
                yVelocity = yVelocity * MathUtils.randomSign();
                velocity[freeIndex] = new Vector2(xVelocity, yVelocity);

                health[freeIndex] = GREEN_UFO_HEALTH;

                float spawnY;
                float spawnX = MathUtils.random(0 - greenUFOTexture.getRegionWidth(), Gdx.graphics.getWidth() + greenUFOTexture.getRegionWidth());
                //if it spawns completely left of screen or right of screen, then the Y position can be anywhere within the screen's height
                if (spawnX < 0 + greenUFOTexture.getRegionWidth()/2 || spawnX > Gdx.graphics.getWidth() - greenUFOTexture.getRegionWidth()/2) {
                    spawnY = MathUtils.random(0 - greenUFOTexture.getRegionHeight(), Gdx.graphics.getHeight() + greenUFOTexture.getRegionHeight());
                }
                //Otherwise, it must spawn either completely above the screen or completely below it
                else {
                    if (MathUtils.randomBoolean()) {
                        spawnY = 0 - greenUFOTexture.getRegionHeight();
                    }
                    else {
                        spawnY = Gdx.graphics.getHeight() + greenUFOTexture.getRegionHeight();
                    }
                }
                sprite[freeIndex].setOrigin(spawnX, spawnY);
                sprite[freeIndex].setCenter(spawnX, spawnY);
                circleColliders[freeIndex].setPosition(sprite[freeIndex].getOriginX(), sprite[freeIndex].getOriginY());

                break;
            default:
                break;
        }
        return freeIndex;
    }


    public void update(float deltaTime) {
        timeInterval += deltaTime;

        //Spawns enemies after waiting a specified amount of time
        if (timeInterval >= ENEMY_SPAWN_INTERVAL) {
            spawn(Type.GREEN_UFO);
            timeInterval = 0;
        }

        for (int i=0; i<MAX_ENEMIES; i++) {
            if (type[i] != Type.NONE) {
                sprite[i].translate(velocity[i].x * deltaTime, velocity[i].y * deltaTime);
                circleColliders[i].setPosition(sprite[i].getOriginX(), sprite[i].getOriginY());

                if (overlaps(playScreen.getPlayer().playerBounds, circleColliders[i])) {
                    type[i] = Type.NONE;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i=0; i<MAX_ENEMIES; i++) {
            if (type[i] != Type.NONE) {
                sprite[i].draw(batch);
            }
        }
    }

    //Collision detection between Circle and Polygon
    //https://stackoverflow.com/questions/15323719/circle-and-polygon-collision-with-libgdx
    public static boolean overlaps(Polygon polygon, Circle circle) {
        float []vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(circle.x, circle.y);
        float squareRadius = circle.radius * circle.radius;

        for (int i=0; i<vertices.length; i+=2){
            if (i==0){
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                    return true;
            }
        }
        return polygon.contains(circle.x, circle.y);
    }
}
