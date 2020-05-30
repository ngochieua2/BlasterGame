package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class SpaceStationBlaster extends Game {
	public enum BulletType { NONE, GREEN, ORANGE, PURPLE, BLUE }

	public static final int V_WIDTH = 1280; // virtual height of screen;
	public static final int V_HEIGHT = 800; // virtual width of screen;
	public static final float PPM = 100f; // number of pixels per meter

	public static final String TEXTURE_ATLAS_PATH = "sprite-sheet.atlas";
	public static final String UI_TEXTURE_ATLAS_PATH = "ui-space-expansion-sprite-sheet.atlas";
	public static final String TILE_MAP_PATH = "game-map.tmx";
	public static final String MAP_BOUNDS = "Bounds";

	public static final int MAP_WIDTH = 3200;
	public static final int MAP_HEIGHT = 2500;

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
