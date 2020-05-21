package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    public Player(PlayScreen playScreen) {
        this.textureAtlas = playScreen.getTextureAtlas();
        this.tiledMap = playScreen.getTiledMap();
        state = State.NORMAL;
        position = new Vector2();
        direction = new Vector2();
        radians = 0;
        //radians = (float) Math.PI / 2;

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
        playerSprite.setOrigin(playerRectangle.getWidth() / 2, playerRectangle.getHeight() / 2);

        playerSprite.setRegion(playerSprite);
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
        // set bounds position and rotation
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
