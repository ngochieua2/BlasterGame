package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Enemies;

import java.util.ArrayList;
import java.util.Random;

public class Asteroids {

    // constants for large brown asteroid
    private static final int MeteorBrown_Big1_TEXTURE_X = 220;
    private static final int MeteorBrown_Big1_TEXTURE_Y = 250;
    private static final int MeteorBrown_Big1_TEXTURE_WIDTH = 101;
    private static final int MeteorBrown_Big1_TEXTURE_HEIGHT = 84;
    private static final float MeteorBrown_Big1_SPEED = 32;
    private static final int MeteorBrown_Big1_MAX_HEALTH = 3;

    // constants for large grey asteroid
    private static final int MeteorGrey_Big1_TEXTURE_X = 321;
    private static final int MeteorGrey_Big1_TEXTURE_Y = 250;
    private static final int MeteorGrey_Big1_TEXTURE_WIDTH = 101;
    private static final int MeteorGrey_Big1_TEXTURE_HEIGHT = 84;
    private static final float MeteorGrey_Big1_SPEED = 32;
    private static final int MeteorGrey_Big1_MAX_HEALTH = 3;

    // constants for medium brown asteroid
    private static final int MeteorBrown_Med1_TEXTURE_X = 651;
    private static final int MeteorBrown_Med1_TEXTURE_Y = 40;
    private static final int MeteorBrown_Med1_TEXTURE_WIDTH = 43;
    private static final int MeteorBrown_Med1_TEXTURE_HEIGHT = 43;
    private static final float MeteorBrown_Med1_SPEED = 64;

    // constants for medium grey asteroid
    private static final int MeteorGrey_Med1_TEXTURE_X = 694;
    private static final int MeteorGrey_Med1_TEXTURE_Y = 40;
    private static final int MeteorGrey_Med1_TEXTURE_WIDTH = 43;
    private static final int MeteorGrey_Med1_TEXTURE_HEIGHT = 43;
    private static final float MeteorGrey_Med1_SPEED = 64;

    // constants for small brown asteroid
    private static final int MeteorBrown_Small1_TEXTURE_X = 469;
    private static final int MeteorBrown_Small1_TEXTURE_Y = 0;
    private static final int MeteorBrown_Small1_TEXTURE_WIDTH = 28;
    private static final int MeteorBrown_Small1_TEXTURE_HEIGHT = 28;
    private static final float MeteorBrown_Small1_SPEED = 96;

    // constants for small grey asteroid
    private static final int MeteorGrey_Small1_TEXTURE_X = 497;
    private static final int MeteorGrey_Small1_TEXTURE_Y = 0;
    private static final int MeteorGrey_Small1_TEXTURE_WIDTH = 28;
    private static final int MeteorGrey_Small1_TEXTURE_HEIGHT = 28;
    private static final float MeteorGrey_Small1_SPEED = 96;


    private static final int Asteroids_Max = 100;

    //TextureRegion of all asteroids
    private TextureRegion LargeBrownAstTexture;
    private TextureRegion LargeGreyAstTexture;
    private TextureRegion MediumBrownAstTexture;
    private TextureRegion MediumGreyAstTexture;
    private TextureRegion SmallBrownAstTexture;
    private TextureRegion SmallGreyAstTexture;

    //Polygon
    public Polygon LargeBrownAstCollider;
    private Polygon LargeGreyAstCollider;
    private Polygon MediumBrownAstCollider;
    private Polygon MediumGreyAstCollider;
    private Polygon SmallBrownAstCollider;
    private Polygon SmallGreyAstCollider;
    public Polygon[] collider;



    private PlayScreen playScreen;

    private Sprite[] asteroidSprite;
    //public Vector2[] position;
    public Vector2[] direction;
    public float[] radians;
    private float[] health;
    private float[] speed;
    private float[] rotationSpeed;

