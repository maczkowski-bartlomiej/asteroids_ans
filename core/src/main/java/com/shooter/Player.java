package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends GameObject {
    private enum ShootMode {
        SINGLE,
        DOUBLE,
        TRIPLE
    }

    private static final Texture SHIP_TEXTURE = new Texture(Gdx.files.internal("textures/ship.png"));
    private static final Texture THRUST_TEXTURE = new Texture(Gdx.files.internal("textures/thrust.png"));

    private static final Sound SHOOT_SOUND_1 = Gdx.audio.newSound(Gdx.files.internal("audio/shoot_1.wav"));
    private static final Sound SHOOT_SOUND_2 = Gdx.audio.newSound(Gdx.files.internal("audio/shoot_2.wav"));
    private static final Sound SHOOT_SOUND_3 = Gdx.audio.newSound(Gdx.files.internal("audio/shoot_3.wav"));

    private static final Sound DEATH_SOUND = Gdx.audio.newSound(Gdx.files.internal("audio/player_die.wav"));

    private static final int HEALTH = 6;
    private static final int DAMAGE = 9999;
    private static final float THRUST = 10.0f;

    private static final float GUN_OFFSET = 5.5f;
    private static final float LASER_OFFSET_FACTOR = 1.0f / 3.0f / 4.0f;
    private static final float LASER_ANGLE_OFFSET = 1.3f;

    private final Vector2 direction;
    private final Sprite thrustSprite;
    private ShootMode shootMode;

    private boolean thrustVisible = false;

    public Player(Vector2 position) {
        this.setHealth(HEALTH);
        this.setMaxHealth(HEALTH);
        this.setPosition(position);
        this.setTexture(SHIP_TEXTURE);
        this.setDamage(DAMAGE);

        this.direction = new Vector2();

        this.thrustSprite = new Sprite(THRUST_TEXTURE);
        this.thrustSprite.setOriginCenter();
        this.thrustSprite.setScale(1.25f);

        this.shootMode = ShootMode.SINGLE;
    }

    @Override
    public void dispose() {
        SHIP_TEXTURE.dispose();
        THRUST_TEXTURE.dispose();
        SHOOT_SOUND_1.dispose();
        SHOOT_SOUND_2.dispose();
        SHOOT_SOUND_3.dispose();
        DEATH_SOUND.dispose();
    }


    @Override
    public void update() {
        super.update();
        handleScreenTeleport();

        if (thrustVisible) {
            Vector2 size = getSize();
            Vector2 thrustOffset = new Vector2(0, size.y / 2.0f);
            Vector2 thrustPosition = thrustOffset.cpy();
            thrustPosition.rotateDeg(getRotation());
            thrustPosition.add(
                getPosition().x - size.x / 2,
                getPosition().y - size.y / 2
            );

            thrustSprite.setPosition(thrustPosition.x, thrustPosition.y);
            thrustSprite.setRotation(getRotation());
        }
    }


    @Override
    public void render(SpriteBatch spriteBatch) {
        if (thrustVisible) {
            thrustSprite.draw(spriteBatch);
            thrustVisible = !thrustVisible;
        }

        super.render(spriteBatch);
    }

    @Override
    public void onHit(int damage) {
        super.onHit(damage);
        if (damage != 0)
            shootMode = ShootMode.SINGLE;
    }

    @Override
    public void onDeath() {
        super.onDeath();
        DEATH_SOUND.play();
    }

    public void incrementLaserShootMode() {
        switch (shootMode) {
            case SINGLE -> shootMode = ShootMode.DOUBLE;
            case DOUBLE -> shootMode = ShootMode.TRIPLE;
        }
    }

    public void handleInputRotation(Vector2 mousePosition) {
        direction.set(mousePosition).sub(getPosition());
        setRotation(direction.angleDeg() + 90.0f);
    }

    private void handleScreenTeleport() {
        Vector2 position = getPosition();
        Vector2 size = getSize();
        float halfWidth = size.x / 2.0f;
        float halfHeight = size.y / 2.0f;
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if (position.x + halfWidth < 0) position.x = screenWidth + halfWidth;
        else if (position.x - halfWidth > screenWidth) position.x = -halfWidth;

        if (position.y + halfHeight < 0) position.y = screenHeight + halfHeight;
        else if (position.y - halfHeight > screenHeight) position.y = -halfHeight;

        setPosition(position);
    }

    public void applyThrust() {
        Vector2 thrustVector = new Vector2(0, THRUST).setAngleDeg(direction.angleDeg());
        setVelocity(getVelocity().add(thrustVector));
        thrustVisible = true;
    }

    List<PlayerLaser> shoot() {
        List<PlayerLaser> lasers = spawnLasers();
        switch (MathUtils.random(0, 2)) {
            case 0 -> SHOOT_SOUND_1.play();
            case 1 -> SHOOT_SOUND_2.play();
            case 2 -> SHOOT_SOUND_3.play();
        }

        return lasers;
    }

    private List<PlayerLaser> spawnLasers()
    {
        ArrayList<PlayerLaser> lasers = new ArrayList<>();
        ArrayList<Vector2> initialPoints = new ArrayList<>();

        Vector2 size = this.getSize();
        float halfWidth = size.x / 2.0f;
        float height = size.y;
        Vector2 left = new Vector2(getPosition().add(-halfWidth + GUN_OFFSET, -height));
        Vector2 right = new Vector2(getPosition().add(halfWidth - GUN_OFFSET, -height));

        float offset = PlayerLaser.getTextureWidth() * LASER_OFFSET_FACTOR;
        switch (shootMode) {
            case SINGLE:
                initialPoints.addAll(Arrays.asList(left, right));
                break;
            case DOUBLE:
                initialPoints.addAll(Arrays.asList(
                    left.cpy().sub(offset, 0), left.cpy().add(offset, 0),
                    right.cpy().add(offset, 0), right.cpy().sub(offset, 0)));
                break;
            case TRIPLE:
                initialPoints.addAll(Arrays.asList(
                    left, left.cpy().sub(offset, 0), left.cpy().add(offset, 0),
                    right, right.cpy().add(offset, 0), right.cpy().sub(offset, 0)));
                break;
        }

        for (int i = 0; i < initialPoints.size(); i++) {
            Vector2 direction = this.direction.cpy();
            float rotation = this.getRotation();
            if (i < initialPoints.size() / 2) {
                rotation += LASER_ANGLE_OFFSET;
                direction.rotateDeg(LASER_ANGLE_OFFSET);
            } else {
                rotation -= LASER_ANGLE_OFFSET;
                direction.rotateDeg(-LASER_ANGLE_OFFSET);
            }

            Vector2 spawnPosition = initialPoints.get(i).rotateAroundDeg(getPosition(), rotation);
            PlayerLaser laser = new PlayerLaser(spawnPosition, rotation, direction);
            lasers.add(laser);
        }

        return lasers;
    }
}
