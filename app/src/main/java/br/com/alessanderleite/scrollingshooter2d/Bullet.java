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

    public boolean shoot(float startX, float startY, float direction) {
        if (!isActive) {
            point.x = startX;
            point.y = startY;

            // How much to move horizontally, each frame
            horizontalVelocity = (float)(Math.cos(Math.toRadians(direction)));

            // How much to move vertically, each frame
            verticalVelocity = (float)(Math.sin(Math.toRadians(direction)));

            isActive = true;
            return true;
        }
        // Bullet already active
        return false;
    }

    public void update(long fps) {
        // Move the bullet
        point.x = point.x + horizontalVelocity * speed / fps;
        point.y = point.y + verticalVelocity * speed / fps;
    }

    public PointF getPoint() {
        return point;
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }
}
