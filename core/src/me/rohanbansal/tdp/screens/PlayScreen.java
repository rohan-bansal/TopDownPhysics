package me.rohanbansal.tdp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.Character;
import me.rohanbansal.tdp.events.ContactManager;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.MapLoader;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;
import me.rohanbansal.tdp.vehicle.CarManager;

import static me.rohanbansal.tdp.Constants.*;

public class PlayScreen implements Screen {

    public static final SpriteBatch batch = new SpriteBatch();
    private final World world;
    private final Box2DDebugRenderer B2DR;
    private final CameraController camera;
    private final MapLoader mapLoader;
    private ModifiedShapeRenderer renderer = new ModifiedShapeRenderer();
    private TiledMapRenderer tiledMapRenderer;
    private Character character;

    private boolean renderingDebug = false, renderingVelocities = false;

    public PlayScreen() {
        world = new World(GRAVITY, false);
        world.setContactListener(new ContactManager());
        B2DR = new Box2DDebugRenderer();
        camera = new CameraController();

        character = new Character(new Vector2(380, 370), world);

        mapLoader = new MapLoader(world).loadMap();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(mapLoader.getMap(), 1/PPM);

        CarManager.createCar(CarManager.CarModel.MUSTANG, new Vector2(390.5f, 386.5f), 90.25f, world);
        CarManager.createCar(CarManager.CarModel.AERO, new Vector2(390.5f, 379f), 90.25f, world);
        CarManager.createCar(CarManager.CarModel.LAMBORGHINI, new Vector2(390.5f, 371.5f), 90.25f, world);
        CarManager.createCar(CarManager.CarModel.RED_TRUCK, new Vector2(380.25f, 356.4f), 0, world);
        CarManager.createCar(CarManager.CarModel.FORMULA_BLUE, new Vector2(340.15f, 417.8f), 0, world);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(12/255f, 114/255f, 80/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        update(delta);

        draw();
    }

    private void handleInput() {

        if(!character.inCar) {
            character.handleInput();
        } else {
            character.getCar().handleInput();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if(camera.getCamera().zoom - 0.2f > 1) {
                camera.lerpZoomTo(camera.getCamera().zoom - 0.2f, 0.25f);
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.C)) {
            if(camera.getCamera().zoom + 0.2f < 6.7) {
            }
            camera.lerpZoomTo(camera.getCamera().zoom + 0.2f, 0.25f);
        } else if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            if(character.inCar) {
                camera.lerpZoomTo(CAR_ZOOM, 0.1f);
            } else {
                camera.lerpZoomTo(PLAYER_ZOOM, 0.1f);
            }
        }
    }

    private void draw() {

        batch.setProjectionMatrix(camera.getCamera().combined);
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            renderingDebug = !renderingDebug;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            renderingVelocities = !renderingVelocities;
        }

        if(renderingDebug) {
            if(renderingVelocities) {
                B2DR.setDrawVelocities(true);
            } else {
                B2DR.setDrawVelocities(false);
            }
            B2DR.render(world, camera.getCamera().combined);
        }
        character.update(batch, camera);
    }

    private void update(float delta) {
        tiledMapRenderer.setView(camera.getCamera());
        tiledMapRenderer.render();

        CarManager.update(delta, camera, renderer);
        if(!character.inCar) {
            camera.getCamera().position.lerp(new Vector3(character.getBody().getPosition().x, character.getBody().getPosition().y, 0), 0.1f);
        } else {
            camera.getCamera().position.lerp(new Vector3(character.getCar().getBody().getPosition().x, character.getCar().getBody().getPosition().y, 0), 0.255f);
        }
        camera.update();

        world.step(delta, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        camera.setViewportSize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        B2DR.dispose();
        world.dispose();
        mapLoader.dispose();
    }
}
