package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.SpaceStationBlaster;
import com.mygdx.game.Sprites.LargeBrownAsteroids;
import com.mygdx.game.Sprites.LargeGreyAsteroids;
import com.mygdx.game.Sprites.MediumBrownAsteroids;
import com.mygdx.game.Sprites.MediumGreyAsteroids;
import com.mygdx.game.Sprites.SmallBrownAsteroids;
import com.mygdx.game.Sprites.SmallGreyAsteroids;

import java.util.ArrayList;

public class Asteroids {
    private static final int Asteroids_Max = 20;

    private static final float MAX_TIME_SPAWN = 10f;
    private static final float MIN_TIME_SPAWN = 1f;


    private PlayScreen playScreen;
    private Sprite asteroidSprite;

    private enum  TYPE {BROWN_LARGE, GREY_LARGE, BROWN_MEDIUM, GREY_MEDIUM, BROWN_SMALL, GREY_SMALL, NONE};
    private TYPE type;
    private float x;
    private float y;
    private float radians;
    private float dx;
    private float dy;
    private Vector2[] position;
    private Vector2[] direction;

    private float health;

    private float speed;
    private float rotationSpeed;
    private float timeSpawn = MathUtils.random(MIN_TIME_SPAWN, MAX_TIME_SPAWN);;

    ArrayList<Asteroids> asteroids = new ArrayList<Asteroids>();

    public Asteroids(PlayScreen playScreen, float x, float y, TYPE type){
        this.playScreen = playScreen;
        this.x = x;
        this.y = y;

        position = new Vector2[Asteroids_Max];
        direction = new Vector2[Asteroids_Max];
        radians = MathUtils.random(2 * 3.1415f);
        rotationSpeed = MathUtils.random(-1,1);

        switch (type){
            case BROWN_LARGE:
                Sprite LBrown = new LargeBrownAsteroids(playScreen);
                asteroidSprite = new Sprite(LBrown);
                speed = 32f;
                health = 3f;

                dx = MathUtils.cos(radians) * speed;
                dy = MathUtils.sin(radians) * speed;

                asteroidSprite.setOrigin(dx,dy);
                asteroidSprite.setCenter(dx,dy);

                break;

            case GREY_LARGE:
                Sprite LGrey = new LargeGreyAsteroids(playScreen);
                asteroidSprite = new Sprite(LGrey);
                speed = 32f;
                health = 3f;

                dx = MathUtils.cos(radians) * speed;
                dy = MathUtils.sin(radians) * speed;

                asteroidSprite.setOrigin(dx,dy);
                asteroidSprite.setCenter(dx,dy);
                break;

            case BROWN_MEDIUM:
                Sprite M_Brown = new MediumBrownAsteroids(playScreen);
                asteroidSprite = new Sprite(M_Brown);
                speed = 64f;
                health = 1.5f;

                dx = MathUtils.cos(radians) * speed;
                dy = MathUtils.sin(radians) * speed;

                asteroidSprite.setOrigin(dx,dy);
                asteroidSprite.setCenter(dx,dy);
                break;

            case GREY_MEDIUM:
                Sprite M_Grey = new MediumGreyAsteroids(playScreen);
                asteroidSprite= new Sprite(M_Grey);
                speed = 64f;
                health = 1.5f;

                dx = MathUtils.cos(radians) * speed;
                dy = MathUtils.sin(radians) * speed;

                asteroidSprite.setOrigin(dx,dy);
                asteroidSprite.setCenter(dx,dy);
                break;

            case BROWN_SMALL:
                Sprite S_Brown = new SmallBrownAsteroids(playScreen);
                asteroidSprite = new Sprite(S_Brown);
                speed = 96f;
                health = 0.75f;

                dx = MathUtils.cos(radians) * speed;
                dy = MathUtils.sin(radians) * speed;

                asteroidSprite.setOrigin(dx,dy);
                asteroidSprite.setCenter(dx,dy);
                break;

            case GREY_SMALL:
                Sprite S_Grey = new SmallGreyAsteroids(playScreen);
                asteroidSprite = new Sprite(S_Grey);
                speed = 96f;
                health = 0.75f;

                dx = MathUtils.cos(radians) * speed;
                dy = MathUtils.sin(radians) * speed;

                asteroidSprite.setOrigin(dx,dy);
                asteroidSprite.setCenter(dx,dy);
                break;

            case NONE:
                break;

        }

    }



    public void update(float dt){
        x = dx * dt;
        y = dy * dt;

        radians += rotationSpeed * dt;
        HitWall();
    }

    public void InitSpawnAsteroids(){
        asteroids.clear();

        for (int i = 0; i < Asteroids_Max ; i ++){
            x = MathUtils.random(Gdx.graphics.getWidth());
            y = MathUtils.random(Gdx.graphics.getHeight());

            int randomNum = MathUtils.random(1,6);
            if (randomNum == 1) {
                asteroids.add(new Asteroids(playScreen, x, y,TYPE.BROWN_LARGE ));
            }
            if (randomNum == 2) {
                asteroids.add(new Asteroids(playScreen, x, y,TYPE.GREY_LARGE ));
            }
            if (randomNum == 3) {
                asteroids.add(new Asteroids(playScreen, x, y,TYPE.BROWN_MEDIUM ));
            }
            if (randomNum == 4) {
                asteroids.add(new Asteroids(playScreen, x, y,TYPE.GREY_MEDIUM ));
            }
            if (randomNum == 5) {
                asteroids.add(new Asteroids(playScreen, x, y,TYPE.BROWN_SMALL ));
            }
            if (randomNum == 6) {
                asteroids.add(new Asteroids(playScreen, x, y,TYPE.GREY_SMALL ));
            }
        }

    }

    public TYPE getType () {return type;}

    public float getX(){return x;}
    public float getY(){return y;}

    public void split(Asteroids a){

        if (a.getType() == TYPE.BROWN_LARGE){
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.BROWN_MEDIUM));
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.BROWN_MEDIUM));
        }
        if (a.getType() == TYPE.GREY_LARGE){
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.GREY_MEDIUM));
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.GREY_MEDIUM));
        }
        if (a.getType() == TYPE.BROWN_MEDIUM){
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.BROWN_SMALL));
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.BROWN_SMALL));
        }
        if (a.getType() == TYPE.GREY_MEDIUM){
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.GREY_SMALL));
            asteroids.add(new Asteroids(playScreen, a.getX(),a.getY(), TYPE.GREY_SMALL));
        }
    }

    public void HitWall(){
        if (getX() < 0) { x = SpaceStationBlaster.MAP_WIDTH;}
        if (getX() > SpaceStationBlaster.MAP_WIDTH){x = 0;}
        if (getY() < 0) {y = SpaceStationBlaster.MAP_HEIGHT;}
        if (getY() > SpaceStationBlaster.MAP_HEIGHT){y = 0;}
    }

    public void render(SpriteBatch batch) {
        for (int i=0; i<asteroids.size(); i++) {
            if (asteroids.get(i).getType() != TYPE.NONE) {
                asteroids.get(i).asteroidSprite.draw(batch);
            }
        }
    }

}
