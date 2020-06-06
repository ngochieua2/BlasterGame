package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.PlayScreen;

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


    public static final int Asteroids_Max = 100;

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
    public Polygon[] Astcollider;



    private PlayScreen playScreen;
    private Sprite[] asteroidSprite;
    public Vector2[] position;
    public Vector2[] direction;
    public float[] radians;
    private float[] health;
    private float[] speed;
    private float[] rollSpeed;
    private TextureRegion[] currentFrame;
    private float[] animationElapsedTime;
    private Animation[] animations;
    public Bullets bullets;
    public Effects effects;
    private Vector2[] animationPosition;

    public enum  TYPE {
        BROWN_LARGE, GREY_LARGE, BROWN_MEDIUM, GREY_MEDIUM, BROWN_SMALL, GREY_SMALL, NONE;
        public static TYPE getRandomType() {
            Random random = new Random();
            TYPE rtype;
            rtype = values()[random.nextInt(values().length-1) + 1];
            while (rtype == NONE){
                rtype = values()[random.nextInt(values().length-1) + 1];
            }
            return rtype;
        }
    };
    public TYPE[] type;
    private int width = 0;
    private int height = 0;

    public Asteroids(PlayScreen playScreen) {
        this.playScreen = playScreen;
        effects = playScreen.getEffects();
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

        //Spawns 20 asteroids when start
        for (int i = 0; i < 20; i++){
            spawn(TYPE.getRandomType());
        }

    }

    public void init(int size ){

        asteroidSprite = new Sprite[size];
        type = new TYPE[size];
        position = new  Vector2[size];
        direction = new Vector2[size];
        radians = new float[size];
        health = new float[size];
        speed = new float[size];
        Astcollider = new Polygon[size];
        rollSpeed = new  float[size];
        animationElapsedTime = new float[size];
        currentFrame = new TextureRegion[size];
        animations = new Animation[size];
        animationPosition = new Vector2[size];

        for (int i = 0; i < size; i++){
            type[i] = TYPE.NONE;
            position[i] = new Vector2();
            direction[i] = new Vector2();
            animationElapsedTime[i] = 0f;
            currentFrame[i] = new TextureRegion();
            radians[i] = 0f;
            Astcollider[i] = new Polygon();
            animationPosition[i] = new Vector2(0f,0f);
        }
    }


    public int spawn(TYPE t){
        // Check null
        if (t == TYPE.NONE) return -1;

        int index = -1;
        for (int i = 0; i < Asteroids_Max; i++) {
            if (type[i] == TYPE.NONE) {
                index = i;
                //break;
            }
        }

        if (index < 0) return -1;


        type[index] = t;
        position[index] = new Vector2(0f,0f);
        direction[index] = new Vector2(0f,0f);
        // initialise for different types of asteroid
        switch (t){
            case GREY_LARGE:
                asteroidSprite[index] = new Sprite(LargeGreyAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }

                speed[index] = 32f;
                health[index] = 3f;
                rollSpeed[index] = MathUtils.random(-2f,2f);;
                radians[index] = MathUtils.random((float) (2 * Math.PI));

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                position[index] = SpawnPosition();

                //asteroidSprite[index].setOrigin(MeteorGrey_Big1_TEXTURE_WIDTH/2, MeteorGrey_Big1_TEXTURE_HEIGHT/2);
                asteroidSprite[index].setCenter(position[index].x, position[index].y);

                break;

            case BROWN_LARGE:
                asteroidSprite[index] = new Sprite(LargeBrownAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 32f;
                health[index] = 3f;
                rollSpeed[index] = MathUtils.random(-2f,2f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                //asteroidSprite[index].setOrigin(MeteorBrown_Big1_TEXTURE_WIDTH/2, MeteorBrown_Big1_TEXTURE_HEIGHT/2);
                asteroidSprite[index].setCenter(SpawnPosition().x, SpawnPosition().y);

                break;

            case GREY_MEDIUM:
                asteroidSprite[index] = new Sprite(MediumGreyAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 64f;
                health[index] = 1.5f;
                rollSpeed[index] = MathUtils.random(-3f,3f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                position[index] = SpawnPosition();

                //asteroidSprite[index].setOrigin(MeteorGrey_Med1_TEXTURE_WIDTH/2, MeteorGrey_Med1_TEXTURE_HEIGHT/2);
                asteroidSprite[index].setCenter(position[index].x, position[index].y);

                break;

            case BROWN_MEDIUM:
                asteroidSprite[index] = new Sprite(MediumBrownAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 64f;
                health[index] = 1.5f;
                rollSpeed[index] = MathUtils.random(-3f,3f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                position[index] = SpawnPosition();

                //asteroidSprite[index].setOrigin(MeteorBrown_Med1_TEXTURE_WIDTH/2, MeteorBrown_Med1_TEXTURE_HEIGHT/2);
                asteroidSprite[index].setCenter(position[index].x, position[index].y);

                break;

            case BROWN_SMALL:
                asteroidSprite[index] = new Sprite(SmallBrownAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 96f;
                health[index] = 0.75f;
                rollSpeed[index] = MathUtils.random(-4f,4f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                position[index] = SpawnPosition();

                //asteroidSprite[index].setOrigin(MeteorBrown_Small1_TEXTURE_WIDTH/2, MeteorBrown_Small1_TEXTURE_HEIGHT/2);
                asteroidSprite[index].setCenter(position[index].x, position[index].y);

                break;

            case GREY_SMALL:
                asteroidSprite[index] = new Sprite(SmallGreyAstTexture);
                if (t == TYPE.NONE) {
                    return -1;
                }
                speed[index] = 96f;
                health[index] = 0f;
                rollSpeed[index] = MathUtils.random(-4f,4f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));

                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];

                position[index] = SpawnPosition();

                //asteroidSprite[index].setOrigin(MeteorGrey_Small1_TEXTURE_WIDTH/2, MeteorGrey_Small1_TEXTURE_HEIGHT/2);
                asteroidSprite[index].setCenter(position[index].x, position[index].y);
                break;

            case NONE:
            default:
                break;

        }
        return index;
    }



    public void update(float dt){

        //all asteroids movement
        for (int index = 0; index < Asteroids_Max; index ++) {
            if (type[index] != TYPE.NONE) {

                position[index].x = direction[index].x *dt;
                position[index].y = direction[index].y *dt;
                radians[index] += rollSpeed[index] * dt;
                asteroidSprite[index].translate(position[index].x, position[index].y);
                asteroidSprite[index].setRotation( radians[index] * MathUtils.radiansToDegrees);

                switch (type[index]){
                    case BROWN_LARGE:
                        width = MeteorBrown_Big1_TEXTURE_WIDTH;
                        height = MeteorBrown_Big1_TEXTURE_HEIGHT;
                        Astcollider[index] = LargeBrownAstCollider;
                        break;
                    case GREY_LARGE:
                        width = MeteorGrey_Big1_TEXTURE_WIDTH;
                        height = MeteorGrey_Big1_TEXTURE_HEIGHT;
                        Astcollider[index] = LargeGreyAstCollider;
                        break;
                    case BROWN_MEDIUM:
                        width = MeteorBrown_Med1_TEXTURE_WIDTH;
                        height = MeteorBrown_Med1_TEXTURE_HEIGHT;
                        Astcollider[index] = MediumBrownAstCollider;
                        break;
                    case GREY_MEDIUM:
                        width = MeteorGrey_Med1_TEXTURE_WIDTH;
                        height = MeteorGrey_Med1_TEXTURE_HEIGHT;
                        Astcollider[index] = MediumGreyAstCollider;
                        break;
                    case BROWN_SMALL:
                        width = MeteorBrown_Small1_TEXTURE_WIDTH;
                        height = MeteorBrown_Small1_TEXTURE_HEIGHT;
                        Astcollider[index] = SmallBrownAstCollider;
                        break;
                    case GREY_SMALL:
                        width = MeteorGrey_Small1_TEXTURE_WIDTH;
                        height = MeteorGrey_Small1_TEXTURE_HEIGHT;
                        Astcollider[index] = SmallGreyAstCollider;
                        break;
                }

                Astcollider[index].setOrigin(width / 2, height / 2);
                Astcollider[index].setPosition(asteroidSprite[index].getX(), asteroidSprite[index].getY());
                Astcollider[index].setRotation( radians[index] * MathUtils.radiansToDegrees);

                //Check collider with wall
                Walls walls = playScreen.getWalls();
                int wallIndex = -1;
                for (Rectangle wall : playScreen.getWalls().colliders) {
                    Polygon polygonWall = new Polygon(new float[] { 0, 0, wall.getWidth(), 0,
                        wall.getWidth(), wall.getHeight(), 0, wall.getHeight() });
                    polygonWall.setPosition(wall.x, wall.y);
                    wallIndex++;
                    if (Intersector.overlapConvexPolygons(polygonWall, Astcollider[index])) {
                        //Collider with top and bot
                        if (wallIndex == walls.TOP_WALL || wallIndex == walls.BOTTOM_WALL) {
                            direction[index].y = -direction[index].y;
                        }
                        //Collider with left and right
                        if (wallIndex == walls.LEFT_WALL || wallIndex == walls.RIGHT_WALL) {
                            direction[index].x = - direction[index].x;
                        }
                    }
                }



                //Collision with player
                Player player = playScreen.getPlayer();
                if (Intersector.overlapConvexPolygons(Astcollider[index], player.playerBounds)) {
                    animationPosition[index].set(Astcollider[index].getX(), Astcollider[index].getY());
                    animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.SMALL_ASTEROID_EXPLOSION);
                    player.playerState = Player.PlayerState.DESTROYED;
                    split(index);

                }
            }
        }
    }

    private Vector2 SpawnPosition() {
        float x = MathUtils.random(MeteorBrown_Big1_TEXTURE_WIDTH , SpaceStationBlaster.MAP_WIDTH - MeteorBrown_Big1_TEXTURE_WIDTH );
        //Make sure spawn position x is not too close with player
        while (x > SpaceStationBlaster.MAP_WIDTH/2  - MeteorBrown_Big1_TEXTURE_WIDTH
                && x < SpaceStationBlaster.MAP_WIDTH/2  + MeteorBrown_Big1_TEXTURE_WIDTH)
        {
            x = MathUtils.random(MeteorBrown_Big1_TEXTURE_WIDTH , SpaceStationBlaster.MAP_WIDTH - MeteorBrown_Big1_TEXTURE_WIDTH );
        }

        float y = MathUtils.random(MeteorBrown_Big1_TEXTURE_HEIGHT , SpaceStationBlaster.MAP_HEIGHT - MeteorBrown_Big1_TEXTURE_HEIGHT );
        //Make sure spawn position y is not too close with player
        while (y > SpaceStationBlaster.MAP_HEIGHT/2   - MeteorBrown_Big1_TEXTURE_HEIGHT
                && y < SpaceStationBlaster.MAP_HEIGHT/2   + MeteorBrown_Big1_TEXTURE_HEIGHT)
        {
            y = MathUtils.random(MeteorBrown_Big1_TEXTURE_HEIGHT , SpaceStationBlaster.MAP_HEIGHT - MeteorBrown_Big1_TEXTURE_HEIGHT );
        }

        return new Vector2(x, y);
    }


    public void split(int index){
        int i = -1;
        //brown large will split into 2 brown mediums
        if (type[index] == TYPE.BROWN_LARGE){
            i = spawn(TYPE.BROWN_MEDIUM);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            i = spawn(TYPE.BROWN_MEDIUM);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        //grey large will split into 2 grey mediums
        if (type[index] == TYPE.GREY_LARGE){
            i = spawn(TYPE.GREY_MEDIUM);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            i = spawn(TYPE.GREY_MEDIUM);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        //brown medium will split into 2 brown smallers
        if (type[index] == TYPE.BROWN_MEDIUM){
            i = spawn(TYPE.BROWN_SMALL);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            i = spawn(TYPE.BROWN_SMALL);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        //grey medium will split into 2 grey smallers
        if (type[index] == TYPE.GREY_MEDIUM){
            i = spawn(TYPE.GREY_SMALL);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            i = spawn(TYPE.GREY_SMALL);
            asteroidSprite[i].setPosition(asteroidSprite[index].getX(),asteroidSprite[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        // brown small will disappear
        if (type[index] == TYPE.BROWN_SMALL){
            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        // grey small will disappear
        if (type[index] == TYPE.GREY_SMALL){
            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }

    }


    public void render(SpriteBatch batch, float dt) {
        for (int i=0; i<Asteroids_Max; i++) {
            if (type[i] != TYPE.NONE) {

                asteroidSprite[i].draw(batch);
            }

            if (animations[i] != null) {
                if (!animations[i].isAnimationFinished(animationElapsedTime[i])) {
                    currentFrame[i] = (TextureRegion) animations[i].getKeyFrame(animationElapsedTime[i], false);
                    batch.draw(currentFrame[i], animationPosition[i].x - currentFrame[i].getRegionWidth()/2, animationPosition[i].y - currentFrame[i].getRegionHeight()/2,
                            currentFrame[i].getRegionWidth() / 2,
                            currentFrame[i].getRegionHeight() / 2,
                            currentFrame[i].getRegionWidth(),
                            currentFrame[i].getRegionHeight(), 1, 1,
                             radians[i] * MathUtils.radiansToDegrees);
                    animationElapsedTime[i] += dt;
                }
                else {
                    animationElapsedTime[i] = 0f;
                    currentFrame[i] = null;
                    animations[i] = null;
                }
            }
        }

    }

}
