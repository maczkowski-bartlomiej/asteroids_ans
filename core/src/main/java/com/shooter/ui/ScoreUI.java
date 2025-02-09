package com.shooter.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shooter.IDrawable;

public class ScoreUI implements IDrawable
{
    private int score;
    private final Vector2 position;
    private final BitmapFont font;

    ScoreUI(Vector2 position, BitmapFont font) {
        this.position = position;
        this.font = font;
        this.score = 0;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        font.draw(spriteBatch, "Score\n" + score, position.x, position.y);
    }

    public void setScore(int score) {
        this.score = score;
    }
}

