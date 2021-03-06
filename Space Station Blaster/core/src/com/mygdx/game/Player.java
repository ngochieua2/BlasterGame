package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Scenes.Controller;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Player: the ship which is controlled by the player on both mobile or desktop mode
 */
public class Player {
    public enum PlayerState { NORMAL, DESTROYED, STAGE_COMPLETE}

    private static final float MAX_SPEED = 300; // the maximum speed the player can travel
    private static final float ACCELERATION = 200; // how fast the player can accelerate
    private static final float DECELERATION = 10; // how fast the player can decelerate
    private static final float ROTATION_SPEED = 3; // speed the player can rotate

    public static final float DEF_SHOOTING_COOLDOWN = 0.5f;
    private static final float MIN_SHOOTING_COOLDOWN = 0.125f;
    // cooldown decrease when player gets bullet powerup
    private static final float SHOOTING_COOLDOWN_DECREASE = 0.5f;

    public static final String PLAYER_TEXTURE_ATLAS_REGION = "playerShip1_blue";
    public static final String TILED_MAP_PLAYER = "PlayerShip";

    public boolean bulletFired;
    public boolean bulletHit;
    public boolean spawningBulletPowerup;
    public boolean spawningShieldPowerup;

    public PlayerState playerState;

    public int hp; // players number of hit points left
    public int lives; // players number of lives left
    public int score; // players current score

    public Vector2 position; // players current position
    public Vector2 direction; // direction the player is travelling
    public float radians; // the angle in radians the player is

    public Sprite playerSprite; // hold the texture for the player
    private Rectangle playerRectangle; // hold the texture collider

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

    public Vector2 bulletPowerupPosition;
    public Vector2 bulletPowerupDirection;
    public float bulletPowerupRadians;
    public float bulletPowerupRotationSpeed;

    public Vector2 shieldPowerupPosition;
    public Vector2 shieldPowerupDirection;
    public float shieldPowerupRadians;
    public float shieldPowerupRotationSpeed;

    public Animation explosionAnimation;
    public Vector2 explosionPosition;
    public Vector2 explosionDirection;
    public float explosionRadians;
    public float explosionElapsedTime;
    public TextureRegion explosionCurrentFrame;

    public float elapsedTime;

    public int currentBulletIndex;
    public int currentUFOIndex;

    private float shootingCooldown = 0f;
    public float shootingCooldownSpeed = DEF_SHOOTING_COOLDOWN;

    private Walls walls;
    private Enemies enemies;
    private Asteroids asteroids;

    public Music music;

    private PlayScreen playScreen;

    /**
     * Player constructor: creates the animations for the player fire, impact, trail and explosion.
     * creates a collider based on the player object in Texture map. Sets the position and origin
     * of the collider
     */
    public Player(PlayScreen playScreen) {
        this.playScreen = playScreen;
        this.textureAtlas = playScreen.getTextureAtlas();
        this.tiledMap = playScreen.getTiledMap();
        this.bullets = playScreen.getBullets();
        this.effects = playScreen.getEffects();
        this.walls = playScreen.getWalls();

        // background music
        music = SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.GAME_MUSIC, Music.class);
        music.setLooping(true);
        music.play();

        playerState = PlayerState.NORMAL;
        bulletFired = false;
        bulletHit = false;
        spawningBulletPowerup = false;
        spawningShieldPowerup = false;
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
        explosionPosition = new Vector2();
        explosionDirection = new Vector2();
        explosionRadians = 0;

        bulletPowerupPosition = new Vector2();
        bulletPowerupDirection = new Vector2();
        bulletPowerupRadians = 0;
        bulletPowerupRotationSpeed = 0;

        shieldPowerupPosition = new Vector2();
        shieldPowerupDirection = new Vector2();
        shieldPowerupRadians = 0;
        shieldPowerupRotationSpeed = 0;

        fireElapsedTime = 0;
        impactElapsedTime = 0;
        elapsedTime = 0;

