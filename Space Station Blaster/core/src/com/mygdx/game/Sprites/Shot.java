package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Tools.ShapeFactory;

public class Shot extends Sprite {

    // type of shot animations and texture
    public enum Type {GREEN_SHOT, ORANGE_SHOT, PURPLE_SHOT, BLUE_SHOT}
    Type shotType;
    // current State of the shot
    public enum State {FIRE, SHOT, IMPACT}

    private int shotTextureX;
    private int shotTextureY;
    private int shotTextureWidth;
    private int shotTextureHeight;
    private int fireShotStartFrame;
    private int fireShotEndFrame;
    private int shotFrame;
    private int impactShotStartFrame;
    private int impactShotEndFrame;
    private float fireShotFrameDuration;
    private float impactShotFrameDuration;
    private float bodyDensity;

    private Animation fireShot;
    private TextureRegion shot;
    private Animation impactShot;

    // world that the shot is spawned into
    public World world;
    // box2D body
    public Body body;

    public Shot(Type shotType, World world, PlayScreen playScreen) {
        this.world = world;
        this.shotType = shotType;

        // initialise the for shotType
        switch(shotType) {
            case GREEN_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-green");
                initShotTexture(1396, 0, 32, 32);
                initShotFrames(0, 3, 4, 5, 9);
                fireShotFrameDuration = 0.1f;
                impactShotFrameDuration = 0.1f;
                bodyDensity = 0.4f;
                break;
            }
            case ORANGE_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-orange");
                initShotTexture(768, 104, 64, 64);
                initShotFrames(0, 4, 5, 6, 13);
                fireShotFrameDuration = 0.1f;
                impactShotFrameDuration = 0.1f;
                bodyDensity = 0.4f;
                break;
            }
            case PURPLE_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-purple");
                initShotTexture(0, 348, 128, 128);
                initShotFrames(0, 3, 4, 5, 14);
                fireShotFrameDuration = 0.1f;
                impactShotFrameDuration = 0.1f;
                bodyDensity = 0.4f;
                break;
            }
            case BLUE_SHOT: {
                playScreen.getTextureAtlas().findRegion("shot-blue");
                initShotTexture(0, 104 , 64,64);
                initShotFrames(0, 5, 6, 7, 11);
                fireShotFrameDuration = 0.1f;
                impactShotFrameDuration = 0.1f;
                bodyDensity = 0.4f;
                break;
            }
        }

        Array<TextureRegion> frames = new Array<TextureRegion>();

        // get the fire shot animation frames and add them to fireShot
        for (int index = fireShotStartFrame; index <= fireShotEndFrame; index++) {
            frames.add(new TextureRegion(getTexture(),
                    shotTextureX + index * shotTextureWidth, shotTextureY,
                    shotTextureWidth, shotTextureHeight));
        }
        fireShot = new Animation(0.1f, frames);
        frames.clear();

        // get the shot texture region
        shot = new TextureRegion(getTexture(), shotTextureX + shotFrame * shotTextureWidth,
                shotTextureY, shotTextureWidth, shotTextureHeight);

        // get the impact shot animation frames and dd them to impactShot
        for (int index = impactShotStartFrame; index <= impactShotEndFrame; index++) {
            frames.add(new TextureRegion(getTexture(),
                    shotTextureX + index * shotTextureWidth,
                    shotTextureY, shotTextureWidth, shotTextureHeight));
        }
        impactShot = new Animation(0.1f, frames);

        body = ShapeFactory.createRectangle(new Vector2(shotTextureX, shotTextureY),
                new Vector2(shotTextureWidth, shotTextureHeight), BodyDef.BodyType.DynamicBody,
                world, 0.4f);

        setOrigin(shotTextureWidth / 2 / SpaceStationBlaster.PPM,
                shotTextureHeight / 2 / SpaceStationBlaster.PPM);

        setBounds(shotTextureX, shotTextureY,
                shotTextureWidth / SpaceStationBlaster.PPM,
                shotTextureHeight / SpaceStationBlaster.PPM);

        setRegion(shot);
    }

    public void update(float deltaTime) {
        // our box2d body is at the centre of our fixture.
        // We need to set the position to be the bottom left corner of our fixture.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    private void initShotTexture(int shotTextureX, int shotTextureY, int shotTextureWidth,
                                 int shotTextureHeight) {
        this.shotTextureX = shotTextureX;
        this.shotTextureY = shotTextureY;
        this.shotTextureWidth = shotTextureWidth;
        this.shotTextureHeight = shotTextureHeight;
    }

    private void initShotFrames(int fireShotStartFrame, int fireShotEndFrame, int shotFrame,
                                int impactShotStartFrame, int impactShotEndFrame) {
        this.fireShotStartFrame = fireShotStartFrame;
        this.fireShotEndFrame = fireShotEndFrame;
        this.shotFrame = shotFrame;
        this.impactShotStartFrame = impactShotStartFrame;
        this.impactShotEndFrame = impactShotEndFrame;
    }
}
