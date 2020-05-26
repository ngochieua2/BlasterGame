package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

import java.util.Random;

public class Enemies {
    private static final int MAX_ENEMIES = 5;
    private static final int GREEN_UFO_HEALTH = 3;
    private static final int RED_UFO_HEALTH = 3;
    private static final float GREEN_UFO_SPEED = 100f;
    private static final float RED_UFO_SPEED = 100f;
    private static final float ENEMY_SPAWN_INTERVAL = 10f;

    private PlayScreen playScreen;
    private OrthographicCamera camera;
    private float timeInterval;

    public enum Type {
        NONE,
        GREEN_UFO,
        RED_UFO;
        public static Type getRandomType() {
            Random random = new Random();
            return values()[random.nextInt(values().length-1) + 1];
        }
    }

    private TextureRegion greenUFOTexture;
    private TextureRegion redUFOTexture;

    private Type[] type;
    private Sprite[] sprite;
    private Vector2[] velocity;
    public Circle[] circleColliders;
    private Rectangle[] rectangleColliders;
    private int[] health;

    public Enemies(PlayScreen playScreen) {
        this.playScreen = playScreen;
        camera = playScreen.getCamera();
        timeInterval = 0f;
        greenUFOTexture = playScreen.getTextureAtlas().findRegion("ufoGreen");
        redUFOTexture = playScreen.getTextureAtlas().findRegion("ufoRed");

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

        float xVelocity;
        float yVelocity;
        Vector2 spawnPoint;
        type[freeIndex] = t;
        switch (t) {
            case GREEN_UFO:
                sprite[freeIndex] = new Sprite(greenUFOTexture);
                if (t == Type.NONE) {
                    return -1;
                }
                //Randomly generate velocity vector so the ships direction is random
                xVelocity = MathUtils.random(-GREEN_UFO_SPEED, GREEN_UFO_SPEED);
                //Use pythagoras to produce a yVelocity so that the speed of the ship is always constant
                yVelocity = (float) Math.sqrt(GREEN_UFO_SPEED * GREEN_UFO_SPEED - xVelocity * xVelocity);
                yVelocity = yVelocity * MathUtils.randomSign();
                velocity[freeIndex] = new Vector2(xVelocity, yVelocity);

                health[freeIndex] = GREEN_UFO_HEALTH;

                spawnPoint = generateSpawnPoint();

                while (camera.frustum.pointInFrustum(spawnPoint.x, spawnPoint.y, 0)) {
                    spawnPoint = generateSpawnPoint();
                }

                sprite[freeIndex].setOrigin(spawnPoint.x, spawnPoint.y);
                sprite[freeIndex].setCenter(spawnPoint.x, spawnPoint.y);

                break;
            case RED_UFO:
                sprite[freeIndex] = new Sprite(redUFOTexture);
                if (t == Type.NONE) {
                    return -1;
                }

                health[freeIndex] = RED_UFO_HEALTH;

                spawnPoint = generateSpawnPoint();

                while (camera.frustum.pointInFrustum(spawnPoint.x, spawnPoint.y, 0)) {
                    spawnPoint = generateSpawnPoint();
                }

                sprite[freeIndex].setOrigin(spawnPoint.x, spawnPoint.y);
                sprite[freeIndex].setCenter(spawnPoint.x, spawnPoint.y);

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
            spawn(Type.getRandomType());
            timeInterval = 0;
        }

        for (int i=0; i<MAX_ENEMIES; i++) {
            if (type[i] != Type.NONE) {
                if (type[i] == Type.RED_UFO) {
                    float dx = playScreen.getPlayer().getSprite().getX() - sprite[i].getX();
                    float dy = playScreen.getPlayer().getSprite().getY() - sprite[i].getY();
                    float distance = (float) Math.sqrt(dx*dx + dy*dy);

                    dx *= RED_UFO_SPEED / distance;
                    dy *= RED_UFO_SPEED / distance;

                    velocity[i].x += dx;
                    velocity[i].y += dy;

                    if (velocity[i].len() > RED_UFO_SPEED) {
                        velocity[i].setLength(RED_UFO_SPEED);
                    }
                }
                sprite[i].translate(velocity[i].x * deltaTime, velocity[i].y * deltaTime);
                circleColliders[i].setPosition(sprite[i].getX() + greenUFOTexture.getRegionWidth()/2, sprite[i].getY() + greenUFOTexture.getRegionWidth() /2);

                //Collision with player
                if (overlaps(playScreen.getPlayer().playerBounds, circleColliders[i])) {
                    type[i] = Type.NONE;
                }
                //Collision with boundary
                for (Rectangle wall : playScreen.getWalls().colliders) {
                    if (Intersector.overlaps(circleColliders[i], wall)) {
                        type[i] = Type.NONE;
                        spawn(Type.getRandomType());
                    }
                }
                if (circleColliders[i].x < 0 || circleColliders[i].x > SpaceStationBlaster.MAP_WIDTH
                        || circleColliders[i].y < 0 || circleColliders[i].y > SpaceStationBlaster.MAP_HEIGHT) {
                    type[i] = Type.NONE;
                    spawn(Type.getRandomType());
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
    private static boolean overlaps(Polygon polygon, Circle circle) {
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

    private Vector2 generateSpawnPoint() {
        float spawnX = MathUtils.random(0, SpaceStationBlaster.MAP_WIDTH - greenUFOTexture.getRegionWidth());
        float spawnY = MathUtils.random(0, SpaceStationBlaster.MAP_HEIGHT - greenUFOTexture.getRegionHeight());

        return new Vector2(spawnX, spawnY);
    }
}
