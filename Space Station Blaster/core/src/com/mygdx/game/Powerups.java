package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.PlayScreen;

public class Powerups {

    // constants for the bullet powerup
    private static final String BULLET_POWERUP_ATLAS_REGION = "bolt_bronze";
    private static final int BULLET_POWERUP_TEXTURE_WIDTH = 19;
    private static final int BULLET_POWERUP_TEXTURE_HEIGHT = 30;
    private static final int BULLET_POWERUP_SPEED = 150;

    // constants for the shield powerup
    private static final String SHIELD_POWERUP_ATLAS_REGION = "shield_bronze";
    private static final int SHIELD_POWERUP_TEXTURE_WIDTH = 19;
    private static final int SHIELD_POWERUP_TEXTURE_HEIGHT = 30;
    private static final int SHIELD_POWERUP_SPEED = 150;

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
    public Vector2[] position; // powerus current position
    public Vector2[] direction; // direction the powerup is travelling
    float[] radians; // the angle in radians the powerup is
    private float[] lifeTime; // the time the bullet is alive

    private PlayScreen playScreen;
    private Walls walls;

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

    private void instantiateEntities(int maxSize) {
        this.powerupType = new SpaceStationBlaster.PowerupType[maxSize];
        position = new Vector2[maxSize];
        direction = new Vector2[maxSize];
        radians = new float[maxSize];
        lifeTime = new float[maxSize];
    }

    public int spawn(SpaceStationBlaster.PowerupType powerupType, float radians) {
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
        this.radians[index] = radians;

        // initialise for different types of powerups
        switch(powerupType) {
            case NONE: {
                break;
            }
            case BULLET: {
                direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2)) * BULLET_POWERUP_SPEED;
                direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2)) * BULLET_POWERUP_SPEED;
                lifeTime[index] = 10f;
                break;
            }
            case SHIELD: {
                direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2)) * BULLET_POWERUP_SPEED;
                direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2)) * BULLET_POWERUP_SPEED;
                lifeTime[index] = 10f;
                break;
            }
        }
        return index;
    }

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
        }
    }

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

    public void checkPlayerCollision(int index) {
        Player player = playScreen.getPlayer();
        if (Intersector.overlapConvexPolygons(refCollider, player.playerBounds)) {
            switch (powerupType[index]) {
                case BULLET: {
                    powerupType[index] = SpaceStationBlaster.PowerupType.NONE;
                    playScreen.getPlayer().decreaseBulletCooldown();
                }
                case SHIELD: {
                    powerupType[index] = SpaceStationBlaster.PowerupType.NONE;
                    playScreen.getGameHud().increaseShield();
                }
            }
        }
    }

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
