package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;

public class PlayerSpaceship extends Sprite {
    private static final int PLAYER_SHIP_TEXTURE_X = 1454;
    private static final int PLAYER_SHIP_TEXTURE_Y = 168;
    private static final int PLAYER_SHIP_TEXTURE_WIDTH = 99;
    private static final int PLAYER_SHIP_TEXTURE_HEIGHT = 75;

    // world that the player spaceship lives in
    public World world;
    // box2D body
    public Body body;
    private TextureRegion spaceship;

    public PlayerSpaceship(World world, PlayScreen playScreen) {
        super(playScreen.getTextureAtlas().findRegion("playerShip1_blue"));
        this.world = world;

        body = playScreen.getMapLoader().getPlayer();

        setOrigin(PLAYER_SHIP_TEXTURE_WIDTH / 2 / SpaceStationBlaster.PPM,
                PLAYER_SHIP_TEXTURE_HEIGHT / 2 /SpaceStationBlaster.PPM);

        spaceship = new TextureRegion(getTexture(), PLAYER_SHIP_TEXTURE_X,
                PLAYER_SHIP_TEXTURE_Y, PLAYER_SHIP_TEXTURE_WIDTH, PLAYER_SHIP_TEXTURE_HEIGHT);

        setBounds(PLAYER_SHIP_TEXTURE_X,
                PLAYER_SHIP_TEXTURE_Y, PLAYER_SHIP_TEXTURE_WIDTH / SpaceStationBlaster.PPM,
                PLAYER_SHIP_TEXTURE_HEIGHT / SpaceStationBlaster.PPM);
        setRegion(spaceship);
    }

    public void update(float deltaTime) {
        // our box2d body is at the centre of our fixture.
        // We need to set the position to be the bottom left corner of our fixture.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }
}
