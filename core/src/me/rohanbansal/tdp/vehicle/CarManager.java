package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.enums.CarType;
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
            temp = new Car(80, 0.4f, 90, CarType.TWO_WHEEL_DRIVE, world, "cars/car_black_yellowstripes.png", position);
        } else if(model == CarModel.AERO) {
            temp = new Car(110, 0.5f, 110, CarType.TWO_WHEEL_DRIVE, world, "cars/car_purple.png", position);
        } else if(model == CarModel.LAMBORGHINI) {
            temp = new Car(115, 0.4f, 150, CarType.TWO_WHEEL_DRIVE, world, "cars/car_red.png", position);
        } else if(model == CarModel.RED_TRUCK) {
            temp = new Car(70, 0.0f, 10, CarType.FOUR_WHEEL_DRIVE, world, "cars/thick_truck_red.png", position);
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
