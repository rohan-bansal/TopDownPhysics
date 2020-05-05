package me.rohanbansal.tdp.map;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import me.rohanbansal.tdp.tools.CameraController;

import static me.rohanbansal.tdp.Constants.PPM;

public class MapManager {

    private TiledMapRenderer tiledMapRenderer;

    public MapManager(MapLoader loader) {
        tiledMapRenderer = new OrthogonalTiledMapRenderer(loader.getMap(), 1/PPM);
    }

    public void update(CameraController cam) {
        tiledMapRenderer.setView(cam.getCamera());
        tiledMapRenderer.render();
    }
}
