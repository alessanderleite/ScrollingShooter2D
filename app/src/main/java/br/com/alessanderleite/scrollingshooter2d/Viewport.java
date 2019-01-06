package br.com.alessanderleite.scrollingshooter2d;

import android.graphics.PointF;
import android.graphics.RectF;

public class Viewport {

    private PointF currentViewportWorldCentre;
    private RectF convertedRect;
    private PointF convertedPoint;
    private int pixelsPerMetreX;
    private int pixelsPerMetreY;
    private int screenCentreX;
    private int screenCentreY;
    private int metresToShowX;
    private int metresToShowY;

    // All the rest of the Viewport code goes here
    public Viewport(int screenXResolution, int screenYResolution) {

        screenCentreX = screenXResolution / 2;
        screenCentreY = screenYResolution / 2;

        pixelsPerMetreX = screenXResolution / 90;
        pixelsPerMetreY = screenYResolution / 55;

        metresToShowX = 92;
        metresToShowY = 57;

        convertedRect = new RectF();
        convertedPoint = new PointF();

        currentViewportWorldCentre = new PointF();

        // End of viewport class
    }
}
