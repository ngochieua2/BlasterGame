package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

public class LargeGreyAsteroids extends Sprite {

    private static final int MeteorGrey_Big1_TEXTURE_X = 321;
    private static final int MeteorGrey_Big1_TEXTURE_Y = 250;
    private static final int MeteorGrey_Big1_TEXTURE_WIDTH = 101;
    private static final int MeteorGrey_Big1_TEXTURE_HEIGHT = 84;
    private static final float MeteorGrey_Big1_SPEED = 32;
    private static final int MeteorGrey_Big1_MAX_HEALTH = 3;


    private TextureRegion LGreyAstTexture;


    public LargeGreyAsteroids(World world, PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("meteorGrey_big1"));
        LGreyAstTexture = new TextureRegion(getTexture(), MeteorGrey_Big1_TEXTURE_X, MeteorGrey_Big1_TEXTURE_Y,
                MeteorGrey_Big1_TEXTURE_WIDTH, MeteorGrey_Big1_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(LGreyAstTexture);
        setBounds(MeteorGrey_Big1_TEXTURE_X,MeteorGrey_Big1_TEXTURE_Y,
                MeteorGrey_Big1_TEXTURE_WIDTH / SpaceStationBlaster.PPM ,MeteorGrey_Big1_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);

    }


    public void update(float deltaTime) {


    }


}
