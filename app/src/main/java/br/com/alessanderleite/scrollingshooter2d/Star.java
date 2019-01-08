package br.com.alessanderleite.scrollingshooter2d;

import java.util.Random;

public class Star {

    private int x;
    private int y;

    private boolean isVisible = true;

    // Declare a random object here because
    // we will use it in the update() method
    // and we don't want the garbage collector to have to keep clear it up
    Random random;

    public Star(int mapWidth, int mapHeight) {
        random = new Random();
        x = random.nextInt(mapWidth);
        y = random.nextInt(mapHeight);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update() {

        // Randomly twinkle the stars
        int n = random.nextInt(5000);
        if (n == 0) {
            // Switch on or off
            isVisible = !isVisible;
        }
    }

    public boolean getVisibility() {
        return isVisible;
    }
}
