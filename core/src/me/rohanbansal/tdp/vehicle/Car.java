package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import me.rohanbansal.tdp.BodyHolder;
import me.rohanbansal.tdp.Character;
import me.rohanbansal.tdp.enums.CarType;
import me.rohanbansal.tdp.screens.PlayScreen;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.enums.Direction;
import me.rohanbansal.tdp.tools.EffectFactory;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;
import me.rohanbansal.tdp.tools.ShapeFactory;

import static me.rohanbansal.tdp.Constants.*;


public class Car extends BodyHolder {

    private Direction driveDirection = Direction.DRIVE_NONE;
    private Direction turnDirection = Direction.TURN_NONE;

    private static final float LINEAR_DAMPING = 0.5f;
    private static final float RESTITUTION = 0.7f;
    private static final float MAX_WHEEL_ANGLE = 20;
    private static final float WHEEL_TURN_INCREMENT = 1.0f;
    private static final Vector2 WHEEL_SIZE = new Vector2(16, 32);
    private static final float WHEEL_DENSITY = 0.4f;

    private float currentWheelAngle = 0;
    private Array<Wheel> allWheels = new Array<>();
    private Array<Wheel> revolvingWheels = new Array<>();
    private float currentMaxSpeed;
    private float regularMaxSpeed;
    public float regularMaxSpeedBackup = 0;

    private CarProperties properties;
    private CarHUD carHUD;
    private AIController AIcontroller;

    private Sprite carSprite, getIn;
    private Character prospectiveDriver = null;
    private Character driver = null;
    private boolean showGetIn = false;
    private float durability;
    private float fuel;

    public Car(CarProperties properties, CameraController camera) {
        super(createBody(properties.getPosition(), properties.getWorld(), properties.getDensity()));

        this.properties = properties;
        this.regularMaxSpeed = properties.getMaxSpeed();
        this.durability = properties.getDurability();
        this.fuel = properties.getFuelMax();

        carSprite = new Sprite(new Texture(Gdx.files.internal(properties.getCarPath())));
        getIn = new Sprite(new Texture(Gdx.files.internal("sprites/f_key.png")));

        createWheels(properties.getWorld(), properties.getWheelDrive());
        carHUD = new CarHUD(this);
        AIcontroller = new AIController(properties.getWorld(), camera);

        getBody().setUserData(this);
        getBody().setLinearDamping(LINEAR_DAMPING);
        getBody().getFixtureList().get(0).setRestitution(RESTITUTION);
        getBody().setTransform(getBody().getPosition().x, getBody().getPosition().y, properties.getAngle()); // set angle
    }

    private static Body createBody(Vector2 position, World world, float density) {
        return ShapeFactory.createRectangle(
                new Vector2(position.x + 128 / 2, position.y + 256 / 2),
                new Vector2(128 / 2, 256 / 2),
                BodyDef.BodyType.DynamicBody, world, density, false);
    }

    private void createWheels(World world, CarType wheelDrive) {
        for(int i = 0; i < 4; i++) {
            float xOffset = properties.getWheelOffsets().get(i)[0];
            float yOffset = properties.getWheelOffsets().get(i)[1];

            boolean powered = wheelDrive == CarType.FOUR_WHEEL_DRIVE || (wheelDrive == CarType.TWO_WHEEL_DRIVE && i < 2);

            Wheel wheel = new Wheel(
                    new Vector2(getBody().getPosition().x * PPM + xOffset, getBody().getPosition().y * PPM + yOffset),
                    WHEEL_SIZE,
                    BodyDef.BodyType.DynamicBody,
                    world, WHEEL_DENSITY, i, this, powered);

            if(i < 2) {
                RevoluteJointDef jointDef = new RevoluteJointDef();
                jointDef.initialize(getBody(), wheel.getBody(), wheel.getBody().getWorldCenter());
                jointDef.enableMotor = false;
                world.createJoint(jointDef);
            } else {
                PrismaticJointDef jointDef = new PrismaticJointDef();;
                jointDef.initialize(getBody(), wheel.getBody(), wheel.getBody().getWorldCenter(), new Vector2(1, 0));
                jointDef.enableLimit = true;
                jointDef.lowerTranslation = jointDef.upperTranslation = 0;
                world.createJoint(jointDef);
            }

            allWheels.add(wheel);
            if(i < 2) {
                revolvingWheels.add(wheel);
            }
            wheel.setDrift(properties.getDrift());
        }

    }

    public float getFuel() {
        return fuel;
    }

    public float getFuelMax() {
        return properties.getFuelMax();
    }

    public float getDurability() {
        return durability;
    }

    public float getMaxDurability() {
        return properties.getDurability();
    }

    public void setDurability(float durability) {
        this.durability = durability;
    }

    public float getMaxSpeed() {
        return regularMaxSpeed;
    }

