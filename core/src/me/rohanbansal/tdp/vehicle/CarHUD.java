package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.rohanbansal.tdp.screens.PlayScreen;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;

import java.util.ArrayList;

public class CarHUD {

    private Car car;
    private SpriteBatch batch;
    private BitmapFont drawer = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));

    private Sprite z_key, x_key, c_key, speedometer_dial, speedometer_needle, durability, triangle, fuel_dial, fuel_needle;
    private Rectangle zO_key, xO_key, cO_key;
    private ArrayList<Sprite> keys;

    public CarHUD(Car car) {
        this.car = car;
        batch = new SpriteBatch();
        drawer.getData().setScale(0.6f);
        drawer.setColor(Color.WHITE);

        keys = new ArrayList<>();

        z_key = new Sprite(new Texture("sprites/z_key.png"));
        keys.add(z_key);
        x_key = new Sprite(new Texture("sprites/x_key.png"));
        keys.add(x_key);
        c_key = new Sprite(new Texture("sprites/c_key.png"));
        keys.add(c_key);

        speedometer_dial = new Sprite(new Texture(("sprites/green_speedometer.png")));
        speedometer_dial.setPosition(10, 0);

        fuel_dial = new Sprite(new Texture(("sprites/fuel2.png")));
        fuel_dial.setPosition(250, 0);

        triangle = new Sprite(new Texture("sprites/triangle.png"));

        durability = new Sprite(new Texture(("sprites/durability.png")));
        durability.setPosition(450, 30);

        speedometer_needle = new Sprite(new Texture(("sprites/needle.png")));
        speedometer_needle.setOrigin(13, 13);
        speedometer_needle.setPosition(100, 55);

        fuel_needle = new Sprite(new Texture(("sprites/fuel_needle.png")));
        fuel_needle.setOrigin(9, 9);
        fuel_needle.setPosition(310, 40);

        z_key.setPosition(Gdx.graphics.getWidth() - 25, 30);
        x_key.setPosition(Gdx.graphics.getWidth() - 25, 60);
        c_key.setPosition(Gdx.graphics.getWidth() - 25, 90);
        z_key.setScale(2f);
        x_key.setScale(2f);
        c_key.setScale(2f);

        zO_key = new Rectangle(z_key.getX(), z_key.getY(), z_key.getWidth(), z_key.getHeight());
        xO_key = new Rectangle(x_key.getX(), x_key.getY(), x_key.getWidth(), x_key.getHeight());
        cO_key = new Rectangle(c_key.getX(), c_key.getY(), c_key.getWidth(), c_key.getHeight());
    }

    public void render(ModifiedShapeRenderer renderer, CameraController camera) {

        batch.setProjectionMatrix(PlayScreen.HUDcamera.getCamera().combined);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.enableBlending();
        batch.begin();

        speedometer_dial.draw(batch);
        fuel_dial.draw(batch);
        speedometer_needle.draw(batch);
        fuel_needle.draw(batch);
        durability.draw(batch);

        drawer.draw(batch, "Durability", 460, 20);
        // start = 237, end = 327
        triangle.setPosition(((car.getDurability() / car.getMaxDurability()) * 90) + 447, 40);
        triangle.draw(batch);

        if(car.getBody().getLinearVelocity().len() < 20) {
            for(Sprite key : keys) {
                if(key.getColor().a != 1) {
                    key.setAlpha(key.getColor().a += 0.01f);
                }
            }
            if(zO_key.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                z_key.setPosition(Gdx.graphics.getWidth() - 150, 30);
            } else {
                z_key.setPosition(Gdx.graphics.getWidth() - 25, 30);
            }
            if(xO_key.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                x_key.setPosition(Gdx.graphics.getWidth() - 150, 60);
            } else {
                x_key.setPosition(Gdx.graphics.getWidth() - 25, 60);
            }
            if(cO_key.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                c_key.setPosition(Gdx.graphics.getWidth() - 150, 90);
            } else {
                c_key.setPosition(Gdx.graphics.getWidth() - 25, 90);
            }
        } else {
            for(Sprite key : keys) {
                if(key.getColor().a != 0) {
                    key.setAlpha(key.getColor().a -= 0.01f);
                }
            }
        }
        z_key.draw(batch);
        drawer.draw(batch, "Zoom In", z_key.getX() + 27, z_key.getY() + 12);
        c_key.draw(batch);
        drawer.draw(batch, "Zoom Out", c_key.getX() + 27, c_key.getY() + 12);
        x_key.draw(batch);
        drawer.draw(batch, "Reset Zoom", x_key.getX() + 27, x_key.getY() + 12);

        batch.end();

        // THE MAGIC ONE LINERS

        speedometer_needle.setRotation(-175 - car.getBody().getLinearVelocity().len());

        //fuel_needle.setRotation(15 - ((455 * car.getFuel()) / car.getFuelMax()));
        // -80 full, 190 empty
        fuel_needle.setRotation(190 - ((270 * car.getFuel()) / car.getFuelMax()));
    }
}
