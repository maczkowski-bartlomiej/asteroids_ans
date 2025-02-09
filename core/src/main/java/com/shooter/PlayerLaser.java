package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PlayerLaser extends GameObject {
    private static final Texture LASER_TEXTURE = new Texture(Gdx.files.internal("textures/laser.png"));

    private static final int HEALTH = 1;
    private static final int DAMAGE = 1;
    private static final float SPEED = 1000.0f;
    private static final float LIFE_TIME = 3.0f;

    private float lifeTime;

    public PlayerLaser(Vector2 startPosition, float rotation, Vector2 direction) {
        this.setPosition(startPosition);
        this.setRotation(rotation);
        this.setVelocity(new Vector2(direction).nor().scl(SPEED));
        this.setTexture(LASER_TEXTURE);
        this.setHealth(HEALTH);
        this.setMaxHealth(HEALTH);
        this.setDamage(DAMAGE);

        this.lifeTime = LIFE_TIME;
    }

    @Override
    public void dispose() {
        LASER_TEXTURE.dispose();
    }

    @Override
    public void update() {
        super.update();
        lifeTime -= Gdx.graphics.getDeltaTime();
        if (lifeTime <= 0)
            this.kill();
    }

    static public int getTextureWidth() {
        return LASER_TEXTURE.getWidth();
    }
}
