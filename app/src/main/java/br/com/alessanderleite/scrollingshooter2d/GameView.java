package br.com.alessanderleite.scrollingshooter2d;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
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
            damageBuildingID = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        prepareLevel();
    }

    @Override
    public void run() {

    }

    class HUD {

        Rect left;
        Rect right;
        Rect thrust;
        Rect shoot;
        Rect pause;

        // create an array of buttons for the draw method
        public ArrayList<Rect> currentButtonList = new ArrayList<>();

        HUD(int screenWidth, int screenHeight) {

            // Configure the player buttons
            int buttonWidth = screenWidth / 8;
            int buttonHeight = screenHeight / 7;
            int buttonPadding = screenWidth / 80;

            left = new Rect(buttonPadding,
                    screenHeight - buttonHeight - buttonPadding,
                    buttonWidth,
                    screenHeight - buttonPadding);

            right = new Rect(buttonWidth + buttonPadding,
                    screenHeight - buttonHeight - buttonPadding,
                    buttonWidth + buttonPadding + buttonWidth,
                    screenHeight - buttonPadding);

            thrust = new Rect(screenWidth - buttonWidth - buttonPadding,
                    screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
                    screenWidth - buttonPadding,
                    screenHeight - buttonPadding - buttonHeight - buttonPadding);

            shoot = new Rect(screenWidth - buttonWidth - buttonPadding,
                    screenHeight - buttonHeight - buttonPadding,
                    screenWidth - buttonPadding,
                    screenHeight - buttonPadding);

            pause = new Rect(screenWidth - buttonPadding - buttonWidth,
                    buttonPadding,
                    screenWidth - buttonPadding,
                    buttonPadding + buttonHeight);

            // Add the rect objects in the same order as the static final values
            currentButtonList.add(left);
            currentButtonList.add(right);
            currentButtonList.add(thrust);
            currentButtonList.add(shoot);
            currentButtonList.add(pause);
        }

        public void handleInput(MotionEvent motionEvent) {

            int x = (int) motionEvent.getX(0);
            int y = (int) motionEvent.getY(0);

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    if (right.contains(x,y)) {
                        player.setMovementState(player.RIGHT);
                    } else if (left.contains(x,y)) {
                        player.setMovementState(player.LEFT);
                    } else if (thrust.contains(x,y)) {
                        player.setMovementState(player.THRUSTING);
                    } else if (shoot.contains(x,y)) {
                        playerBullets[nextPlayerBullet].shoot(
                                player.getA().x,player.getA().y,player.getFacingAngle());
                        nextPlayerBullet++;
                        if (nextPlayerBullet == maxPlayerBullets) {
                            nextPlayerBullet = 0;
                        }
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    } else if (pause.contains(x,y)) {
                        paused = !paused;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    player.setMovementState(player.STOPPING);
            }
        }
    } // End of HUB inner class
} // End of GameView