        // create the animations
        playScreen.getTextureAtlas();
        textureRegion = textureAtlas.findRegion(PLAYER_TEXTURE_ATLAS_REGION);
        fireAnimation = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_FIRE);
        trailAnimation = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_TRAIL);
        impactAnimation = effects.getAnimation(SpaceStationBlaster.EffectType.GREEN_IMPACT);
        explosionAnimation = effects.getAnimation(SpaceStationBlaster.EffectType.PLAYER_EXPLOSION);

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

    /**
     * decreaseBulletCooldown: for decreasing the cooldown of the fire which therefore increases
     * the firing rate of the Player
     */
    public void decreaseBulletCooldown() {
        // allow only 4 levels of cooldown (0.5, 0.25, 0.125, 0.0625)
        if (shootingCooldownSpeed >= MIN_SHOOTING_COOLDOWN) {
            shootingCooldownSpeed *= SHOOTING_COOLDOWN_DECREASE;
            playScreen.getGameHud().shootingCooldown = shootingCooldownSpeed;
        }
    }

    /**
     * handleInput: for handling input for both the mobile and desktop modes. Pressing left makes
     * the Player ship rotate left. Pressing right makes the Player ship rotate right, pressing up
     * makes the ship thrust forward the ship is facing. Pressing fire makes the Player ship fire
     * a Bullet
     * @param deltaTime is the time passed since the last frame of animation
     */
    private void handleInput(float deltaTime) {

        // player turning right or left
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playerState == PlayerState.NORMAL) {
                radians -= ROTATION_SPEED * deltaTime;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && playerState == PlayerState.NORMAL) {
                radians += ROTATION_SPEED * deltaTime;
            }
        }
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (playScreen.controller.isRightPressed() && playerState == PlayerState.NORMAL) {
                radians -= ROTATION_SPEED * deltaTime;
            } else if (playScreen.controller.isLeftPressed() && playerState == PlayerState.NORMAL) {
                radians += ROTATION_SPEED * deltaTime;
            }
        }

        // player accelerating
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && playerState == PlayerState.NORMAL) {
                direction.x += MathUtils.cos((float) (radians + Math.PI / 2)) * ACCELERATION * deltaTime;
                direction.y += MathUtils.sin((float) (radians + Math.PI / 2)) * ACCELERATION * deltaTime;
            }
        }
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (playScreen.controller.isUpPressed() && playerState == PlayerState.NORMAL) {
                direction.x += MathUtils.cos((float) (radians + Math.PI / 2)) * ACCELERATION * deltaTime;
                direction.y += MathUtils.sin((float) (radians + Math.PI / 2)) * ACCELERATION * deltaTime;
            }
        }

        // player shooting
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (shootingCooldown <= 0f && !bulletFired && Gdx.input.isKeyPressed(Input.Keys.SPACE)
                    && playerState == PlayerState.NORMAL) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.PLAYER_LASER_SOUND, Sound.class).play(0.3f);
                bulletFired = true;

                // spawn the bullet when animation is finished
            } else if (fireAnimation.isAnimationFinished(fireElapsedTime)) {
                currentBulletIndex = bullets.spawn(SpaceStationBlaster.BulletType.GREEN, radians);
                bullets.position[currentBulletIndex].set(firePosition);

                shootingCooldown = shootingCooldownSpeed;
            } else {
                shootingCooldown -= deltaTime;
            }
        }
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (shootingCooldown <= 0f && !bulletFired && playScreen.controller.isShootPressed()
                    && playerState == PlayerState.NORMAL) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.PLAYER_LASER_SOUND, Sound.class).play();
                bulletFired = true;

                // spawn the bullet when animation is finished
            } else if (fireAnimation.isAnimationFinished(fireElapsedTime)) {
                currentBulletIndex = bullets.spawn(SpaceStationBlaster.BulletType.GREEN, radians);
                bullets.position[currentBulletIndex].set(firePosition);

                shootingCooldown = shootingCooldownSpeed;
            } else {
                shootingCooldown -= deltaTime;
            }

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
        if (playerState == PlayerState.NORMAL) {
            position.x += direction.x * deltaTime;
            position.y += direction.y * deltaTime;
        }

    }

    /**
     * update: sets the sprites position and rotation. If the player state is normal sets the
     * collider to the current position. If the plyers is destroyed or stage complete sets the
     * collider to a position off the game world. If the Player has been hit by a Bullet set the
     * bulletsRadians, bulletDirection and bulletPosistion to be equal to the impactRadians, impactDirection and impactPosition.
     * If the Bullet is fired by the player sets the fireRadians, firePosistion, FireDirection so
     * it can fire from the front of the Player ship. If Player collider collides with Enemy or
     * space station collider sets player state to be destroyed. If player collider collides with
     * Wall collider negate the Player collider posisiton to make it bounce of Wall collider. Sets
     * the trailRaidians, trailDirection and trailPosistion for trail effect.
     *
     * @param deltaTime
     */
    public void update(float deltaTime) {
        handleInput(deltaTime);


        // set sprite position and rotation
        playerSprite.setPosition(position.x, position.y);
        playerSprite.setRotation(radians * MathUtils.radiansToDegrees);
        if (playerState == PlayerState.NORMAL) {
            playerBounds.setPosition(position.x, position.y);
        } else if (playerState == PlayerState.DESTROYED) {
            playerBounds.setPosition(-500, -500);
        } else if (playerState == PlayerState.STAGE_COMPLETE) {
            playerBounds.setPosition(-500, -500);
        }

        playerBounds.setRotation(radians * MathUtils.radiansToDegrees);

        elapsedTime += deltaTime;

        if (bulletHit) {
            impactRadians = bullets.radians[currentBulletIndex];
            impactDirection.x = bullets.direction[currentBulletIndex].x;
            impactDirection.y = bullets.direction[currentBulletIndex].y;
            // set impactPosition to center of playerSprite
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

        // collision with enemy
        enemies = playScreen.getEnemies();
        for (int index = 0; index < enemies.circleColliders.length; index++) {
            if (playScreen.getEnemies().overlaps(playerBounds, enemies.circleColliders[index])) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                playerState = PlayerState.DESTROYED;
            }
        }

        // collision with space station
        if (enemies.spaceStationSpawned()) {
            if (Intersector.overlapConvexPolygons(enemies.spaceStationPolygons[0], playerBounds)) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                playerState = Player.PlayerState.DESTROYED;
            }
            if (Intersector.overlapConvexPolygons(enemies.spaceStationPolygons[1], playerBounds)) {
                SpaceStationBlaster.soundAssetManager.get(SpaceStationBlaster.EXPLOSION_SOUND, Sound.class).play();
                playerState = Player.PlayerState.DESTROYED;
            }
        }

        // collision with walls
        for (int index = 0; index < walls.colliders.size(); index++) {
            Polygon polygonWall = new Polygon(new float[] { 0, 0,
                    walls.colliders.get(index).getWidth(), 0,
                    walls.colliders.get(index).getWidth(), walls.colliders.get(index).getHeight(),
                    0, walls.colliders.get(index).getHeight() });
            polygonWall.setPosition(walls.colliders.get(index).x,
                    walls.colliders.get(index).y);
            if (Intersector.overlapConvexPolygons(polygonWall, playerBounds)) {
                if (index == walls.TOP_WALL || index == walls.BOTTOM_WALL) {
                    direction.y = -direction.y;
                } else if (index == walls.LEFT_WALL || index == walls.RIGHT_WALL) {
                    direction.x = -direction.x;
                }
            }
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

    /**
     * render: draw the Player sprite on the screen
     * @param spriteBatch is the time passed since the last frame of animation
     */
    public void render(SpriteBatch spriteBatch) {
        playerSprite.draw(spriteBatch);
    }

    /**
     * getSprite: gets the Player sprite
     * @return playerSprite
     */
    public Sprite getSprite() {
        return playerSprite;
    }

    /**
     * spawnBulletPowerup: spawns a Bullet powerup and sets the Bullet powerup position to center of
     * the Red UFOs current UFO index
     */
    public void spawnBulletPowerup() {
        int index = playScreen.getPowerups().spawn(SpaceStationBlaster.PowerupType.BULLET);
        // set the bullet powerup position to center of UFO
        playScreen.getPowerups().position[index].x = playScreen.getEnemies().position[currentUFOIndex].x -
                Powerups.BULLET_POWERUP_TEXTURE_WIDTH / 2;
        playScreen.getPowerups().position[index].y = playScreen.getEnemies().position[currentUFOIndex].y -
                Powerups.SHIELD_POWERUP_TEXTURE_HEIGHT / 2;
        spawningBulletPowerup = false;
    }

    /**
     * spawnShieldPowerup: spawns a Shield powerup and sets the Sheild powerup position to center of
     * the Green UFOs current UFO index
     */
    public void spawnShieldPowerup() {
        int index = playScreen.getPowerups().spawn(SpaceStationBlaster.PowerupType.SHIELD);
        // set the bullet powerup position to center of UFO
        playScreen.getPowerups().position[index].x = playScreen.getEnemies().position[currentUFOIndex].x -
                Powerups.SHIELD_POWERUP_TEXTURE_WIDTH / 2;
        playScreen.getPowerups().position[index].y = playScreen.getEnemies().position[currentUFOIndex].y -
                Powerups.SHIELD_POWERUP_TEXTURE_HEIGHT / 2;
        spawningShieldPowerup = false;
    }

}
