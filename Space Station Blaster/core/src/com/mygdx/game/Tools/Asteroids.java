package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.LargeBrownAsteroids;
import com.mygdx.game.Sprites.LargeGreyAsteroids;
import com.mygdx.game.Sprites.MediumBrownAsteroids;
import com.mygdx.game.Sprites.MediumGreyAsteroids;
import com.mygdx.game.Sprites.SmallBrownAsteroids;
import com.mygdx.game.Sprites.SmallGreyAsteroids;

public class Asteroids {
    private static final int Asteroids_Max = 20;

    private static final float MAX_TIME_SPAWN = 10f;
    private static final float MIN_TIME_SPAWN = 1f;


    private PlayScreen playScreen;
    private Sprite[] asteroidSprite;

    private enum  TYPE {BROWN_LARGE, GREY_LARGE, BROWN_MEDIUM, GREY_MEDIUM, BROWN_SMALL, GREY_SMALL, NONE};
    private TYPE[] type;
    private float x;
    private float y;
    private float[] radians;
    private float VectorX;
    private float VectorY;
    private Vector2[] position;
    private Vector2[] direction;


    private float[] health;

    private float speed;
    private float timeSpawn = MathUtils.random(MIN_TIME_SPAWN, MAX_TIME_SPAWN);;


    public Asteroids(PlayScreen playScreen, TYPE t){
        this.playScreen = playScreen;


        position = new Vector2[Asteroids_Max];
        direction = new Vector2[Asteroids_Max];
        radians = new float[Asteroids_Max];

        int index = -1;
        for (int i = 0; i < Asteroids_Max; i++){
            if (type[i] == TYPE.NONE){
                index = i;
            }
        }

        type[index] = t;
        switch (t){
            case BROWN_LARGE:
                Sprite LBrown = new LargeBrownAsteroids(playScreen);
                asteroidSprite[index] = new Sprite(LBrown);
                speed = 32f;
                health[index] = 3;

                //Spawn random in x-axis
                VectorX = MathUtils.random(0 - LBrown.getRegionWidth(), Gdx.graphics.getWidth() + LBrown.getRegionWidth());
                //if asteroids spawn both sides of screen, Y position will spawn random
                if (VectorX < 0 + LBrown.getRegionWidth()/2 || VectorX > Gdx.graphics.getWidth() - LBrown.getRegionWidth()/2){
                    VectorY = MathUtils.random(0 - LBrown.getRegionHeight(), Gdx.graphics.getHeight() + LBrown.getRegionHeight());
                }
                //Or Asteroids only spawn on the top of screen
                else {
                    VectorY = Gdx.graphics.getHeight() + LBrown.getRegionHeight();
                }

                asteroidSprite[index].setOrigin(VectorX,VectorY);
                asteroidSprite[index].setCenter(VectorX,VectorY);

                break;

            case GREY_LARGE:
                Sprite LGrey = new LargeGreyAsteroids(playScreen);
                asteroidSprite[index] = new Sprite(LGrey);
                speed = 32f;
                health[index] = 3;

                //Spawn random in x-axis
                VectorX = MathUtils.random(0 - LGrey.getRegionWidth(), Gdx.graphics.getWidth() + LGrey.getRegionWidth());
                //if asteroids spawn both sides of screen, Y position will spawn random
                if (VectorX < 0 + LGrey.getRegionWidth()/2 || VectorX > Gdx.graphics.getWidth() - LGrey.getRegionWidth()/2){
                    VectorY = MathUtils.random(0 - LGrey.getRegionHeight(), Gdx.graphics.getHeight() + LGrey.getRegionHeight());
                }
                //Or Asteroids only spawn on the top of screen
                else {
                    VectorY = Gdx.graphics.getHeight() + LGrey.getRegionHeight();
                }

                asteroidSprite[index].setOrigin(VectorX,VectorY);
                asteroidSprite[index].setCenter(VectorX,VectorY);
                break;

            case BROWN_MEDIUM:
                Sprite M_Brown = new MediumBrownAsteroids(playScreen);
                asteroidSprite[index] = new Sprite(M_Brown);
                speed = 32f;
                health[index] = 3;

                //Spawn random in x-axis
                VectorX = MathUtils.random(0 - M_Brown.getRegionWidth(), Gdx.graphics.getWidth() + M_Brown.getRegionWidth());
                //if asteroids spawn both sides of screen, Y position will spawn random
                if (VectorX < 0 + M_Brown.getRegionWidth()/2 || VectorX > Gdx.graphics.getWidth() - M_Brown.getRegionWidth()/2){
                    VectorY = MathUtils.random(0 - M_Brown.getRegionHeight(), Gdx.graphics.getHeight() + M_Brown.getRegionHeight());
                }
                //Or Asteroids only spawn on the top of screen
                else {
                    VectorY = Gdx.graphics.getHeight() + M_Brown.getRegionHeight();
                }

                asteroidSprite[index].setOrigin(VectorX,VectorY);
                asteroidSprite[index].setCenter(VectorX,VectorY);
                break;

            case GREY_MEDIUM:
                Sprite M_Grey = new MediumGreyAsteroids(playScreen);
                asteroidSprite[index] = new Sprite(M_Grey);
                speed = 32f;
                health[index] = 3;

                //Spawn random in x-axis
                VectorX = MathUtils.random(0 - M_Grey.getRegionWidth(), Gdx.graphics.getWidth() + M_Grey.getRegionWidth());
                //if asteroids spawn both sides of screen, Y position will spawn random
                if (VectorX < 0 + M_Grey.getRegionWidth()/2 || VectorX > Gdx.graphics.getWidth() - M_Grey.getRegionWidth()/2){
                    VectorY = MathUtils.random(0 - M_Grey.getRegionHeight(), Gdx.graphics.getHeight() + M_Grey.getRegionHeight());
                }
                //Or Asteroids only spawn on the top of screen
                else {
                    VectorY = Gdx.graphics.getHeight() + M_Grey.getRegionHeight();
                }

                asteroidSprite[index].setOrigin(VectorX,VectorY);
                asteroidSprite[index].setCenter(VectorX,VectorY);
                break;

            case BROWN_SMALL:
                Sprite S_Brown = new SmallBrownAsteroids(playScreen);
                asteroidSprite[index] = new Sprite(S_Brown);
                speed = 32f;
                health[index] = 3;

                //Spawn random in x-axis
                VectorX = MathUtils.random(0 - S_Brown.getRegionWidth(), Gdx.graphics.getWidth() + S_Brown.getRegionWidth());
                //if asteroids spawn both sides of screen, Y position will spawn random
                if (VectorX < 0 + S_Brown.getRegionWidth()/2 || VectorX > Gdx.graphics.getWidth() - S_Brown.getRegionWidth()/2){
                    VectorY = MathUtils.random(0 - S_Brown.getRegionHeight(), Gdx.graphics.getHeight() + S_Brown.getRegionHeight());
                }
                //Or Asteroids only spawn on the top of screen
                else {
                    VectorY = Gdx.graphics.getHeight() + S_Brown.getRegionHeight();
                }

                asteroidSprite[index].setOrigin(VectorX,VectorY);
                asteroidSprite[index].setCenter(VectorX,VectorY);
                break;

            case GREY_SMALL:
                Sprite S_Grey = new SmallGreyAsteroids(playScreen);
                asteroidSprite[index] = new Sprite(S_Grey);
                speed = 32f;
                health[index] = 3;

                //Spawn random in x-axis
                VectorX = MathUtils.random(0 - S_Grey.getRegionWidth(), Gdx.graphics.getWidth() + S_Grey.getRegionWidth());
                //if asteroids spawn both sides of screen, Y position will spawn random
                if (VectorX < 0 + S_Grey.getRegionWidth()/2 || VectorX > Gdx.graphics.getWidth() - S_Grey.getRegionWidth()/2){
                    VectorY = MathUtils.random(0 - S_Grey.getRegionHeight(), Gdx.graphics.getHeight() + S_Grey.getRegionHeight());
                }
                //Or Asteroids only spawn on the top of screen
                else {
                    VectorY = Gdx.graphics.getHeight() + S_Grey.getRegionHeight();
                }

                asteroidSprite[index].setOrigin(VectorX,VectorY);
                asteroidSprite[index].setCenter(VectorX,VectorY);
                break;

            case NONE:
                break;

        }

    }



    public void update(float dt){



    }


}
