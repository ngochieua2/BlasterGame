package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

public class SmallGreyAsteroids extends Sprite {
    private static final int MeteorGrey_Small1_TEXTURE_X = 497;
    private static final int MeteorGrey_Small1_TEXTURE_Y = 0;
    private static final int MeteorGrey_Small1_TEXTURE_WIDTH = 28;
    private static final int MeteorGrey_Small1_TEXTURE_HEIGHT = 28;
    private static final float MeteorGrey_Small1_SPEED = 96;



    private TextureRegion SGreyAstTexture;


    public SmallGreyAsteroids(PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("meteorGrey_small1"));
        SGreyAstTexture = new TextureRegion(getTexture(), MeteorGrey_Small1_TEXTURE_X, MeteorGrey_Small1_TEXTURE_Y,
                MeteorGrey_Small1_TEXTURE_WIDTH, MeteorGrey_Small1_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(SGreyAstTexture);
        setBounds(MeteorGrey_Small1_TEXTURE_X,MeteorGrey_Small1_TEXTURE_Y,
                MeteorGrey_Small1_TEXTURE_WIDTH / SpaceStationBlaster.PPM ,MeteorGrey_Small1_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);

    }
}
