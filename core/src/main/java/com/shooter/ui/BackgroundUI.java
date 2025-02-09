package com.shooter.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.shooter.IDrawable;

public class BackgroundUI implements IDrawable
{
    private static final Texture BACKGROUND_TEXTURE = new Texture("ui/background.png");

    Sprite sprite;

    BackgroundUI() {
        this.sprite = new Sprite(BACKGROUND_TEXTURE);
        this.sprite.setTexture(BACKGROUND_TEXTURE);
        this.sprite.setOrigin(0.0f, 0.0f);
        this.sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void dispose() {
        BACKGROUND_TEXTURE.dispose();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        this.sprite.draw(spriteBatch);
    }
}
