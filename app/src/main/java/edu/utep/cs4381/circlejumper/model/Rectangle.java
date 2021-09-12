/*
Oscar Castro
CS 4381
Circle Jumper - Rectangle
 */

package edu.utep.cs4381.circlejumper.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import edu.utep.cs4381.circlejumper.R;

public class Rectangle extends GameObject {

    private int velY = 8;
    private boolean left;

    // Left rectangle
    public Rectangle(Context context, int screenX, int screenY, int posXL, int posXR, boolean left) {
        setBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.rectangle));
        setBitmap(scaleBitmap(getBitmap(), screenX));

        this.left = left;
        maxY = screenY;
        minY = 0;
        maxX = screenX;
        minX = 0;
        y = 50;
        if(left){
            x = posXL;
        }else {
            x = posXR;
        }
        setRectHitBox();
    }

    @Override
    public void update() {
        // go down if its not jumping
        y += velY;
        setRectHitBox();
    }
}
