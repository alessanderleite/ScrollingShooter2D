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

    private void prepareLevel() {

        player = new Ship();

        // Initialize the playerBullets array
        for (int i = 0; i < playerBullets.length; i++) {
            playerBullets[i] = new Bullet();
        }

        // Reset the players location as the world centre of the viewport
        // if game is playing
        vp.setWorldCentre(player.getCentre().x, player.getCentre().y);

        Random random = new Random();
        int gapFromLastBuilding;
        int maxGap = 25;
        int buildingWidth;
        int maxBuildingWidth = 10;
        int buildingHeight;
        int maxBuildingHeight = 85;

        for (worldWidth = 0;
                worldWidth < targetWorldWidth;
                worldWidth += buildingWidth + gapFromLastBuilding) {

            buildingWidth = random.nextInt(maxBuildingWidth) + 3;
            buildingHeight = random.nextInt(maxBuildingHeight) + 1;
            gapFromLastBuilding = random.nextInt(maxGap) + 1;

            for (int x = 0; x < buildingWidth; x++) {
                for (int y = groundLevel; y > groundLevel - buildingHeight; y--) {

                    boolean isLeft = false;
                    boolean isRight = false;
                    boolean isTop = false;

                    // Is this brick on left, right or top?
                    if (x == 0) {
                        isLeft = true;
                    }
                    if (x == buildingWidth - 1) {
                        isRight = true;
                    }
                    if (y == (groundLevel - buildingHeight) + 1) {
                        isTop = true;
                    }

                    bricks[numBricks] = new Brick(x + worldWidth, y,
                            isLeft, isRight, isTop);

                    numBricks++;
                }
            }
        }

        // Instantiate some stars
        for (int i = 0; i < 500; i++) {

            stars[i] = new Star(targetWorldWidth, targetWorldHeight);
            numStars++;
        }
    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {

        // Reset the players location as the world centre of the viewport
        // if game is playing
        vp.setWorldCentre(player.getCentre().x, player.getCentre().y);

        // Clip all off screen bricks
        for (int i = 0; i < numBricks; i++) {
            if (vp.clipObjects(bricks[i].getRect().left,
                    bricks[i].getRect().top, 1, 1)) {

                bricks[i].clip();
            } else {
                bricks[i].unClip();
            }
        }

        player.update(fps);

        // Update all the player bullets if active
        for (int  i = 0; i < playerBullets.length; i++) {
            if (playerBullets[i].getStatus()) {
                playerBullets[i].update(fps);
            }
        }

        // Have the player's bullets hit a building?
        // Update all the player bullets if active
        for (int i = 0; i < maxPlayerBullets; i++) {
            if (playerBullets[i].getStatus()) {
                for (int j = 0; j < numBricks; j++) {
                    if (!bricks[j].isClipped()) {

                        // Only process this brick if not destroyed
                        if (!bricks[j].isDestroyed()) {
                            if (bricks[j].getRect().contains(
                                    playerBullets[i].getPoint().x,
                                    playerBullets[i].getPoint().y)){

                                playerBullets[i].setInactive();
                                soundPool.play(damageBuildingID, 1, 1, 0, 0, 1);
                                bricks[j].destroy();

                                int chainReactionSize = random.nextInt(6);
                                for (int k = 1; k < chainReactionSize; k++) {
                                    bricks[j+k].destroy();
                                    bricks[j-k].destroy();
                                }
                            }
                        }
                    }
                }
            }
        }

        // set bullets inactive when they go out of view
        // Clip all off screen bricks
        for (int i = 0; i < maxPlayerBullets; i++) {
            if (playerBullets[i].getStatus()) {

                if(playerBullets[i].getPoint().x < 0){
                    playerBullets[i].setInactive();
                }

                else if(playerBullets[i].getPoint().x > targetWorldWidth){
                    playerBullets[i].setInactive();
                }

                else if(playerBullets[i].getPoint().y < 0){
                    playerBullets[i].setInactive();
                }

                else if(playerBullets[i].getPoint().y > targetWorldHeight){
                    playerBullets[i].setInactive();
                }
            }
        }


        // Update the stars
        for (int i = 0; i < numStars; i++) {
            stars[i].update();
        }

        // Update the bricks
        for (int i = 0; i < numBricks; i++) {
            if (!bricks[i].isClipped()) {
                bricks[i].update();
            }
        }


        // Has the ship collided with a top, left or right brick?
        // that isn't destroyed
        for (int i = 0; i < numBricks; i++) {
            if (!bricks[i].isClipped() && !bricks[i].isDestroyed()) {
                if (bricks[i].getRect().contains(player.getA().x, player.getA().y) ||
                        bricks[i].getRect().contains(player.getB().x, player.getB().y) ||
                        bricks[i].getRect().contains(player.getC().x, player.getC().y)) {

                    player.bump();
                }
            }
        }

        // Has the ship collided with the floor?
        if (player.getA().y > groundLevel ||
                player.getB().y > groundLevel ||
                player.getC().y > groundLevel) {

            player.bump();
        }


        // Has the ship collided with the game world ceiling?
        if (player.getA().y < 0 ||
                player.getB().y < 0 ||
                player.getC().y < 0) {

            player.bump();
        }

        // Has the ship collided with the game world left?
        if (player.getA().x < 0 ||
                player.getB().x < 0 ||
                player.getC().x < 0) {

            player.bump();

        }

        // Has the ship collided with the game world right?
        if (player.getA().x > worldWidth ||
                player.getB().x > worldWidth ||
                player.getC().x > worldWidth) {

            player.bump();
        }
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