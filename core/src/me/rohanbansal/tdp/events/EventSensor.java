package me.rohanbansal.tdp.events;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class EventSensor {

    private Rectangle rectangle;
    private Body body;
    private String name;

    public EventSensor(Rectangle rect, String name) {
        this.rectangle = rect;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
