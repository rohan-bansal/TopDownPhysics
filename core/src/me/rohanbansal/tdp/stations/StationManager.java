package me.rohanbansal.tdp.stations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import me.rohanbansal.tdp.map.MapLoader;
import me.rohanbansal.tdp.tools.CameraController;

import java.util.ArrayList;

public class StationManager {

    private ArrayList<GasStation> stations;
    private MapLoader loader;

    private SpriteBatch batch;

    public StationManager(MapLoader loader) {
        this.loader = loader;
        this.batch = new SpriteBatch();

        this.stations = new ArrayList<>();
    }

    public void generateStations() {
        for(RectangleMapObject obj : loader.getGasStations()) {
            this.createStation(new Vector2(obj.getRectangle().getX() + (obj.getRectangle().getWidth() / 2), obj.getRectangle().getY() + (obj.getRectangle().getHeight() / 2)));
        }
    }

    public void createStation(Vector2 location) {
        stations.add(new GasStation(location));
    }

    public void update(CameraController camera) {
        for(GasStation station : stations) {
            station.render(batch, camera);
        }
    }
}
