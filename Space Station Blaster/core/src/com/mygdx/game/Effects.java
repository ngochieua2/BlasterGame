package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Effects:
 * TODO Still barebone. Needs allot more work.
 */
public class Effects {
    private enum BulletType { NONE, FIRE, BULLET, IMPACT}
    private enum ShipType { NONE, PLAYER, GREEN_UFO, RED_UFO, JUDGEMENT }

    // constants for green bullet
    private static final String GREEN_BULLET_TEXTURE_ATLAS_REGION = "shot-green";
    private static final int GREEN_BULLET_TEXTURE_X = 1396;
    private static final int GREEN_BULLET_TEXTURE_Y = 0;
    private static final int GREEN_BULLET_TEXTURE_WIDTH = 32;
    private static final int GREEN_BULLET_TEXTURE_HEIGHT = 32;
    private static final int GREEN_FIRE_BULLET_START_FRAME = 0;
    private static final int GREEN_FIRE_BULLET_END_FRAME = 3;
    private static final int GREEN_BULLET_FRAME = 4;
    private static final int GREEN_IMPACT_BULLET_START_FRAME = 5;
    private static final int GREEN_IMPACT_BULLET_END_FRAME = 9;
    private static final float GREEN_FIRE_BULLET_FRAME_DURATION = 0.1f;
    private static final float GREEN_IMPACT_BULLET_FRAME_DURATION = 0.1f;
    private static final int GREEN_MAX_BULLETS = 4;
    private static final int GREEN_BULLET_RECTANGLE_WIDTH = 0;
    private static final int GREEN_BULLET_RECTANGLE_HEIGHT = 0;

    // constants for orange bullet
    private static final String ORANGE_BULLET_TEXTURE_ATLAS_REGION = "shot-orange";
    private static final int ORANGE_BULLET_TEXTURE_X = 768;
    private static final int ORANGE_BULLET_TEXTURE_Y = 104;
    private static final int ORANGE_BULLET_TEXTURE_WIDTH = 64;
    private static final int ORANGE_BULLET_TEXTURE_HEIGHT = 64;
    private static final int ORANGE_FIRE_BULLET_START_FRAME = 0;
    private static final int ORANGE_FIRE_BULLET_END_FRAME = 4;
    private static final int ORANGE_BULLET_FRAME = 5;
    private static final int ORANGE_IMPACT_BULLET_START_FRAME = 6;
    private static final int ORANGE_IMPACT_BULLET_END_FRAME = 13;
    private static final float ORANGE_FIRE_BULLET_FRAME_DURATION = 0.1f;
    private static final float ORANGE_IMPACT_BULLET_FRAME_DURATION = 0.1f;
    private static final int ORANGE_MAX_BULLETS = 2;
    private static final int ORANGE_BULLET_RECTANGLE_WIDTH = 0;
    private static final int ORANGE_BULLET_RECTANGLE_HEIGHT = 0;

    // constants for purple bullet
    private static final String PURPLE_BULLET_TEXTURE_ATLAS_REGION = "shot-purple";
    private static final int PURPLE_BULLET_TEXTURE_X = 0;
    private static final int PURPLE_BULLET_TEXTURE_Y = 348;
    private static final int PURPLE_BULLET_TEXTURE_WIDTH = 128;
    private static final int PURPLE_BULLET_TEXTURE_HEIGHT = 128;
    private static final int PURPLE_FIRE_BULLET_START_FRAME = 0;
    private static final int PURPLE_FIRE_BULLET_END_FRAME = 3;
    private static final int PURPLE_BULLET_FRAME = 1;
    private static final int PURPLE_IMPACT_BULLET_START_FRAME = 5;
    private static final int PURPLE_IMPACT_BULLET_END_FRAME = 14;
    private static final float PURPLE_FIRE_BULLET_FRAME_DURATION = 0.1f;
    private static final float PURPLE_IMPACT_BULLET_FRAME_DURATION = 0.1f;
    private static final int PURPLE_MAX_BULLETS = 2;
    private static final int PURPLE_BULLET_RECTANGLE_WIDTH = 0;
    private static final int PURPLE_BULLET_RECTANGLE_HEIGHT = 0;

