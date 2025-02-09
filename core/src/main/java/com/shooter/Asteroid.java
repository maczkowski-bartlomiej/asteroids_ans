package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid extends GameObject {
    private static final Texture ASTEROID_TEXTURE = new Texture(Gdx.files.internal("textures/asteroid_1.png"));

    private static final Sound HIT_SOUND_1 = Gdx.audio.newSound(Gdx.files.internal("audio/asteroid_hit_1.wav"));
    private static final Sound HIT_SOUND_2 = Gdx.audio.newSound(Gdx.files.internal("audio/asteroid_hit_2.wav"));
    private static final Sound DEATH_SOUND_1 = Gdx.audio.newSound(Gdx.files.internal("audio/asteroid_destroy_1.wav"));
    private static final Sound DEATH_SOUND_2 = Gdx.audio.newSound(Gdx.files.internal("audio/asteroid_destroy_2.wav"));

    private static final int BASE_HEALTH = 20;
    private static final int DAMAGE = 1;
    private static final float MAX_VELOCITY = 150.0f;
    private static final float OUTSIDE_SPAWN_AREA_SIZE = 500.0f;
    private static final float MIN_SCALE = 0.5f;
    private static final float MAX_SCALE = 1.65f;

    private final float rotationSpeed;

    public Asteroid() {
        float rotation = MathUtils.random(0.0f, 360.0f);
        float scale = MathUtils.random(MIN_SCALE, MAX_SCALE);
        int health = (int)(BASE_HEALTH * scale);

        this.rotationSpeed = MathUtils.random(360.0f);

        this.setScale(scale);
        this.setTexture(ASTEROID_TEXTURE);
        this.setRotation(rotation);
        this.setDamage(DAMAGE);
        this.setHealth(health);
        this.setMaxHealth(health);

        this.spawnOutsideScreen();
    }

    @Override
    public void dispose() {
        ASTEROID_TEXTURE.dispose();
        HIT_SOUND_1.dispose();
        HIT_SOUND_2.dispose();
        DEATH_SOUND_1.dispose();
        DEATH_SOUND_2.dispose();
    }

    @Override
    public void update() {
        super.update();
        this.handleScreenTeleport();
        this.setRotation(getRotation() + rotationSpeed * Gdx.graphics.getDeltaTime());
    }

    @Override
    public void onHit(int damage) {
        super.onHit(damage);
        switch (MathUtils.random(0, 1)) {
            case 0 -> HIT_SOUND_1.play();
            case 1 -> HIT_SOUND_2.play();
        }

    }

    @Override
    public void onDeath() {
        super.onDeath();
        switch (MathUtils.random(0, 1)) {
            case 0 -> DEATH_SOUND_1.play();
            case 1 -> DEATH_SOUND_2.play();
        }
    }

    private void spawnOutsideScreen() {
        this.setPosition(getRandomSpawnPositionOnEdge());
        this.setVelocity(getRandomVelocity());
    }

    private Vector2 getRandomSpawnPositionOnEdge() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        float extendedWidth = screenWidth + OUTSIDE_SPAWN_AREA_SIZE;
        float extendedHeight = screenHeight + OUTSIDE_SPAWN_AREA_SIZE;
        int edge = MathUtils.random(0, 4);

        return switch (edge) {
            case 0 -> new Vector2(-OUTSIDE_SPAWN_AREA_SIZE, MathUtils.random() * extendedHeight);
            case 1 -> new Vector2(screenWidth + OUTSIDE_SPAWN_AREA_SIZE, MathUtils.random() * extendedHeight);
            case 2 -> new Vector2(MathUtils.random() * extendedWidth, -OUTSIDE_SPAWN_AREA_SIZE);
            default -> new Vector2(MathUtils.random() * extendedWidth, screenHeight + OUTSIDE_SPAWN_AREA_SIZE);
        };
    }

    private Vector2 getRandomVelocity() {
        float velocityRange = 2 * MAX_VELOCITY;
        return new Vector2(
            MathUtils.random() * velocityRange - MAX_VELOCITY,
            MathUtils.random() * velocityRange - MAX_VELOCITY
        );
    }

    private void handleScreenTeleport() {
        Vector2 position = this.getPosition();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if (position.x < -OUTSIDE_SPAWN_AREA_SIZE)
            position.x = screenWidth + OUTSIDE_SPAWN_AREA_SIZE;
        else if (position.x > screenWidth + OUTSIDE_SPAWN_AREA_SIZE)
            position.x = -OUTSIDE_SPAWN_AREA_SIZE;

        if (position.y < -OUTSIDE_SPAWN_AREA_SIZE)
            position.y = screenHeight + OUTSIDE_SPAWN_AREA_SIZE;
        else if (position.y > screenHeight + OUTSIDE_SPAWN_AREA_SIZE)
            position.y = -OUTSIDE_SPAWN_AREA_SIZE;

        this.setPosition(position);
    }
}
