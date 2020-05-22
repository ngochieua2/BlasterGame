package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;

import javax.xml.soap.Text;

public class Bullets<E> {
    private enum BulletType { FIRE, BULLET, IMPACT}
    private enum BulletColour { GREEN, ORANGE, PURPLE, BLUE }

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
    private static final int PURPLE_BULLET_FRAME = 4;
    private static final int PURPLE_IMPACT_BULLET_START_FRAME = 5;
    private static final int PURPLE_IMPACT_BULLET_END_FRAME = 14;
    private static final float PURPLE_FIRE_BULLET_FRAME_DURATION = 0.1f;
    private static final float PURPLE_IMPACT_BULLET_FRAME_DURATION = 0.1f;
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
    private static final int BLUE_BULLET_RECTANGLE_WIDTH = 0;
    private static final int BLUE_BULLET_RECTANGLE_HEIGHT = 0;

    private E collider;
    private BulletType bulletType;
    private BulletColour bulletColour;
    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    private TextureRegion bulletTextureRegion;
    private Animation fireBulletAnimation;
    private Animation impactBulletAnimation;

    public Bullets(PlayScreen playScreen, BulletColour bulletColour) {
        textureAtlas = playScreen.getTextureAtlas();
        this.bulletColour = bulletColour;

        switch(bulletColour) {
            case GREEN: {
                textureRegion = textureAtlas.findRegion(GREEN_BULLET_TEXTURE_ATLAS_REGION);
                Array<TextureRegion> frames = new Array<TextureRegion>();

                // get bullet texture region
                bulletTextureRegion = new TextureRegion(textureRegion, GREEN_BULLET_TEXTURE_X,
                        GREEN_BULLET_TEXTURE_Y, GREEN_BULLET_TEXTURE_WIDTH,
                        GREEN_BULLET_TEXTURE_HEIGHT);

                // get the fire bullet animation frames and add them to fireBulletAnimation
                fireBulletAnimation = createAnimation(frames, textureRegion,
                        GREEN_FIRE_BULLET_START_FRAME, GREEN_FIRE_BULLET_END_FRAME,
                        GREEN_BULLET_TEXTURE_X, GREEN_BULLET_TEXTURE_Y, GREEN_BULLET_TEXTURE_WIDTH,
                        GREEN_BULLET_TEXTURE_HEIGHT, GREEN_FIRE_BULLET_FRAME_DURATION);

                frames.clear();

                // get the impact bullet animation and add them to impactBulletAnimation
                impactBulletAnimation = createAnimation(frames, textureRegion,
                        GREEN_IMPACT_BULLET_START_FRAME, GREEN_IMPACT_BULLET_END_FRAME,
                        GREEN_BULLET_TEXTURE_X, GREEN_BULLET_TEXTURE_Y, GREEN_BULLET_TEXTURE_WIDTH,
                        GREEN_BULLET_TEXTURE_HEIGHT, GREEN_IMPACT_BULLET_FRAME_DURATION);

                frames.clear();
                break;
            }
            case ORANGE: {
                textureRegion = textureAtlas.findRegion(ORANGE_BULLET_TEXTURE_ATLAS_REGION);
                Array<TextureRegion> frames = new Array<TextureRegion>();

                // get bullet texture region
                bulletTextureRegion = new TextureRegion(textureRegion, ORANGE_BULLET_TEXTURE_X,
                        ORANGE_BULLET_TEXTURE_Y, ORANGE_BULLET_TEXTURE_WIDTH,
                        ORANGE_BULLET_TEXTURE_HEIGHT);

                // get the fire bullet animation frames and add them to fireBulletAnimation
                fireBulletAnimation = createAnimation(frames, textureRegion,
                        ORANGE_FIRE_BULLET_START_FRAME, ORANGE_FIRE_BULLET_END_FRAME,
                        ORANGE_BULLET_TEXTURE_X, ORANGE_BULLET_TEXTURE_Y, ORANGE_BULLET_TEXTURE_WIDTH,
                        ORANGE_BULLET_TEXTURE_HEIGHT, ORANGE_FIRE_BULLET_FRAME_DURATION);

                frames.clear();

                // get the impact bullet animation and add them to impactBulletAnimation
                impactBulletAnimation = createAnimation(frames, textureRegion,
                        ORANGE_IMPACT_BULLET_START_FRAME, ORANGE_IMPACT_BULLET_END_FRAME,
                        ORANGE_BULLET_TEXTURE_X, ORANGE_BULLET_TEXTURE_Y, ORANGE_BULLET_TEXTURE_WIDTH,
                        ORANGE_BULLET_TEXTURE_HEIGHT, ORANGE_IMPACT_BULLET_FRAME_DURATION);

                frames.clear();
                break;
            }
            case PURPLE: {
                textureRegion = textureAtlas.findRegion(PURPLE_BULLET_TEXTURE_ATLAS_REGION);
                Array<TextureRegion> frames = new Array<TextureRegion>();

                // get bullet texture region
                bulletTextureRegion = new TextureRegion(textureRegion, PURPLE_BULLET_TEXTURE_X,
                        PURPLE_BULLET_TEXTURE_Y, PURPLE_BULLET_TEXTURE_WIDTH,
                        PURPLE_BULLET_TEXTURE_HEIGHT);

                // get the fire bullet animation frames and add them to fireBulletAnimation
                fireBulletAnimation = createAnimation(frames, textureRegion,
                        PURPLE_FIRE_BULLET_START_FRAME, PURPLE_FIRE_BULLET_END_FRAME,
                        PURPLE_BULLET_TEXTURE_X, PURPLE_BULLET_TEXTURE_Y, PURPLE_BULLET_TEXTURE_WIDTH,
                        PURPLE_BULLET_TEXTURE_HEIGHT, PURPLE_FIRE_BULLET_FRAME_DURATION);

                frames.clear();

                // get the impact bullet animation and add them to impactBulletAnimation
                impactBulletAnimation = createAnimation(frames, textureRegion,
                        PURPLE_IMPACT_BULLET_START_FRAME, PURPLE_IMPACT_BULLET_END_FRAME,
                        PURPLE_BULLET_TEXTURE_X, PURPLE_BULLET_TEXTURE_Y, PURPLE_BULLET_TEXTURE_WIDTH,
                        PURPLE_BULLET_TEXTURE_HEIGHT, PURPLE_IMPACT_BULLET_FRAME_DURATION);

                frames.clear();
                break;
            }
            case BLUE: {
                textureRegion = textureAtlas.findRegion(BLUE_BULLET_TEXTURE_ATLAS_REGION);
                Array<TextureRegion> frames = new Array<TextureRegion>();

                // get bullet texture region
                bulletTextureRegion = new TextureRegion(textureRegion, BLUE_BULLET_TEXTURE_X,
                        BLUE_BULLET_TEXTURE_Y, BLUE_BULLET_TEXTURE_WIDTH,
                        BLUE_BULLET_TEXTURE_HEIGHT);

                // get the fire bullet animation frames and add them to fireBulletAnimation
                fireBulletAnimation = createAnimation(frames, textureRegion,
                        BLUE_FIRE_BULLET_START_FRAME, BLUE_FIRE_BULLET_END_FRAME,
                        BLUE_BULLET_TEXTURE_X, BLUE_BULLET_TEXTURE_Y, BLUE_BULLET_TEXTURE_WIDTH,
                        BLUE_BULLET_TEXTURE_HEIGHT, BLUE_FIRE_BULLET_FRAME_DURATION);

                frames.clear();

                // get the impact bullet animation and add them to impactBulletAnimation
                impactBulletAnimation = createAnimation(frames, textureRegion,
                        BLUE_IMPACT_BULLET_START_FRAME, BLUE_IMPACT_BULLET_END_FRAME,
                        BLUE_BULLET_TEXTURE_X, BLUE_BULLET_TEXTURE_Y, BLUE_BULLET_TEXTURE_WIDTH,
                        BLUE_BULLET_TEXTURE_HEIGHT, BLUE_IMPACT_BULLET_FRAME_DURATION);

                frames.clear();
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

    public void spawn(E collider, float angle) {

    }
}
