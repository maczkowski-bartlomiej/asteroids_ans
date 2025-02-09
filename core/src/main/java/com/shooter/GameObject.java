package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject implements IDrawable {

    private final Vector2 position;
    private final Vector2 velocity;
    private final TextureRegion textureRegion;

    private int damage;
    private int health;
    private int maxHealth;

    private float rotation;
    private float scale;

    public Event onHitEvent;
    public Event onDeathEvent;

    GameObject() {
        damage = 0;
        health = 0;
        maxHealth = 0;
        rotation = 0.0f;
        scale = 1.0f;

        position = new Vector2();
        velocity = new Vector2();
        textureRegion = new TextureRegion();

        onHitEvent = new Event();
        onDeathEvent = new Event();
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        spriteBatch.draw(textureRegion,
            position.x - halfWidth, position.y - halfHeight,
            halfWidth, halfHeight,
            width, height,
            scale, scale,
            rotation
        );
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public Vector2 getSize() {
        return new Vector2(textureRegion.getRegionWidth() * scale, textureRegion.getRegionHeight() * scale);
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Texture getTexture() {
        return textureRegion.getTexture();
    }

    public Rectangle getRect() {
        float width = textureRegion.getRegionWidth() * scale;
        float height = textureRegion.getRegionHeight() * scale;

        float x = position.x - width / 2.0f;
        float y = position.y - height / 2.0f;

        return new Rectangle(x, y, width, height);
    }


    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setTexture(Texture texture) {
        textureRegion.setTexture(texture);
        textureRegion.setRegionWidth(texture.getWidth());
        textureRegion.setRegionHeight(texture.getHeight());
        textureRegion.flip(false, true);
    }

    public void addHealth(int amount) {
        if (amount < 0) return;
        health = Math.min(health + amount, maxHealth);
    }

    public void subHealth(int amount) {
        if (amount < 0) return;
        health = Math.max(health - amount, 0);

        if (health == 0)
            onDeath();
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void kill() {
        health = 0;
        onDeath();
    }

    public void onHit(int damage) {
        subHealth(damage);
        onHitEvent.broadcast(new EventArgs(this));
    }

    public void onDeath() {
        onDeathEvent.broadcast(new EventArgs(this));
    }
}
