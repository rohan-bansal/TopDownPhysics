package me.rohanbansal.tdp.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.rohanbansal.tdp.screens.PlayScreen;

import java.util.ArrayList;

public class HUDText {

    private static BitmapFont drawer = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));
    private static ArrayList<Text> drawCalls;
    private static ArrayList<TextWithTimeout> drawTimeout;

    static {
        drawer.getData().setScale(0.6f);
        drawCalls = new ArrayList<>();
        drawTimeout = new ArrayList<>();
    }

    public static void drawText(SpriteBatch batch, String text, float x, float y, Color color, float scale) {
        drawCalls.add(new Text(batch, text, x, y, color, scale));
    }

    public static void drawTimeoutText(SpriteBatch batch, String text, float x, float y, Color color, float scale, float secTimeout) {
        drawTimeout.add(new TextWithTimeout(batch, text, x, y, color, scale, secTimeout));
    }

    public static BitmapFont getDrawer(float scale) {
        drawer.getData().setScale(scale);
        return drawer;
    }

    public static void draw() {
        if(drawCalls.size() > 0) {
            for(int i = 0; i < drawCalls.size(); i++) {
                drawCalls.get(i).batch.setProjectionMatrix(PlayScreen.HUDcamera.getCamera().combined);
                drawCalls.get(i).batch.begin();
                drawer.setColor(drawCalls.get(i).color);
                drawer.getData().setScale(drawCalls.get(i).scale);
                drawer.draw(drawCalls.get(i).batch, drawCalls.get(i).text, drawCalls.get(i).x, drawCalls.get(i).y);
                drawCalls.get(i).batch.end();
                drawCalls.remove(i);
            }
        }
        if(drawTimeout.size() > 0) {
            for(int i = 0; i < drawTimeout.size(); i++) {
                if(drawTimeout.get(i).startedTime == 0f) {
                    drawTimeout.get(i).startedTime = System.currentTimeMillis();
                    Gdx.app.log("here", "" + drawTimeout.get(i).startedTime);
                }
                if(((System.currentTimeMillis() - drawTimeout.get(i).startedTime) / 1000f) > drawTimeout.get(i).goalTime) {
                    drawTimeout.remove(i);
                    break;
                } else {
                    Gdx.app.log("", "" + (float) ((System.currentTimeMillis() - drawTimeout.get(i).startedTime) / 1000f) );
                }
                drawTimeout.get(i).batch.setProjectionMatrix(PlayScreen.HUDcamera.getCamera().combined);
                drawTimeout.get(i).batch.begin();
                drawer.setColor(drawTimeout.get(i).color);
                drawer.getData().setScale(drawTimeout.get(i).scale);
                drawer.draw(drawTimeout.get(i).batch, drawTimeout.get(i).text, drawTimeout.get(i).x, drawTimeout.get(i).y);
                drawTimeout.get(i).batch.end();
            }
        }
    }
}

class Text {
    public SpriteBatch batch;
    public String text;
    public float x, y;
    public Color color;
    public float scale;

    public Text(SpriteBatch batch, String text, float x, float y, Color color, float scale) {
        this.batch = batch;
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.scale = scale;
    }
}

class TextWithTimeout {
    public SpriteBatch batch;
    public String text;
    public float x, y;
    public Color color;
    public float scale;
    public float startedTime;
    public float goalTime;

    public TextWithTimeout(SpriteBatch batch, String text, float x, float y, Color color, float scale, float secTimeout) {
        this.batch = batch;
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.scale = scale;
        this.goalTime = secTimeout;
    }
}

