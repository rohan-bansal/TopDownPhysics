package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerFinalizerInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.BodyHolder;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;

import static me.rohanbansal.tdp.Constants.PPM;

public class Wheel extends BodyHolder {

    public static final int TOP_RIGHT = 0;
    public static final int TOP_LEFT = 1;
    public static final int BOTTOM_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;

    private Car car;
    private boolean powered;
    private Vector2 size;


    public Wheel(Vector2 position, Vector2 size, BodyDef.BodyType type, World world, float density, int id, Car car, boolean powered) {
        super(position, size, type, world, density, true, id);
        this.car = car;
        this.powered = powered;
        this.size = new Vector2(size.x * 2 / PPM, size.y * 2 / PPM);
    }

    public void setAngle(float angle) {
        getBody().setTransform(getBody().getPosition(), this.car.getBody().getAngle() + MathUtils.degreesToRadians * angle);
    }

    public boolean isPowered() {
        return powered;
    }

    @Override
    public void update(float delta, CameraController camera, ModifiedShapeRenderer renderer) {
        super.update(delta, camera, renderer);

        renderer.setProjectionMatrix(camera.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);

        renderer.rect(getBody().getPosition().x - size.x / 2,
                getBody().getPosition().y - size.y / 2,
                size.x / 2,
                size.y / 2,
                size.x,
                size.y,
                1.0f,
                1.0f,
                (float) Math.toDegrees(getBody().getAngle()));

        renderer.end();
    }


}