    private enum  TYPE {
        BROWN_LARGE, GREY_LARGE, BROWN_MEDIUM, GREY_MEDIUM, BROWN_SMALL, GREY_SMALL, NONE;
        public static TYPE getRandomType() {
            Random random = new Random();
            return values()[random.nextInt(values().length-1) + 1];
        }
    };
    private TYPE[] type;
    private int width = 0;
    private int height = 0;

    public Asteroids(PlayScreen playScreen) {
        this.playScreen = playScreen;

        //get texture region
        LargeBrownAstTexture = playScreen.getTextureAtlas().findRegion("meteorBrown_big1");
        LargeGreyAstTexture = playScreen.getTextureAtlas().findRegion("meteorGrey_big1");
        MediumBrownAstTexture = playScreen.getTextureAtlas().findRegion("meteorBrown_med1");
        MediumGreyAstTexture = playScreen.getTextureAtlas().findRegion("meteorGrey_med1");
        SmallBrownAstTexture = playScreen.getTextureAtlas().findRegion("meteorBrown_small1");
        SmallGreyAstTexture = playScreen.getTextureAtlas().findRegion("meteorGrey_small1");

        //Get Collider
        LargeBrownAstCollider = new Polygon(new float[]{0, 0, MeteorBrown_Big1_TEXTURE_WIDTH, 0,
                MeteorBrown_Big1_TEXTURE_WIDTH, MeteorBrown_Big1_TEXTURE_HEIGHT,
                0, MeteorBrown_Big1_TEXTURE_HEIGHT});
        LargeGreyAstCollider = new Polygon(new float[]{0, 0, MeteorGrey_Big1_TEXTURE_WIDTH, 0,
                MeteorGrey_Big1_TEXTURE_WIDTH, MeteorGrey_Big1_TEXTURE_HEIGHT,
                0, MeteorGrey_Big1_TEXTURE_HEIGHT});
        MediumBrownAstCollider = new Polygon(new float[]{0, 0, MeteorBrown_Med1_TEXTURE_WIDTH, 0,
                MeteorBrown_Med1_TEXTURE_WIDTH, MeteorBrown_Med1_TEXTURE_HEIGHT,
                0, MeteorBrown_Med1_TEXTURE_HEIGHT});
        MediumGreyAstCollider = new Polygon(new float[]{0, 0, MeteorGrey_Med1_TEXTURE_WIDTH, 0,
                MeteorGrey_Med1_TEXTURE_WIDTH, MeteorGrey_Med1_TEXTURE_HEIGHT,
                0, MeteorGrey_Med1_TEXTURE_HEIGHT});
        SmallBrownAstCollider = new Polygon(new float[]{0, 0, MeteorBrown_Small1_TEXTURE_WIDTH, 0,
                MeteorBrown_Small1_TEXTURE_WIDTH, MeteorBrown_Small1_TEXTURE_HEIGHT,
                0, MeteorBrown_Small1_TEXTURE_HEIGHT});
        SmallGreyAstCollider = new Polygon(new float[]{0, 0, MeteorGrey_Small1_TEXTURE_WIDTH, 0,
                MeteorGrey_Small1_TEXTURE_WIDTH, MeteorGrey_Small1_TEXTURE_HEIGHT,
                0, MeteorGrey_Small1_TEXTURE_HEIGHT});

        init(Asteroids_Max);


    }

    public void init(int size ){

        asteroidSprite = new Sprite[size];
        type = new TYPE[size];

        direction = new Vector2[size];
        radians = new float[size];
        rotationSpeed = new float[size];
        health = new float[size];
        speed = new float[size];
        collider = new Polygon[size];

        for (int i = 0; i < size; i++){
            type[i] = TYPE.NONE;

            direction[i] = new Vector2();
        }
    }


