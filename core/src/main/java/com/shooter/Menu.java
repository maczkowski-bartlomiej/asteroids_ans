package com.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shooter.ui.MenuBackgroundUI;

public class Menu implements IGameState {
    private static final BitmapFont font = new BitmapFont(Gdx.files.internal("Fifth Grader.fnt"), Gdx.files.internal("Fifth Grader.png"), false);

    MenuBackgroundUI menuBackgroundUI;

    TextButton startButton;
    TextButton exitButton;
    boolean goToGame;

    Stage stage;

    @Override
    public void dispose() {
        font.dispose();
        menuBackgroundUI.dispose();
    }

    @Override
    public void reset() {
        menuBackgroundUI = new MenuBackgroundUI();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        goToGame = false;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Vector2 buttonSize = new Vector2(screenSize.x * 0.05f, screenSize.y * 0.02f);

        startButton = new TextButton("Start", buttonStyle);
        startButton.setSize(buttonSize.x, buttonSize.y);
        startButton.setPosition(screenSize.x / 2 - buttonSize.x / 2, screenSize.y / 2 + buttonSize.y * 2 + 160);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToGame = true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        exitButton = new TextButton("Exit", buttonStyle);
        exitButton.setSize(buttonSize.x, buttonSize.y);
        exitButton.setPosition(screenSize.x / 2 - buttonSize.x / 2, screenSize.y / 2 - buttonSize.y * 2 + 170);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        stage.addActor(startButton);
        stage.addActor(exitButton);
    }

    @Override
    public IGameState.State update() {
        stage.act(Gdx.graphics.getDeltaTime());

        if (goToGame) {
            return IGameState.State.GAME;
        }
        else
            return IGameState.State.MENU;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        menuBackgroundUI.render(spriteBatch);
        spriteBatch.end();
        stage.draw();
    }
}
