package br.com.alessanderleite.scrollingshooter2d;

import android.graphics.PointF;

public class Ship {

    PointF a;
    PointF b;
    PointF c;
    PointF centre;

    /*
    * Which way is the ship facing
    * Straight up to start with
    */

    float facingAngle = 270;

    // How long will our spaceship be
    // private float length;
    // private float width;

    // This will hold the pixels per second speed that the ship can move at
    private float speed = 0;
    //private final float MAX_SPEED = 80;
    //private final float ACCELERATION_RATE = 40;
    //private final float BREAK_RATE = 30;

    /*
    These next two variables control the actual movement rate per frame
    their values are set each frame based on speed and heading
    */

    private float horizontalVelocity;
    private float verticalVelocity;

    /*
    How fast does the ship rotate?
    1 complete circle per second
    */

    //private final float ROTATION_SPEED = 200;

    // Which ways can the ship move
    public final int STOPPING = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int THRUSTING = 3;

    // Is the ship moving and in which direction
    private int shipMoving = STOPPING;
}
