package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Explosion extends GameObject
{
    private static final Texture EXPLOSION_TEXTURE = new Texture(Gdx.files.internal("textures/explosion.png"));

    private static final int HEALTH = 1;
    private static final int DAMAGE = 0;
    private static final float LIFE_TIME = 0.15f;
    private static final float SCALE_MODIFIER = 1.25f;

    private float lifeTime;

    public Explosion(Vector2 position, float rotation, float scale) {
        this.setPosition(position);
        this.setRotation(rotation);
        this.setScale(scale * SCALE_MODIFIER);
        this.setTexture(EXPLOSION_TEXTURE);
        this.setHealth(HEALTH);
        this.setMaxHealth(HEALTH);
        this.setDamage(DAMAGE);
        this.lifeTime = LIFE_TIME * scale;
    }

    @Override
    public void dispose() {
        EXPLOSION_TEXTURE.dispose();
    }

    @Override
    public void update() {
        super.update();
        lifeTime -= Gdx.graphics.getDeltaTime();
        if (lifeTime <= 0.0f) {
            kill();
        }
    }
}
