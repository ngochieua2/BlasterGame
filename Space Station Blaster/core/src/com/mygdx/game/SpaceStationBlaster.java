package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class SpaceStationBlaster extends Game {

	public static final int V_WIDTH = 1280; // virtual height of screen;
	public static final int V_HEIGHT = 800; // virtual width of screen;

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
