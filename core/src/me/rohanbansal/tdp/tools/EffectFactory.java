package me.rohanbansal.tdp.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class EffectFactory {

    public enum EffectType {
            VORTEX, EXPLOSION
    }

    private static TextureAtlas atlas;
    private static ArrayList<Effect> effects = new ArrayList<>();
    private static ArrayList<Effect> effectRemoveQueue = new ArrayList<>();

    public static Effect createEffect(EffectType effect, Vector2 position, boolean looping, float scale) {
        Effect particle = null;

        if(effect == EffectType.EXPLOSION) {
            atlas = new TextureAtlas(Gdx.files.internal("effects/explosion.pack"));
            particle = new Effect(0.2f, atlas, position, looping, scale);
            effects.add(particle);
        } else if(effect == EffectType.VORTEX) {
            atlas = new TextureAtlas(Gdx.files.internal("effects/fireRing.pack"));
            particle = new Effect(0.05f, atlas, position, looping, scale);
            effects.add(particle);
        }

        return particle;
    }

    public static void render(CameraController HUDcamera, SpriteBatch batch) {
        for(Effect f : effects) {
            f.render(HUDcamera, batch);
        }

        for(Effect ef : effectRemoveQueue) {
            effects.remove(ef);
        }

        effectRemoveQueue.clear();
    }

    public static void deleteEffect(Effect effect) {
        effectRemoveQueue.add(effect);
    }

    public static void disposeAtlas() {
        atlas.dispose();
    }
}
