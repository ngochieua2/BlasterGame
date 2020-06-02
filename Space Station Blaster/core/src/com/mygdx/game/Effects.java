package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.Screens.PlayScreen;

import sun.rmi.runtime.Log;

/**
 * Effects:
 * TODO Still barebone. Needs allot more work.
 */
public class Effects {
    Logger log = new Logger("current deltaTime");

    public static final int MAX_EFFECTS = 100;
    public static final float GREEN_FIRE_LIFETIME = 1f/30f;
    public static final float ORANGE_FIRE_LIFETIME = 0.1f;
    public static final float PURPLE_FIRE_LIFETIME = 0.1f;
    public static final float BLUE_FIRE_LIFETIME = 0.1f;
    public static final float GREEN_IMPACT_LIFETIME = 1f/30f;
    public static final float ORANGE_IMPACT_LIFETIME = 0.2f;
    public static final float PURPLE_IMPACT_LIFETIME = 0.2f;
    public static final float BLUE_IMPACT_LIFETIME = 0.2f;
    public static final float TRAIL_LIFETIME = 0.1f;
    public static final float EXPLOSION_LIFETIME = 0.6f;

    // constants for explosion frames
    private static final int PLAYER_EXPLOSION_COL_FRAMES = 8;
    private static final int PLAYER_EXPLOSION_ROW_FRAMES = 8;
    private static final int SMALL_ASTEROID_EXPLOSION_ROW_FRAMES = 8;
    private static final int SMALL_ASTEROID_EXPLOSION_COL_FRAMES = 8;
    private static final int ENEMY_EXPLOSION_ROW_FRAMES = 8;
    private static final int ENEMY_EXPLOSION_COL_FRAMES = 8;

    // constants for fire TextureAtlas
    private static final String GREEN_FIRE_TEXTURE_ATLAS = "shot1";
    private static final String ORANGE_FIRE_TEXTURE_ATLAS = "shot4";
    private static final String PURPLE_FIRE_TEXTURE_ATLAS = "shot6";
    private static final String BLUE_FIRE_TEXTURE_ATLAS = "shot2";

    // constants for impact TextureAtlas
    private static final String GREEN_IMPACT_TEXTURE_ATLAS = "shot1_exp";
    private static final String ORANGE_IMPACT_TEXTURE_ATLAS = "shot4_exp";
    private static final String PURPLE_IMPACT_TEXTURE_ATLAS = "shot6_exp";
    private static final String BLUE_IMPACT_TEXTURE_ATLAS = "shot2_exp";

    // constants for trail TextureAtlas
    private static final String GREEN_TRAIL_TEXTURE_ATLAS = "Ship1_normal_flight";
    private static final String ORANGE_TRAIL_TEXTURE_ATLAS = "Ship4_normal_flight";
    private static final String PURPLE_TRAIL_TEXTURE_ATLAS = "Ship6_normal_flight";
    private static final String BLUE_TRAIL_TEXTURE_ATLAS = "Ship2_normal_flight";

    // constants for explosion sprite sheet
    private static final String PLAYER_EXPLOSION_SPRITE_SHEET = "Explosions2/1.png";
    private static final String SMALL_ASTEROID_EXPLOSION_SPRITE_SHEET = "Explosions2/4.png";
    private static final String ENEMY_EXPLOSION_SPRITE_SHEET = "Explosions2/2.png";

    // fire bullet effect animations
    private Animation greenFireBulletAnimation;
    private Animation orangeFireBulletAnimation;
    private Animation purpleFireBulletAnimation;
    private Animation blueFireBulletAnimation;

    // impact bullet effect animations
    private Animation greenImpactBulletAnimation;
    private Animation orangeImpactBulletAnimation;
    private Animation purpleImpactBulletAnimation;
    private Animation blueImpactBulletAnimation;

    // trail effect animations
    private Animation greenTrailAnimation;
    private Animation orangeTrailAnimation;
    private Animation purpleTrailAnimation;
    private Animation blueTrailAnimation;

    // explosion effect animations
    private Animation playerExplosionAnimation;
    private Animation smallAsteroidExplosionAnimation;
    private Animation enemyExplosionAnimation;

