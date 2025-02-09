package com.shooter.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shooter.IDrawable;

public class GameUI implements IDrawable {

    private final ScoreUI scoreUI;
    private final HealthUI healthUI;
    private final BackgroundUI backgroundUI;

    public GameUI(BitmapFont font, int playerMaxHealth) {
        this.backgroundUI = new BackgroundUI();
        this.healthUI = new HealthUI(new Vector2(0, 0), playerMaxHealth / 2);
        this.scoreUI = new ScoreUI(new Vector2(Gdx.graphics.getWidth() - 200, 0), font);
        onPlayerHealthChange(playerMaxHealth);
    }

    @Override
    public void dispose() {
        scoreUI.dispose();
        healthUI.dispose();
        backgroundUI.dispose();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        healthUI.render(spriteBatch);
        scoreUI.render(spriteBatch);
    }

    public void renderBackground(SpriteBatch spriteBatch) {
        backgroundUI.render(spriteBatch);
    }

    public void onPlayerHealthChange(int health) {
        System.out.println(health);
        int remainingHealth = health;
        int heartCount = healthUI.getHeartAmount();

        for (int i = 0; i < heartCount; i++) {
            if (remainingHealth >= 2) {
                healthUI.setFullHealthTexture(i);
                remainingHealth -= 2;
            } else if (remainingHealth == 1) {
                healthUI.setHalfHealthTexture(i);
                remainingHealth = 0;
            } else {
                healthUI.setNoneHealthTexture(i);
            }
        }
    }

    public void onScoreChange(int score) {
        scoreUI.setScore(score);
    }
}
