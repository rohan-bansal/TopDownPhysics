package me.rohanbansal.tdp.vehicle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import me.rohanbansal.tdp.screens.PlayScreen;
import me.rohanbansal.tdp.tools.CameraController;
import me.rohanbansal.tdp.tools.ModifiedShapeRenderer;

import java.util.ArrayList;

public class AIController {

    private ArrayList<RayCastCallback> callbacks = new ArrayList<>();
    private ArrayList<Vector2> intersectPoints = new ArrayList<>();
    private ArrayList<Vector2> intersectPointsQueueRemove = new ArrayList<>();

    private final ModifiedShapeRenderer renderer = new ModifiedShapeRenderer();
    private World world;
    private CameraController camera;

    public AIController(World world, final CameraController camera) {

        this.world = world;
        this.camera = camera;

        for(int z = 0; z < 8; z++) {
            callbacks.add(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    if (fixture.getBody().getUserData() != null && fixture.getBody().getUserData().equals("Wall")) {
                        if(PlayScreen.renderingDebug) {
                            renderer.setProjectionMatrix(camera.getCamera().combined);
                            renderer.begin(ShapeRenderer.ShapeType.Filled);
                            renderer.circle(point.x, point.y, 1f, 20);
                            renderer.end();
                        }
                    }
                    return 0;
                }
            });
        }
    }

    public void rayCast(World world, Rectangle rectangle) {

        world.rayCast(callbacks.get(0),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2 + 25));
        world.rayCast(callbacks.get(1),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2 + 25, rectangle.getY() + rectangle.getHeight()));
        world.rayCast(callbacks.get(2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2 - 25, rectangle.getY() + rectangle.getHeight() / 2));
        world.rayCast(callbacks.get(3),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2 - 25));

        world.rayCast(callbacks.get(4),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2 + 25, rectangle.getY() + rectangle.getHeight() / 2 + 25));
        world.rayCast(callbacks.get(5),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2 - 25, rectangle.getY() + rectangle.getHeight() / 2 - 25));
        world.rayCast(callbacks.get(6),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2 + 25, rectangle.getY() + rectangle.getHeight() / 2 - 25));
        world.rayCast(callbacks.get(7),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getX() + rectangle.getWidth() / 2 - 25, rectangle.getY() + rectangle.getHeight() / 2 + 25));

        drawRays(rectangle, camera);


    }

    private void drawRays(Rectangle rectangle, CameraController camera) {

        if(PlayScreen.renderingVelocities) {
            renderer.setProjectionMatrix(camera.getCamera().combined);
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.WHITE);
            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2 + 25));
            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2 + 25, rectangle.getY() + rectangle.getHeight() / 2));
            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2 - 25, rectangle.getY() + rectangle.getHeight() / 2));
            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2 - 25));

            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2 - 25, rectangle.getY() + rectangle.getHeight() / 2 - 25));
            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2 + 25, rectangle.getY() + rectangle.getHeight() / 2 - 25));
            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2 + 25, rectangle.getY() + rectangle.getHeight() / 2 + 25));
            renderer.line(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2 - 25, rectangle.getY() + rectangle.getHeight() / 2 + 25));
            renderer.end();

        }

        renderer.setProjectionMatrix(camera.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        for (Vector2 intersectPoint : intersectPoints) {
            renderer.line(new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2), intersectPoint);
            //renderer.circle(intersectPoint.x, intersectPoint.y, 1f);
            intersectPointsQueueRemove.add(intersectPoint);
        }
        renderer.end();

        for(Vector2 v : intersectPointsQueueRemove) {
            intersectPoints.remove(v);
        }
        intersectPointsQueueRemove.clear();
    }
}
