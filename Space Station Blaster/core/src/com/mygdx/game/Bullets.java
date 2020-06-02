package com.mygdx.game;

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
import com.mygdx.game.Screens.PlayScreen;

import javax.xml.soap.Text;

public class Bullets {

    // constants for green bullet
    private static final String GREEN_BULLET_TEXTURE_ATLAS_REGION = "shot1_asset";
    public static final int GREEN_BULLET_TEXTURE_WIDTH = 32;
    public static final int GREEN_BULLET_TEXTURE_HEIGHT = 32;
    private static final float GREEN_BULLET_SPEED = 500;

    // constants for orange bullet
    private static final String ORANGE_BULLET_TEXTURE_ATLAS_REGION = "shot4_asset";
    private static final int ORANGE_BULLET_TEXTURE_WIDTH = 64;
    private static final int ORANGE_BULLET_TEXTURE_HEIGHT = 64;
    private static final float ORANGE_BULLET_SPEED = 300;

    // constants for purple bullet
    private static final String PURPLE_BULLET_TEXTURE_ATLAS_REGION = "shot6_asset";
    private static final int PURPLE_BULLET_TEXTURE_WIDTH = 128;
    private static final int PURPLE_BULLET_TEXTURE_HEIGHT = 128;
    private static final float PURPLE_BULLET_SPEED = 300;

    // constants for blue bullet
    private static final String BLUE_BULLET_TEXTURE_ATLAS_REGION = "shot2_asset";
    private static final int BLUE_BULLET_TEXTURE_WIDTH = 64;
    private static final int BLUE_BULLET_TEXTURE_HEIGHT = 64;
    private static final float BLUE_BULLET_SPEED = 300;

    private static final int MAX_BULLETS = 120;

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
    private SpaceStationBlaster.BulletType[] bulletType;
    public Vector2[] position; // bullets current position
    public Vector2[] direction; // direction the bullet is travelling
    float[] radians; // the angle in radians the bullet is
    private float[] lifeTime; // the time the bullet is alive

    private PlayScreen playScreen;

    public Bullets(PlayScreen playScreen) {
        this.playScreen = playScreen;
        textureAtlas = playScreen.getTextureAtlas();
        instantiateEntities(MAX_BULLETS);
        // clear our entities by setting by setting all shipTypes to NONE.
        for (int index = 0; index < MAX_BULLETS; index++) {
            this.bulletType[index] = SpaceStationBlaster.BulletType.NONE;
        }

        // get bullet texture region
        greenBulletTextureRegion = textureAtlas.findRegion(GREEN_BULLET_TEXTURE_ATLAS_REGION);

        greenBulletCollider = new Polygon(new float[]{0, 0, GREEN_BULLET_TEXTURE_WIDTH, 0,
                GREEN_BULLET_TEXTURE_WIDTH, GREEN_BULLET_TEXTURE_HEIGHT, 0,
                GREEN_BULLET_TEXTURE_HEIGHT});

        // get orange bullet texture region
        orangeBulletTextureRegion = textureAtlas.findRegion(ORANGE_BULLET_TEXTURE_ATLAS_REGION);

        orangeBulletCollider = new Polygon(new float[]{0, 0, ORANGE_BULLET_TEXTURE_WIDTH, 0,
                ORANGE_BULLET_TEXTURE_WIDTH, ORANGE_BULLET_TEXTURE_HEIGHT, 0,
                ORANGE_BULLET_TEXTURE_HEIGHT});

        // get purple bullet texture region
        purpleBulletTextureRegion = textureAtlas.findRegion(PURPLE_BULLET_TEXTURE_ATLAS_REGION);

        purpleBulletCollider = new Polygon(new float[]{0, 0, PURPLE_BULLET_TEXTURE_WIDTH, 0,
                PURPLE_BULLET_TEXTURE_WIDTH, PURPLE_BULLET_TEXTURE_HEIGHT, 0,
                PURPLE_BULLET_TEXTURE_HEIGHT});

        // get blue bullet texture region
        blueBulletTextureRegion = textureAtlas.findRegion(BLUE_BULLET_TEXTURE_ATLAS_REGION);

        blueBulletCollider = new Polygon(new float[]{0, 0, BLUE_BULLET_TEXTURE_WIDTH, 0,
                BLUE_BULLET_TEXTURE_WIDTH, BLUE_BULLET_TEXTURE_HEIGHT, 0,
                BLUE_BULLET_TEXTURE_HEIGHT});
    }

