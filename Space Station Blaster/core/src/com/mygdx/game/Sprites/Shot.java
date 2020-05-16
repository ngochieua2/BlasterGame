package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Tools.ShapeFactory;

import java.util.Set;

/**
 * Shot: for the creation of four different shot types that can be used any type of enemy or
 * player ship.
 */
public class Shot extends Sprite {

    // constants for green shot
    private static final int GREEN_SHOT_TEXTURE_X = 1396;
    private static final int GREEN_SHOT_TEXTURE_Y = 0;
    private static final int GREEN_SHOT_TEXTURE_WIDTH = 32;
    private static final int GREEN_SHOT_TEXTURE_HEIGHT = 32;
    private static final int GREEN_FIRE_SHOT_START_FRAME = 0;
    private static final int GREEN_FIRE_SHOT_END_FRAME = 3;
    private static final int GREEN_SHOT_FRAME = 4;
    private static final int GREEN_IMPACT_SHOT_START_FRAME = 5;
    private static final int GREEN_IMPACT_SHOT_END_FRAME = 9;
    private static final float GREEN_FIRE_SHOT_FRAME_DURATION = 0.1f;
    private static final float GREEN_IMPACT_SHOT_FRAME_DURATION = 0.1f;
    private static final int GREEN_SHOT_RECTANGLE_WIDTH = 0;
    private static final int GREEN_SHOT_RECTANGLE_HEIGHT = 0;
    private static final float GREEN_SHOT_FIXTURE_DEF_DENSITY = 0.4f;

    // constants for orange shot
    private static final int ORANGE_SHOT_TEXTURE_X = 768;
    private static final int ORANGE_SHOT_TEXTURE_Y = 104;
    private static final int ORANGE_SHOT_TEXTURE_WIDTH = 64;
    private static final int ORANGE_SHOT_TEXTURE_HEIGHT = 64;
    private static final int ORANGE_FIRE_SHOT_START_FRAME = 0;
    private static final int ORANGE_FIRE_SHOT_END_FRAME = 4;
    private static final int ORANGE_SHOT_FRAME = 5;
    private static final int ORANGE_IMPACT_SHOT_START_FRAME = 6;
    private static final int ORANGE_IMPACT_SHOT_END_FRAME = 13;
    private static final float ORANGE_FIRE_SHOT_FRAME_DURATION = 0.1f;
    private static final float ORANGE_IMPACT_SHOT_FRAME_DURATION = 0.1f;
    private static final int ORANGE_SHOT_RECTANGLE_WIDTH = 0;
    private static final int ORANGE_SHOT_RECTANGLE_HEIGHT = 0;
    private static final float ORANGE_SHOT_FIXTURE_DEF_DENSITY = 0.4f;

    // constants for purple shot
    private static final int PURPLE_SHOT_TEXTURE_X = 0;
    private static final int PURPLE_SHOT_TEXTURE_Y = 348;
    private static final int PURPLE_SHOT_TEXTURE_WIDTH = 128;
    private static final int PURPLE_SHOT_TEXTURE_HEIGHT = 128;
    private static final int PURPLE_FIRE_SHOT_START_FRAME = 0;
    private static final int PURPLE_FIRE_SHOT_END_FRAME = 3;
    private static final int PURPLE_SHOT_FRAME = 4;
    private static final int PURPLE_IMPACT_SHOT_START_FRAME = 5;
    private static final int PURPLE_IMPACT_SHOT_END_FRAME = 14;
    private static final float PURPLE_FIRE_SHOT_FRAME_DURATION = 0.1f;
    private static final float PURPLE_IMPACT_SHOT_FRAME_DURATION = 0.1f;
    private static final int PURPLE_SHOT_RECTANGLE_WIDTH = 0;
    private static final int PURPLE_SHOT_RECTANGLE_HEIGHT = 0;
    private static final float PURPLE_SHOT_FIXTURE_DEF_DENSITY = 0.4f;

    // constants for blue shot
    private static final int BLUE_SHOT_TEXTURE_X = 0;
    private static final int BLUE_SHOT_TEXTURE_Y = 104;
    private static final int BLUE_SHOT_TEXTURE_WIDTH = 64;
    private static final int BLUE_SHOT_TEXTURE_HEIGHT = 64;
    private static final int BLUE_FIRE_SHOT_START_FRAME = 0;
    private static final int BLUE_FIRE_SHOT_END_FRAME = 5;
    private static final int BLUE_SHOT_FRAME = 6;
    private static final int BLUE_IMPACT_SHOT_START_FRAME = 7;
    private static final int BLUE_IMPACT_SHOT_END_FRAME = 11;
    private static final float BLUE_FIRE_SHOT_FRAME_DURATION = 0.1f;
    private static final float BLUE_IMPACT_SHOT_FRAME_DURATION = 0.1f;
    private static final int BLUE_SHOT_RECTANGLE_WIDTH = 0;
    private static final int BLUE_SHOT_RECTANGLE_HEIGHT = 0;
    private static final float BLUE_SHOT_FIXTURE_DEF_DENSITY = 0.4f;

