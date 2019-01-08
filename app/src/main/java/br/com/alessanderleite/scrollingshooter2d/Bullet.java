package br.com.alessanderleite.scrollingshooter2d;

import android.graphics.PointF;

public class Bullet {

    private PointF point;

    /*
    * These next two variables control the actual movement rate per frame
    * their values are set each frame based on speed and heading
    */

    private float horizontalVelocity;
    private float verticalVelocity;
    float speed = 60;

    // Is the bullet currently in action?
    private boolean isActive;

    public Bullet() {

        // Inactive until fired
        isActive = false;

        // World coordinates contained in a point
        point = new PointF();
    }
}
