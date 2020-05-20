package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.enums.CarType;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;

import java.util.ArrayList;

import static me.rohanbansal.tdp.Constants.PPM;

public class CarManager {

    public enum CarModel {
        MUSTANG, AERO, LAMBORGHINI,

        RED_TRUCK, PICKUP_TRUCK,

        FORMULA_BLUE, FORMULA_ORANGE, FORMULA_LIME, FORMULA_GREEN, FORMULA_PINK, FORMULA_RED
    }

    private static ArrayList<Car> cars = new ArrayList<>();
    private static ArrayList<Car> carsToRemoveQueue = new ArrayList<>();

    public static Car createCar(CarModel model, Vector2 position, float angle, World world, CameraController camera) {
        position = position.scl(PPM);
        Car temp = processModel(model, position, angle, world, camera);
        cars.add(temp);
        return temp;
    }

    private static Car processModel(CarModel model, Vector2 position, float angle, World world, CameraController camera) {
        Car temp = null;
        if(model == CarModel.MUSTANG) {
            CarProperties properties = new CarProperties(position, 80, 0.4f, 90, 0.4f, angle, 80, 100, CarType.TWO_WHEEL_DRIVE, world, "cars/car_black_yellowstripes.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-58, -68});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {58, -68});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-58, 70});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {58, 70});
            temp = new Car(properties, camera);
        } else if(model == CarModel.AERO) {
            CarProperties properties = new CarProperties(position, 110, 0.5f, 110, 0.5f, angle, 70, 95, CarType.TWO_WHEEL_DRIVE, world, "cars/car_purple.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-57, -72});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {57, -72});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-57, 70});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {57, 70});
            temp = new Car(properties, camera);
        } else if(model == CarModel.LAMBORGHINI) {
            CarProperties properties = new CarProperties(position, 115, 0.4f, 150, 0.5f, angle, 60, 100, CarType.TWO_WHEEL_DRIVE, world, "cars/car_red.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-55, -68});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {55, -68});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-55, 70});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {55, 70});
            temp = new Car(properties, camera);
        } else if(model == CarModel.RED_TRUCK) {
            CarProperties properties = new CarProperties(position, 70, 0.0f, 10, 0.3f, angle, 150, 150, CarType.FOUR_WHEEL_DRIVE, world, "cars/thick_truck_red.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[]{-65, -93});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[]{65, -93});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[]{-65, 80});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[]{65, 80});
            temp = new Car(properties, camera);
        } else if(model == CarModel.PICKUP_TRUCK) {
            CarProperties properties = new CarProperties(position, 70, 0.0f, 25, 0.35f, angle, 140, 110, CarType.TWO_WHEEL_DRIVE, world, "cars/pickup_truck.png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-57, -90});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {57, -90});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-57, 75});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {57, 75});
            temp = new Car(properties, camera);
        } else if(model.name().toLowerCase().contains("formula")) {
            CarProperties properties = new CarProperties(position, 170, 0.2f, 100, 0.4f, angle, 65, 90, CarType.TWO_WHEEL_DRIVE, world, "cars/" + model.name().toLowerCase() + ".png");
            properties.getWheelOffsets().put(Wheel.BOTTOM_LEFT, new Integer[] {-52, -105});
            properties.getWheelOffsets().put(Wheel.BOTTOM_RIGHT, new Integer[] {52, -105});
            properties.getWheelOffsets().put(Wheel.TOP_LEFT, new Integer[] {-52, 60});
            properties.getWheelOffsets().put(Wheel.TOP_RIGHT, new Integer[] {52, 60});
            temp = new Car(properties, camera);
        }
        return temp;
    }

    public static void deleteCar(Car car, World world) {
        world.destroyBody(car.getBody());
        carsToRemoveQueue.add(car);
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

    public static void update(float delta, CameraController camera, ModifiedShapeRenderer renderer, World world) {
        for(Car car : cars) {
            car.update(delta, camera, renderer, world);
        }

        for(Car car : carsToRemoveQueue) {
            cars.remove(car);
        }

        carsToRemoveQueue.clear();
    }
}