    // type of shot animations and texture
    public enum Type {GREEN_SHOT, ORANGE_SHOT, PURPLE_SHOT, BLUE_SHOT}
    Type shotType;

    public enum State {FIRE, SHOT, IMPACT} // current State of the shot
    public State currentState;
    public State previousState;
    private float stateTimer; // amount of time we are in any given state

    private int shotTextureX; // the X position of the texture region in texture atlas
    private int shotTextureY; // the Y position of the texture region in texture atlas
    private int shotTextureWidth; // the width of the texture region in texture atlas
    private int shotTextureHeight; // the height of the texture region in texture atlas
    private int fireShotStartFrame; // the fire shot start frame of the animation
    private int fireShotEndFrame; // the fire shot end frame of the animation
    private int shotFrame; // the single shot frame
    private int impactShotStartFrame; // the impact shot start frame of the the animation
    private int impactShotEndFrame; // the impact shot end frame of the animation
    private float fireShotFrameDuration; // the fire shot frame duration for the animation
    private float impactShotFrameDuration; // the impact shot frame duration for the animation
    private int shotRectangleWidth; // the height of the Rectangle shape applied to FixtureDef
    private int shotRectangleHeight; // the height of the Rectangle shape applied to FixtureDef
    private float shotFixtureDefDensity; // the fixtureDef density for a shot

    private Animation<TextureRegion> fireShot;
    private Animation<TextureRegion> shot;
    private Animation<TextureRegion> impactShot;

    // world that the shot is spawned into
    public World world;
    // box2D body
    public Body body;

    private short categoryBit;
    private short maskBit;

