package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.enums.CarType;
import me.rohanbansal.tdp.enums.WheelType;

import java.util.HashMap;

public class CarProperties {

    private Vector2 position;
    private float maxSpeed, drift, acceleration, density;
    private CarType wheelDrive;
    private World world;
    private String carPath;
    private float angle;
    private float durability;

    private HashMap<Integer, Integer[]> wheelOffsets;

    public CarProperties(Vector2 position, float maxSpeed, float drift, float acceleration, float density, float angle, float durability, CarType wheelDrive, World world, String carPath) {
        this.position = position;
        this.maxSpeed = maxSpeed;
        this.drift = drift;
        this.acceleration = acceleration;
        this.density = density;
        this.wheelDrive = wheelDrive;
        this.world = world;
        this.carPath = carPath;
        this.angle = angle;
        this.durability = durability;

        wheelOffsets = new HashMap<>();
    }

    public float getDurability() {
        return durability;
    }

    public float getAngle() {
        return angle;
    }

    public HashMap<Integer, Integer[]> getWheelOffsets() {
        return wheelOffsets;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getDrift() {
        return drift;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getDensity() {
        return density;
    }

    public CarType getWheelDrive() {
        return wheelDrive;
    }

    public World getWorld() {
        return world;
    }

    public String getCarPath() {
        return carPath;
    }
}
