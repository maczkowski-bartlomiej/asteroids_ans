package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PowerUpPickup extends GameObject
{
    public enum Type {
        SHIELD,
        HEALTH,
        LASERS
    }

    static final private Texture SHIELD_PICKUP_TEXTURE = new Texture(Gdx.files.internal("textures/shield_pickup.png"));
    static final private Texture HEALTH_PICKUP_TEXTURE = new Texture(Gdx.files.internal("textures/health_restore_pickup.png"));
    static final private Texture LASERS_PICKUP_TEXTURE = new Texture(Gdx.files.internal("textures/laser_upgrade_pickup.png"));

    public static final Sound SHIELD_PICKUP_SOUND = Gdx.audio.newSound(Gdx.files.internal("audio/shield_powerup.wav"));
    public static final Sound HEALTH_PICKUP_SOUND = Gdx.audio.newSound(Gdx.files.internal("audio/health_powerup.wav"));
    public static final Sound LASERS_PICKUP_SOUND = Gdx.audio.newSound(Gdx.files.internal("audio/laser_upgrade_powerup.wav"));

    static final private int HEALTH = 1;

    private final Type type;

    public PowerUpPickup(Vector2 position) {
        PowerUpPickup.Type[] types = PowerUpPickup.Type.values();
        this.type = types[MathUtils.random(types.length - 1)];

        this.setTexture(getTextureByType());
        this.setRotation(MathUtils.random(360.0f));
        this.setHealth(HEALTH);
        this.setMaxHealth(HEALTH);
        this.setPosition(clipPosition(position));
    }

    @Override
    public void dispose() {
        SHIELD_PICKUP_TEXTURE.dispose();
        HEALTH_PICKUP_TEXTURE.dispose();
        LASERS_PICKUP_TEXTURE.dispose();
        SHIELD_PICKUP_SOUND.dispose();
        HEALTH_PICKUP_SOUND.dispose();
        LASERS_PICKUP_SOUND.dispose();
    }

    @Override
    public void onDeath() {
        super.onDeath();
        switch (type) {
            case SHIELD -> SHIELD_PICKUP_SOUND.play();
            case HEALTH -> HEALTH_PICKUP_SOUND.play();
            case LASERS -> LASERS_PICKUP_SOUND.play();
        }
    }

    public Type getType() {
        return this.type;
    }

    private Vector2 clipPosition(Vector2 position) {
        if (position.x < 0)
            position.x = 0;
        else if (position.x > Gdx.graphics.getWidth())
            position.x = Gdx.graphics.getWidth();

        if (position.y < 0)
            position.y = 0;
        else if (position.y > Gdx.graphics.getHeight())
            position.y = Gdx.graphics.getHeight();

        return position;
    }

    private Texture getTextureByType() {
        return switch (type) {
            case SHIELD -> SHIELD_PICKUP_TEXTURE;
            case HEALTH -> HEALTH_PICKUP_TEXTURE;
            case LASERS -> LASERS_PICKUP_TEXTURE;
        };
    }
}
