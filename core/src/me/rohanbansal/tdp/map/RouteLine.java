package me.rohanbansal.tdp.map;

import com.badlogic.gdx.math.Vector2;

public class RouteLine {

    private Vector2 start;
    private Vector2 end;

    private LineNode[] nodes = new LineNode[4];

    public RouteLine(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;

        generateNodes();
    }

    private void generateNodes() {
        for(int i = 0; i < 4; i++) {
            if(i == 0) {
                nodes[i] = new LineNode(new Vector2(start));
            } else if(i == 3) {
                nodes[i] = new LineNode(new Vector2(end));
            } else if(i == 1) {
                nodes[i] = new LineNode(new Vector2((2f/3f) * start.x + (1f/3f) * end.x, (2f/3f) * start.y + (1f/3f) * end.y));
            } else if(i == 2) {
                nodes[i] = new LineNode(new Vector2((1f/3f) * start.x + (2f/3f) * end.x, (1f/3f) * start.y + (2f/3f) * end.y));
            }
        }
    }

    public LineNode[] getNodes() {
        return nodes;
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public void setStart(Vector2 start) {
        this.start = start;
    }

    public void setEnd(Vector2 end) {
        this.end = end;
    }
}
