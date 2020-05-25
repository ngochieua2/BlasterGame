package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;

import javax.xml.soap.Text;

public class Bullets {
    private enum BulletType { NONE, GREEN, ORANGE, PURPLE, BLUE }

    // constants for green bullet
    private static final String GREEN_BULLET_TEXTURE_ATLAS_REGION = "shot-green";
    private static final int GREEN_BULLET_TEXTURE_X = 1396;
    private static final int GREEN_BULLET_TEXTURE_Y = 0;
    private static final int GREEN_BULLET_TEXTURE_WIDTH = 32;
    private static final int GREEN_BULLET_TEXTURE_HEIGHT = 32;
    private static final int GREEN_BULLET_FRAME = 4;
    private static final int GREEN_MAX_BULLETS = 4;
    private static final float GREEN_BULLET_SPEED = 350;
    private static final int GREEN_BULLET_RECTANGLE_WIDTH = 0;
    private static final int GREEN_BULLET_RECTANGLE_HEIGHT = 0;

    // constants for orange bullet
    private static final String ORANGE_BULLET_TEXTURE_ATLAS_REGION = "shot-orange";
    private static final int ORANGE_BULLET_TEXTURE_X = 768;
    private static final int ORANGE_BULLET_TEXTURE_Y = 104;
    private static final int ORANGE_BULLET_TEXTURE_WIDTH = 64;
    private static final int ORANGE_BULLET_TEXTURE_HEIGHT = 64;
    private static final int ORANGE_BULLET_FRAME = 5;
    private static final int ORANGE_MAX_BULLETS = 2;
    private static final float ORANGE_BULLET_SPEED = 300;
    private static final int ORANGE_BULLET_RECTANGLE_WIDTH = 0;
    private static final int ORANGE_BULLET_RECTANGLE_HEIGHT = 0;

    // constants for purple bullet
    private static final String PURPLE_BULLET_TEXTURE_ATLAS_REGION = "shot-purple";
    private static final int PURPLE_BULLET_TEXTURE_X = 0;
    private static final int PURPLE_BULLET_TEXTURE_Y = 348;
    private static final int PURPLE_BULLET_TEXTURE_WIDTH = 128;
    private static final int PURPLE_BULLET_TEXTURE_HEIGHT = 128;
    private static final int PURPLE_BULLET_FRAME = 4;
    private static final int PURPLE_MAX_BULLETS = 2;
    private static final float PURPLE_BULLET_SPEED = 300;
    private static final int PURPLE_BULLET_RECTANGLE_WIDTH = 0;
    private static final int PURPLE_BULLET_RECTANGLE_HEIGHT = 0;

    // constants for blue bullet
    private static final String BLUE_BULLET_TEXTURE_ATLAS_REGION = "shot-green";
    private static final int BLUE_BULLET_TEXTURE_X = 0;
    private static final int BLUE_BULLET_TEXTURE_Y = 104;
    private static final int BLUE_BULLET_TEXTURE_WIDTH = 64;
    private static final int BLUE_BULLET_TEXTURE_HEIGHT = 64;
    private static final int BLUE_BULLET_FRAME = 6;
    private static final int BLUE_MAX_BULLETS = 2;
    private static final float BLUE_BULLET_SPEED = 300;
    private static final int BLUE_BULLET_RECTANGLE_WIDTH = 0;
    private static final int BLUE_BULLET_RECTANGLE_HEIGHT = 0;

    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    private Polygon collider;
    private TextureRegion bulletTextureRegion;

    Array<TextureRegion> frames;
    private BulletType currentBulletType;
    private int maxBullets;

    // bullet entities
    private BulletType[] bulletType;
    Vector2[] position; // bullets current position
    Vector2[] direction; // direction the bullet is travelling
    float[] radians; // the angle in radians the bullet is
    private float[] lifeTime; // the time the bullet is alive

