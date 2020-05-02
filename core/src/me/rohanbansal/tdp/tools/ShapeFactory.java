package me.rohanbansal.tdp.tools;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static me.rohanbansal.tdp.Constants.PPM;

public class ShapeFactory {

    private ShapeFactory() {}

    public static Body createRectangle(final Vector2 position, final Vector2 size, final BodyDef.BodyType type, final World world, float density, boolean sensor) {

        final BodyDef bdef = new BodyDef();
        bdef.position.set(position.x / PPM, position.y / PPM);
        bdef.type = type;

        final Body body = world.createBody(bdef);
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / PPM, size.y / PPM);
        final FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = density;
        fdef.isSensor = sensor;

        body.createFixture(fdef);
        shape.dispose();

        return body;
    }

    public static PolygonShape createWallPolygon(PolygonMapObject polygonObject, World world) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / PPM;
        }

        polygon.set(worldVertices);

        final BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        final FixtureDef fdef = new FixtureDef();

        fdef.shape = polygon;
        body.createFixture(fdef);
        polygon.dispose();

        return polygon;
    }
}
