package me.rohanbansal.tdp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.vehicle.Car;

import static me.rohanbansal.tdp.Constants.PPM;

public class Character extends BodyHolder implements Disposable {

    private static final float FRAME_DURATION = 0.25f;
    private static final float MOVE_SPEED = 0.1f;
    private static final Vector2 SIZE = new Vector2(80, 90);

    private TextureAtlas atlas;
    private Animation<AtlasRegion> walkAnimationUp;
    private Animation<AtlasRegion> walkAnimationLeft;
    private Animation<AtlasRegion> walkAnimationRight;
    private Animation<AtlasRegion> walkAnimationDown;

    private float stateTime = 0f;
    public boolean inCar = false;

    public Car car = null;
    private AtlasRegion currentFrame = null;

    public Character(final Vector2 position, final World world) {
        super(position.scl(PPM), new Vector2(SIZE.x / 2, SIZE.y / 2), BodyDef.BodyType.DynamicBody, world, 0.8f, false, -1);

        getBody().setFixedRotation(true);
        getBody().setUserData(this);

        atlas = new TextureAtlas(Gdx.files.internal("characters/character.pack"));
        walkAnimationUp = new Animation<>(FRAME_DURATION,
                atlas.findRegion("tile000"), atlas.findRegion("tile001"), atlas.findRegion("tile002"));
        walkAnimationDown = new Animation<>(FRAME_DURATION,
                atlas.findRegion("tile006"), atlas.findRegion("tile007"), atlas.findRegion("tile008"));
        walkAnimationLeft = new Animation<>(FRAME_DURATION,
                atlas.findRegion("tile009"), atlas.findRegion("tile010"), atlas.findRegion("tile011"));
        walkAnimationRight = new Animation<>(FRAME_DURATION,
                atlas.findRegion("tile003"), atlas.findRegion("tile004"), atlas.findRegion("tile005"));

        walkAnimationDown.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkAnimationRight.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkAnimationLeft.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkAnimationUp.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void handleInput() {

        boolean moving = false;
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            currentFrame = walkAnimationUp.getKeyFrame(stateTime, true);
            getBody().setTransform(new Vector2(getBody().getPosition().x, getBody().getPosition().y + MOVE_SPEED), 0);
            moving = true;
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            currentFrame = walkAnimationDown.getKeyFrame(stateTime, true);
            getBody().setTransform(new Vector2(getBody().getPosition().x, getBody().getPosition().y - MOVE_SPEED), 0);
            moving = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            currentFrame = walkAnimationLeft.getKeyFrame(stateTime, true);
            getBody().setTransform(new Vector2(getBody().getPosition().x - MOVE_SPEED, getBody().getPosition().y), 0);
            moving = true;
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            currentFrame = walkAnimationRight.getKeyFrame(stateTime, true);
            getBody().setTransform(new Vector2(getBody().getPosition().x + MOVE_SPEED, getBody().getPosition().y), 0);
            moving = true;
        }

        if(!moving) {
            getBody().setLinearVelocity(0f, 0f);
            currentFrame = atlas.findRegion("tile007");
        }
    }

    public void setInCar(boolean inCar) {
        this.inCar = inCar;
    }

    public void update(SpriteBatch batch, CameraController camera) {
        stateTime += Gdx.graphics.getDeltaTime();

        if(!inCar) {
            batch.setProjectionMatrix(camera.getCamera().combined);
            batch.begin();
            batch.draw(currentFrame, getBody().getPosition().x - SIZE.x / PPM / 2, getBody().getPosition().y - SIZE.y / PPM / 2, SIZE.x / PPM, SIZE.y / PPM);
            batch.end();
        } else {
            getBody().setTransform(0, 0, 0);
        }

    }

    public static boolean isPlayer(Body b) {
        try {
            if(((Character) b.getUserData()) != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
