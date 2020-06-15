package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class SpaceStationBlaster extends Game {
	public enum BulletType { NONE, GREEN, ORANGE, PURPLE, BLUE, RESERVED }
	public enum EffectType { NONE, GREEN_FIRE, ORANGE_FIRE, PURPLE_FIRE, BLUE_FIRE, GREEN_IMPACT,
		ORANGE_IMPACT, PURPLE_IMPACT, BLUE_IMPACT, GREEN_TRAIL, ORANGE_TRAIL, PURPLE_TRAIL,
		BLUE_TRAIL, PLAYER_EXPLOSION, SMALL_ASTEROID_EXPLOSION, ENEMY_EXPLOSION };
	public enum PowerupType { NONE, BULLET, SHIELD }

	public static final int V_WIDTH = 1280; // virtual height of screen;
	public static final int V_HEIGHT = 800; // virtual width of screen;

	public static final String TEXTURE_ATLAS_PATH = "sprite-sheet.atlas";
	public static final String UI_TEXTURE_ATLAS_PATH = "ui-space-expansion-sprite-sheet.atlas";
	public static final String TILE_MAP_PATH = "game-map.tmx";
	public static final String MAP_BOUNDS = "Bounds";

	public static final int MAP_WIDTH = 3200;
	public static final int MAP_HEIGHT = 2500;

	//sounds and music constants
	public static final String PLAYER_LASER_SOUND = "Sound/PlayerLaser.wav";
	public static final String UFO_SHOOT_SOUND = "Sound/UFOShoot.wav";
	public static final String IMPACT_SOUND = "Sound/Impact.wav";
	public static final String ASTEROID_EXPLOSION_SOUND = "Sound/AsteroidExplosion.wav";
	public static final String EXPLOSION_SOUND = "Sound/Explosion.wav";
	public static final String POWERUP_SHIELD_SOUND = "Sound/PowerupShield.wav";
	public static final String POWERUP_SHOOTING_SOUND = "Sound/PowerupShooting.wav";
	public static final String SPACE_STATION_SPAWN_SOUND = "Sound/SpaceStationSpawn.wav";
	public static final String UFO_SPAWN_SOUND = "Sound/UFOSpawn.wav";
	public static final String GAME_MUSIC = "Sound/vyra-mars-colony.mp3";
	public static final String BUTTON_PRESS_SOUND = "Sound/ButtonPress.wav";

	public SpriteBatch spriteBatch; // public to allow access to batch from SpaceStationBlaster

	public Sound ButtonPressSound;

	public static AssetManager soundAssetManager;

	/**
	 * create: sets the Game Screen as a new PlayScreen
	 */
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();

		soundAssetManager = new AssetManager();
		soundAssetManager.load(GAME_MUSIC, Music.class);
		soundAssetManager.load(PLAYER_LASER_SOUND, Sound.class);
		soundAssetManager.load(UFO_SHOOT_SOUND, Sound.class);
		soundAssetManager.load(IMPACT_SOUND, Sound.class);
		soundAssetManager.load(ASTEROID_EXPLOSION_SOUND, Sound.class);
		soundAssetManager.load(EXPLOSION_SOUND, Sound.class);
		soundAssetManager.load(POWERUP_SHIELD_SOUND, Sound.class);
		soundAssetManager.load(POWERUP_SHOOTING_SOUND, Sound.class);
		soundAssetManager.load(SPACE_STATION_SPAWN_SOUND, Sound.class);
		soundAssetManager.load(UFO_SPAWN_SOUND, Sound.class);
		soundAssetManager.load(BUTTON_PRESS_SOUND, Sound.class);
		soundAssetManager.finishLoading();

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
		soundAssetManager.dispose();
	}
}
