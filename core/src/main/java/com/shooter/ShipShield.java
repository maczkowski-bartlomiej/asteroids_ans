package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ShipShield extends GameObject
{
    static final private Texture SHIP_SHIELD_TEXTURE = new Texture(Gdx.files.internal("textures/ship_shield.png"));

    static final private int HEALTH = 1;
    static final private int DAMAGE = 10000;
    static final private float SCALE = 1.50f;

    private final Player playerRef;

    public ShipShield(Player player) {
        this.setHealth(HEALTH);
        this.setMaxHealth(HEALTH);
        this.setTexture(SHIP_SHIELD_TEXTURE);
        this.setDamage(DAMAGE);
        this.setPosition(player.getPosition());
        this.setScale(SCALE);

        this.playerRef = player;
    }

    @Override
    public void dispose() {
        SHIP_SHIELD_TEXTURE.dispose();
    }

    @Override
    public void update() {
        setPosition(playerRef.getPosition());
        setRotation(playerRef.getRotation());
    }
}
