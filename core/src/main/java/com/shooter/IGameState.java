package com.shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IGameState {
    enum State {
        MENU, GAME
    }

    void dispose();
    void reset();
    IGameState.State update();
    void render(SpriteBatch spriteBatch);
}