    private Animation refAnimation;

    // explosion sprite sheet textures
    private Texture playerExplosionTexture;
    private Texture smallAsteroidExplosionTexture;
    private Texture enemyExplosionTexture;

    private TextureAtlas textureAtlas;

    public Effects(PlayScreen playScreen) {
        textureAtlas = playScreen.getTextureAtlas();
        greenFireBulletAnimation = createAnimation(textureAtlas, GREEN_FIRE_TEXTURE_ATLAS, GREEN_FIRE_LIFETIME);
        orangeFireBulletAnimation = createAnimation(textureAtlas, ORANGE_FIRE_TEXTURE_ATLAS, ORANGE_FIRE_LIFETIME);
        purpleFireBulletAnimation = createAnimation(textureAtlas, PURPLE_FIRE_TEXTURE_ATLAS, PURPLE_FIRE_LIFETIME);
        blueFireBulletAnimation = createAnimation(textureAtlas, BLUE_FIRE_TEXTURE_ATLAS, BLUE_FIRE_LIFETIME);
        greenImpactBulletAnimation = createAnimation(textureAtlas, GREEN_IMPACT_TEXTURE_ATLAS, GREEN_IMPACT_LIFETIME);
        orangeImpactBulletAnimation = createAnimation(textureAtlas, ORANGE_IMPACT_TEXTURE_ATLAS, ORANGE_IMPACT_LIFETIME);
        purpleImpactBulletAnimation = createAnimation(textureAtlas, PURPLE_IMPACT_TEXTURE_ATLAS, PURPLE_IMPACT_LIFETIME);
        blueImpactBulletAnimation = createAnimation(textureAtlas, BLUE_IMPACT_TEXTURE_ATLAS, BLUE_IMPACT_LIFETIME);
        greenTrailAnimation = createAnimation(textureAtlas, GREEN_TRAIL_TEXTURE_ATLAS, TRAIL_LIFETIME);
        orangeTrailAnimation = createAnimation(textureAtlas, ORANGE_TRAIL_TEXTURE_ATLAS, TRAIL_LIFETIME);
        purpleTrailAnimation = createAnimation(textureAtlas, PURPLE_TRAIL_TEXTURE_ATLAS, TRAIL_LIFETIME);
        blueTrailAnimation = createAnimation(textureAtlas, BLUE_TRAIL_TEXTURE_ATLAS, TRAIL_LIFETIME);

        playerExplosionTexture = new Texture(Gdx.files.internal(PLAYER_EXPLOSION_SPRITE_SHEET));
        smallAsteroidExplosionTexture = new Texture(Gdx.files.internal(
                SMALL_ASTEROID_EXPLOSION_SPRITE_SHEET));
        enemyExplosionTexture = new Texture(Gdx.files.internal(ENEMY_EXPLOSION_SPRITE_SHEET));
        playerExplosionAnimation = createAnimation(playerExplosionTexture, PLAYER_EXPLOSION_COL_FRAMES,
                PLAYER_EXPLOSION_ROW_FRAMES, EXPLOSION_LIFETIME);
        smallAsteroidExplosionAnimation = createAnimation(smallAsteroidExplosionTexture,
                SMALL_ASTEROID_EXPLOSION_COL_FRAMES, SMALL_ASTEROID_EXPLOSION_ROW_FRAMES, EXPLOSION_LIFETIME);
        enemyExplosionAnimation = createAnimation(enemyExplosionTexture, ENEMY_EXPLOSION_COL_FRAMES,
                ENEMY_EXPLOSION_ROW_FRAMES, EXPLOSION_LIFETIME);
    }

