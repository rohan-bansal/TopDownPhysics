package me.rohanbansal.tdp.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static me.rohanbansal.tdp.Constants.*;

public class CameraController {

    private OrthographicCamera camera;
    private Viewport viewport;

    public CameraController() {
        camera = new OrthographicCamera();
        camera.zoom = PLAYER_ZOOM;
        viewport = new FitViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM, camera);
    }

    public void update() {
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setViewportSize(int x, int y) {
        viewport.update(x, y);
    }
}
