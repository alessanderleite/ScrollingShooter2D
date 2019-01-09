package br.com.alessanderleite.scrollingshooter2d;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    // A handy Random instance
    Random random = new Random();

    // For sound FX
    private SoundPool soundPool;
    private int shootID = -1;
    private int damageBuildingID = -1;

    // This is an instance of an inner class
    // See end of this class for code
    HUD hud;

    // How big is the world?
    // Change these for lots of fun...
    // Ande a slow game
    int worldWidth = 0;
    int targetWorldWidth = 500;
    int targetWorldHeight = 150;
    int groundLevel = 145;

    // For the returned rectangle from the viewport
    // These we will use in the main game loop, multiple times
    // Saves creating new instances each frame
    RectF convertedRect = new RectF();
    PointF convertedPointA = new PointF();
    PointF convertedPointB = new PointF();
    PointF convertedPointC = new PointF();
    PointF tempPointF = new PointF();

    // This is our thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // Game is paused ate the start
    private boolean paused = true;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // The city is built from bricks
    private Brick[] bricks = new Brick[20000];
    private int numBricks;

    // Twinkling stars in the sky above the city
    private Star[] stars = new Star[5000];
    private int numStars;

    // The player's ship
    Ship player;

    // The player's bullets
    private Bullet[] playerBullets = new Bullet[10];
    private int nextPlayerBullet;
    private int maxPlayerBullets = 10;

    // Our neat viewport/camera/clipping machine
    Viewport vp;

    // The constructor
    public GameView(Context context, int screenX, int screenY) {

        // The next line of code asks the
        // SurfaceView class to set up our object.
        super(context);

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        // Initialize the Viewport
        vp = new Viewport(screenX, screenY);


        hud = new HUD(screenX, screenY);

        // This SoundPool is deprecated but don't worry
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try {
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("damagebuilding.ogg");
            damageBuildingID = soundPool.load(descriptor,0);
        } catch (IOException e) {
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        prepareLevel();
    }

    @Override
    public void run() {

    }
}

class HUD {

}
