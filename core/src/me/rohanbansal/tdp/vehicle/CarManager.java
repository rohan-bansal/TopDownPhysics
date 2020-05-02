package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.enums.CarType;
import me.rohanbansal.tdp.tools.MapLoader;

import java.util.ArrayList;

public class CarManager {

    private static ArrayList<Car> cars = new ArrayList<>();

    public static Car createCar(float maxSpeed, float drift, float acceleration, MapLoader loader, CarType wheelDrive, World world) {
        Car temp = new Car(maxSpeed, drift, acceleration, loader, wheelDrive, world);
        cars.add(temp);

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
}
