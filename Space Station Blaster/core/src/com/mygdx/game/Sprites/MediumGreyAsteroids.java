package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

public class MediumGreyAsteroids extends Sprite {
    private static final int MeteorGrey_Med1_TEXTURE_X = 694;
    private static final int MeteorGrey_Med1_TEXTURE_Y = 40;
    private static final int MeteorGrey_Med1_TEXTURE_WIDTH = 43;
    private static final int MeteorGrey_Med1_TEXTURE_HEIGHT = 43;
    private static final float MeteorGrey_Med1_SPEED = 64;



    private TextureRegion MGreyAstTexture;


    public MediumGreyAsteroids(World world, PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("meteorGrey_med1"));
        MGreyAstTexture = new TextureRegion(getTexture(), MeteorGrey_Med1_TEXTURE_X, MeteorGrey_Med1_TEXTURE_Y,
                MeteorGrey_Med1_TEXTURE_WIDTH, MeteorGrey_Med1_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(MGreyAstTexture);
        setBounds(MeteorGrey_Med1_TEXTURE_X,MeteorGrey_Med1_TEXTURE_Y,
                MeteorGrey_Med1_TEXTURE_WIDTH / SpaceStationBlaster.PPM ,MeteorGrey_Med1_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);

    }


    public void update(float deltaTime) {


    }

}
