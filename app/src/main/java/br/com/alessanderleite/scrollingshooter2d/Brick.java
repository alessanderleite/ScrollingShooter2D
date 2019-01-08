package br.com.alessanderleite.scrollingshooter2d;

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
}
