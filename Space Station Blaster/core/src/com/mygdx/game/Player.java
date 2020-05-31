package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.PlayScreen;

public class Player {
    private enum PlayerState { NORMAL, DESTROYED, }
    public boolean bulletFired;
    public boolean bulletHit;

    private static final float MAX_SPEED = 300; // the maximum speed the player can travel
    private static final float ACCELERATION = 200; // how fast the player can accelerate
    private static final float DECELERATION = 10; // how fast the player can decelerate
    private static final float ROTATION_SPEED = 3; // speed the player can rotate

    public static final String PLAYER_TEXTURE_ATLAS_REGION = "playerShip1_blue";
    public static final String TILED_MAP_PLAYER = "PlayerShip";

    PlayerState playerState;

    private int hp; // players number of hit points left
    private int lives; // players number of lives left
    private int score; // players current score

    Vector2 position; // players current position
    Vector2 direction; // direction the player is travelling
    float radians; // the angle in radians the player is

    private Sprite playerSprite; // hold the texture for the player
    Rectangle playerRectangle; // hold the texture collider

    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    private TiledMap tiledMap;

    public Polygon playerBounds;
    public Bullets bullets;
    private Effects effects;

    public Animation fireAnimation;
    public Vector2 firePosition;
    public Vector2 fireDirection;
    public float fireRadians;
    public TextureRegion fireCurrentFrame;
    public float fireElapsedTime;

    public Animation impactAnimation;
    public Animation impactAnimation2;
    public Vector2 impactPosition;
    public Vector2 impactDirection;
    public float impactRadians;
    public TextureRegion impactCurrentFrame;
    public float impactElapsedTime;
    public float impactElapsedTime2;

    public Animation trailAnimation;
    public Vector2 trailPosition;
    public Vector2 trailDirection;
    public float trailRadians;
    public TextureRegion trailCurrentFrame;

    public float elapsedTime;

    public int currentBulletIndex;

    float shootingCooldown = 0f;
    float shootingCooldownSpeed = 0.5f;

    private PlayScreen playScreen;

