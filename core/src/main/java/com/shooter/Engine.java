package com.shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Engine extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;

    private IGameState currentState;
    private IGameState.State currentStateEnum;
    private Menu menu;
    private Game game;

    private Music music;

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.15f);
        music.play();

        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        menu = new Menu();
        game = new Game();

        currentState = menu;
        currentStateEnum = IGameState.State.MENU;
        menu.reset();
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        IGameState.State newState = currentState.update();
        currentState.render(spriteBatch);

        if (currentStateEnum != newState) {
            switch (newState) {
                case GAME -> currentState = game;
                case MENU -> currentState = menu;
            }
            currentState.reset();
        }

        currentStateEnum = newState;

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
    }

    @Override
    public void dispose() {
        menu.dispose();
        game.dispose();
        music.dispose();
    }
}
