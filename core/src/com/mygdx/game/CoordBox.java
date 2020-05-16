package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CoordBox {

    private int playerIdentify;
    boolean flipped;
    String BplayerName;
    Vector2 BpositionPlayer;
    int BplayerAnimNumber;
    Rectangle BrectanglePlayer;
    int Bhp;
    Vector2 BbulletsPosition;
    Rectangle BrectangleShoot;
    Double Bangles;
    String Btime;
    int seconds;

    CoordBox(int playerIdentify, Vector2 positionPlayer ,int playerAnimNumber ,boolean flipped, Rectangle rectanglePlayer, int hp,
             Vector2 bulletsPosition,Double angle,Rectangle rectangleShoot){
        this.playerIdentify=playerIdentify;
        this.flipped=flipped;
        BpositionPlayer=positionPlayer;
        BplayerAnimNumber=playerAnimNumber;
        BrectanglePlayer=rectanglePlayer;
        Bhp=hp;

        BbulletsPosition = bulletsPosition;
        Bangles = angle;
        BrectangleShoot = rectangleShoot;
    }

    CoordBox(int playerIdentify){
        this.playerIdentify=playerIdentify;
    }

    CoordBox(int playerIdentify,Vector2 positionPlayer ,int playerAnimNumber ,boolean flipped, Rectangle rectanglePlayer, int hp){
        this.playerIdentify=playerIdentify;
        this.flipped=flipped;
        BpositionPlayer=positionPlayer;
        BplayerAnimNumber=playerAnimNumber;
        BrectanglePlayer=rectanglePlayer;
        Bhp=hp;
    }

    CoordBox(){}

    public int getPlayerIdentify() {
        return playerIdentify;
    }

    public void setPlayerIdentify(int playerIdentify) {
        this.playerIdentify = playerIdentify;
    }
}
