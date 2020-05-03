package me.rohanbansal.tdp.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static me.rohanbansal.tdp.Constants.*;

public class CameraController {

    private OrthographicCamera camera;
    private Viewport viewport;

    private boolean lerping = false;
    private float lerpValue = 0f;
    private float lerpSpeed = 0f;

    public CameraController() {
        camera = new OrthographicCamera();
        camera.zoom = PLAYER_ZOOM;
        viewport = new FitViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM, camera);
    }

    public void update() {
        camera.update();

        if(lerping) {
            if(Math.abs(lerpValue - camera.zoom) <= 0.1f) {
                lerping = false;
            } else {
                camera.zoom = MathUtils.lerp(camera.zoom, lerpValue, lerpSpeed);
            }
        }
    }

    public void lerpZoomTo(float value, float speed) {
        lerping = true;
        lerpValue = value;
        lerpSpeed = speed;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setViewportSize(int x, int y) {
        viewport.update(x, y);
    }
}