    // constants for blue bullet
    private static final String BLUE_BULLET_TEXTURE_ATLAS_REGION = "shot-green";
    private static final int BLUE_BULLET_TEXTURE_X = 0;
    private static final int BLUE_BULLET_TEXTURE_Y = 104;
    private static final int BLUE_BULLET_TEXTURE_WIDTH = 64;
    private static final int BLUE_BULLET_TEXTURE_HEIGHT = 64;
    private static final int BLUE_FIRE_BULLET_START_FRAME = 0;
    private static final int BLUE_FIRE_BULLET_END_FRAME = 5;
    private static final int BLUE_BULLET_FRAME = 6;
    private static final int BLUE_IMPACT_BULLET_START_FRAME = 7;
    private static final int BLUE_IMPACT_BULLET_END_FRAME = 11;
    private static final float BLUE_FIRE_BULLET_FRAME_DURATION = 0.1f;
    private static final float BLUE_IMPACT_BULLET_FRAME_DURATION = 0.1f;
    private static final int BLUE_MAX_BULLETS = 2;
    private static final int BLUE_BULLET_RECTANGLE_WIDTH = 0;
    private static final int BLUE_BULLET_RECTANGLE_HEIGHT = 0;

    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;

    Array<TextureRegion> frames;
    private ShipType currentShipType;
    private int maxBullets;

    // bullet entities
    private Effects.BulletType[] bulletType;
    private Effects.ShipType[] shipTypes;
    private Sprite[] sprite;
    private Polygon[] collider;
    private TextureRegion[] bulletTextureRegion;
    private Animation[] fireBulletAnimation;
    private Animation[] impactBulletAnimation;
    Vector2[] position; // bullets current position
    Vector2[] direction; // direction the bullet is travelling
    float[] radians; // the angle in radians the bullet is

