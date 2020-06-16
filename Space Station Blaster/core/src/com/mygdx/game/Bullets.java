package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

import java.util.Random;

import javax.xml.soap.Text;

/**
 * Bullets: used for creating a bullet of a particular type and spawning it into our game world
 */
public class Bullets {

    // constants for green bullet
    private static final String GREEN_BULLET_TEXTURE_ATLAS_REGION = "shot1_asset";
    public static final int GREEN_BULLET_TEXTURE_WIDTH = 32;
    public static final int GREEN_BULLET_TEXTURE_HEIGHT = 32;
    private static final float GREEN_BULLET_SPEED = 500;
    private static final int GREEN_BULLET_COLLIDER_WIDTH = 16;
    private static final int GREEN_BULLET_COLLIDER_HEIGHT = 16;

    // constants for orange bullet
    private static final String ORANGE_BULLET_TEXTURE_ATLAS_REGION = "shot4_asset";
    public static final int ORANGE_BULLET_TEXTURE_WIDTH = 64;
    public static final int ORANGE_BULLET_TEXTURE_HEIGHT = 64;
    private static final float ORANGE_BULLET_SPEED = 300;
    private static final int ORANGE_BULLET_COLLIDER_WIDTH = 16;
    private static final int ORANGE_BULLET_COLLIDER_HEIGHT = 16;

    // constants for purple bullet
    private static final String PURPLE_BULLET_TEXTURE_ATLAS_REGION = "shot6_asset";
    private static final int PURPLE_BULLET_TEXTURE_WIDTH = 128;
    private static final int PURPLE_BULLET_TEXTURE_HEIGHT = 128;
    private static final float PURPLE_BULLET_SPEED = 250;
    private static final int PURPLE_BULLET_COLLIDER_WIDTH = 16;
    private static final int PURPLE_BULLET_COLLIDER_HEIGHT = 16;

    // constants for blue bullet
    private static final String BLUE_BULLET_TEXTURE_ATLAS_REGION = "shot2_asset";
    private static final int BLUE_BULLET_TEXTURE_WIDTH = 64;
    private static final int BLUE_BULLET_TEXTURE_HEIGHT = 64;
    private static final float BLUE_BULLET_SPEED = 300;
    private static final int BLUE_BULLET_COLLIDER_WIDTH = 16;
    private static final int BLUE_BULLET_COLLIDER_HEIGHT = 16;

    public static final int MAX_BULLETS = 120;

    private TextureAtlas textureAtlas;
    // TextureRegion of the bullet frames
    private TextureRegion greenBulletTextureRegion;
    private TextureRegion orangeBulletTextureRegion;
    private TextureRegion purpleBulletTextureRegion;
    private TextureRegion blueBulletTextureRegion;

    public Polygon greenBulletCollider;
    private Polygon orangeBulletCollider;
    private Polygon purpleBulletCollider;
    private Polygon blueBulletCollider;
    public Polygon refCollider;

    public Sprite bulletSprite;

    // bullet entities
    public SpaceStationBlaster.BulletType[] bulletType;
    public Vector2[] position; // bullets current position
    public Vector2[] direction; // direction the bullet is travelling
    float[] radians; // the angle in radians the bullet is
    private float[] lifeTime; // the time the bullet is alive
    private TextureRegion[] currentFrame;
    private Animation[] animations;
    private float[] animationElapsedTime;

    private PlayScreen playScreen;
    private Effects effects;

