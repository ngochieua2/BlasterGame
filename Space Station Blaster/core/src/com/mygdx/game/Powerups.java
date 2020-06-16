package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Powerups: for setting up the Shield and Bullet powerups and spawning then into the game world
 */
public class Powerups {

    // constants for the bullet powerup
    private static final String BULLET_POWERUP_ATLAS_REGION = "bolt_bronze";
    public static final int BULLET_POWERUP_TEXTURE_WIDTH = 19;
    public static final int BULLET_POWERUP_TEXTURE_HEIGHT = 30;

    // constants for the shield powerup
    private static final String SHIELD_POWERUP_ATLAS_REGION = "shield_bronze";
    public static final int SHIELD_POWERUP_TEXTURE_WIDTH = 19;
    public static final int SHIELD_POWERUP_TEXTURE_HEIGHT = 30;

    private static final int SPEED = 100;

    // the will never be this many powerups. just in case
    private static final int MAX_POWERUPS = 20;

    private TextureAtlas textureAtlas;
    // TextureRegion of the powerup frames
    private TextureRegion bulletPowerupTextureRegion;
    private TextureRegion shieldPowerupTextureRegion;

    public Polygon bulletPowerupCollider;
    private Polygon shieldPowerupCollider;
    public Polygon refCollider;

    public Sprite powerupSprite;

    public SpaceStationBlaster.PowerupType[] powerupType;
    public Vector2[] position; // powerup current position
    public Vector2[] direction; // direction the powerup is travelling
    float[] radians; // the angle in radians the powerup is
    private float[] lifeTime; // the time the bullet is alive

    private PlayScreen playScreen;
    private Walls walls;

    /**
     * Powerups constructor: for setting up entities and textures for shield and bullet powerups
     * @param playScreen is the screen that the player spaceship lives in as well as asteroids,
     *                   enemies and the walls
     */
    public Powerups(PlayScreen playScreen) {
        this.playScreen = playScreen;
        this.walls = playScreen.getWalls();
        this.textureAtlas = playScreen.getTextureAtlas();
        instantiateEntities(MAX_POWERUPS);
        // clear our entities by setting by setting all poweupTypes to NONE.
        for (int index = 0; index < MAX_POWERUPS; index++) {
            this.powerupType[index] = SpaceStationBlaster.PowerupType.NONE;
        }

        // get bullet poweup texture region
        bulletPowerupTextureRegion = textureAtlas.findRegion(BULLET_POWERUP_ATLAS_REGION);

        bulletPowerupCollider = new Polygon(new float[]{0, 0, BULLET_POWERUP_TEXTURE_WIDTH, 0,
                BULLET_POWERUP_TEXTURE_WIDTH, BULLET_POWERUP_TEXTURE_HEIGHT, 0,
                BULLET_POWERUP_TEXTURE_HEIGHT});

        // get shield powerup texture region
        shieldPowerupTextureRegion = textureAtlas.findRegion(SHIELD_POWERUP_ATLAS_REGION);

        shieldPowerupCollider = new Polygon(new float[]{0, 0, SHIELD_POWERUP_TEXTURE_WIDTH, 0,
                SHIELD_POWERUP_TEXTURE_WIDTH, SHIELD_POWERUP_TEXTURE_HEIGHT, 0,
                SHIELD_POWERUP_TEXTURE_HEIGHT});

    }

    /**
     * instantiateEntities: instiates entities with their maxSize
     * @param maxSize is the maximum size of the entity arrays
     */
    private void instantiateEntities(int maxSize) {
        this.powerupType = new SpaceStationBlaster.PowerupType[maxSize];
        position = new Vector2[maxSize];
        direction = new Vector2[maxSize];
        radians = new float[maxSize];
        lifeTime = new float[maxSize];
    }

