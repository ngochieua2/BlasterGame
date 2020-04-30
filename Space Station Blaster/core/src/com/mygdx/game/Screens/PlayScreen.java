package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SpaceStationBlaster;

public class PlayScreen implements Screen {
    private SpaceStationBlaster game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud gameHud;

    public PlayScreen(SpaceStationBlaster game) {
        this.game = game;
        gameCamera = new OrthographicCamera();
        // maintain aspect ratio of the gameViewPort for compatibility with different size devices
        gameViewport = new FitViewport(SpaceStationBlaster.V_WIDTH,
                SpaceStationBlaster.V_HEIGHT, gameCamera);
        gameHud = new Hud(game.spriteBatch);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.setProjectionMatrix(gameHud.stage.getCamera().combined);
        gameHud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