    /**
     * Bullets constructor: sets up our entity arrays and gets our texure regions and creates
     * colliders for each of them.
     * @param playScreen is the screen that the player spaceship lives in as well as asteroids,
     *                   enemies and the walls
     */
    public Bullets(PlayScreen playScreen) {
        this.playScreen = playScreen;
        effects = playScreen.getEffects();
        textureAtlas = playScreen.getTextureAtlas();
        instantiateEntities(MAX_BULLETS);
        // clear our entities by setting by setting all bulletTypes to NONE.
        for (int index = 0; index < MAX_BULLETS; index++) {
            this.bulletType[index] = SpaceStationBlaster.BulletType.NONE;
            currentFrame[index] = new TextureRegion();
            animationElapsedTime[index] = 0f;
        }

        // get green bullet texture region
        greenBulletTextureRegion = textureAtlas.findRegion(GREEN_BULLET_TEXTURE_ATLAS_REGION);

        greenBulletCollider = new Polygon(new float[]{0, 0, GREEN_BULLET_COLLIDER_WIDTH, 0,
                GREEN_BULLET_COLLIDER_WIDTH, GREEN_BULLET_COLLIDER_HEIGHT, 0,
                GREEN_BULLET_COLLIDER_HEIGHT});

        // get orange bullet texture region
        orangeBulletTextureRegion = textureAtlas.findRegion(ORANGE_BULLET_TEXTURE_ATLAS_REGION);

        orangeBulletCollider = new Polygon(new float[]{0, 0, ORANGE_BULLET_COLLIDER_WIDTH, 0,
                ORANGE_BULLET_COLLIDER_WIDTH, ORANGE_BULLET_COLLIDER_HEIGHT, 0,
                ORANGE_BULLET_COLLIDER_HEIGHT});

        // get purple bullet texture region
        purpleBulletTextureRegion = textureAtlas.findRegion(PURPLE_BULLET_TEXTURE_ATLAS_REGION);

        purpleBulletCollider = new Polygon(new float[]{0, 0, PURPLE_BULLET_COLLIDER_WIDTH, 0,
                PURPLE_BULLET_COLLIDER_WIDTH, PURPLE_BULLET_COLLIDER_HEIGHT, 0,
                PURPLE_BULLET_COLLIDER_HEIGHT});

        // get blue bullet texture region
        blueBulletTextureRegion = textureAtlas.findRegion(BLUE_BULLET_TEXTURE_ATLAS_REGION);

        blueBulletCollider = new Polygon(new float[]{0, 0, BLUE_BULLET_COLLIDER_WIDTH, 0,
                BLUE_BULLET_COLLIDER_WIDTH, BLUE_BULLET_COLLIDER_HEIGHT, 0,
                BLUE_BULLET_COLLIDER_HEIGHT});
    }

    /**
     * instantiateEntities: instiates entities with their maxSize
     * @param maxSize is the maximum size of the entity arrays
     */
    private void instantiateEntities(int maxSize) {
        this.bulletType = new SpaceStationBlaster.BulletType[maxSize];
        position = new Vector2[maxSize];
        direction = new Vector2[maxSize];
        radians = new float[maxSize];
        lifeTime = new float[maxSize];
        currentFrame = new TextureRegion[maxSize];
        animations = new Animation[maxSize];
        animationElapsedTime = new float[maxSize];
    }

