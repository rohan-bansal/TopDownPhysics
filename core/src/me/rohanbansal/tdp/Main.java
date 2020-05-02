package me.rohanbansal.tdp;

import com.badlogic.gdx.Game;
import me.rohanbansal.tdp.screens.PlayScreen;

public class Main extends Game {
	
	@Override
	public void create () {
		setScreen(new PlayScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
