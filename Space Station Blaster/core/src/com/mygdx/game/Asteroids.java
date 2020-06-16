package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
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

/**
 * Asteroids: is used to create the number of asteroid with particular type and spawn them into game world
 */
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


    public static final int Asteroids_Max = 480; // Maximum asteroids can be appeared one time in game world
    private static final int DEF_NUMBER_OF_ASTEROIDS = 20; // default number of asteroids for stage 1

    //TextureRegion of all asteroids
    private TextureRegion LargeBrownAstTexture;
    private TextureRegion LargeGreyAstTexture;
    private TextureRegion MediumBrownAstTexture;
    private TextureRegion MediumGreyAstTexture;
    private TextureRegion SmallBrownAstTexture;
    private TextureRegion SmallGreyAstTexture;

    //Polygon to check collision with other entities: enemies, bullets, player, wall
    public Polygon[] LargeBrownAstCollider;
    public Polygon[] LargeGreyAstCollider;
    public Polygon[] MediumBrownAstCollider;
    public Polygon[] MediumGreyAstCollider;
    public Polygon[] SmallBrownAstCollider;
    public Polygon[] SmallGreyAstCollider;
    public Polygon[] Astcollider;

    // declare all asteroid entities
    private PlayScreen playScreen;
    private Sprite[] asteroidSprite;
    public Vector2[] position;
    public Vector2[] direction;
    public float[] radians;
    private float[] speed;
    private float[] rollSpeed;
    private TextureRegion[] currentFrame;
    private float[] animationElapsedTime;
    public Animation[] animations;
    public Effects effects;
    private Vector2[] animationPosition;
    private int numberOfAsteroids;

    // all types of asteroids
    public enum  TYPE {
        BROWN_LARGE, GREY_LARGE, BROWN_MEDIUM, GREY_MEDIUM, BROWN_SMALL, GREY_SMALL, NONE;
        // pick up random type
        public static TYPE getRandomType() {
            Random random = new Random();
            TYPE rtype;
            rtype = values()[random.nextInt(values().length-1) + 1];
            // Asteroid types when random should not return NONE
            // to make sure the specific number of asteroid will appear into game world
            while (rtype == NONE){
                rtype = values()[random.nextInt(values().length-1) + 1];
            }
            return rtype;
        }
    };
    public TYPE[] type;
    private int width = 0;
    private int height = 0;

    /**
     * Asteroids constructor: set up, gets texture regions, creates colliders for each type of them,
     * get the specific number of asteroid for current stage and spawn them through for loop
     * @param playScreen is where the player spaceship lives with asteroids, enemies and the walls
     *
     */
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

        // call init method
        init(Asteroids_Max);

        // set number of asteroids based on the current stage number
        if (playScreen.getGameHud().stageNumber > 6) {
            numberOfAsteroids = DEF_NUMBER_OF_ASTEROIDS * 6;
        } else {
            numberOfAsteroids = DEF_NUMBER_OF_ASTEROIDS * playScreen.getGameHud().stageNumber;
        }
        //Spawns number of asteroids when starting
        for (int i = 0; i < numberOfAsteroids; i++){
            spawn(TYPE.getRandomType());
        }

    }

    /**
     * init: Initialises all asteroid entities variables with their array size
     */
    public void init(int size ){
        asteroidSprite = new Sprite[size];
        type = new TYPE[size];
        position = new  Vector2[size];
        direction = new Vector2[size];
        radians = new float[size];
        speed = new float[size];
        Astcollider = new Polygon[size];
        rollSpeed = new  float[size];
        animationElapsedTime = new float[size];
        currentFrame = new TextureRegion[size];
        animations = new Animation[size];
        animationPosition = new Vector2[size];
        LargeBrownAstCollider = new Polygon[Asteroids_Max];
        LargeGreyAstCollider = new Polygon[Asteroids_Max];
        MediumBrownAstCollider = new Polygon[Asteroids_Max];
        MediumGreyAstCollider = new Polygon[Asteroids_Max];
        SmallBrownAstCollider = new Polygon[Asteroids_Max];
        SmallGreyAstCollider = new Polygon[Asteroids_Max];

        for (int i = 0; i < size; i++){
            type[i] = TYPE.NONE;
            position[i] = new Vector2();
            direction[i] = new Vector2();
            animationElapsedTime[i] = 0f;
            currentFrame[i] = new TextureRegion();
            radians[i] = 0f;
            Astcollider[i]= new Polygon(new float[]{0,0,0,0,0,0,0,0});
            animationPosition[i] = new Vector2(0f,0f);
            LargeBrownAstCollider[i] = new Polygon(new float[]{0, 0, MeteorBrown_Big1_TEXTURE_WIDTH, 0,
                    MeteorBrown_Big1_TEXTURE_WIDTH, MeteorBrown_Big1_TEXTURE_HEIGHT,
                    0, MeteorBrown_Big1_TEXTURE_HEIGHT});
            LargeGreyAstCollider[i] = new Polygon(new float[]{0, 0, MeteorGrey_Big1_TEXTURE_WIDTH, 0,
                    MeteorGrey_Big1_TEXTURE_WIDTH, MeteorGrey_Big1_TEXTURE_HEIGHT,
                    0, MeteorGrey_Big1_TEXTURE_HEIGHT});
            MediumBrownAstCollider[i] = new Polygon(new float[]{0, 0, MeteorBrown_Med1_TEXTURE_WIDTH, 0,
                    MeteorBrown_Med1_TEXTURE_WIDTH, MeteorBrown_Med1_TEXTURE_HEIGHT,
                    0, MeteorBrown_Med1_TEXTURE_HEIGHT});
            MediumGreyAstCollider[i] = new Polygon(new float[]{0, 0, MeteorGrey_Med1_TEXTURE_WIDTH, 0,
                    MeteorGrey_Med1_TEXTURE_WIDTH, MeteorGrey_Med1_TEXTURE_HEIGHT,
                    0, MeteorGrey_Med1_TEXTURE_HEIGHT});
            SmallBrownAstCollider[i] = new Polygon(new float[]{0, 0, MeteorBrown_Small1_TEXTURE_WIDTH, 0,
                    MeteorBrown_Small1_TEXTURE_WIDTH, MeteorBrown_Small1_TEXTURE_HEIGHT,
                    0, MeteorBrown_Small1_TEXTURE_HEIGHT});
            SmallGreyAstCollider[i] = new Polygon(new float[]{0, 0, MeteorGrey_Small1_TEXTURE_WIDTH, 0,
                    MeteorGrey_Small1_TEXTURE_WIDTH, MeteorGrey_Small1_TEXTURE_HEIGHT,
                    0, MeteorGrey_Small1_TEXTURE_HEIGHT});
        }
    }

    /**
     * spawn: spawns a asteroid with random position, direction of movement depending on the
     * angle in radians and speed depending on asteroid type
     * @param t is the type of spawned asteroid
     * @return index of current asteroid, which can used to access position, radian, direction,
     * rollspeed, speed, sprite and asteroid collider
     */

    public int spawn(TYPE t){
        // Check null
        if (t == TYPE.NONE) return -1;
        // Select index of empty array
        int index = -1;
        for (int i = 0; i < Asteroids_Max; i++) {
            if (type[i] == TYPE.NONE) {
                index = i;
                break;
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

                speed[index] = 32f;
                // get random roll speed and radian
                rollSpeed[index] = MathUtils.random(-2f,2f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));
                // calculate direction depending on current angle of radian
                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];
                // get spawn position
                position[index] = spawnPosition();

                break;

            case BROWN_LARGE:
                asteroidSprite[index] = new Sprite(LargeBrownAstTexture);

                speed[index] = 32f;
                // get random roll speed and radian
                rollSpeed[index] = MathUtils.random(-2f,2f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));
                // calculate direction depending on current angle of radian
                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];
                // get spawn position
                position[index] = spawnPosition();

                break;

            case GREY_MEDIUM:
                asteroidSprite[index] = new Sprite(MediumGreyAstTexture);

                speed[index] = 64f;
                // get random roll speed and radian
                rollSpeed[index] = MathUtils.random(-3f,3f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));
                // calculate direction depending on current angle of radian
                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];
                // get spawn position
                position[index] = spawnPosition();

                break;

            case BROWN_MEDIUM:
                asteroidSprite[index] = new Sprite(MediumBrownAstTexture);

                speed[index] = 64f;
                // get random roll speed and radian
                rollSpeed[index] = MathUtils.random(-3f,3f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));
                // calculate direction depending on current angle of radian
                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];
                // get spawn position
                position[index] = spawnPosition();

                break;

            case BROWN_SMALL:
                asteroidSprite[index] = new Sprite(SmallBrownAstTexture);

                speed[index] = 96f;
                // get random roll speed and radian
                rollSpeed[index] = MathUtils.random(-4f,4f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));
                // calculate direction depending on current angle of radian
                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];
                // get spawn position
                position[index] = spawnPosition();

                break;

            case GREY_SMALL:
                asteroidSprite[index] = new Sprite(SmallGreyAstTexture);

                speed[index] = 96f;
                // get random roll speed and radian
                rollSpeed[index] = MathUtils.random(-4f,4f);
                radians[index] = MathUtils.random((float) (2 * Math.PI));
                // calculate direction depending on current angle of radian
                direction[index].x = MathUtils.cos(radians[index]) * speed[index];
                direction[index].y = MathUtils.sin(radians[index]) * speed[index];
                // get spawn position
                position[index] = spawnPosition();

                break;

            case NONE:
            default:
                break;

        }
        return index;
    }

    /**
     * update: updates new position and sets up a asteroid collider depending on type.
     * Next sets the collider's origin, position and rotation.
     * Then, using this collider to check collision between asteroid with player, walls and UFO.
     * @param dt is the time passed since the last frame
     */
    public void update(float dt){

        //all asteroids movement
        for (int index = 0; index < Asteroids_Max; index ++) {

            if (type[index] != TYPE.NONE) {

                position[index].x += direction[index].x *dt;
                position[index].y += direction[index].y *dt;
                radians[index] += rollSpeed[index] * dt;
                // set collider depending on type
                switch (type[index]){
                    case BROWN_LARGE:
                        width = MeteorBrown_Big1_TEXTURE_WIDTH;
                        height = MeteorBrown_Big1_TEXTURE_HEIGHT;
                        Astcollider[index] = LargeBrownAstCollider[index];
                        break;
                    case GREY_LARGE:
                        width = MeteorGrey_Big1_TEXTURE_WIDTH;
                        height = MeteorGrey_Big1_TEXTURE_HEIGHT;
                        Astcollider[index] = LargeGreyAstCollider[index];
                        break;
                    case BROWN_MEDIUM:
                        width = MeteorBrown_Med1_TEXTURE_WIDTH;
                        height = MeteorBrown_Med1_TEXTURE_HEIGHT;
                        Astcollider[index] = MediumBrownAstCollider[index];
                        break;
                    case GREY_MEDIUM:
                        width = MeteorGrey_Med1_TEXTURE_WIDTH;
                        height = MeteorGrey_Med1_TEXTURE_HEIGHT;
                        Astcollider[index] = MediumGreyAstCollider[index];
                        break;
                    case BROWN_SMALL:
                        width = MeteorBrown_Small1_TEXTURE_WIDTH;
                        height = MeteorBrown_Small1_TEXTURE_HEIGHT;
                        Astcollider[index] = SmallBrownAstCollider[index];
                        break;
                    case GREY_SMALL:
                        width = MeteorGrey_Small1_TEXTURE_WIDTH;
                        height = MeteorGrey_Small1_TEXTURE_HEIGHT;
                        Astcollider[index] = SmallGreyAstCollider[index];
                        break;
                    case NONE:
                        break;
                }

                Astcollider[index].setOrigin(width / 2, height / 2);
                Astcollider[index].setPosition(position[index].x, position[index].y);
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
                        //Bounce off the wall by negating direction;
                        if (wallIndex == walls.TOP_WALL || wallIndex == walls.BOTTOM_WALL) {
                            direction[index].y = -direction[index].y;
                        }
                        //Collider with left and right
                        //Bounce off the wall by negating direction;
                        if (wallIndex == walls.LEFT_WALL || wallIndex == walls.RIGHT_WALL) {
                            direction[index].x = - direction[index].x;
                        }
                    }
                }

                //Collision with player
                // Destroy player ship if it hits any asteroid
                Player player = playScreen.getPlayer();
                if (Intersector.overlapConvexPolygons(Astcollider[index], player.playerBounds)) {
                    SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                    player.playerState = Player.PlayerState.DESTROYED;

                }

                //Collision with UFO
                // Destroy any UFO if it hits any asteroid
                Enemies enemies = playScreen.getEnemies();
                for (int enemyIndex = 0; enemyIndex < Enemies.MAX_ENEMIES; enemyIndex++) {
                    if (enemies.overlaps(Astcollider[index], enemies.circleColliders[enemyIndex])) {
                        SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                        enemies.type[enemyIndex] = Enemies.Type.NONE;
                        enemies.circleColliders[enemyIndex].setPosition(0, 0);
                        enemies.animations[enemyIndex] = effects.getAnimation(SpaceStationBlaster.EffectType.ENEMY_EXPLOSION);
                    }
                }

            }
        }
    }
    /**
     * spawnPosition: Generate a random spawn point for asteroids,
     * make sure spawn position will be in game world and not too close with player spawn position
     * @return Vector2, the initial spawn position of asteroid
     */
    private Vector2 spawnPosition() {
        // Select x in somewhere inside game world
        float x = MathUtils.random(MeteorBrown_Big1_TEXTURE_WIDTH + 50 , SpaceStationBlaster.MAP_WIDTH - MeteorBrown_Big1_TEXTURE_WIDTH - 50);
        //Make sure spawn position x is not too close with player
        while (x > SpaceStationBlaster.MAP_WIDTH/2  - MeteorBrown_Big1_TEXTURE_WIDTH *3
                && x < SpaceStationBlaster.MAP_WIDTH/2  + MeteorBrown_Big1_TEXTURE_WIDTH *3)
        {
            x = MathUtils.random(MeteorBrown_Big1_TEXTURE_WIDTH , SpaceStationBlaster.MAP_WIDTH - MeteorBrown_Big1_TEXTURE_WIDTH );
        }
        // Select y in somewhere inside game world
        float y = MathUtils.random(MeteorBrown_Big1_TEXTURE_HEIGHT + 50 , SpaceStationBlaster.MAP_HEIGHT - MeteorBrown_Big1_TEXTURE_HEIGHT - 50);
        //Make sure spawn position y is not too close with player
        while (y > SpaceStationBlaster.MAP_HEIGHT/2   - MeteorBrown_Big1_TEXTURE_HEIGHT *3
                && y < SpaceStationBlaster.MAP_HEIGHT/2   + MeteorBrown_Big1_TEXTURE_HEIGHT *3)
        {
            y = MathUtils.random(MeteorBrown_Big1_TEXTURE_HEIGHT , SpaceStationBlaster.MAP_HEIGHT - MeteorBrown_Big1_TEXTURE_HEIGHT );
        }

        return new Vector2(x, y);
    }

    /**
     * split: events when collision between asteroid and bullets occurs.
     * Checking collision between them is in Bullets class
     * @param index is the current index of asteroids. Asteroid type will be taken
     *              to decide with event will occur
     */
    public void split(int index){
        int i = -1;
        // brown large will split into 2 brown mediums
        // 2 brown mediums will spawn in the same position of the destroyed brown large
        if (type[index] == TYPE.BROWN_LARGE){
            i = spawn(TYPE.BROWN_MEDIUM);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            i = spawn(TYPE.BROWN_MEDIUM);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        // grey large will split into 2 grey mediums
        // 2 grey mediums will spawn in the same position of the destroyed grey large
        if (type[index] == TYPE.GREY_LARGE){
            i = spawn(TYPE.GREY_MEDIUM);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            i = spawn(TYPE.GREY_MEDIUM);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        // brown medium will split into 2 brown smallers
        // 2 brown smallers will spawn in the same position of the destroyed brown medium
        if (type[index] == TYPE.BROWN_MEDIUM){
            i = spawn(TYPE.BROWN_SMALL);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            i = spawn(TYPE.BROWN_SMALL);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        // grey medium will split into 2 grey smallers
        // 2 grey smallers will spawn in the same position of the destroyed grey medium
        if (type[index] == TYPE.GREY_MEDIUM){
            i = spawn(TYPE.GREY_SMALL);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            i = spawn(TYPE.GREY_SMALL);
            position[i] = new Vector2(Astcollider[index].getX(),Astcollider[index].getY());

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        // brown small will disappear
        if (type[index] == TYPE.BROWN_SMALL){
            SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.ASTEROID_EXPLOSION_SOUND, Sound.class).play();
            animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.SMALL_ASTEROID_EXPLOSION);
            animationPosition[index].set(Astcollider[index].getX() + MeteorBrown_Small1_TEXTURE_WIDTH/2, Astcollider[index].getY() + MeteorBrown_Small1_TEXTURE_HEIGHT/2);
            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }
        // grey small will disappear
        if (type[index] == TYPE.GREY_SMALL){
            SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.ASTEROID_EXPLOSION_SOUND, Sound.class).play();
            animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.SMALL_ASTEROID_EXPLOSION);
            animationPosition[index].set(Astcollider[index].getX() + MeteorGrey_Small1_TEXTURE_WIDTH/2, Astcollider[index].getY() + MeteorGrey_Small1_TEXTURE_WIDTH/2);

            type[index] = TYPE.NONE;
            Astcollider[index].setPosition(0f, 0f);
        }

    }

    /**
     * render: render all asteroids with their types are not NONE
     * then sets the origin, posistion and rotation and draws the sprite.
     * Animation will also be render through this method
     * @param batch used to draw textures or sprites onto the screen for the current frame
     * @param dt The time passed since the last frame
     */
    public void render(SpriteBatch batch, float dt) {
        for (int i=0; i<Asteroids_Max; i++) {
            if (type[i] != TYPE.NONE) {

                asteroidSprite[i].setOrigin(asteroidSprite[i].getWidth() / 2, asteroidSprite[i].getHeight() / 2);
                asteroidSprite[i].setPosition(position[i].x, position[i].y);
                asteroidSprite[i].setRotation( radians[i] * MathUtils.radiansToDegrees);
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
