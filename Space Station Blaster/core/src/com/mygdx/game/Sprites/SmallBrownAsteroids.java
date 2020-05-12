package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

public class SmallBrownAsteroids extends Sprite {
    private static final int MeteorBrown_Small1_TEXTURE_X = 469;
    private static final int MeteorBrown_Small1_TEXTURE_Y = 0;
    private static final int MeteorBrown_Small1_TEXTURE_WIDTH = 28;
    private static final int MeteorBrown_Small1_TEXTURE_HEIGHT = 28;
    private static final float MeteorBrown_Small1_SPEED = 96;



    private TextureRegion SBrownAstTexture;


    public SmallBrownAsteroids(World world, PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("meteorBrown_small1"));
        SBrownAstTexture = new TextureRegion(getTexture(), MeteorBrown_Small1_TEXTURE_X, MeteorBrown_Small1_TEXTURE_Y,
                MeteorBrown_Small1_TEXTURE_WIDTH, MeteorBrown_Small1_TEXTURE_HEIGHT);
        setTexture(getTexture());
        setRegion(SBrownAstTexture);
        setBounds(MeteorBrown_Small1_TEXTURE_X,MeteorBrown_Small1_TEXTURE_Y,
                MeteorBrown_Small1_TEXTURE_WIDTH / SpaceStationBlaster.PPM ,MeteorBrown_Small1_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);

    }


    public void update(float deltaTime) {


    }

}