    /**
     * Shot: create one of the four shot types. When the shotType is passed in it creates the
     * fire shot animation, shot textures and impact animation of that Type
     * @param shotType is the shot type that is to be created
     * @param shipBody the Box2d shipBody
     * @param playScreen the main game play screen
     */
    public Shot(Type shotType, Body shipBody, PlayScreen playScreen) {
        this.world = playScreen.getWorld();
        this.shotType = shotType;

        // set the initial state
        currentState = State.FIRE;
        previousState = State.FIRE;
        stateTimer = 0f;

        // initialise the for shotType
        switch(shotType) {
            case GREEN_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-green");
                initShotTexture(GREEN_SHOT_TEXTURE_X, GREEN_SHOT_TEXTURE_Y,
                        GREEN_SHOT_TEXTURE_WIDTH, GREEN_SHOT_TEXTURE_HEIGHT);
                initShotFrames(GREEN_FIRE_SHOT_START_FRAME, GREEN_FIRE_SHOT_END_FRAME,
                        GREEN_SHOT_FRAME, GREEN_IMPACT_SHOT_START_FRAME,
                        GREEN_IMPACT_SHOT_END_FRAME, GREEN_FIRE_SHOT_FRAME_DURATION,
                        GREEN_IMPACT_SHOT_FRAME_DURATION);
                initFixtureDefValues(GREEN_SHOT_RECTANGLE_WIDTH, GREEN_SHOT_RECTANGLE_HEIGHT, GREEN_SHOT_FIXTURE_DEF_DENSITY);
                break;
            }
            case ORANGE_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-orange");
                initShotTexture(ORANGE_SHOT_TEXTURE_X, ORANGE_SHOT_TEXTURE_Y,
                        ORANGE_SHOT_TEXTURE_WIDTH, ORANGE_SHOT_TEXTURE_HEIGHT);
                initShotFrames(ORANGE_FIRE_SHOT_START_FRAME, ORANGE_FIRE_SHOT_END_FRAME,
                        ORANGE_SHOT_FRAME, ORANGE_IMPACT_SHOT_START_FRAME,
                        ORANGE_IMPACT_SHOT_END_FRAME, ORANGE_FIRE_SHOT_FRAME_DURATION,
                        ORANGE_IMPACT_SHOT_FRAME_DURATION);
                initFixtureDefValues(ORANGE_SHOT_RECTANGLE_WIDTH, ORANGE_SHOT_RECTANGLE_HEIGHT, ORANGE_SHOT_FIXTURE_DEF_DENSITY);
                break;
            }
            case PURPLE_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-purple");
                initShotTexture(PURPLE_SHOT_TEXTURE_X, PURPLE_SHOT_TEXTURE_Y,
                        PURPLE_SHOT_TEXTURE_WIDTH, PURPLE_SHOT_TEXTURE_HEIGHT);
                initShotFrames(PURPLE_FIRE_SHOT_START_FRAME, PURPLE_FIRE_SHOT_END_FRAME,
                        PURPLE_SHOT_FRAME, PURPLE_IMPACT_SHOT_START_FRAME,
                        PURPLE_IMPACT_SHOT_END_FRAME, PURPLE_FIRE_SHOT_FRAME_DURATION,
                        PURPLE_IMPACT_SHOT_FRAME_DURATION);
                initFixtureDefValues(PURPLE_SHOT_RECTANGLE_WIDTH, PURPLE_SHOT_RECTANGLE_HEIGHT, PURPLE_SHOT_FIXTURE_DEF_DENSITY);
                break;
            }
            case BLUE_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-blue");
                initShotTexture(BLUE_SHOT_TEXTURE_X, BLUE_SHOT_TEXTURE_Y, BLUE_SHOT_TEXTURE_WIDTH,
                        BLUE_SHOT_TEXTURE_HEIGHT);
                initShotFrames(BLUE_FIRE_SHOT_START_FRAME, BLUE_FIRE_SHOT_END_FRAME,
                        BLUE_SHOT_FRAME, BLUE_IMPACT_SHOT_START_FRAME, BLUE_IMPACT_SHOT_END_FRAME,
                        BLUE_FIRE_SHOT_FRAME_DURATION, BLUE_IMPACT_SHOT_FRAME_DURATION);
                initFixtureDefValues(BLUE_SHOT_RECTANGLE_WIDTH, BLUE_SHOT_RECTANGLE_HEIGHT, BLUE_SHOT_FIXTURE_DEF_DENSITY);
                break;
            }
        }

        if (shotType == Type.GREEN_SHOT) {
            categoryBit = SpaceStationBlaster.PLAYER_SHOT_BIT;
            maskBit = SpaceStationBlaster.ASTEROID_BIT |
                    SpaceStationBlaster.UFO_BIT | SpaceStationBlaster.ENEMY_SHIP_BIT |
                    SpaceStationBlaster.SPACE_STATION_BIT | SpaceStationBlaster.MISSILE_BIT |
                    SpaceStationBlaster.WALL_BIT;
        } else {
            categoryBit = SpaceStationBlaster.ENEMY_SHOT_BIT;
            maskBit = SpaceStationBlaster.PLAYER_BIT | SpaceStationBlaster.ASTEROID_BIT |
                    SpaceStationBlaster.UFO_BIT | SpaceStationBlaster.SPACE_STATION_BIT |
                    SpaceStationBlaster.MISSILE_BIT | SpaceStationBlaster.WALL_BIT;
        }

        Array<TextureRegion> frames = new Array<TextureRegion>();

        // get the fire shot animation frames and add them to fireShot
        for (int index = fireShotStartFrame; index <= fireShotEndFrame; index++) {
            frames.add(new TextureRegion(getTexture(),
                    shotTextureX + index * shotTextureWidth, shotTextureY,
                    shotTextureWidth, shotTextureHeight));
        }
        fireShot = new Animation(fireShotFrameDuration, frames);
        frames.clear();

        // get the shot texture region
        frames.add(new TextureRegion(getTexture(), shotTextureX + shotFrame * shotTextureWidth,
                shotTextureY, shotTextureWidth, shotTextureHeight));
        shot = new Animation(0.1f, frames);

        // get the impact shot animation frames and dd them to impactShot
        for (int index = impactShotStartFrame; index <= impactShotEndFrame; index++) {
            frames.add(new TextureRegion(getTexture(),
                    shotTextureX + index * shotTextureWidth,
                    shotTextureY, shotTextureWidth, shotTextureHeight));
        }
        impactShot = new Animation(impactShotFrameDuration, frames);
        frames.clear();

        setOrigin(shotRectangleWidth / 2 / SpaceStationBlaster.PPM,
                shotRectangleHeight / 2 / SpaceStationBlaster.PPM);

        /*
        TODO 1. Set bullet sensor to false
        TODO 2. Set bullet to DynamicBody
        TODO 3. Transform bullet to in front of gun
        TODO 4. Set linear velocity of bullet to zero
        TODO 5. Shoot bullet with using force
        */

        // set bounds as the shipBody.position so it is in the same position as shot
        setBounds(shipBody.getPosition().x, shipBody.getPosition().y,
                shotRectangleWidth / SpaceStationBlaster.PPM,
                shotRectangleHeight / SpaceStationBlaster.PPM);

        setOrigin(shotRectangleWidth / 2 / SpaceStationBlaster.PPM,
                shotRectangleHeight / 2 / SpaceStationBlaster.PPM);

        setRegion(fireShot.getKeyFrame(0));

        defineShot();
        // set angular velocity and position Shot body in front of shipBody
        body.setTransform(5, 5, shipBody.getAngularVelocity());
    }

    public void defineShot() {

        body = ShapeFactory.createRectangle(new Vector2(getX(), getY()),
                new Vector2(getWidth(), getHeight()), BodyDef.BodyType.DynamicBody,
                world, shotFixtureDefDensity, true, categoryBit, maskBit);
    }

    public void defineImpactShot() {
        body = ShapeFactory.createRectangle(new Vector2(getX(), getY()),
                new Vector2(getWidth(), getHeight()), BodyDef.BodyType.DynamicBody,
                world, shotFixtureDefDensity, false, categoryBit, maskBit);

    }

    public void defineFireShot() {
        body = ShapeFactory.createRectangle(new Vector2(getX(), getY()),
                new Vector2(getWidth(), getHeight()), BodyDef.BodyType.DynamicBody,
                world, shotFixtureDefDensity, false, categoryBit, maskBit);
    }


    /**
     * update: set the body position in the game world
     * @param deltaTime is the amount of time to process one frame of animation
     */
    public void update(float deltaTime) {
        // our box2d body is at the centre of our fixture.
        // We need to set the position to be the bottom left corner of our fixture.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaTime));
    }

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region = null;
        switch(currentState) {
            case FIRE: {
                region = (TextureRegion) fireShot.getKeyFrame(stateTimer, true);
                break;
            }
            case SHOT: {
                region = shot.getKeyFrame(stateTimer);
                break;
            }
            case IMPACT: {
                region = (TextureRegion) impactShot.getKeyFrame(stateTimer, true);
                break;
            }
        }

        if (currentState == previousState) {
            stateTimer += deltaTime;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (body.getLinearVelocity().x > 0 || body.getLinearVelocity().y > 0) {
            return State.SHOT;
        } else if (body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0 || previousState == State.SHOT) {
            return State.IMPACT;
        } else {
            return State.FIRE;
        }
    }

    /**
     * initShotTexture: initialise shot texture
     * @param shotTextureX the X position of the texture region in texture atlas
     * @param shotTextureY the Y position of the texture region in texture atlas
     * @param shotTextureWidth the width of the texture region in texture atlas
     * @param shotTextureHeight the height of the texture region in texture atlas
     */
    private void initShotTexture(int shotTextureX, int shotTextureY, int shotTextureWidth,
                                 int shotTextureHeight) {
        this.shotTextureX = shotTextureX;
        this.shotTextureY = shotTextureY;
        this.shotTextureWidth = shotTextureWidth;
        this.shotTextureHeight = shotTextureHeight;
    }

    /**
     * initShotFrames: initialise shot frames
     * @param fireShotStartFrame the fire shot start frame of the animation
     * @param fireShotEndFrame the fire shot end frame of the animation
     * @param shotFrame the single shot frame
     * @param impactShotStartFrame the impact shot start frame of the the animation
     * @param impactShotEndFrame the impact shot end frame of the animation
     */
    private void initShotFrames(int fireShotStartFrame, int fireShotEndFrame, int shotFrame,
                                int impactShotStartFrame, int impactShotEndFrame,
                                float fireShotFrameDuration, float impactShotFrameDuration) {
        this.fireShotStartFrame = fireShotStartFrame;
        this.fireShotEndFrame = fireShotEndFrame;
        this.shotFrame = shotFrame;
        this.impactShotStartFrame = impactShotStartFrame;
        this.impactShotEndFrame = impactShotEndFrame;
        this.fireShotFrameDuration = fireShotFrameDuration;
        this.impactShotFrameDuration = impactShotFrameDuration;
    }

    /**
     * initFixtureDefValue: set values for the fixtureDef in the ShapeFactory.createRectangle()
     * @param shotRectangleWidth the height of the Rectangle shape applied to FixtureDef
     * @param shotRectangleHeight this height of the Rectangle shape applied to FixtureDef
     * @param shotFixtureDefDensity the fixtureDef density for a shot
     */
    private void initFixtureDefValues(int shotRectangleWidth, int shotRectangleHeight,
                                     float shotFixtureDefDensity) {
        this.shotRectangleWidth = shotRectangleWidth;
        this.shotRectangleHeight = shotRectangleHeight;
        this.shotFixtureDefDensity = shotFixtureDefDensity;
    }
}
