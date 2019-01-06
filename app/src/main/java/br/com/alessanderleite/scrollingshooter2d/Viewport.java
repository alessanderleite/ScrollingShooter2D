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

    public void setWorldCentre(float x, float y) {
        currentViewportWorldCentre.x = x;
        currentViewportWorldCentre.y = y;
    }

    public RectF worldToScreen(float objectX, float objectY, float objectWidth, float objectHeight) {
        int left = (int)(screenCentreX - ((currentViewportWorldCentre.x - objectX) * pixelsPerMetreX));
        int top = (int)(screenCentreY - ((currentViewportWorldCentre.y - objectY) * pixelsPerMetreY));
        int right = (int)(left + (objectWidth * pixelsPerMetreX));
        int bottom = (int)(top + (objectHeight * pixelsPerMetreY));
        convertedRect.set(left, top, right, bottom);
        return convertedRect;
    }

    public PointF worldToScreenPoint(float objectX, float objectY) {
        int left = (int)(screenCentreX - ((currentViewportWorldCentre.x - objectX) * pixelsPerMetreX));
        int top = (int)(screenCentreY - ((currentViewportWorldCentre.y - objectY) * pixelsPerMetreY));

        convertedPoint.x = left;
        convertedPoint.y = top;
        return convertedPoint;
    }
    
}
