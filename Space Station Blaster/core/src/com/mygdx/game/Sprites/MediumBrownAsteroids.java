package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

public class MediumBrownAsteroids extends Sprite {
    private static final int MeteorBrown_Med1_TEXTURE_X = 651;
    private static final int MeteorBrown_Med1_TEXTURE_Y = 40;
    private static final int MeteorBrown_Med1_TEXTURE_WIDTH = 43;
    private static final int MeteorBrown_Med1_TEXTURE_HEIGHT = 43;
    private static final float MeteorBrown_Med1_SPEED = 64;



    private TextureRegion MBrownAstTexture;


    public MediumBrownAsteroids(PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("meteorBrown_med1"));
        MBrownAstTexture = new TextureRegion(getTexture(), MeteorBrown_Med1_TEXTURE_X, MeteorBrown_Med1_TEXTURE_Y,
                MeteorBrown_Med1_TEXTURE_WIDTH, MeteorBrown_Med1_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(MBrownAstTexture);
        setBounds(MeteorBrown_Med1_TEXTURE_X,MeteorBrown_Med1_TEXTURE_Y,
                MeteorBrown_Med1_TEXTURE_WIDTH / SpaceStationBlaster.PPM ,MeteorBrown_Med1_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);

    }

}
