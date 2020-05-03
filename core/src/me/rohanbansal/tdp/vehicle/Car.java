package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import me.rohanbansal.tdp.BodyHolder;
import me.rohanbansal.tdp.Character;
import me.rohanbansal.tdp.enums.CarType;
import me.rohanbansal.tdp.screens.PlayScreen;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.enums.Direction;
import me.rohanbansal.tdp.tools.MapLoader;
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
    private float drift;
    private float currentMaxSpeed;
    private float regularMaxSpeed;
    public float regularMaxSpeedBackup = 0;
    private float acceleration;

    private Sprite carSprite, getIn;
    private Character prospectiveDriver = null;
    private Character driver = null;
    private boolean showGetIn = false;

    public static int carID = 1;

    public Car(float maxSpeed, float drift, float acceleration, CarType wheelDrive, World world, String carPath, Vector2 position) {
        super(createBody(position, world));

        this.regularMaxSpeed = maxSpeed;
        this.drift = drift;
        this.acceleration = acceleration;

        getBody().setLinearDamping(LINEAR_DAMPING);

        getBody().getFixtureList().get(0).setRestitution(RESTITUTION);

        carSprite = new Sprite(new Texture(Gdx.files.internal(carPath)));
        getIn = new Sprite(new Texture(Gdx.files.internal("sprites/f_key.png")));

        createWheels(world, wheelDrive);
        getBody().setUserData(this);
        carID++;
    }

    private static Body createBody(Vector2 position, World world) {
        return ShapeFactory.createRectangle(
                new Vector2(position.x + 128 / 2, position.y + 256 / 2),
                new Vector2(128 / 2, 256 / 2),
                BodyDef.BodyType.DynamicBody, world, 0.4f, false);
    }

    private void createWheels(World world, CarType wheelDrive) {
        for(int i = 0; i < 4; i++) {
            float xOffset = 0;
            float yOffset = 0;

            switch(i) {
                case Wheel.TOP_LEFT:
                    xOffset = -60;
                    yOffset = 70;
                    break;
                case Wheel.TOP_RIGHT:
                    xOffset = 60;
                    yOffset = 70;
                    break;
                case Wheel.BOTTOM_LEFT:
                    xOffset = -60;
                    yOffset = -68;
                    break;
                case Wheel.BOTTOM_RIGHT:
                    xOffset = 60;
                    yOffset = -68;
                    break;
                default:
                    throw new IllegalArgumentException("Wheel number not supported. Create logic for positioning wheel with number " + i);
            }
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
            wheel.setDrift(drift);
        }

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
            baseVector.set(0, acceleration);
        } else if (driveDirection == Direction.DRIVE_BACKWARD) {
            if(direction() == Direction.DRIVE_BACKWARD) {
                baseVector.set(0, -acceleration * 0.7f);
            } else if(direction() == Direction.DRIVE_FORWARD) {
                baseVector.set(0, -acceleration * 1.3f);
            } else {
                baseVector.set(0, -acceleration);
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

    }

    public void setDriveDirection(Direction driveDirection) {
        this.driveDirection = driveDirection;
    }

    public void setTurnDirection(Direction turnDirection) {
        this.turnDirection = turnDirection;
    }

    @Override
    public void update(float delta, CameraController camera, ModifiedShapeRenderer renderer) {
        super.update(delta, camera, renderer);
        processInput();

        for(Wheel wheel : new Array.ArrayIterator<>(allWheels)) {
            wheel.update(delta, camera, renderer);
        }

        carSprite.setBounds(getBody().getPosition().x - carSprite.getWidth() / 2, getBody().getPosition().y - carSprite.getHeight() / 2, 150 / PPM, 300 / PPM);
        carSprite.setOriginCenter();
        carSprite.setRotation(MathUtils.radiansToDegrees * getBody().getAngle());

        PlayScreen.batch.begin();

        carSprite.draw(PlayScreen.batch);

        if(Gdx.input.isKeyJustPressed(Input.Keys.F) && this.driver != null && this.driver.getCar() == this) {
            driver.setInCar(false);
            driver.setCar(null);
            driver.getBody().setTransform(getBody().getPosition().x, getBody().getPosition().y, 0);
            driver = null;

            setDriveDirection(Direction.DRIVE_NONE);
            setTurnDirection(Direction.TURN_NONE);
            camera.getCamera().zoom = PLAYER_ZOOM;

        }

        if(showGetIn) {
            getIn.setCenter(getBody().getPosition().x, getBody().getPosition().y);
            getIn.setSize(64 / PPM, 64 / PPM);
            getIn.draw(PlayScreen.batch);

            if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                driver = prospectiveDriver;
                driver.setInCar(true);
                driver.setCar(this);
                camera.getCamera().zoom = CAR_ZOOM;
            }
        }


        PlayScreen.batch.end();
    }
}