    public Animation getAnimation(SpaceStationBlaster.EffectType effectType) {
        switch (effectType) {
            case GREEN_FIRE: {
                refAnimation = greenFireBulletAnimation;
                break;
            }
            case ORANGE_FIRE: {
                refAnimation = orangeFireBulletAnimation;
                break;
            }
            case PURPLE_FIRE: {
                refAnimation = purpleFireBulletAnimation;
                break;
            }
            case BLUE_FIRE: {
                refAnimation = blueFireBulletAnimation;
                break;
            }
            case GREEN_IMPACT: {
                refAnimation = greenImpactBulletAnimation;
                break;
            }
            case ORANGE_IMPACT: {
                refAnimation = orangeImpactBulletAnimation;
                break;
            }
            case PURPLE_IMPACT: {
                refAnimation = purpleImpactBulletAnimation;
                break;
            }
            case BLUE_IMPACT: {
                refAnimation = blueImpactBulletAnimation;
                break;
            }
            case GREEN_TRAIL: {
                refAnimation = greenTrailAnimation;
                break;
            }
            case ORANGE_TRAIL: {
                refAnimation = orangeTrailAnimation;
                break;
            }
            case PURPLE_TRAIL: {
                refAnimation = purpleTrailAnimation;
                break;
            }
            case BLUE_TRAIL: {
                refAnimation = blueTrailAnimation;
                break;
            }
            case PLAYER_EXPLOSION: {
                refAnimation = playerExplosionAnimation;
                break;
            }
            case SMALL_ASTEROID_EXPLOSION: {
                refAnimation = smallAsteroidExplosionAnimation;
                break;
            }
            case ENEMY_EXPLOSION: {
                refAnimation = enemyExplosionAnimation;
                break;
            }
        }
        return refAnimation;
    }

//    public int spawn(SpaceStationBlaster.EffectType effectType, float radians) {
//        // effectType should not be null
//        if (effectType == null) return -1;
//        // find a free index by looping through from the beginning
//        int index = -1;
//        for (int free = 0; free < MAX_EFFECTS; free++) {
//            if (this.effectType[free] == SpaceStationBlaster.EffectType.NONE) {
//                index = free;
//                break;
//            }
//        }
//
//        // return a fail indicator if no free index was found
//        if (index < 0) return -1;
//
//        // register the index as in-use
//        this.effectType[index] = effectType;
//        elapsedTime[index] = 0f;
//        position[index] = new Vector2(0f, 0f);
//        direction[index] = new Vector2(0f, 0f);
//        this.radians[index] = radians;
//
//        switch (effectType) {
//            case GREEN_FIRE: {
//                lifeTime[index] = GREEN_FIRE_LIFETIME;
//                break;
//            }
//            case ORANGE_FIRE: {
//                lifeTime[index] = ORANGE_FIRE_LIFETIME;
//                break;
//            }
//            case PURPLE_FIRE: {
//                lifeTime[index] = PURPLE_FIRE_LIFETIME;
//                break;
//            }
//            case BLUE_FIRE: {
//                lifeTime[index] = BLUE_FIRE_LIFETIME;
//                break;
//            }
//            case GREEN_IMPACT: {
//                lifeTime[index] = GREEN_IMPACT_LIFETIME;
//                break;
//            }
//            case ORANGE_IMPACT: {
//                lifeTime[index] = ORANGE_IMPACT_LIFETIME;
//                break;
//            }
//            case PURPLE_IMPACT: {
//                lifeTime[index] = PURPLE_IMPACT_LIFETIME;
//                break;
//            }
//            case BLUE_IMPACT: {
//                lifeTime[index] = BLUE_IMPACT_LIFETIME;
//                break;
//            }
//            case GREEN_TRAIL:
//            case ORANGE_TRAIL:
//            case PURPLE_TRAIL:
//            case BLUE_TRAIL: {
//                lifeTime[index] = TRAIL_LIFETIME;
//                break;
//            }
//            case PLAYER_EXPLOSION:
//            case SMALL_ASTEROID_EXPLOSION:
//            case ENEMY_EXPLOSION: {
//                lifeTime[index] = EXPLOSION_LIFETIME;
//                break;
//            }
//
//        }
//        direction[index].x = MathUtils.cos((float) (this.radians[index] + Math.PI / 2));
//        direction[index].y = MathUtils.sin((float) (this.radians[index] + Math.PI / 2));
//
//        return index;
//    }

