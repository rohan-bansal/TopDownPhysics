package me.rohanbansal.tdp.events;

import com.badlogic.gdx.physics.box2d.*;
import me.rohanbansal.tdp.Character;
import me.rohanbansal.tdp.map.MapLoader;
import me.rohanbansal.tdp.vehicle.Car;
import me.rohanbansal.tdp.vehicle.CarManager;

import static me.rohanbansal.tdp.Constants.CAR_SPEED_PITS;

public class ContactManager implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if(CarManager.isCar(b)) {
            Car car = (Car) b.getUserData();
            if(Character.isPlayer(a)) {
                car.displayGetIn((Character) a.getUserData());
            }
            if(a.getUserData() instanceof String && ((String) a.getUserData()).equals("Wall")) {
                if(car.getBody().getLinearVelocity().len() > car.getMaxSpeed() / 5) {
                    car.setDurability(car.getDurability() - 1);
                    System.out.println(car.getDurability());
                }
            }
        }

        for(EventSensor sensor : MapLoader.getEventRects()) {
            if(sensor.getBody() == a && sensor.getName().equals("pit-speed-trigger")) {
                if(CarManager.isCar(b)) {
                    if(((Car) b.getUserData()).regularMaxSpeedBackup == 0) {
                        ((Car) b.getUserData()).setMaxSpeed(CAR_SPEED_PITS, false); //TURN DOWN SPEED
                    } else {
                        ((Car) b.getUserData()).setMaxSpeed(0f, true); // REVERT SPEED TO NORMAL
                    }
                }
            } else if(sensor.getBody() == b && sensor.getName().equals("pit-speed-trigger")) {
                if(CarManager.isCar(a)) {
                    if(((Car) a.getUserData()).regularMaxSpeedBackup == 0) {
                        ((Car) a.getUserData()).setMaxSpeed(CAR_SPEED_PITS, false); //TURN DOWN SPEED
                    } else {
                        ((Car) a.getUserData()).setMaxSpeed(0f, true); // REVERT SPEED TO NORMAL
                    }
                }
            }
        }


    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if(CarManager.isCar(b)) {
            Car car = (Car) b.getUserData();
            if(Character.isPlayer(a)) {
                car.hideGetIn();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
