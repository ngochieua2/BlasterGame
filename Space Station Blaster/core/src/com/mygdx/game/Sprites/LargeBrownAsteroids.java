package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Tools.ShapeFactory;

public class LargeBrownAsteroids extends Sprite {

    private static final int MeteorBrown_Big1_TEXTURE_X = 220;
    private static final int MeteorBrown_Big1_TEXTURE_Y = 250;
    private static final int MeteorBrown_Big1_TEXTURE_WIDTH = 101;
    private static final int MeteorBrown_Big1_TEXTURE_HEIGHT = 84;
    private static final float MeteorBrown_Big1_SPEED = 32;
    private static final int MeteorBrown_Big1_MAX_HEALTH = 3;


    private TextureRegion LBrownAstTexture;


    public LargeBrownAsteroids(PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("meteorBrown_big1"));
        LBrownAstTexture = new TextureRegion(getTexture(), MeteorBrown_Big1_TEXTURE_X, MeteorBrown_Big1_TEXTURE_Y,
                MeteorBrown_Big1_TEXTURE_WIDTH, MeteorBrown_Big1_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(LBrownAstTexture);
        setBounds(MeteorBrown_Big1_TEXTURE_X,MeteorBrown_Big1_TEXTURE_Y,
                MeteorBrown_Big1_TEXTURE_WIDTH / SpaceStationBlaster.PPM ,MeteorBrown_Big1_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);

    }

}