    private void processInput() {
        Vector2 baseVector = new Vector2(0, 0);

        if (turnDirection == Direction.TURN_LEFT) {
            if(currentWheelAngle < 0) {
                currentWheelAngle = 0;
            }
            currentWheelAngle = Math.min(currentWheelAngle += WHEEL_TURN_INCREMENT, MAX_WHEEL_ANGLE);
        } else if (turnDirection == Direction.TURN_RIGHT) {
            if(currentWheelAngle > 0) {
                currentWheelAngle = 0;
            }
            currentWheelAngle = Math.max(currentWheelAngle -= WHEEL_TURN_INCREMENT, -MAX_WHEEL_ANGLE);
        } else {
            currentWheelAngle = 0;
        }

        for(Wheel wheel : new Array.ArrayIterator<>(revolvingWheels)) {
            wheel.setAngle(currentWheelAngle);
        }

        if (driveDirection == Direction.DRIVE_FORWARD) {
            baseVector.set(0, properties.getAcceleration());
        } else if (driveDirection == Direction.DRIVE_BACKWARD) {
            if(direction() == Direction.DRIVE_BACKWARD) {
                baseVector.set(0, -properties.getAcceleration() * 0.7f);
            } else if(direction() == Direction.DRIVE_FORWARD) {
                baseVector.set(0, -properties.getAcceleration() * 1.3f);
            } else {
                baseVector.set(0, -properties.getAcceleration());
            }
        }

        if(direction() == Direction.DRIVE_BACKWARD) {
            currentMaxSpeed = regularMaxSpeed / 2;
        } else {
            currentMaxSpeed = regularMaxSpeed;
        }

        if(getBody().getLinearVelocity().len() < currentMaxSpeed) {
            for(Wheel wheel : new Array.ArrayIterator<>(allWheels)) {
                if(wheel.isPowered()) {
                    wheel.getBody().applyForceToCenter(wheel.getBody().getWorldVector(baseVector), true);
                }
            }
        }

    }

    public void displayGetIn(Character character) {
        showGetIn = true;
        this.prospectiveDriver = character;
    }

    public void hideGetIn() {
        showGetIn = false;
        this.prospectiveDriver = null;
    }

    public void setMaxSpeed(float speed, boolean revert) {
        if(revert) {
            regularMaxSpeed = regularMaxSpeedBackup;
            regularMaxSpeedBackup = 0;
        } else {
            regularMaxSpeedBackup = regularMaxSpeed;
            regularMaxSpeed = speed;
        }
    }

    public void handleInput() {

        if(fuel > 0) {
            if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                setDriveDirection(Direction.DRIVE_FORWARD);
            } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                setDriveDirection(Direction.DRIVE_BACKWARD);
            } else {
                setDriveDirection(Direction.DRIVE_NONE);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                setTurnDirection(Direction.TURN_LEFT);
            } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                setTurnDirection(Direction.TURN_RIGHT);
            } else {
                setTurnDirection(Direction.TURN_NONE);
            }
        } else {
            setDriveDirection(Direction.DRIVE_NONE);
            setTurnDirection(Direction.TURN_NONE);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Gdx.app.log(getBody().getPosition().x + "", getBody().getPosition().y + "");
            this.fuel -= 10;
        }

    }

    public void setDriveDirection(Direction driveDirection) {
        this.driveDirection = driveDirection;
    }

    public void setTurnDirection(Direction turnDirection) {
        this.turnDirection = turnDirection;
    }

    @Override
    public void update(float delta, CameraController camera, ModifiedShapeRenderer renderer, World world) {
        super.update(delta, camera, renderer, world);
        processInput();


        if(durability <= 0) {
            EffectFactory.createEffect(EffectFactory.EffectType.EXPLOSION, getBody().getPosition(), false, 4f);
            removePlayerFromCar(camera);
            CarManager.deleteCar(this, world);
        }

        if(driver != null) {
            carHUD.render(renderer, camera);
            this.fuel -= ((allWheels.get(0).getBody().getLinearVelocity().len() + allWheels.get(1).getBody().getLinearVelocity().len() + allWheels.get(2).getBody().getLinearVelocity().len() +
                    allWheels.get(3).getBody().getLinearVelocity().len()) / 4 / 15000);

            AIcontroller.update(world, getRectangle());
        }

        for(Wheel wheel : new Array.ArrayIterator<>(allWheels)) {
            wheel.update(delta, camera, renderer, world);
        }

        carSprite.setBounds(getBody().getPosition().x - carSprite.getWidth() / 2, getBody().getPosition().y - carSprite.getHeight() / 2, 150 / PPM, 300 / PPM);
        carSprite.setOriginCenter();
        carSprite.setRotation(MathUtils.radiansToDegrees * getBody().getAngle());

        PlayScreen.batch.begin();

        carSprite.draw(PlayScreen.batch);

        if(Gdx.input.isKeyJustPressed(Input.Keys.F) && this.driver != null && this.driver.getCar() == this) {
            removePlayerFromCar(camera);
        }

        if(showGetIn) {
            getIn.setCenter(getBody().getPosition().x, getBody().getPosition().y);
            getIn.setSize(64 / PPM, 64 / PPM);
            getIn.draw(PlayScreen.batch);

            if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                driver = prospectiveDriver;
                driver.setInCar(true);
                driver.setCar(this);
                camera.lerpZoomTo(CAR_ZOOM, 0.1f);
            }
        }

        PlayScreen.batch.end();
    }

    public void incrementFuelBy(float value) {
        this.fuel += value;
    }

    public Rectangle getRectangle() {
        return new Rectangle(getBody().getPosition().x - 64 / 2 / PPM, getBody().getPosition().y - 128 / 2 / PPM, 64 / PPM, 128 / PPM);
    }

    private void removePlayerFromCar(CameraController camera) {
        driver.setInCar(false);
        driver.setCar(null);
        driver.getBody().setTransform(getBody().getPosition().x, getBody().getPosition().y, 0);
        driver = null;

        setDriveDirection(Direction.DRIVE_NONE);
        setTurnDirection(Direction.TURN_NONE);

        camera.lerpZoomTo(PLAYER_ZOOM, 0.2f);
    }
}