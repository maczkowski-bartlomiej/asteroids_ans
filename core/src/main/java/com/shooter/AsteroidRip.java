package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class AsteroidRip extends GameObject
{
    private static final Texture ASTEROID_RIP_TEXTURE_1 = new Texture(Gdx.files.internal("textures/asteroid_2.png"));
    private static final Texture ASTEROID_RIP_TEXTURE_2 = new Texture(Gdx.files.internal("textures/asteroid_3.png"));

    private static final int HEALTH = 1;
    private static final float LIFE_TIME = 0.25f;

    private float lifeTime;

    public AsteroidRip(Vector2 position, float rotation, float scale) {
        this.setPosition(position);
        this.setRotation(rotation);
        this.setScale(scale);
        this.setTexture(ASTEROID_RIP_TEXTURE_1);
        this.setHealth(HEALTH);
        this.setMaxHealth(HEALTH);
        this.lifeTime = LIFE_TIME * scale;
    }

    @Override
    public void dispose() {
        ASTEROID_RIP_TEXTURE_1.dispose();
        ASTEROID_RIP_TEXTURE_2.dispose();
    }

    @Override
    public void update() {
        super.update();
        this.lifeTime -= Gdx.graphics.getDeltaTime();

        if (this.lifeTime <= LIFE_TIME / 2.0f)
            this.setTexture(ASTEROID_RIP_TEXTURE_2);

        if (this.lifeTime <= 0.0f)
            this.kill();
    }
}
