package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    private enum EffectsType { NONE, GREEN_FIRE, ORANGE_FIRE, PURPLE_FIRE, BLUE_FIRE, GREEN_IMPACT,
        ORANGE_IMPACT, PURPLE_IMPACT, BLUE_IMPACT, GREEN_TRAIL, ORANGE_TRAIL, PURPLE_TRAIL,
        BLUE_TRAIL, PLAYER_EXPLOSION, SMALL_ASTEROID_EXPLOSION, ENEMY_EXPLOSION };

    public static final int MAX_EFFECTS = 200;
    public static final float TRAIL_LIFETIME = 0.1f;
    public static final float IMPACT_LIFETIME = 0.2f;
    public static final float EXPLOSION_LIFETIME = 0.6f;

    // constants for fire frames
    private static final int GREEN_FIRE_FRAMES = 4;
    private static final int ORANGE_FIRE_FRAMES = 5;
    private static final int PURPLE_FIRE_FRAMES = 4;
    private static final int BLUE_FIRE_FRAMES = 6;

    // constants for impact frames
    private static final int GREEN_IMPACT_FRAMES = 5;
    private static final int ORANGE_IMPACT_FRAMES = 8;
    private static final int PURPLE_IMPACT_FRAMES = 10;
    private static final int BLUE_IMPACT_FRAMES = 5;

    // constants for trail frames
    private static final int GREEN_TRAIL_FRAMES = 4;
    private static final int ORANGE_TRAIL_FRAMES = 4;
    private static final int PURPLE_TRAIL_FRAMES = 4;
    private static final int BLUE_TRAIL_FRAMES = 4;

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
    private static final String PLAYER_EXPLOSION_SPRITE_SHEET = "Explosion2/1.png";
    private static final String SMALL_ASTEROID_EXPLOSION_SPRITE_SHEET = "Explosion2/4.png";
    private static final String ENEMY_EXPLOSION_SPRITE_SHEET = "Explosion2/2.png";

    // effects type data
    // fire bullet textures
    private TextureRegion[] greenFireBulletTextures;
    private TextureRegion[] orangeFireBulletTextures;
    private TextureRegion[] purpleFireBulletTextures;
    private TextureRegion[] blueFireBulletTextures;

    // impact bullet textures
    private TextureRegion[] greenImpactBulletTextures;
    private TextureRegion[] orangeImpactBulletTextures;
    private TextureRegion[] purpleImpactBulletTextures;
    private TextureRegion[] blueImpactBulletTextures;

    // trail ship textures
    private TextureRegion[] greenTrailTextures;
    private TextureRegion[] orangeTrailTextures;
    private TextureRegion[] purpleTrailTextures;
    private TextureRegion[] blueTrailTextures;

    private Animation<TextureRegion> playerExplosionAnimation;
    private Animation<TextureRegion> smallAsteroidExplosionAnimation;
    private Animation<TextureRegion> enemyExplosionAnimation;

    TextureAtlas textureAtlas;
    // effects entity data
    private Effects.EffectsType[] effectsType;
    private Vector2[] position; // bullets current position
    private Vector2[] direction; // direction the bullet is travelling
    private float[] radians; // the angle in radians the bullet is
    private float[] lifeTime; // the time the animation is alive

    public Effects(PlayScreen playScreen) {
        textureAtlas = playScreen.getTextureAtlas();

        // initialise type data
        greenFireBulletTextures = new TextureRegion[GREEN_FIRE_FRAMES];
        orangeFireBulletTextures = new TextureRegion[ORANGE_FIRE_FRAMES];
        purpleFireBulletTextures = new TextureRegion[PURPLE_FIRE_FRAMES];
        blueFireBulletTextures = new TextureRegion[BLUE_FIRE_FRAMES];
        greenImpactBulletTextures = new TextureRegion[GREEN_IMPACT_FRAMES];
        orangeImpactBulletTextures = new TextureRegion[ORANGE_IMPACT_FRAMES];
        purpleImpactBulletTextures = new TextureRegion[PURPLE_IMPACT_FRAMES];
        blueImpactBulletTextures = new TextureRegion[BLUE_IMPACT_FRAMES];
        greenTrailTextures = new TextureRegion[GREEN_TRAIL_FRAMES];
        orangeTrailTextures = new TextureRegion[ORANGE_TRAIL_FRAMES];
        purpleTrailTextures = new TextureRegion[PURPLE_TRAIL_FRAMES];
        blueTrailTextures = new TextureRegion[BLUE_TRAIL_FRAMES];


        instantiateEntities(MAX_EFFECTS);
        for (int index = 0; index < MAX_EFFECTS; index++) {
            effectsType[index] = EffectsType.NONE;
        }

        createFrames(greenFireBulletTextures, GREEN_FIRE_FRAMES, GREEN_FIRE_TEXTURE_ATLAS);
        createFrames(orangeFireBulletTextures, ORANGE_FIRE_FRAMES, ORANGE_FIRE_TEXTURE_ATLAS);
        createFrames(purpleFireBulletTextures, PURPLE_FIRE_FRAMES, PURPLE_FIRE_TEXTURE_ATLAS);
        createFrames(blueFireBulletTextures, BLUE_FIRE_FRAMES, BLUE_FIRE_TEXTURE_ATLAS);
        createFrames(greenImpactBulletTextures, GREEN_IMPACT_FRAMES, GREEN_IMPACT_TEXTURE_ATLAS);
        createFrames(orangeImpactBulletTextures, ORANGE_IMPACT_FRAMES, ORANGE_IMPACT_TEXTURE_ATLAS);
        createFrames(purpleImpactBulletTextures, PURPLE_IMPACT_FRAMES, PURPLE_IMPACT_TEXTURE_ATLAS);
        createFrames(blueImpactBulletTextures, BLUE_IMPACT_FRAMES, BLUE_IMPACT_TEXTURE_ATLAS);
        createFrames(greenTrailTextures, GREEN_TRAIL_FRAMES, GREEN_TRAIL_TEXTURE_ATLAS);
        createFrames(orangeTrailTextures, ORANGE_TRAIL_FRAMES, ORANGE_TRAIL_TEXTURE_ATLAS);
        createFrames(purpleTrailTextures, PURPLE_TRAIL_FRAMES, PURPLE_TRAIL_TEXTURE_ATLAS);
        createFrames(blueTrailTextures, BLUE_TRAIL_FRAMES, BLUE_TRAIL_TEXTURE_ATLAS);

        playerExplosionAnimation = createAnimation(PLAYER_EXPLOSION_SPRITE_SHEET,
                PLAYER_EXPLOSION_COL_FRAMES, PLAYER_EXPLOSION_ROW_FRAMES);
        smallAsteroidExplosionAnimation = createAnimation(SMALL_ASTEROID_EXPLOSION_SPRITE_SHEET,
                SMALL_ASTEROID_EXPLOSION_COL_FRAMES, SMALL_ASTEROID_EXPLOSION_ROW_FRAMES);
        enemyExplosionAnimation = createAnimation(ENEMY_EXPLOSION_SPRITE_SHEET,
                ENEMY_EXPLOSION_COL_FRAMES, ENEMY_EXPLOSION_ROW_FRAMES);
    }

    private void createFrames(TextureRegion[] frames, int frameSize, String regionName) {
        for (int index = 1; index <= frameSize; index++) {
            frames[index - 1] = textureAtlas.findRegion(regionName, index);
        }
    }

    private Animation<TextureRegion> createAnimation(String path, int cols, int rows) {
        Texture spriteSheet = new Texture(Gdx.files.internal(path));
        TextureRegion[][] temp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / cols,spriteSheet.getHeight() / rows);

        TextureRegion[] spriteSheetFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                spriteSheetFrames[index++] = temp[i][j];
            }
        }
        return new Animation<TextureRegion>(0.025f, spriteSheetFrames);
    }

    private void instantiateEntities(int maxSize) {
        effectsType = new EffectsType[maxSize];
        position = new Vector2[maxSize];
        direction = new Vector2[maxSize];
        radians = new float[maxSize];
        lifeTime = new float[maxSize];
    }

    private int findFreeIndex(int maxSize) {
        //Find a free index by looping through from the beginning
        int index = -1;
        for (int free = 0; free < maxSize; free++) {
            if (effectsType[free] == EffectsType.NONE) {
                index = free;
                break;
            }
        }
        return index;
    }
}