    private void instantiateEntities(int maxSize) {
        this.bulletType = new SpaceStationBlaster.BulletType[maxSize];
        position = new Vector2[maxSize];
        direction = new Vector2[maxSize];
        radians = new float[maxSize];
        lifeTime = new float[maxSize];
    }

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

    public void update(float deltaTime) {
        for (int index = 0; index < MAX_BULLETS; index++) {
            if (bulletType[index] == SpaceStationBlaster.BulletType.NONE) {
                continue;
            }

            // recycle dead bullets to free their memory for use by new bullets;
            if (lifeTime[index] < 0f) {
                bulletType[index] = SpaceStationBlaster.BulletType.NONE;
                continue;
            }

            lifeTime[index] -= deltaTime;
            position[index].x += direction[index].x * deltaTime;
            position[index].y += direction[index].y * deltaTime;

            int width = 0;
            int height = 0;

            switch(bulletType[index]) {
                case GREEN: {
                    width = GREEN_BULLET_TEXTURE_WIDTH;
                    height = GREEN_BULLET_TEXTURE_HEIGHT;
                    refCollider = greenBulletCollider;
                    break;
                }
                case ORANGE: {
                    width = ORANGE_BULLET_TEXTURE_WIDTH;
                    height = ORANGE_BULLET_TEXTURE_HEIGHT;
                    refCollider = orangeBulletCollider;
                    break;
                }
                case PURPLE: {
                    width = PURPLE_BULLET_TEXTURE_WIDTH;
                    height = PURPLE_BULLET_TEXTURE_HEIGHT;
                    refCollider = purpleBulletCollider;
                    break;
                }
                case BLUE: {
                    width = BLUE_BULLET_TEXTURE_HEIGHT;
                    height = BLUE_BULLET_TEXTURE_HEIGHT;
                    refCollider = blueBulletCollider;
                    break;
                }
            }
            refCollider.setOrigin(width / 2, height / 2);
            refCollider.setPosition(position[index].x, position[index].y);
            refCollider.setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);

            checkWallCollision(index);
            checkUFOCollision(index);
        }
    }

    private void checkWallCollision(int index) {
        for (Rectangle wall : playScreen.getWalls().colliders) {
            Polygon polygonWall = new Polygon(new float[] { 0, 0, wall.getWidth(), 0,
                    wall.getWidth(), wall.getHeight(), 0, wall.getHeight() });
            polygonWall.setPosition(wall.x, wall.y);
            if (Intersector.overlapConvexPolygons(polygonWall, refCollider))
            {
                switch(bulletType[index]) {
                    case GREEN: {
                        playScreen.getPlayer().bulletHit = true;
                        playScreen.getPlayer().currentBulletIndex = index;
                        bulletType[index] = SpaceStationBlaster.BulletType.NONE;
                        break;
                    }
                    case ORANGE: {
                        //TODO Clayton
                        break;
                    }
                    case PURPLE: {
                        //TODO Clayton
                        break;
                    }
                    case BLUE: {
                        //TODO Clayton
                        break;
                    }
                }
            }
        }
    }

    private void checkUFOCollision(int index) {
        for (Circle enemy : playScreen.getEnemies().circleColliders) {
            if (playScreen.getEnemies().overlaps(refCollider, enemy)) {
                switch(bulletType[index]) {
                    case GREEN: {
                        playScreen.getPlayer().bulletHit = true;
                        playScreen.getPlayer().currentBulletIndex = index;
                        bulletType[index] = SpaceStationBlaster.BulletType.NONE;
                    }
                    case ORANGE: {
                        //TODO Clayton
                        break;
                    }
                    case PURPLE: {
                        //TODO Clayton
                        break;
                    }
                    case BLUE: {
                        //TODO Clayton
                        break;
                    }
                }
            }
        }
    }

    public void render(SpriteBatch spriteBatch) {
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
            bulletSprite.setPosition(position[index].x, position[index].y);
            bulletSprite.setRotation((float) (radians[index] + Math.PI / 2) * MathUtils.radiansToDegrees);

            bulletSprite.draw(spriteBatch);
        }
    }
}