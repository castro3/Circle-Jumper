/*
Oscar Castro
CS 4381
Circle Jumper - Obstacle
 */

package edu.utep.cs4381.circlejumper.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import edu.utep.cs4381.circlejumper.R;

public class Obstacle extends GameObject{

    private int velY = 8;
    private boolean right;

    public Obstacle(Context context, int screenX, int screenY, boolean right) {
        setBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.obstacle));
        setBitmap(scaleBitmap(getBitmap(), screenX));

        this.right = right;

        if(right) {
            x = screenX / 4 + screenX / 2;
            y = screenY - screenX + 400;
            maxX = screenX - getBitmap().getWidth();
            minX = screenX / 2;
        }else {
            x = screenX / 4;
            y = screenY - screenX;
            maxX = (screenX / 2) - getBitmap().getWidth();
            minX = 0;
        }
        minY = 0;
        maxY = screenY;
        setRectHitBox();
    }

    @Override
    public void update() {
        y += velY;
        if(y > maxY && right){
            y = -getBitmap().getHeight();
            x = generate(maxX);
        } else if(y > maxY){
            y = -getBitmap().getHeight();
            x = generate(maxX);
        }

        setRectHitBox();
    }
}
