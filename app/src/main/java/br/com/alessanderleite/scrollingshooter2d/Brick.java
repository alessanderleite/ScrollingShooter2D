package br.com.alessanderleite.scrollingshooter2d;

import android.graphics.Color;
import android.graphics.RectF;

import java.util.Random;

public class Brick {

    private RectF rect;
    Random random = new Random();

    boolean destroyed;

    private boolean isRight;
    private boolean isLeft;
    private boolean isTop;
    private int color;
    private boolean clipped;

    public Brick(int columnNum, int rowNum,
                 boolean isLeft, boolean isRight,
                 boolean isTop) {

        int brickWidth = 1;
        int brickHeight = 1;

        this.isLeft = isLeft;
        this.isRight = isRight;
        this.isTop = isTop;

        float brickXPadding = .0f;
        float brickYPadding = .0f;

        rect = new RectF(
                (columnNum * (brickXPadding + brickWidth +
                        brickXPadding) + brickXPadding),
                ((rowNum * (brickYPadding + brickHeight +
                        brickYPadding)) + brickYPadding),
                (columnNum * (brickXPadding + brickWidth +
                        brickXPadding) + brickXPadding + brickWidth),
                ((rowNum * (brickYPadding + brickHeight +
                        brickYPadding)) + brickYPadding + brickHeight)
        );

        // Assign a color
        if (random.nextInt(9) == 0) {
            // Vary the alpha for effect
            int alpha = random.nextInt(256);
            color = Color.argb(alpha,255,255,0);
        } else {
            color = Color.argb(255,0,0,0);
        }
    }
}