    /**
     * spawn: spawns a Powerup with a fixed speed and random direction of movement depending on the
     * power up type
     * @param powerupType a SpaceStationBlaster.PowerupType which represents the type of power up
     *                    to be spawned
     * @return index of the enitity array containg all the position, direction, radians
     * and lifeTime
     */
    public int spawn(SpaceStationBlaster.PowerupType powerupType) {
        // bulletType should not be null
        if (powerupType == null) return -1;
        // find a free index by looping through from the beginning
        int index = -1;
        for (int free = 0; free < MAX_POWERUPS; free++) {
            if (this.powerupType[free] == SpaceStationBlaster.PowerupType.NONE) {
                index = free;
                break;
            }
        }

        // return a fail indicator if no free index was found
        if (index < 0) return -1;

        // register the index as in-use
        this.powerupType[index] = powerupType;

        position[index] = new Vector2(0f, 0f);
        direction[index] = new Vector2(0f, 0f);
        this.radians[index] = MathUtils.random((float) (2 * Math.PI));

        // initialise for different types of powerups
        if (powerupType != SpaceStationBlaster.PowerupType.NONE) {
            direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2)) * SPEED;
            direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2)) * SPEED;
            lifeTime[index] = 15f;
        }

        return index;
    }

    /**
     * update: updates the powerup lifetime, position and sets up a Powerup collider. Then sets the
     * collider's origin, position and rotation.
     * Uses: checkWallCollision, checkPlayerCollision
     * @param deltaTime is the time passed since the last frame of animation
     */
    public void update(float deltaTime) {
        for (int index = 0; index < MAX_POWERUPS; index++) {
            if (powerupType[index] == SpaceStationBlaster.PowerupType.NONE) {
                continue;
            }

            // recycle dead powerups to free their memory for use by new powerups;
            if (lifeTime[index] < 0f) {
                powerupType[index] = SpaceStationBlaster.PowerupType.NONE;
                continue;
            }

            lifeTime[index] -= deltaTime;
            position[index].x += direction[index].x * deltaTime;
            position[index].y += direction[index].y * deltaTime;

            int textureWidth = 0;
            int textureHeight = 0;

            switch (powerupType[index]) {
                case BULLET: {
                    textureWidth = BULLET_POWERUP_TEXTURE_WIDTH;
                    textureHeight = BULLET_POWERUP_TEXTURE_HEIGHT;
                    refCollider = bulletPowerupCollider;
                    break;
                }
                case SHIELD: {
                    textureWidth = SHIELD_POWERUP_TEXTURE_WIDTH;
                    textureHeight = SHIELD_POWERUP_TEXTURE_HEIGHT;
                    refCollider = shieldPowerupCollider;
                    break;
                }
            }

            refCollider.setOrigin(textureWidth / 2, textureWidth / 2);
            refCollider.setPosition(position[index].x + textureWidth / 2 -
                            textureWidth / 2,
                    position[index].y + textureHeight / 2 -
                            textureWidth / 2);
            refCollider.setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);

            checkWallCollision(index);
            checkPlayerCollision(index);
        }
    }

    /**
     * checkWallCOllision: checks to see if the Powerup collider has collided with a Wall collider.
     * If it has it makes the Powerup collider bounce of the Wall collider
     * @param index is the position in the entity arrays
     */
    public void checkWallCollision(int index) {
        for (int i = 0; i < walls.colliders.size(); i++) {
            Polygon polygonWall = new Polygon(new float[]{0, 0, walls.colliders.get(i).getWidth(), 0,
                    walls.colliders.get(i).getWidth(), walls.colliders.get(i).getHeight(), 0,
                    walls.colliders.get(i).getHeight()});
            polygonWall.setPosition(walls.colliders.get(i).x, walls.colliders.get(i).y);
            if (Intersector.overlapConvexPolygons(polygonWall, refCollider)) {
                if (i == walls.TOP_WALL || i == walls.BOTTOM_WALL) {
                    direction[index].y = -direction[index].y;
                } else if (i == walls.LEFT_WALL || i == walls.RIGHT_WALL) {
                    direction[index].x = -direction[index].x;
                }
            }
        }
    }

    /**
     * checkPlayerCollision: check to see if the Player collider has collided with the Powerup
     * collider. If it has it removes it form the enitity array and then decreases the bullet
     * cooldown if it is a Bullet Powerup and increases shield if it is a Shield poserup.
     * @param index is the position in the entity arrays
     */
    public void checkPlayerCollision(int index) {
        Player player = playScreen.getPlayer();
        if (Intersector.overlapConvexPolygons(refCollider, player.playerBounds)) {
            switch (powerupType[index]) {
                case BULLET: {
                    SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.POWERUP_SHOOTING_SOUND, Sound.class).play();
                    powerupType[index] = SpaceStationBlaster.PowerupType.NONE;
                    playScreen.getPlayer().decreaseBulletCooldown();
                }
                case SHIELD: {
                    SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.POWERUP_SHIELD_SOUND, Sound.class).play();
                    powerupType[index] = SpaceStationBlaster.PowerupType.NONE;
                    playScreen.getGameHud().increaseShield();
                }
            }
        }
    }

    /**
     * render: sets up the sprites based on the assigned texture region for Shield and Bullet
     * Powerups. Also sets the sprites origin, position, rotation and then draws it to the screen
     * @param spriteBatch used to draw textures or sprites onto the screen for the current frame
     * @param deltaTime is the time passed since the last frame of animation
     */
    public void render(SpriteBatch spriteBatch, float deltaTime) {
        for (int index = 0; index < MAX_POWERUPS; index++) {
            switch (powerupType[index]) {
                case NONE: {
                    continue;
                }
                case BULLET: {
                    powerupSprite = new Sprite(bulletPowerupTextureRegion);
                    break;
                }
                case SHIELD: {
                    powerupSprite = new Sprite(shieldPowerupTextureRegion);
                    break;
                }

            }

            powerupSprite.setOrigin(powerupSprite.getWidth() / 2, powerupSprite.getHeight() / 2);
            powerupSprite.setPosition(position[index].x, position[index].y);

            powerupSprite.setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);

            powerupSprite.draw(spriteBatch);

        }
    }
}