    public int spawn(TYPE t){
        // Check null
        if (t == TYPE.NONE) return -1;

        int index = -1;
        for (int i = 0; i < Asteroids_Max; i++) {
            if (type[i] == TYPE.NONE) {
                index = i;
                break;
            }
        }

        if (index < 0) return -1;


        type[index] = t;
        // initialise for different types of asteroid
        switch (t){
            case GREY_LARGE:
                asteroidSprite[index] = new Sprite(LargeBrownAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }

                speed[index] = 32f;
                health[index] = 3f;
                radians[index] = MathUtils.random(2 * 3.1415f);

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];


                asteroidSprite[index].setOrigin(SpawnPosition().x, SpawnPosition().y);
                asteroidSprite[index].setCenter(SpawnPosition().x, SpawnPosition().y);

                break;

            case BROWN_LARGE:
                asteroidSprite[index] = new Sprite(LargeGreyAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 32f;
                health[index] = 3f;
                radians[index] = MathUtils.random(2 * 3.1415f);

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                asteroidSprite[index].setOrigin(SpawnPosition().x, SpawnPosition().y);
                asteroidSprite[index].setCenter(SpawnPosition().x, SpawnPosition().y);

                break;

            case GREY_MEDIUM:
                asteroidSprite[index] = new Sprite(MediumGreyAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 64f;
                health[index] = 1.5f;

                radians[index] = MathUtils.random(2 * 3.1415f);

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                asteroidSprite[index].setOrigin(SpawnPosition().x, SpawnPosition().y);
                asteroidSprite[index].setCenter(SpawnPosition().x, SpawnPosition().y);

                break;

            case BROWN_MEDIUM:
                asteroidSprite[index] = new Sprite(MediumBrownAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 64f;
                health[index] = 1.5f;

                radians[index] = MathUtils.random(2 * 3.1415f);

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                asteroidSprite[index].setOrigin(SpawnPosition().x, SpawnPosition().y);
                asteroidSprite[index].setCenter(SpawnPosition().x, SpawnPosition().y);

                break;

            case BROWN_SMALL:
                asteroidSprite[index] = new Sprite(SmallBrownAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 96f;
                health[index] = 0.75f;

                radians[index] = MathUtils.random(2 * 3.1415f);

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];


                asteroidSprite[index].setOrigin(SpawnPosition().x, SpawnPosition().y);
                asteroidSprite[index].setCenter(SpawnPosition().x, SpawnPosition().y);

                break;

            case GREY_SMALL:
                asteroidSprite[index] = new Sprite(SmallGreyAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 96f;
                health[index] = 0.75f;

                radians[index] = MathUtils.random(2 * 3.1415f);

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];


                asteroidSprite[index].setOrigin(SpawnPosition().x, SpawnPosition().y);
                asteroidSprite[index].setCenter(SpawnPosition().x, SpawnPosition().y);
                break;

            case NONE:
                break;

        }
        return index;
    }



    public void update(float dt){

        //Spawns 20 asteroids when start
        for (int i = 0; i < 20; i++){
            spawn(TYPE.getRandomType());
        }


        //all asteroids movement
        for (int index = 0; index < Asteroids_Max; index ++) {
            if (type[index] != TYPE.NONE) {

                switch (type[index]){
                    case BROWN_LARGE:
                        width = MeteorBrown_Big1_TEXTURE_WIDTH;
                        height = MeteorBrown_Big1_TEXTURE_HEIGHT;
                        collider[index] = LargeBrownAstCollider;
                        break;
                    case GREY_LARGE:
                        width = MeteorGrey_Big1_TEXTURE_WIDTH;
                        height = MeteorGrey_Big1_TEXTURE_HEIGHT;
                        collider[index] = LargeGreyAstCollider;
                        break;
                    case BROWN_MEDIUM:
                        width = MeteorBrown_Med1_TEXTURE_WIDTH;
                        height = MeteorBrown_Med1_TEXTURE_HEIGHT;
                        collider[index] = MediumBrownAstCollider;
                        break;
                    case GREY_MEDIUM:
                        width = MeteorGrey_Med1_TEXTURE_WIDTH;
                        height = MeteorGrey_Med1_TEXTURE_HEIGHT;
                        collider[index] = MediumGreyAstCollider;
                        break;
                    case BROWN_SMALL:
                        width = MeteorBrown_Small1_TEXTURE_WIDTH;
                        height = MeteorBrown_Small1_TEXTURE_HEIGHT;
                        collider[index] = SmallBrownAstCollider;
                        break;
                    case GREY_SMALL:
                        width = MeteorGrey_Small1_TEXTURE_WIDTH;
                        height = MeteorGrey_Small1_TEXTURE_HEIGHT;
                        collider[index] = SmallGreyAstCollider;
                        break;

                }
                asteroidSprite[index].translate(direction[index].x * dt, direction[index].y * dt);

                collider[index].setOrigin(width / 2, height / 2);
                collider[index].setPosition(asteroidSprite[index].getX() + width/2, asteroidSprite[index].getY() + height/2);
                collider[index].setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);

                for (Rectangle wall : playScreen.getWalls().colliders) {
                    Polygon polygonWall = new Polygon(new float[] { 0, 0, wall.getWidth(), 0,
                        wall.getWidth(), wall.getHeight(), 0, wall.getHeight() });
                    polygonWall.setPosition(wall.x, wall.y);
                    if (Intersector.overlapConvexPolygons(polygonWall, collider[index])) {
                        radians[index] = radians[index] + 3.1415f/2;
                        direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                        direction[index].y = MathUtils.sin(radians[index]) * speed[index];
                        //radians[index] = MathUtils.random(2 * 3.1415f);
                    }


                }

            }

        }

    }

    private Vector2 SpawnPosition() {
        float x = MathUtils.random(0, SpaceStationBlaster.MAP_WIDTH);
        //Make sure spawn position x is not too close with player
        while (x > SpaceStationBlaster.MAP_WIDTH/2  - MeteorBrown_Big1_TEXTURE_WIDTH * 2
                && x < SpaceStationBlaster.MAP_WIDTH/2  + MeteorBrown_Big1_TEXTURE_WIDTH * 2  )
        {
            x = MathUtils.random(0, SpaceStationBlaster.MAP_WIDTH);
        }

        float y = MathUtils.random(0, SpaceStationBlaster.MAP_HEIGHT);
        //Make sure spawn position y is not too close with player
        while (y > SpaceStationBlaster.MAP_HEIGHT/2   - MeteorBrown_Big1_TEXTURE_HEIGHT * 2
                && y < SpaceStationBlaster.MAP_HEIGHT/2   + MeteorBrown_Big1_TEXTURE_HEIGHT * 2  )
        {
                y = MathUtils.random(0, SpaceStationBlaster.MAP_HEIGHT);
        }

        return new Vector2(x, y);
    }


//    public void split(int index){
//
//        if (type[index] == TYPE.BROWN_LARGE){
//            type[index] = TYPE.NONE;
//
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.BROWN_MEDIUM;
//                    break;
//                }
//            }
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.BROWN_MEDIUM;
//                    break;
//                }
//            }
//        }
//        if (type[index] == TYPE.GREY_LARGE){
//            type[index] = TYPE.NONE;
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.GREY_MEDIUM;
//                    break;
//                }
//            }
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.GREY_MEDIUM;
//                    break;
//                }
//            }
//        }
//        if (type[index] == TYPE.BROWN_MEDIUM){
//            type[index] = TYPE.NONE;
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.BROWN_SMALL;
//                    break;
//                }
//            }
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.BROWN_SMALL;
//                    break;
//                }
//            }
//        }
//        if (type[index] == TYPE.GREY_MEDIUM){
//            type[index] = TYPE.NONE;
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.GREY_SMALL;
//                    break;
//                }
//            }
//            for (int i = index + 1; i < Asteroids_Max; i ++) {
//                if (type[i] == TYPE.NONE) {
//                    position[i] = position[index];
//                    type[i] = TYPE.GREY_SMALL;
//                    break;
//                }
//            }
//        }
//    }



    public void render(SpriteBatch batch) {
        for (int i=0; i<Asteroids_Max; i++) {
            if (type[i] != TYPE.NONE) {
                asteroidSprite[i].draw(batch);
            }
        }

    }

}
