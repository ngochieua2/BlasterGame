package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.mygdx.game.Bullets;
import com.mygdx.game.Effects;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

import java.util.Random;

public class Enemies {
    public static final int MAX_ENEMIES = 10;
    private static final int GREEN_UFO_HEALTH = 3;
    private static final int RED_UFO_HEALTH = 3;
    private static final float GREEN_UFO_SPEED = 100f;
    private static final float RED_UFO_SPEED = 100f;
    private static final float ENEMY_SPAWN_INTERVAL = 30f;
    private static final float ENEMY_SHOOT_INTERVAL = 0.8f;
    private static final float ROTATION_SPEED = 3;

    private PlayScreen playScreen;
    private Effects effects;
    private Bullets bullets;
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

    public Type[] type;
    private Sprite[] sprite;
    public Animation[] animations;
    private TextureRegion[] currentFrame;
    private float[] animationElapsedTime;
    private Vector2[] position;
    private Vector2[] velocity;
    private float[] radians;
    public Circle[] circleColliders;
    private Rectangle[] rectangleColliders;
    private int[] health;
    private float[] shootInterval;

    public Enemies(PlayScreen playScreen) {
        this.playScreen = playScreen;
        effects = playScreen.getEffects();
        camera = playScreen.getCamera();
        timeInterval = 0f;
        greenUFOTexture = playScreen.getTextureAtlas().findRegion("ufoGreen");
        redUFOTexture = playScreen.getTextureAtlas().findRegion("ufoRed");

        bullets = playScreen.getBullets();

        type = new Type[MAX_ENEMIES];
        sprite = new Sprite[MAX_ENEMIES];
        animations = new Animation[MAX_ENEMIES];
        currentFrame = new TextureRegion[MAX_ENEMIES];
        animationElapsedTime = new float[MAX_ENEMIES];
        position = new Vector2[MAX_ENEMIES];
        velocity = new Vector2[MAX_ENEMIES];
        radians = new float[MAX_ENEMIES];
        circleColliders = new Circle[MAX_ENEMIES];
        rectangleColliders = new Rectangle[MAX_ENEMIES];
        health = new int[MAX_ENEMIES];
        shootInterval = new float[MAX_ENEMIES];
        init();
    }

    public void init() {
        for (int i=0; i<MAX_ENEMIES; i++) {
            type[i] = Type.NONE;
            position[i] = new Vector2();
            velocity[i] = new Vector2();
            currentFrame[i] = new TextureRegion();
            animationElapsedTime[i] = 0f;
            radians[i] = 0f;
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
                sprite[freeIndex].setCenter(spawnPoint.x, spawnPoint.y);
                break;
            default:
                break;
        }
        return freeIndex;
    }


    public void update(float deltaTime) {
        timeInterval += deltaTime;
        int currentBulletIndex;

        //Spawns enemies after waiting a specified amount of time
        if (timeInterval >= ENEMY_SPAWN_INTERVAL) {
            spawn(Type.getRandomType());
            timeInterval = 0f;
        }

        for (int i=0; i<MAX_ENEMIES; i++) {
            if (type[i] != Type.NONE) {
                rotate(i, deltaTime);

                shootInterval[i] += deltaTime;
                if (shootInterval[i] >= ENEMY_SHOOT_INTERVAL) {
                    currentBulletIndex = bullets.spawn(SpaceStationBlaster.BulletType.PURPLE, radians[i]);
                    if (currentBulletIndex >= 0) {
                        bullets.position[currentBulletIndex].set(sprite[i].getX(), sprite[i].getY());
                        shootInterval[i] = 0f;
                    }
                }

                if (type[i] == Type.RED_UFO) {
                    Sprite player = playScreen.getPlayer().getSprite();
                    float dx = player.getX() - sprite[i].getX();
                    float dy = player.getY() - sprite[i].getY();
                    float distance = (float) Math.sqrt(dx*dx + dy*dy);

                    dx *= RED_UFO_SPEED / distance;
                    dy *= RED_UFO_SPEED / distance;

                    velocity[i].x += dx;
                    velocity[i].y += dy;

                    if (velocity[i].len() > RED_UFO_SPEED) {
                        velocity[i].setLength(RED_UFO_SPEED);
                    }
                    radians[i] = velocity[i].angleRad() - MathUtils.PI / 2;
                    sprite[i].setRotation(radians[i] * MathUtils.radiansToDegrees);
                }
                sprite[i].translate(velocity[i].x * deltaTime, velocity[i].y * deltaTime);
                circleColliders[i].setPosition(sprite[i].getX() + greenUFOTexture.getRegionWidth()/2, sprite[i].getY() + greenUFOTexture.getRegionWidth() /2);
                //record the position of the sprite's centre fo the purpose of animation
                position[i].set(circleColliders[i].x, circleColliders[i].y);

                //Collision with player
                if (overlaps(playScreen.getPlayer().playerBounds, circleColliders[i])) {
                    type[i] = Type.NONE;
                    circleColliders[i].setPosition(0, 0);
                }
                //Collision with boundary
                Walls walls = playScreen.getWalls();
                int wallIndex = -1;
                for (Rectangle wall : playScreen.getWalls().colliders) {
                    wallIndex++;
                    if (Intersector.overlaps(circleColliders[i], wall)) {
                        //Bounce off the wall by negating velocity;
                        if (wallIndex == walls.TOP_WALL || wallIndex == walls.BOTTOM_WALL) {
                            velocity[i].y = -velocity[i].y;
                        }
                        if (wallIndex == walls.LEFT_WALL || wallIndex == walls.RIGHT_WALL) {
                            velocity[i].x = -velocity[i].x;
                        }
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

    public void render(SpriteBatch batch, float deltaTime) {
        for (int i=0; i<MAX_ENEMIES; i++) {
            if (type[i] != Type.NONE) {
                sprite[i].draw(batch);
            }

            if (animations[i] != null) {
                if (!animations[i].isAnimationFinished(animationElapsedTime[i])) {
                    currentFrame[i] = (TextureRegion) animations[i].getKeyFrame(animationElapsedTime[i], false);
                    batch.draw(currentFrame[i], position[i].x - currentFrame[i].getRegionWidth()/2, position[i].y - currentFrame[i].getRegionHeight()/2,
                            currentFrame[i].getRegionWidth() / 2,
                            currentFrame[i].getRegionHeight() / 2,
                            currentFrame[i].getRegionWidth(),
                            currentFrame[i].getRegionHeight(), 1, 1,
                            (float) (radians[i] + Math.PI / 2) * MathUtils.radiansToDegrees);
                    animationElapsedTime[i] += deltaTime;
                }
                else {
                    animationElapsedTime[i] = 0f;
                    currentFrame[i] = null;
                    animations[i] = null;
                }
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

    private Vector2 generateSpawnPoint() {
        float spawnX = MathUtils.random(0 + greenUFOTexture.getRegionWidth(), SpaceStationBlaster.MAP_WIDTH - greenUFOTexture.getRegionWidth());
        float spawnY = MathUtils.random(0 + greenUFOTexture.getRegionHeight(), SpaceStationBlaster.MAP_HEIGHT - greenUFOTexture.getRegionHeight());

        return new Vector2(spawnX, spawnY);
    }

    private void rotate(int typeIndex, float deltaTime) {
        switch(type[typeIndex]) {
            case GREEN_UFO:
                radians[typeIndex] += ROTATION_SPEED * deltaTime;
                sprite[typeIndex].setRotation(radians[typeIndex] * MathUtils.radiansToDegrees);
                break;
        }
    }
}
