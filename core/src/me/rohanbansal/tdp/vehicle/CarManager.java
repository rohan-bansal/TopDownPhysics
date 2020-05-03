package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.enums.CarType;
import me.rohanbansal.tdp.enums.WheelType;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.MapLoader;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;

import java.util.ArrayList;

public class CarManager {

    public enum CarModel {
        MUSTANG, AERO, LAMBORGHINI, RED_TRUCK
    }

    private static ArrayList<Car> cars = new ArrayList<>();

    public static Car createCar(CarModel model, Vector2 position, World world) {
        Car temp = processModel(model, position, world);
        cars.add(temp);
        return temp;
    }

    private static Car processModel(CarModel model, Vector2 position, World world) {
        Car temp = null;
        if(model == CarModel.MUSTANG) {
            CarProperties properties = new CarProperties(position, 80, 0.4f, 90, 0.4f, CarType.TWO_WHEEL_DRIVE, world, "cars/car_black_yellowstripes.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-58, -68});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {58, -68});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-58, 70});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {58, 70});
            temp = new Car(properties);
        } else if(model == CarModel.AERO) {
            CarProperties properties = new CarProperties(position, 110, 0.5f, 110, 0.5f, CarType.TWO_WHEEL_DRIVE, world, "cars/car_purple.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-57, -72});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {57, -72});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-57, 70});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {57, 70});
            temp = new Car(properties);
        } else if(model == CarModel.LAMBORGHINI) {
            CarProperties properties = new CarProperties(position, 115, 0.4f, 150, 0.5f, CarType.TWO_WHEEL_DRIVE, world, "cars/car_red.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-55, -68});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {55, -68});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-55, 70});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {55, 70});
            temp = new Car(properties);
        } else if(model == CarModel.RED_TRUCK) {
            CarProperties properties = new CarProperties(position, 70, 0.0f, 10, 0.3f, CarType.FOUR_WHEEL_DRIVE, world, "cars/thick_truck_red.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-65, -93});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {65, -93});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-65, 80});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {65, 80});
            temp = new Car(properties);
        }
        return temp;
    }

    public static ArrayList<Car> getCars() {
        return cars;
    }

    public static boolean isCar(Body car) {
        try {
            if(((Car) car.getUserData()) != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static void update(float delta, CameraController camera, ModifiedShapeRenderer renderer) {
        for(Car car : cars) {
            car.update(delta, camera, renderer);
        }
    }
}
