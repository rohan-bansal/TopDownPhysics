package me.rohanbansal.tdp.stations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.rohanbansal.tdp.Character;
import me.rohanbansal.tdp.Constants;
import me.rohanbansal.tdp.screens.PlayScreen;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.HUDText;

import static me.rohanbansal.tdp.Constants.PPM;

public class GasStation {

    private Sprite sprite, r_key;

    private boolean selectingCar = false, tooFar = false, fueling = false;
    private GlyphLayout layout = new GlyphLayout();

    private int id;

    public GasStation(Vector2 position, int id) {
        this.id = id;

        this.sprite = new Sprite(new Texture("sprites/gas_station.png"));
        this.sprite.setScale(0.2f);
        this.sprite.setCenter(position.x / PPM, position.y / PPM);


        r_key = new Sprite(new Texture("sprites/r_key.png"));
        r_key.setSize(64 / PPM, 64 / PPM);
        r_key.setCenter(sprite.getX() + sprite.getWidth() / 2 + 1, sprite.getY() + sprite.getHeight() / 2);
    }

    public void render(SpriteBatch batch, CameraController cam, Character player) {

        batch.setProjectionMatrix(cam.getCamera().combined);
        batch.begin();

        sprite.draw(batch);

        if(sprite.getBoundingRectangle().overlaps(player.getRectangle())) {
            if(!selectingCar) {
                r_key.draw(batch);
                if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                    selectingCar = true;
                }
            }
        }

        batch.end();

        batch.setProjectionMatrix(PlayScreen.HUDcamera.getCamera().combined);
        batch.begin();
        if(selectingCar) {
            layout.setText(HUDText.getDrawer(0.6f), "Press 'R' next to the car, 'Q' to cancel");
            HUDText.drawText(batch, "Press 'R' next to the car, 'Q' to cancel", (Gdx.graphics.getWidth() - layout.width) / 2, 25, Color.WHITE, 0.6f);
            player.selectingGasCar = true;
        }
        if(tooFar) {
            layout.setText(HUDText.getDrawer(0.6f), "The car is too far away!");
            HUDText.drawText(batch, "The car is too far away!", (Gdx.graphics.getWidth() - layout.width) / 2, 50, Color.SCARLET, 0.6f);
        } else if(fueling) {
            if(player.gasSelected != null) {
                int currentFuelPercent = Math.round((player.gasSelected.getFuel() / player.gasSelected.getFuelMax()) * 100);
                if(player.gasSelected.getFuel() <= player.gasSelected.getFuelMax()) {
                    player.gasSelected.incrementFuelBy(Constants.GAS_STATION_FUEL_RATE);
                } else {
                    layout.setText(HUDText.getDrawer(0.6f), "Fueling Finished");
                    //HUDText.drawTimeoutText(batch, "Fueling Finished", (Gdx.graphics.getWidth() - layout.width) / 2, 25, Color.GREEN, 0.6f, 2f);
                    finishStation(player);
                }
                layout.setText(HUDText.getDrawer(0.6f), "Fueling, 'Q' to cancel");
                HUDText.drawText(batch, "Fueling, 'Q' to cancel", (Gdx.graphics.getWidth() - layout.width) / 2, 25, Color.OLIVE, 0.6f);
                layout.setText(HUDText.getDrawer(0.8f), currentFuelPercent + "%");
                HUDText.drawText(batch, currentFuelPercent + "%", (Gdx.graphics.getWidth() - layout.width) / 2, 55, Color.LIME, 0.8f);
            }
        }


        batch.end();
        batch.setProjectionMatrix(cam.getCamera().combined);

        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            finishStation(player);
        }

        if(selectingCar) {
            if(player.gasSelected != null) {
                if(player.gasSelected.getBody().getPosition().dst(sprite.getX(), sprite.getY()) > Constants.CAR_TO_GAS_STATION) {
                    tooFar = true;
                } else {
                    tooFar = false;
                    selectingCar = false;
                    fueling = true;
                }
            }
        }
    }

    private void finishStation(Character player) {
        player.selectingGasCar = false;
        player.gasSelected = null;
        selectingCar = false;
        tooFar = false;
        fueling = false;
    }
}
