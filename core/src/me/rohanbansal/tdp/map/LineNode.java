package me.rohanbansal.tdp.map;

import com.badlogic.gdx.math.Vector2;

public class LineNode {

    private Vector2 position;

    public LineNode(Vector2 position) {
        this.position = position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }
}