    public Bullets(PlayScreen playScreen, BulletType bulletType) {
        textureAtlas = playScreen.getTextureAtlas();
        frames = new Array<TextureRegion>();
        currentBulletType = bulletType;
        switch(currentBulletType) {
            case NONE: {
                break;
            }
            case GREEN: {
                maxBullets = GREEN_MAX_BULLETS;
                instantiateEntities(maxBullets);
                // clear our entities by setting by setting all shipTypes to NONE.
                for (int index = 0; index < maxBullets; index++) {
                    this.bulletType[index] = BulletType.NONE;
                }
                textureRegion = textureAtlas.findRegion(GREEN_BULLET_TEXTURE_ATLAS_REGION);

                // get bullet texture region
                bulletTextureRegion = new TextureRegion(textureRegion,
                        GREEN_BULLET_TEXTURE_X + (GREEN_BULLET_FRAME * GREEN_BULLET_TEXTURE_WIDTH),
                        GREEN_BULLET_TEXTURE_Y, GREEN_BULLET_TEXTURE_WIDTH,
                        GREEN_BULLET_TEXTURE_HEIGHT);

                collider = new Polygon(new float[]{0, 0, GREEN_BULLET_TEXTURE_WIDTH, 0,
                        GREEN_BULLET_TEXTURE_WIDTH, GREEN_BULLET_TEXTURE_HEIGHT, 0,
                        GREEN_BULLET_TEXTURE_HEIGHT});
                break;
            }
            case ORANGE: {
                maxBullets = ORANGE_MAX_BULLETS;
                instantiateEntities(maxBullets);
                // clear our entities by setting by setting all shipTypes to NONE.
                for (int index = 0; index < maxBullets; index++) {
                    this.bulletType[index] = BulletType.NONE;
                }
                textureRegion = textureAtlas.findRegion(ORANGE_BULLET_TEXTURE_ATLAS_REGION);

                // get bullet texture region
                bulletTextureRegion = new TextureRegion(textureRegion,
                        ORANGE_BULLET_TEXTURE_X + (ORANGE_BULLET_FRAME * ORANGE_BULLET_TEXTURE_WIDTH),
                        ORANGE_BULLET_TEXTURE_Y, ORANGE_BULLET_TEXTURE_WIDTH,
                        ORANGE_BULLET_TEXTURE_HEIGHT);

                collider = new Polygon(new float[]{0, 0, ORANGE_BULLET_TEXTURE_WIDTH, 0,
                        ORANGE_BULLET_TEXTURE_WIDTH, ORANGE_BULLET_TEXTURE_HEIGHT, 0,
                        ORANGE_BULLET_TEXTURE_HEIGHT});

                break;
            }
            case PURPLE: {
                maxBullets = PURPLE_MAX_BULLETS;
                instantiateEntities(maxBullets);
                // clear our entities by setting by setting all shipTypes to NONE.
                for (int index = 0; index < maxBullets; index++) {
                    this.bulletType[index] = BulletType.NONE;
                }
                textureRegion = textureAtlas.findRegion(PURPLE_BULLET_TEXTURE_ATLAS_REGION);

                // get bullet texture region
                bulletTextureRegion =new TextureRegion(textureRegion,
                        PURPLE_BULLET_TEXTURE_X + (PURPLE_BULLET_FRAME * PURPLE_BULLET_TEXTURE_WIDTH),
                        PURPLE_BULLET_TEXTURE_Y, PURPLE_BULLET_TEXTURE_WIDTH,
                        PURPLE_BULLET_TEXTURE_HEIGHT);

                collider = new Polygon(new float[]{0, 0, PURPLE_BULLET_TEXTURE_WIDTH, 0,
                        PURPLE_BULLET_TEXTURE_WIDTH, PURPLE_BULLET_TEXTURE_HEIGHT, 0,
                        PURPLE_BULLET_TEXTURE_HEIGHT});

                break;
            }
            case BLUE: {
                maxBullets = BLUE_MAX_BULLETS;
                instantiateEntities(maxBullets);
                // clear our entities by setting by setting all shipTypes to NONE.
                for (int index = 0; index < maxBullets; index++) {
                    this.bulletType[index] = BulletType.NONE;
                }
                textureRegion = textureAtlas.findRegion(BLUE_BULLET_TEXTURE_ATLAS_REGION);

                // get bullet texture region
                bulletTextureRegion = new TextureRegion(textureRegion,
                        BLUE_BULLET_TEXTURE_X + (BLUE_BULLET_FRAME * BLUE_BULLET_TEXTURE_WIDTH),
                        BLUE_BULLET_TEXTURE_Y, BLUE_BULLET_TEXTURE_WIDTH,
                        BLUE_BULLET_TEXTURE_HEIGHT);

                collider = new Polygon(new float[]{0, 0, BLUE_BULLET_TEXTURE_WIDTH, 0,
                        BLUE_BULLET_TEXTURE_WIDTH, BLUE_BULLET_TEXTURE_HEIGHT, 0,
                        BLUE_BULLET_TEXTURE_HEIGHT});
                break;
            }
        }
    }

    private void instantiateEntities(int maxSize) {
        this.bulletType = new BulletType[maxSize];
        position = new Vector2[maxSize];
        direction = new Vector2[maxSize];
        radians = new float[maxSize];
        lifeTime = new float[maxSize];
    }

    public int spawn(float radians) {
        //bulletType should not be null
        if (currentBulletType == null) return -1;
        //Find a free index by looping through from the beginning
        int index = -1;
        for (int free = 0; free < maxBullets; free++) {
            if (this.bulletType[free] == BulletType.NONE) {
                index = free;
                break;
            }
        }

        //Return a fail indicator if no free index was found
        if (index < 0) return -1;

        //Register the index as in-use
        bulletType[index] = currentBulletType;
        position[index].x = 0f;
        position[index].y = 0f;
        this.radians[index] = radians;

        // initialise for different types of bullets
        switch(currentBulletType) {
            case NONE: {
                break;
            }
            case GREEN: {
                direction[index].x = MathUtils.cos(radians) * GREEN_BULLET_SPEED;
                direction[index].y = MathUtils.sin(radians) * GREEN_BULLET_SPEED;
                lifeTime[index] = 2f;
                break;
            }
            case ORANGE: {
                //TODO initialisation for Green UFO
                break;
            }
            case PURPLE: {
                //TODO initialisation for Red UFO
                break;
            }
            case BLUE: {
                //TODO initialisation for Battlecruiser
                break;
            }
        }

        return index;
    }

    public void update(float deltaTime) {
        for (int index = 0; index < maxBullets; index++) {
            if (bulletType[index] == BulletType.NONE) {
                continue;
            }

            //Recycle dead bullets to free their memory for use by new bullets;
            if (lifeTime[index] < 0f) {
                bulletType[index] = BulletType.NONE;
                continue;
            }

            lifeTime[index] -= deltaTime;
            position[index].x += direction[index].x * deltaTime;
            position[index].y += direction[index].y * deltaTime;
        }
    }

    public void render(SpriteBatch spriteBatch) {
        for (int index = 0; index < maxBullets; index++) {
            if (this.bulletType[index] == BulletType.NONE) {
                continue;
            }
            spriteBatch.draw(bulletTextureRegion, position[index].x - bulletTextureRegion.getRegionWidth() / 2f,
                    position[index].y - bulletTextureRegion.getRegionHeight() / 2f);

        }
    }
}
