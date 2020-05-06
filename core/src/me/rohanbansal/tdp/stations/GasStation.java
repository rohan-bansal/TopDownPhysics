package me.rohanbansal.tdp.stations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.rohanbansal.tdp.tools.CameraController;

import static me.rohanbansal.tdp.Constants.PPM;

public class GasStation {

    private Sprite sprite;

    public GasStation(Vector2 position) {
        this.sprite = new Sprite(new Texture("sprites/gas_station.png"));
        this.sprite.setScale(0.2f);
        this.sprite.setCenter(position.x / PPM, position.y / PPM);
    }

    public void render(SpriteBatch batch, CameraController cam) {

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        sprite.draw(batch);

        batch.end();
    }
}
