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
import com.badlogic.gdx.physics.box2d.World;
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

    public GreenUFO(World world, PlayScreen playScreen) {
        super(playScreen.getTextureAtlas().findRegion("ufoGreen"));

        greenUFOTexture = new TextureRegion(getTexture(), GREEN_UFO_TEXTURE_X, GREEN_UFO_TEXTURE_Y,
                GREEN_UFO_TEXTURE_WIDTH, GREEN_UFO_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(greenUFOTexture);
        setBounds(GREEN_UFO_TEXTURE_X,
                GREEN_UFO_TEXTURE_Y, GREEN_UFO_TEXTURE_WIDTH / SpaceStationBlaster.PPM,
                GREEN_UFO_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);
    }

    public void update(float deltaTime) {

    }
}