    public Effects(PlayScreen playScreen, ShipType shipType) {
        textureAtlas = playScreen.getTextureAtlas();
        frames = new Array<TextureRegion>();
        currentShipType = shipType;
        switch(shipType) {
            case PLAYER: {
                maxBullets = GREEN_MAX_BULLETS;
                instantiateEntities(maxBullets);
                textureRegion = textureAtlas.findRegion(GREEN_BULLET_TEXTURE_ATLAS_REGION);

                for (int index = 0; index < GREEN_MAX_BULLETS; index++) {
                    clearEntities(index);

                    // get the fire bullet animation frames and add them to fireBulletAnimation
                    fireBulletAnimation[index] = createAnimation(frames, textureRegion,
                            GREEN_FIRE_BULLET_START_FRAME, GREEN_FIRE_BULLET_END_FRAME,
                            GREEN_BULLET_TEXTURE_X, GREEN_BULLET_TEXTURE_Y, GREEN_BULLET_TEXTURE_WIDTH,
                            GREEN_BULLET_TEXTURE_HEIGHT, GREEN_FIRE_BULLET_FRAME_DURATION);

                    frames.clear();

                    // get the impact bullet animation and add them to impactBulletAnimation
                    impactBulletAnimation[index] = createAnimation(frames, textureRegion,
                            GREEN_IMPACT_BULLET_START_FRAME, GREEN_IMPACT_BULLET_END_FRAME,
                            GREEN_BULLET_TEXTURE_X, GREEN_BULLET_TEXTURE_Y, GREEN_BULLET_TEXTURE_WIDTH,
                            GREEN_BULLET_TEXTURE_HEIGHT, GREEN_IMPACT_BULLET_FRAME_DURATION);

                    frames.clear();
                }
                break;
            }
            case GREEN_UFO: {
                maxBullets = ORANGE_MAX_BULLETS;
                instantiateEntities(maxBullets);
                textureRegion = textureAtlas.findRegion(ORANGE_BULLET_TEXTURE_ATLAS_REGION);

                for (int index = 0; index < ORANGE_MAX_BULLETS; index++) {
                    clearEntities(index);

                    // get the fire bullet animation frames and add them to fireBulletAnimation
                    fireBulletAnimation[index] = createAnimation(frames, textureRegion,
                            ORANGE_FIRE_BULLET_START_FRAME, ORANGE_FIRE_BULLET_END_FRAME,
                            ORANGE_BULLET_TEXTURE_X, ORANGE_BULLET_TEXTURE_Y, ORANGE_BULLET_TEXTURE_WIDTH,
                            ORANGE_BULLET_TEXTURE_HEIGHT, ORANGE_FIRE_BULLET_FRAME_DURATION);

                    frames.clear();

                    // get the impact bullet animation and add them to impactBulletAnimation
                    impactBulletAnimation[index] = createAnimation(frames, textureRegion,
                            ORANGE_IMPACT_BULLET_START_FRAME, ORANGE_IMPACT_BULLET_END_FRAME,
                            ORANGE_BULLET_TEXTURE_X, ORANGE_BULLET_TEXTURE_Y, ORANGE_BULLET_TEXTURE_WIDTH,
                            ORANGE_BULLET_TEXTURE_HEIGHT, ORANGE_IMPACT_BULLET_FRAME_DURATION);

                    frames.clear();
                }
                break;
            }
            case RED_UFO: {
                maxBullets = PURPLE_MAX_BULLETS;
                instantiateEntities(maxBullets);
                textureRegion = textureAtlas.findRegion(PURPLE_BULLET_TEXTURE_ATLAS_REGION);

                for (int index = 0; index < PURPLE_MAX_BULLETS; index++) {
                    clearEntities(index);

                    // get the fire bullet animation frames and add them to fireBulletAnimation
                    fireBulletAnimation[index] = createAnimation(frames, textureRegion,
                            PURPLE_FIRE_BULLET_START_FRAME, PURPLE_FIRE_BULLET_END_FRAME,
                            PURPLE_BULLET_TEXTURE_X, PURPLE_BULLET_TEXTURE_Y, PURPLE_BULLET_TEXTURE_WIDTH,
                            PURPLE_BULLET_TEXTURE_HEIGHT, PURPLE_FIRE_BULLET_FRAME_DURATION);

                    frames.clear();

                    // get the impact bullet animation and add them to impactBulletAnimation
                    impactBulletAnimation[index] = createAnimation(frames, textureRegion,
                            PURPLE_IMPACT_BULLET_START_FRAME, PURPLE_IMPACT_BULLET_END_FRAME,
                            PURPLE_BULLET_TEXTURE_X, PURPLE_BULLET_TEXTURE_Y, PURPLE_BULLET_TEXTURE_WIDTH,
                            PURPLE_BULLET_TEXTURE_HEIGHT, PURPLE_IMPACT_BULLET_FRAME_DURATION);

                    frames.clear();
                }
                break;
            }
            case JUDGEMENT: {
                maxBullets = BLUE_MAX_BULLETS;
                instantiateEntities(maxBullets);
                textureRegion = textureAtlas.findRegion(BLUE_BULLET_TEXTURE_ATLAS_REGION);

                for (int index = 0; index < PURPLE_MAX_BULLETS; index++) {
                    clearEntities(index);

                    // get the fire bullet animation frames and add them to fireBulletAnimation
                    fireBulletAnimation[index] = createAnimation(frames, textureRegion,
                            BLUE_FIRE_BULLET_START_FRAME, BLUE_FIRE_BULLET_END_FRAME,
                            BLUE_BULLET_TEXTURE_X, BLUE_BULLET_TEXTURE_Y, BLUE_BULLET_TEXTURE_WIDTH,
                            BLUE_BULLET_TEXTURE_HEIGHT, BLUE_FIRE_BULLET_FRAME_DURATION);

                    frames.clear();

                    // get the impact bullet animation and add them to impactBulletAnimation
                    impactBulletAnimation[index] = createAnimation(frames, textureRegion,
                            BLUE_IMPACT_BULLET_START_FRAME, BLUE_IMPACT_BULLET_END_FRAME,
                            BLUE_BULLET_TEXTURE_X, BLUE_BULLET_TEXTURE_Y, BLUE_BULLET_TEXTURE_WIDTH,
                            BLUE_BULLET_TEXTURE_HEIGHT, BLUE_IMPACT_BULLET_FRAME_DURATION);

                    frames.clear();
                }
                break;
            }
        }
    }

    private Animation createAnimation(Array<TextureRegion> frames, TextureRegion texture, int startFrame,
    int endFrame, int textureX, int textureY, int width,
    int height, float frameDuration) {
        for (int index = startFrame; index <= endFrame; index++) {
            frames.add(new TextureRegion(texture, textureX + index * width, textureY,
                    height, height));
        }

        return new Animation(frameDuration, frames);
    }

    private void instantiateEntities(int maxSize) {
        shipTypes = new ShipType[maxSize];
        position = new Vector2[maxSize];
        direction = new Vector2[maxSize];
        radians = new float[maxSize];
    }

    private void clearEntities(int index) {
        shipTypes[index] = ShipType.NONE;
        position[index] = new Vector2();
        direction[index] = new Vector2();
        radians[index] = 0f;
    }

    private int findFreeIndex(int maxSize) {
        //Find a free index by looping through from the beginning
        int index = -1;
        for (int free = 0; free < maxSize; free++) {
            if (bulletType[free] == BulletType.NONE) {
                index = free;
                break;
            }
        }
        return index;
    }
}
