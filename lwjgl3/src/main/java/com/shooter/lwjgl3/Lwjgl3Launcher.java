package com.shooter.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.shooter.Engine;


public class Lwjgl3Launcher {
    public static void main(String[] args) {
        try {
            if (StartupHelper.startNewJvmIfRequired()) return;
            createApplication();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Engine(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Asteroids");
        configuration.useVsync(true);
        configuration.setWindowedMode(1920, 1080);
        configuration.setDecorated(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setResizable(false);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}
