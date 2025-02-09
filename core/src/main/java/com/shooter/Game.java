package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shooter.ui.GameUI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Game implements IGameState {

    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("Fifth Grader.fnt"), Gdx.files.internal("Fifth Grader.png"), true);

    private static final float POWER_UP_SPAWN_CHANCE = 0.45f;
    private static final int INITIAL_ASTEROID_COUNT = 50;
    private static final float ASTEROID_SPAWN_TIME = 1.5f;

    private static final Set<String> ALLOWED_COLLISIONS = Set.of(
        createKey(PlayerLaser.class, Asteroid.class),
        createKey(Asteroid.class, Player.class),
        createKey(PowerUpPickup.class, Player.class),
        createKey(ShipShield.class, Asteroid.class)
    );

    private static String createKey(Class<?> a, Class<?> b) {
        if (a.getName().compareTo(b.getName()) <= 0)
            return a.getName() + ":" + b.getName();
        else
            return b.getName() + ":" + a.getName();
    }

    private int score;

    private float asteroidSpawnTimer;

    private boolean isGameOver;
    private boolean isShieldActive;

    private GameUI gameUI;
    private Player player;
    private List<GameObject> gameObjects;
    private List<GameObject> newObjectsBuffer;

    @Override
    public void reset() {
        Gdx.input.setInputProcessor(null);
        gameObjects = new ArrayList<>();
        newObjectsBuffer = new ArrayList<>();

        gameObjects.add(new Player(new Vector2(Gdx.graphics.getWidth() / 2.0f,
            Gdx.graphics.getHeight() / 2.0f)));
        player = (Player) gameObjects.getFirst();

        spawnAsteroids(INITIAL_ASTEROID_COUNT);

        this.gameUI = new GameUI(font, player.getMaxHealth());

        player.onHitEvent.addListener(this::handlePlayerHit);
        player.onDeathEvent.addListener(this::handlePlayerDeath);

        score = 0;

        isGameOver = false;
        isShieldActive = false;
        asteroidSpawnTimer = ASTEROID_SPAWN_TIME;
    }

    @Override
    public void dispose() {
        gameObjects.forEach(GameObject::dispose);
        newObjectsBuffer.forEach(GameObject::dispose);
        font.dispose();
    }

    @Override
    public IGameState.State update() {

        asteroidSpawnTimer -= Gdx.graphics.getDeltaTime();
        if (asteroidSpawnTimer <= 0) {
            spawnAsteroids(1);
            asteroidSpawnTimer = ASTEROID_SPAWN_TIME;
        }

        handleInput();

        Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            gameObject.update();

            if (!gameObject.isDead()) {
                checkCollisions(gameObject);
            } else {
                iterator.remove();
            }
        }

        gameObjects.addAll(newObjectsBuffer);
        newObjectsBuffer.clear();

        if (isGameOver)
            return IGameState.State.MENU;
        else
            return IGameState.State.GAME;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        gameUI.renderBackground(spriteBatch);

        for (GameObject gameObject : gameObjects) {
            gameObject.render(spriteBatch);
        }

        gameUI.render(spriteBatch);
        spriteBatch.end();
    }


    private void handleInput() {
        player.handleInputRotation(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.applyThrust();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            List<PlayerLaser> lasers = player.shoot();
            newObjectsBuffer.addAll(lasers);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            isGameOver = true;
    }

    private void checkCollisions(GameObject gameObject) {
        for (GameObject other : gameObjects) {
            if (gameObject.isDead() || other.isDead()) continue;
            if (gameObject.getClass() == other.getClass()) continue;
            if (!ALLOWED_COLLISIONS.contains(createKey(gameObject.getClass(), other.getClass()))) continue;
            if (!gameObject.getRect().overlaps(other.getRect())) continue;

            gameObject.onHit(other.getDamage());
            other.onHit(gameObject.getDamage());
        }
    }

    private void handlePlayerHit(EventArgs args) {
        gameUI.onPlayerHealthChange(player.getHealth());
    }

    private void handlePlayerDeath(EventArgs args) {
        isGameOver = true;
    }

    private void handleAsteroidDeath(EventArgs eventArgs) {
        Asteroid asteroid = (Asteroid)eventArgs.gameObject;

        newObjectsBuffer.add(new AsteroidRip(
            asteroid.getPosition(),
            asteroid.getRotation(),
            asteroid.getScale()
        ));
        newObjectsBuffer.add(new Explosion(
            asteroid.getPosition(),
            asteroid.getRotation(),
            asteroid.getScale()
        ));

        if (shouldSpawnPowerUp()) {
            PowerUpPickup powerUpPickup = new PowerUpPickup(asteroid.getPosition());
            powerUpPickup.onDeathEvent.addListener(this::handlePowerUpPickupDeath);
            newObjectsBuffer.add(powerUpPickup);
        }

        score += (int)(10.0f * asteroid.getScale());
        gameUI.onScoreChange(score);
    }

    private void handleShipShieldDeath(EventArgs args) {
        isShieldActive = false;
    }

    private void handlePowerUpPickupDeath(EventArgs eventArgs) {
        PowerUpPickup powerUpPickup = (PowerUpPickup) eventArgs.gameObject;
        switch (powerUpPickup.getType()) {
            case SHIELD -> applyShieldPowerUp();
            case HEALTH -> applyExtraHealthPowerUp();
            case LASERS -> applyLaserUpgradePowerUp();
        }
    }

    private void applyShieldPowerUp() {
        if (!isShieldActive) {
            ShipShield shipShield = new ShipShield(player);
            shipShield.onDeathEvent.addListener(this::handleShipShieldDeath);
            newObjectsBuffer.add(shipShield);
            isShieldActive = true;
        }
    }

    private void applyExtraHealthPowerUp() {
        player.addHealth(1);
        gameUI.onPlayerHealthChange(player.getHealth());
    }

    private void applyLaserUpgradePowerUp() {
        player.incrementLaserShootMode();
    }

    private void spawnAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            Asteroid asteroid = new Asteroid();
            asteroid.onDeathEvent.addListener(this::handleAsteroidDeath);
            newObjectsBuffer.add(asteroid);
        }
    }

    private boolean shouldSpawnPowerUp() {
        return MathUtils.random() < POWER_UP_SPAWN_CHANCE;
    }
}
