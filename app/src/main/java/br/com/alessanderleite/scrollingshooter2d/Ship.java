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

    /*
    * These next two variables control the actual movement rate per frame
    * their values are set each frame based on speed and heading
    */

    private float horizontalVelocity;
    private float verticalVelocity;

    /*
    * How fast does the ship rotate?
    * 1 complete circle per second
    */

    // Which ways can the ship move
    public final int STOPPING = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int THRUSTING = 3;

    // Is the ship moving and in which direction
    private int shipMoving = STOPPING;

    /*
    * This the the constructor method
    * When we create an object from this class we will pass
    * in the screen width and height
    */

    public Ship() {

        float length = 2.5f;
        float width = 1.25f;

        a = new PointF();
        b = new PointF();
        c = new PointF();
        centre = new PointF();

        centre.x = 50;
        centre.y = 50;

        a.x = centre.x;
        a.y = centre.y - length / 2;

        b.x = centre.x - width / 2;
        b.y = centre.y + length / 2;

        c.x = centre.x + width / 2;
        c.y = centre.y + length / 2;
    }

    public PointF getA() {
        return a;
    }

    public PointF getB() {
        return b;
    }

    public PointF getC() {
        return c;
    }

    public PointF getCentre() {
        return centre;
    }

    public float getFacingAngle() {
        return facingAngle;
    }

    public void bump() {

        speed = 0;

        // Move back
        centre.x = centre.x - horizontalVelocity * 2;
        centre.y = centre.y - verticalVelocity * 2;

        a.x = a.x - horizontalVelocity * 2;
        a.y = a.y - verticalVelocity * 2;

        b.x = b.x - horizontalVelocity * 2;
        b.y = b.y - verticalVelocity * 2;

        c.x = c.x - horizontalVelocity * 2;
        c.y = c.y - verticalVelocity * 2;
    }

    /*
    * This method will be used to change/set if the
    * ship is rotating left, right or thrusting
    */

    public void setMovementState(int state) {
        shipMoving = state;
    }

    /*
    * This update method will be called from update in HeadingAndRotationView
    * It determines if the player ship needs to move and changes the coordinates
    * and rotation when necessary.
    */

    public void  update(long fps) {

        final float ROTATION_SPEED = 200;
        final float BREAK_RATE = 30;

        /*
        * Where are we facing ate the moment
        * Then when we rotate we can work out
        * by how much
        */
        float previusFA = facingAngle;

        if (shipMoving == LEFT) {

            facingAngle = facingAngle - ROTATION_SPEED / fps;

            if (facingAngle < 1) {
                facingAngle = 360;
            }
        }

        if (shipMoving == RIGHT) {

            facingAngle = facingAngle + ROTATION_SPEED / fps;

            if (facingAngle > 360) {
                facingAngle = 1;
            }
        }

        if (shipMoving == THRUSTING) {

            final float MAX_SPEED = 80;
            final float ACCELERATION_RATE = 40;

            /*
            * facingAngle can be any angle between 1 and 360 degrees
            * the Math.toRadians method simply converts the more conventional
            * degree measurements to radians which are required by
            * the cos and sin methods.
            */

            if (speed < MAX_SPEED) {
                speed = speed + ACCELERATION_RATE / fps;
            }

            horizontalVelocity = (float)(Math.cos(Math.toRadians(facingAngle)));
            verticalVelocity = (float)(Math.sin(Math.toRadians(facingAngle)));
        }

        centre.x = centre.x + horizontalVelocity * speed / fps;
        centre.y = centre.y + verticalVelocity * speed / fps;

        a.x = a.x + horizontalVelocity * speed / fps;
        a.y = a.y + verticalVelocity * speed / fps;

        b.x = b.x + horizontalVelocity * speed / fps;
        b.y = b.y + verticalVelocity * speed / fps;

        c.x = c.x + horizontalVelocity * speed / fps;
        c.y = c.y + verticalVelocity * speed / fps;

        if (shipMoving != THRUSTING) {
            if (speed > 0) {
                speed = speed - (BREAK_RATE / fps);
            }
        }
    }
}
