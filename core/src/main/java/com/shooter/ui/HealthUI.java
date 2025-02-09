package com.shooter.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shooter.IDrawable;

import java.util.ArrayList;

public class HealthUI implements IDrawable
{
    private static final Texture HEALTH_FULL_TEXTURE = new Texture(Gdx.files.internal("ui/health_full.png"));
    private static final Texture HEALTH_HALF_TEXTURE = new Texture(Gdx.files.internal("ui/health_half.png"));
    private static final Texture HEALTH_NONE_TEXTURE = new Texture(Gdx.files.internal("ui/health_none.png"));

    private static final float HEALTH_SEPARATION_SCALE = 1.5f;

    private final ArrayList<Sprite> spriteList;
    private final int heartAmount;

    HealthUI(Vector2 position, int heartAmount) {
        this.heartAmount = heartAmount;
        this.spriteList = new ArrayList<>();

        Vector2 startPosition = position.cpy();
        Vector2 size = new Vector2(HEALTH_FULL_TEXTURE.getWidth(), HEALTH_FULL_TEXTURE.getHeight());
        Vector2 offset = size.scl(HEALTH_SEPARATION_SCALE, 0);

        for (int i = 0; i < heartAmount; i++) {
            Sprite sprite = new Sprite(HEALTH_FULL_TEXTURE);
            sprite.setTexture(HEALTH_FULL_TEXTURE);
            sprite.flip(false, true);
            sprite.setOrigin(0, 0);
            Vector2 finalPosition = startPosition.cpy().add(offset.cpy().scl(i));
            sprite.setPosition(finalPosition.x, finalPosition.y);
            this.spriteList.add(sprite);
        }
    }

    @Override
    public void dispose() {
        HEALTH_FULL_TEXTURE.dispose();
        HEALTH_HALF_TEXTURE.dispose();
        HEALTH_NONE_TEXTURE.dispose();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (Sprite sprite : spriteList) {
            sprite.draw(spriteBatch);
        }
    }

    public void setFullHealthTexture(int index) {
        this.spriteList.get(index).setTexture(HEALTH_FULL_TEXTURE);
    }

    public void setHalfHealthTexture(int index) {
        this.spriteList.get(index).setTexture(HEALTH_HALF_TEXTURE);
    }

    public void setNoneHealthTexture(int index) {
        this.spriteList.get(index).setTexture(HEALTH_NONE_TEXTURE);
    }

    public int getHeartAmount() {
        return heartAmount;
    }

    private boolean checkIndex(int index) {
        return index < 0 || index >= this.spriteList.size();
    }
}

