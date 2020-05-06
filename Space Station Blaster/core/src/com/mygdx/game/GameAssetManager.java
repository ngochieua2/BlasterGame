package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * GameAssetManager: for loading and disposal of resources
 */
public class GameAssetManager {
    public final AssetManager assetManager = new AssetManager();

    // texture packs
    public final String spriteSheetPack = "sprite-sheet.atlas";
    public final String uiSpaceExpansionSpriteSheetPack = "ui-space-expansion-sprite-sheet.atlas";
    public final String starNebTexture = "star-neb1.png";

    /**
     * loadImages: for loading the images and setting type of class they will be when they are
     * loaded
     */
    public void loadImages() {
        assetManager.load(spriteSheetPack, TextureAtlas.class);
        assetManager.load(uiSpaceExpansionSpriteSheetPack, TextureAtlas.class);
        assetManager.load(starNebTexture, Texture.class);
    }

    /**
     * loadSounds: for loading the sounds
     */
    public void loadSounds() {

    }

    /**
     * loadParticleEffects: for loading particle effects
     */
    public void loadParticleEffects() {

    }
}