    public Player(PlayScreen playScreen) {
        this.playScreen = playScreen;
        this.textureAtlas = playScreen.getTextureAtlas();
        this.tiledMap = playScreen.getTiledMap();
        this.bullets = playScreen.getBullets();
        this.effects = playScreen.getEffects();
        playerState = PlayerState.NORMAL;
        bulletFired = false;
        bulletHit = false;
        position = new Vector2();
        direction = new Vector2();
        radians = 0;
        firePosition = new Vector2();
        fireDirection = new Vector2();
        fireRadians  = 0;
        impactPosition= new Vector2();
        impactDirection = new Vector2();
        impactRadians = 0;
        trailPosition = new Vector2();
        trailDirection = new Vector2();
        trailRadians = 0;

        fireElapsedTime = 0;
        impactElapsedTime = 0;
        elapsedTime = 0;

        playScreen.getTextureAtlas();
        textureRegion = textureAtlas.findRegion(PLAYER_TEXTURE_ATLAS_REGION);
        fireAnimation = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_FIRE);
        trailAnimation = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_TRAIL);
        impactAnimation = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_IMPACT);
        playerSprite = new Sprite(textureRegion);

        Rectangle playerRectangle = tiledMap.getLayers().get(TILED_MAP_PLAYER).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        position.x = playerRectangle.getX();
        position.y = playerRectangle.getY();

        playerBounds = new Polygon(new float[]{0, 0, playerRectangle.getWidth(), 0,
                playerRectangle.getWidth(), playerRectangle.getHeight(), 0,
                playerRectangle.getHeight()});

        playerBounds.setPosition(position.x, position.y);
        playerBounds.setOrigin(playerRectangle.getWidth() / 2, playerRectangle.getHeight() / 2);

        playerSprite.setRegion(playerSprite);
    }

    /* currentOrbitDegrees: the degrees around the orbit that the satellite is(can greater that 360(361 would be equivalent to 1))
     * distanceFromCenterPoint: the distance in world units from the center point that the satellite is
     * centerPoint: the vector of the center point of the orbit system
     */

    /**
     *
     * @param currentOrbitRadians the angle in radians arround the orbit of the satellite is
     * @param distanceFromCenterPoint the distance in world units from the center point that the satellite is
     * @param centerPoint the vector of the center point of the orbit system
     * @return a vector of the calculated orbit position
     */
    public Vector2 calculateOrbit(float currentOrbitRadians, float distanceFromCenterPoint, Vector2 centerPoint) {
        float radians = currentOrbitRadians;

        float x = (float) ((Math.cos(radians) * distanceFromCenterPoint) + centerPoint.x);
        float y = (float) ((Math.sin(radians) * distanceFromCenterPoint) + centerPoint.y);

        return new Vector2(x, y);
    }

    private void handleInput(float deltaTime) {

        // player turning right or left
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            radians -= ROTATION_SPEED * deltaTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            radians += ROTATION_SPEED * deltaTime;
        }

        // player accelerating
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.x += MathUtils.cos((float) (radians + Math.PI / 2)) * ACCELERATION * deltaTime;
            direction.y += MathUtils.sin((float) (radians + Math.PI / 2)) * ACCELERATION * deltaTime;
        }

        // player shooting
        if (shootingCooldown <= 0f && !bulletFired && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            bulletFired = true;

        // spawn the bullet when animation is finished
        } else if (fireAnimation.isAnimationFinished(fireElapsedTime)) {
            currentBulletIndex = bullets.spawn(SpaceStationBlaster.BulletType.GREEN, radians);
            bullets.position[currentBulletIndex].set(firePosition);

            shootingCooldown = shootingCooldownSpeed;
        } else {
            shootingCooldown -= deltaTime;
        }

        // player deceleration
        float speedTravelling = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y); {
            if (speedTravelling < 0) {
                // normalise then multiply by deceleration
                direction.x -= (direction.x / speedTravelling) * DECELERATION * deltaTime;
                direction.y -= (direction.y / speedTravelling) * DECELERATION * deltaTime;
            }
        }

        // cap the speed
        if (speedTravelling > MAX_SPEED) {
            // normalise then multiply by maxSpeed
            direction.x = (direction.x / speedTravelling) * MAX_SPEED;
            direction.y = (direction.y / speedTravelling) * MAX_SPEED;
        }

        // set position
        position.x += direction.x * deltaTime;
        position.y += direction.y * deltaTime;

    }

    public void update(float deltaTime) {
        handleInput(deltaTime);

        // set sprite position and rotation
        playerSprite.setPosition(position.x, position.y);
        playerSprite.setRotation(radians * MathUtils.radiansToDegrees);
        playerBounds.setPosition(position.x, position.y);
        playerBounds.setRotation(radians * MathUtils.radiansToDegrees);

        elapsedTime += deltaTime;

        if (bulletHit) {
            impactRadians = bullets.radians[currentBulletIndex];
            impactDirection.x = bullets.direction[currentBulletIndex].x;
            impactDirection.y = bullets.direction[currentBulletIndex].y;
            // set trailPosition to center of playerSprite
            impactPosition.x = bullets.position[currentBulletIndex].x;
            impactPosition.y = bullets.position[currentBulletIndex].y;
        }

        if (bulletFired) {
            // set the fireDirection
            fireRadians = radians;
            fireDirection.x = MathUtils.cos((float) (radians + Math.PI / 2));
            fireDirection.y = MathUtils.sin((float) (radians + Math.PI / 2));
            // set firePosition to center of playerSprite
            firePosition.x = position.x - Bullets.GREEN_BULLET_TEXTURE_WIDTH / 2 +
                    playerSprite.getWidth() / 2;
            firePosition.y = position.y - Bullets.GREEN_BULLET_TEXTURE_HEIGHT / 2 +
                    playerSprite.getHeight() / 2;

            // calculate firePositions orbit
            firePosition = calculateOrbit((float) (radians + Math.PI / 2),
                    playerSprite.getHeight() /
                            2, firePosition);
        }

        // trail effects
        // set the trailDirection
        trailRadians = radians;
        trailDirection.x = MathUtils.cos((float) (radians + Math.PI / 2));
        trailDirection.y = MathUtils.sin((float) (radians + Math.PI / 2));
        // set trailPosition to center of playerSprite
        trailPosition.x = position.x - Bullets.GREEN_BULLET_TEXTURE_WIDTH / 2 +
                playerSprite.getWidth() / 2;
        trailPosition.y = position.y - Bullets.GREEN_BULLET_TEXTURE_HEIGHT / 2 +
                playerSprite.getHeight() / 2;

        // calculate trailPositions orbit
        trailPosition = calculateOrbit((float) (radians + 3 * Math.PI / 2),
                playerSprite.getHeight() /
                        2 + 12, trailPosition);
    }

    public void render(SpriteBatch spriteBatch) {
        playerSprite.draw(spriteBatch);
    }

    public Sprite getSprite() {
        return playerSprite;
    }

    public int getHp() {
        return hp;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

}