    /**
     * spawn: spawns a Bullet with a fixed speed and direction of movement depending on the
     * angle in radians
     * @param bulletType a SpaceStationBlaster.BulletType which represents the type of bullet to
     *                   be spawned
     * @param radians the angle represented in radians
     * @return index of the enitity array containg all the position, direction, radians
     * and lifeTime
     */
    public int spawn(SpaceStationBlaster.BulletType bulletType, float radians) {
        // bulletType should not be null
        if (bulletType == null) return -1;
        // find a free index by looping through from the beginning
        int index = -1;
        for (int free = 0; free < MAX_BULLETS; free++) {
            if (this.bulletType[free] == SpaceStationBlaster.BulletType.NONE) {
                index = free;
                break;
            }
        }

        // return a fail indicator if no free index was found
        if (index < 0) return -1;

        // register the index as in-use
        this.bulletType[index] = bulletType;

        position[index] = new Vector2(0f, 0f);
        direction[index] = new Vector2(0f, 0f);
        this.radians[index] = radians;

        // initialise for different types of bullets
        switch(bulletType) {
            case NONE: {
                break;
            }
            case GREEN: {
                direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2)) * GREEN_BULLET_SPEED;
                direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2)) * GREEN_BULLET_SPEED;
                lifeTime[index] = 2f;
                break;
            }
            case ORANGE: {
                direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2)) * ORANGE_BULLET_SPEED;
                direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2)) * ORANGE_BULLET_SPEED;
                lifeTime[index] = 2f;
                break;
            }
            case PURPLE: {
                direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2)) * PURPLE_BULLET_SPEED;
                direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2)) * PURPLE_BULLET_SPEED;
                lifeTime[index] = 2f;
                break;
            }
            case BLUE: {
                direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2)) * BLUE_BULLET_SPEED;
                direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2)) * BLUE_BULLET_SPEED;
                lifeTime[index] = 2f;
                break;
            }

        }
        return index;
    }

    /**
     * update: updates the Bullet lifetime, position and sets up a Bullet collider. Then sets the
     * collider's origin, position and rotation.
     * Uses: checkWallCollision, checkUFOCollision, checkPlayerCollision, checkAsteroidsCollision
     * @param deltaTime is the time passed since the last frame of animation
     */
    public void update(float deltaTime) {
        for (int index = 0; index < MAX_BULLETS; index++) {
            if (bulletType[index] == SpaceStationBlaster.BulletType.NONE || bulletType[index] == SpaceStationBlaster.BulletType.RESERVED) {
                continue;
            }

            // recycle dead bullets to free their memory for use by new bullets;
            if (lifeTime[index] < 0f && bulletType[index] != SpaceStationBlaster.BulletType.RESERVED) {
                bulletType[index] = SpaceStationBlaster.BulletType.NONE;
                continue;
            }

            lifeTime[index] -= deltaTime;
            position[index].x += direction[index].x * deltaTime;
            position[index].y += direction[index].y * deltaTime;

            int colliderWidth = 0;
            int colliderHeight = 0;
            int textureWidth = 0;
            int textureHeight = 0;

            switch(bulletType[index]) {
                case GREEN: {
                    colliderWidth = GREEN_BULLET_COLLIDER_WIDTH;
                    colliderHeight = GREEN_BULLET_COLLIDER_HEIGHT;
                    textureWidth = GREEN_BULLET_TEXTURE_WIDTH;
                    textureHeight = GREEN_BULLET_TEXTURE_HEIGHT;
                    refCollider = greenBulletCollider;
                    break;
                }
                case ORANGE: {
                    colliderWidth = ORANGE_BULLET_COLLIDER_WIDTH;
                    colliderHeight = ORANGE_BULLET_COLLIDER_HEIGHT;
                    textureWidth = ORANGE_BULLET_TEXTURE_WIDTH;
                    textureHeight = ORANGE_BULLET_TEXTURE_HEIGHT;
                    refCollider = orangeBulletCollider;
                    break;
                }
                case PURPLE: {
                    colliderWidth = PURPLE_BULLET_COLLIDER_WIDTH;
                    colliderHeight = PURPLE_BULLET_COLLIDER_HEIGHT;
                    textureWidth = PURPLE_BULLET_TEXTURE_WIDTH;
                    textureHeight = PURPLE_BULLET_TEXTURE_HEIGHT;
                    refCollider = purpleBulletCollider;
                    break;
                }
                case BLUE: {
                    colliderWidth = BLUE_BULLET_COLLIDER_HEIGHT;
                    colliderHeight = BLUE_BULLET_COLLIDER_HEIGHT;
                    textureWidth = BLUE_BULLET_TEXTURE_WIDTH;
                    textureHeight = BLUE_BULLET_TEXTURE_HEIGHT;
                    refCollider = blueBulletCollider;
                    break;
                }
            }
            refCollider.setOrigin(colliderWidth / 2, colliderHeight / 2);
            refCollider.setPosition(position[index].x + textureWidth / 2 -
                            colliderWidth / 2,
                    position[index].y + textureHeight / 2 -
                            colliderHeight / 2);
            refCollider.setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);

            checkWallCollision(index);
            checkUFOCollision(index);
            checkPlayerCollision(index);
            checkAsteroidsCollision(index);


        }
    }

    /**
     * checks if a Bullet collider collides with a Wall collider and if it does creates an
     * impact animation for that type of bulletType.
     * @param index is the position in the entity arrays
     */
    private void checkWallCollision(int index) {
        for (Rectangle wall : playScreen.getWalls().colliders) {
            Polygon polygonWall = new Polygon(new float[] { 0, 0, wall.getWidth(), 0,
                    wall.getWidth(), wall.getHeight(), 0, wall.getHeight() });
            polygonWall.setPosition(wall.x, wall.y);
            if (Intersector.overlapConvexPolygons(polygonWall, refCollider)) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.IMPACT_SOUND, Sound.class).play();
                switch(bulletType[index]) {
                    case GREEN: {
                        bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_IMPACT);
                        break;
                    }
                    case ORANGE: {
                        bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.ORANGE_IMPACT);
                        break;
                    }
                    case PURPLE: {
                        bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.PURPLE_IMPACT);
                        break;
                    }
                    case BLUE: {
                        bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.BLUE_IMPACT);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Checks collisions between a given bullet and every UFO 
     * @param index The index position of the bullet being checked
     */
    private void checkUFOCollision(int index) {
        Enemies enemies = playScreen.getEnemies();

        if (enemies.spaceStationSpawned()) {
            if (Intersector.overlapConvexPolygons(enemies.spaceStationPolygons[0], refCollider) ||
                    Intersector.overlapConvexPolygons(enemies.spaceStationPolygons[1], refCollider)) {
                if (bulletType[index] == SpaceStationBlaster.BulletType.GREEN) {
                    SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.IMPACT_SOUND, Sound.class).play();
                    bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                    animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_IMPACT);
                    enemies.damageSpaceStation();
                }
            }
        }

        for (int enemyIndex = 0; enemyIndex < Enemies.MAX_ENEMIES; enemyIndex++) {

            if (enemies.overlaps(refCollider, enemies.circleColliders[enemyIndex])) {
                Random random = new Random();
                int randomValue;
                switch(bulletType[index]) {
                    case GREEN: {
                        // update the score for destroying Red and Green UFOs
                        if (enemies.type[enemyIndex] == Enemies.Type.RED_UFO) {
                            playScreen.getGameHud().updateScore(Hud.RED_UFO_POINTS);
                            SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                            if (playScreen.getGameHud().extraShipAwarded()) {
                                playScreen.getGameHud().addShip();
                            }
                            randomValue = random.nextInt(3) + 1;
                            if (randomValue == 1) {
                                playScreen.getPlayer().currentUFOIndex = enemyIndex;
                                playScreen.getPlayer().spawnBulletPowerup();
                            }

                        } else if (enemies.type[enemyIndex] == Enemies.Type.GREEN_UFO) {
                            playScreen.getGameHud().updateScore(Hud.GREEN_UFO_POINTS);
                            SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                            if (playScreen.getGameHud().extraShipAwarded()) {
                                playScreen.getGameHud().addShip();
                            }
                            randomValue = random.nextInt(3) + 1;
                            if (randomValue == 1) {
                                playScreen.getPlayer().currentUFOIndex = enemyIndex;
                                playScreen.getPlayer().spawnShieldPowerup();
                            }

                        }

                        enemies.type[enemyIndex] = Enemies.Type.NONE;
                        enemies.circleColliders[enemyIndex].setPosition(0, 0);
                        enemies.animations[enemyIndex] = effects.getAnimation(SpaceStationBlaster.EffectType.ENEMY_EXPLOSION);

                        bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_IMPACT);
                    }
                    case ORANGE: {
                        bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.ORANGE_IMPACT);
                        break;
                    }
                    case PURPLE: {
                        //bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        //animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.PURPLE_IMPACT);
                        break;
                    }
                    case BLUE: {
                        bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                        animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.BLUE_IMPACT);
                        break;
                    }
                }
            }
        }
    }

    /**
     * checkPlayerCollision: checks if Player collider and collidered with Bullet collider of
     * a particular bulletType. If it has it sets that bulletType to reserved and then creates an
     * impact animation for that bulletType
     * @param index is the position in the entity arrays
     */
    public void checkPlayerCollision(int index) {
        Player player = playScreen.getPlayer();
        if (Intersector.overlapConvexPolygons(refCollider, player.playerBounds)) {
            SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.IMPACT_SOUND, Sound.class).play();
            switch(bulletType[index]) {
                case ORANGE: {
                    playScreen.getGameHud().decreaseShield();

                    bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                    animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.ORANGE_IMPACT);
                    break;
                }
                case PURPLE: {
                    playScreen.getGameHud().decreaseShield();

                    bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                    animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.PURPLE_IMPACT);
                    break;
                }
                case BLUE: {
                    playScreen.getGameHud().decreaseShield();

                    bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                    animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.BLUE_FIRE);
                    break;
                }
            }
        }
    }

    /**
     * checkAsteroidsCollision: use to check collision between all asteroids in game world
     * and current bullets, and events if it occurs.
     * @param index is index of bullet to check collision
     */
    private void checkAsteroidsCollision(int index) {
        Asteroids asteroids = playScreen.getAsteroids();

        for (int asteroidIndex = 0; asteroidIndex < Asteroids.Asteroids_Max; asteroidIndex++) {
                if (Intersector.overlapConvexPolygons(refCollider, asteroids.Astcollider[asteroidIndex])) {
                    SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.ASTEROID_EXPLOSION_SOUND, Sound.class).play();
                    switch (bulletType[index]) {
                        case GREEN:
                            if (asteroids.type[asteroidIndex] == Asteroids.TYPE.GREY_LARGE ||asteroids.type[asteroidIndex] == Asteroids.TYPE.BROWN_LARGE){
                                playScreen.getGameHud().updateScore(Hud.LARGE_ASTEROID_POINTS);
                                if (playScreen.getGameHud().extraShipAwarded()) {
                                    playScreen.getGameHud().addShip();
                                }
                            }
                            else if (asteroids.type[asteroidIndex] == Asteroids.TYPE.GREY_MEDIUM ||asteroids.type[asteroidIndex] == Asteroids.TYPE.BROWN_MEDIUM){
                                playScreen.getGameHud().updateScore(Hud.MEDIUM_ASTEROID_POINTS);
                                if (playScreen.getGameHud().extraShipAwarded()) {
                                    playScreen.getGameHud().addShip();
                                }
                            }
                            else if (asteroids.type[asteroidIndex] == Asteroids.TYPE.GREY_SMALL ||asteroids.type[asteroidIndex] == Asteroids.TYPE.BROWN_SMALL){
                                playScreen.getGameHud().updateScore(Hud.SMALL_ASTEROID_POINTS);
                                if (playScreen.getGameHud().extraShipAwarded()) {
                                    playScreen.getGameHud().addShip();
                                }
                            }

                            bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                            animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_IMPACT);

                            asteroids.split(asteroidIndex);

                            break;
                        case ORANGE:
                            bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                            animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.ORANGE_IMPACT);
                            asteroids.split(asteroidIndex);
                            break;
                        case PURPLE:
                            bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                            animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.PURPLE_IMPACT);
                            asteroids.split(asteroidIndex);
                            break;
                        case BLUE:
                            bulletType[index] = SpaceStationBlaster.BulletType.RESERVED;
                            animations[index] = effects.getAnimation(SpaceStationBlaster.EffectType.BLUE_FIRE);
                            asteroids.split(asteroidIndex);
                            break;
                    }
                }
        }

    }

    /**
     * render: render the Bullet for each bulletType in the entity array that is not NONE and then
     * sets the origin, posistion and rotation and draws the sprite. For BulletTypes that are
     * RESERVED renders the current impact animation frame
     * @param spriteBatch used to draw textures or sprites onto the screen for the current frame
     * @param deltaTime is the time passed since the last frame of animation
     */
    public void render(SpriteBatch spriteBatch, float deltaTime) {
        for (int index = 0; index < MAX_BULLETS; index++) {
            switch(bulletType[index]) {
                case NONE: {
                    continue;
                }
                case GREEN: {
                    bulletSprite = new Sprite(greenBulletTextureRegion);
                    break;
                }
                case ORANGE: {
                    bulletSprite = new Sprite(orangeBulletTextureRegion);
                    break;
                }
                case PURPLE: {
                    bulletSprite = new Sprite(purpleBulletTextureRegion);
                    break;
                }
                case BLUE: {
                    bulletSprite = new Sprite(blueBulletTextureRegion);
                    break;
                }
            }
            bulletSprite.setOrigin(bulletSprite.getWidth() / 2, bulletSprite.getHeight() / 2);
            bulletSprite.setPosition(position[index].x , position[index].y);

            bulletSprite.setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);

            bulletSprite.draw(spriteBatch);


            //Impact Animations
            if (bulletType[index] == SpaceStationBlaster.BulletType.RESERVED) {
                if (!animations[index].isAnimationFinished(animationElapsedTime[index])) {
                    currentFrame[index] = (TextureRegion) animations[index].getKeyFrame(animationElapsedTime[index], false);
                    spriteBatch.draw(currentFrame[index], position[index].x, position[index].y,
                            currentFrame[index].getRegionWidth() / 2,
                            currentFrame[index].getRegionHeight() / 2,
                            currentFrame[index].getRegionWidth(),
                            currentFrame[index].getRegionHeight(), 1, 1,
                            (float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);
                    animationElapsedTime[index] += deltaTime;
                }
                else {
                    bulletType[index] = SpaceStationBlaster.BulletType.NONE;
                    animationElapsedTime[index] = 0f;
                    currentFrame[index] = null;
                    animations[index] = null;
                }
            }
        }
    }
}
