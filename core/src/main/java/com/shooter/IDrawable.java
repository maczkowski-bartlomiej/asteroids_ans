package com.shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IDrawable {
    void dispose();
    void render(SpriteBatch spriteBatch);
}