    private Animation createAnimation(TextureAtlas textureAtlas, String regionName,
                                      float frameDuration) {

        Array<TextureAtlas.AtlasRegion> atlasRegions = textureAtlas.findRegions(regionName);
        return new Animation<TextureRegion>(frameDuration, atlasRegions);
    }

    private Animation createAnimation(Texture spriteSheetTexture, int cols,
                                      int rows, float frameDuration) {


        TextureRegion[][] temp = TextureRegion.split(spriteSheetTexture,
                spriteSheetTexture.getWidth() / cols,spriteSheetTexture.getHeight() / rows);

        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = temp[i][j];
            }
        }

        return new Animation(frameDuration, frames);

    }

//    public void update(float deltaTime) {
//        for (int index = 0; index < MAX_EFFECTS; index++) {
//            if (effectType[index] == SpaceStationBlaster.EffectType.NONE) continue;
//            // recycle dead effects to free their memory for use by new effects
//            if (lifeTime[index] < 0f) {
//                effectType[index] = SpaceStationBlaster.EffectType.NONE;
//                continue;
//            }
//            lifeTime[index] -= deltaTime;
//            elapsedTime[index] += deltaTime;
//            Gdx.app.log("elapsedTime[index]: ", Float.toString(elapsedTime[index]));
//            position[index].x += direction[index].x * deltaTime;
//            position[index].y += direction[index].y * deltaTime;
//        }
//    }

//    public void render(SpriteBatch spriteBatch) {
//        for (int index = 0; index < MAX_EFFECTS; index++) {
//            // get current animations
//            TextureRegion currentFrame = null;
//            switch(effectType[index]) {
//                case NONE: {
//                    continue;
//                }
//                case GREEN_FIRE: {
//                    refAnimation = greenFireBulletAnimation;
//                    break;
//                }
//                case ORANGE_FIRE: {
//                    refAnimation = orangeFireBulletAnimation;
//                    break;
//                }
//                case PURPLE_FIRE: {
//                    refAnimation = purpleFireBulletAnimation;
//                    break;
//                }
//                case BLUE_FIRE: {
//                    refAnimation = blueFireBulletAnimation;
//                    break;
//                }
//                case GREEN_IMPACT: {
//                    refAnimation = greenImpactBulletAnimation;
//                    break;
//                }
//                case ORANGE_IMPACT: {
//                    refAnimation = orangeImpactBulletAnimation;
//                    break;
//                }
//                case PURPLE_IMPACT: {
//                    refAnimation = purpleImpactBulletAnimation;
//                    break;
//                }
//                case BLUE_IMPACT: {
//                    refAnimation = blueImpactBulletAnimation;
//                    break;
//                }
//                case GREEN_TRAIL: {
//                    refAnimation = greenTrailAnimation;
//                    break;
//                }
//                case ORANGE_TRAIL: {
//                    refAnimation = orangeTrailAnimation;
//                    break;
//                }
//                case PURPLE_TRAIL: {
//                    refAnimation = purpleTrailAnimation;
//                    break;
//                }
//                case BLUE_TRAIL: {
//                    refAnimation = blueTrailAnimation;
//                    break;
//                }
//                case PLAYER_EXPLOSION: {
//                    refAnimation = playerExplosionAnimation;
//                    break;
//                }
//                case SMALL_ASTEROID_EXPLOSION: {
//                    refAnimation = smallAsteroidExplosionAnimation;
//                    break;
//                }
//                case ENEMY_EXPLOSION: {
//                    refAnimation = enemyExplosionAnimation;
//                    break;
//                }
//            }
//            currentFrame = (TextureRegion) refAnimation.getKeyFrame(elapsedTime[index]);
//            effectSprite = new Sprite(currentFrame);
//            effectSprite.setOrigin(effectSprite.getWidth() / 2, effectSprite.getHeight() / 2);
//            effectSprite.setPosition(position[index].x, position[index].y);
//            effectSprite.setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);
//
//            effectSprite.draw(spriteBatch);
//        }
//    }

    private void dispose() {
        textureAtlas.dispose();
        playerExplosionTexture.dispose();
        smallAsteroidExplosionTexture.dispose();
        enemyExplosionTexture.dispose();
    }
}
