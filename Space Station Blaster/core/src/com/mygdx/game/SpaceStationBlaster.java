package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class SpaceStationBlaster extends Game {

	public static final int V_WIDTH = 1280; // virtual height of screen;
	public static final int V_HEIGHT = 800; // virtual width of screen;
	public static final float PPM = 100f; // number of pixels per meter

	// default values for filters
	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short ASTEROID_BIT = 4;
	public static final short UFO_BIT = 8;
	public static final short ENEMY_SHIP_BIT = 16;
	public static final short PLAYER_SHOT_BIT = 32;
	public static final short ENEMY_SHOT_BIT = 64;
	public static final short SPACE_STATION_BIT = 128;
	public static final short MISSILE_BIT = 256;

	public SpriteBatch spriteBatch; // public to allow access to batch from SpaceStationBlaster
									// instance

	/**
	 * create: sets the Game Screen as a new PlayScreen
	 */
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		this.setScreen(new PlayScreen(this));
	}

	/**
	 * render: game loop that renders the Game
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * dispose: disposes of spriteBatch
	 */
	@Override
	public void dispose () {
		spriteBatch.dispose();
	}
}
