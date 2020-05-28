package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Screens.PlayScreen;

public class Player {
    enum State { NORMAL, DESTROYED }

    private static final float MAX_SPEED = 300; // the maximum speed the player can travel
    private static final float ACCELERATION = 200; // how fast the player can accelerate
    private static final float DECELERATION = 10; // how fast the player can decelerate
    private static final float ROTATION_SPEED = 3; // speed the player can rotate

    public static final String PLAYER_TEXTURE_ATLAS_REGION = "playerShip1_blue";
    public static final String TILED_MAP_PLAYER = "PlayerShip";

    State state;

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

    float shootingCooldown = 0f;
    float shootingCooldownSlow = 0.3f;

    public Player(PlayScreen playScreen) {
        this.textureAtlas = playScreen.getTextureAtlas();
        this.tiledMap = playScreen.getTiledMap();
        this.bullets = playScreen.getBullets();
        state = State.NORMAL;
        position = new Vector2();
        direction = new Vector2();
        radians = 0;

        playScreen.getTextureAtlas();
        textureRegion = textureAtlas.findRegion(PLAYER_TEXTURE_ATLAS_REGION);
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
        if (shootingCooldown <= 0f && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            int index = bullets.spawn(SpaceStationBlaster.BulletType.GREEN, radians);
            //bullets.position[index].set(getTipPosition());
            // set bullets position to the centre of the player position
            bullets.position[index].x = position.x - Bullets.GREEN_BULLET_TEXTURE_WIDTH / 2 + playerSprite.getWidth() / 2;
            bullets.position[index].y = position.y - Bullets.GREEN_BULLET_TEXTURE_HEIGHT / 2 + playerSprite.getHeight() / 2;
            bullets.position[index] = calculateOrbit((float) (radians + Math.PI / 2), playerSprite.getHeight() / 2 - Bullets.GREEN_BULLET_TEXTURE_HEIGHT / 2, bullets.position[index]);

            shootingCooldown = shootingCooldownSlow;
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
