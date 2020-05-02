package me.rohanbansal.tdp.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import me.rohanbansal.tdp.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Top Down Physics";
		config.addIcon("effects/particle.png", Files.FileType.Internal);
		config.resizable = false;
		config.width = 1000;
		config.height = 900;

		new LwjglApplication(new Main(), config);
	}
}
