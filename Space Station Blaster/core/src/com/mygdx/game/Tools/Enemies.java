package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemies {
    public static final int MAX_ENEMIES = 5;

    public enum Type {NONE, GREEN_UFO};

    public Type[] type;
    public Sprite[] sprite;
    public Vector2[] velocity;
    public int[] health;


    public Enemies() {
        type = new Type[MAX_ENEMIES];
        sprite = new Sprite[MAX_ENEMIES];
        velocity = new Vector2[MAX_ENEMIES];
        health = new int[MAX_ENEMIES];
    }

    public void init() {
        for (int i=0; i<MAX_ENEMIES; i++) {
            type[i] = Type.NONE;
            velocity[i] = new Vector2();
        }
    }
    //TODO: spawn enemies
    public int spawn(Type t) {
        return -1;
    }

    public void update(float deltaTime) {

    }

    public void render(SpriteBatch batch) {

    }
}
