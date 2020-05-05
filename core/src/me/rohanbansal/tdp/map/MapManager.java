package me.rohanbansal.tdp.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.enums.CarType;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.vehicle.Car;
import me.rohanbansal.tdp.vehicle.CarManager;

import static me.rohanbansal.tdp.Constants.PPM;

public class MapManager {

    private TiledMapRenderer tiledMapRenderer;
    private MapLoader loader;

    public MapManager(MapLoader loader) {
        tiledMapRenderer = new OrthogonalTiledMapRenderer(loader.getMap(), 1/PPM);

        this.loader = loader;
    }

    public void update(CameraController cam) {
        tiledMapRenderer.setView(cam.getCamera());
        tiledMapRenderer.render();
    }

    public void loadCars(World world) {
        for(RectangleMapObject obj : loader.getCarRects()) {
            Rectangle rect = obj.getRectangle();
            Rectangle realRect = new Rectangle(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2, rect.getWidth() / 2, rect.getHeight() / 2);

            String carType = (String) obj.getProperties().get("type");
            CarManager.CarModel carModel = CarManager.CarModel.valueOf(obj.getName().toUpperCase());

            CarManager.createCar(carModel, new Vector2((realRect.getX() - realRect.getWidth()) / PPM, (realRect.getY() - realRect.getHeight()) / PPM), processCarType(carType), world);
        }
    }

    private float processCarType(String type) {
        if(type.equals("car-up")) {
            return 0f;
        } else if(type.equals("car-down")) {
            return 180f;
        } else if(type.equals("car-left")) {
            return 90.25f;
        } else if(type.equals("car-right")) {
            return -90.5f;
        }
        return 0f;
    }
}
