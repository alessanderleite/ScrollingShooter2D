package br.com.alessanderleite.scrollingshooter2d;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    // View will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    GameView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        // Initialize gameView and set it as the view
        view = new GameView(this, size.x, size.y);
        setContentView(view);
    }
}
