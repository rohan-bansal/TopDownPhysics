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
import me.rohanbansal.tdp.events.EventManager;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.MapLoader;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;
import me.rohanbansal.tdp.vehicle.CarManager;

import static me.rohanbansal.tdp.Constants.GRAVITY;
import static me.rohanbansal.tdp.Constants.PPM;

public class PlayScreen implements Screen {

    public static final SpriteBatch batch = new SpriteBatch();
    private final World world;
    private final Box2DDebugRenderer B2DR;
    private final CameraController camera;
    private final MapLoader mapLoader;
    private ModifiedShapeRenderer renderer = new ModifiedShapeRenderer();
    private TiledMapRenderer tiledMapRenderer;
    private EventManager eManager;
    private Character character;



    public PlayScreen() {
        world = new World(GRAVITY, false);
        world.setContactListener(new ContactManager());
        B2DR = new Box2DDebugRenderer();
        camera = new CameraController();
        eManager = new EventManager();

        character = new Character(new Vector2(11000, 14000), world);

        mapLoader = new MapLoader(world).loadMap();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(mapLoader.getMap(), 1/PPM);

        CarManager.createCar(CarManager.CarModel.MUSTANG, new Vector2(11400, 14000), world);
        CarManager.createCar(CarManager.CarModel.AERO, new Vector2(11450, 14000), world);
        CarManager.createCar(CarManager.CarModel.LAMBORGHINI, new Vector2(11500, 14000), world);
        CarManager.createCar(CarManager.CarModel.RED_TRUCK, new Vector2(11550, 14000), world);

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
        //TODO if player in car, pass event to car.handleInput()

        if(!character.inCar) {
            character.handleInput();
        } else {
            character.getCar().handleInput();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if(camera.getCamera().zoom - 0.4f > 1) {
                camera.getCamera().zoom -= 0.4f;
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            camera.getCamera().zoom += 0.4f;
        }
    }

    private void draw() {

        batch.setProjectionMatrix(camera.getCamera().combined);
        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            B2DR.render(world, camera.getCamera().combined);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.V)) {
            B2DR.setDrawVelocities(true);
        } else {
            B2DR.setDrawVelocities(false);
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
