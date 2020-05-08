package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Tools.ShapeFactory;

public class GreenUFO extends Sprite {
    //X & Y positions in the texture atlas
    public static final int GREEN_UFO_TEXTURE_X = 1048;
    public static final int GREEN_UFO_TEXTURE_Y = 250;
    public static final int GREEN_UFO_TEXTURE_HEIGHT = 91;
    public static final int GREEN_UFO_TEXTURE_WIDTH = 91;
    public static final float GREEN_UFO_SPEED = 20f;
    public static final int MAX_GREEN_UFOS = 3;

    private TextureRegion greenUFOTexture;

    private float xVelocity;
    private float yVelocity;
    private Vector2 velocity;
    private int health;

    public GreenUFO(PlayScreen playScreen) {
        super(playScreen.getTextureAtlas().findRegion("ufoGreen"));

        greenUFOTexture = new TextureRegion(getTexture(), GREEN_UFO_TEXTURE_X, GREEN_UFO_TEXTURE_Y,
                GREEN_UFO_TEXTURE_WIDTH, GREEN_UFO_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(greenUFOTexture);
        setBounds(GREEN_UFO_TEXTURE_X,
                GREEN_UFO_TEXTURE_Y, GREEN_UFO_TEXTURE_WIDTH / SpaceStationBlaster.PPM,
                GREEN_UFO_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);
    }

    public void spawn() {
        //Randomly generate velocity vector so the ships direction is random
        xVelocity = MathUtils.random(-20f, 20f);
        //Use pythagoras to produce a yVelocity so that the speed of the ship is always constant
        yVelocity = (float) Math.sqrt(GREEN_UFO_SPEED*GREEN_UFO_SPEED - xVelocity*xVelocity);
        yVelocity = yVelocity * MathUtils.randomSign();
        velocity = new Vector2(xVelocity, yVelocity);

        health = 3;

        float spawnX;
        float spawnY;
        //TODO: get camera position to come up with random spawn point
        if (xVelocity < 0) {

        }
    }

    public void update(float deltaTime) {
        translate(xVelocity*deltaTime, yVelocity*deltaTime);
    }

    public void render(SpriteBatch batch) {
        draw(batch);
    }
}
