/*
Oscar Castro
CS 4381
Circle Jumper - Player
 */

package edu.utep.cs4381.circlejumper.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import edu.utep.cs4381.circlejumper.R;

public class Player extends GameObject{

    // player settings
    private int velY = -30;
    private int maxVelY = 10;
    private int gravity = 4;
    private int jumpVel = -40;
    private boolean isJumping;

    public Player(Context context, int screenX, int screenY) {
        setBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.player));
        //setBitmap(scaleBitmap(getBitmap(), screenX));
        x = screenX / 2 - getBitmap().getWidth()/2;
        y = screenY / 4 + screenX + 200 - getBitmap().getHeight()/2;
        maxY = screenY;
        minY = 0;
        maxX = screenX;
        minX = 0;
        isJumping = false;
        setRectHitBox();
    }

    public void startJumping() {
        isJumping = true;
    }

    public void stopJumping() {
        isJumping = false;
    }

    public void move(float posX){
        startJumping();
        if(y > 0){
            // move X depending on the touch position X
            velY = jumpVel;
            if(posX < maxX / 2) {
                x = x + Math.min(jumpVel * 3, x - getBitmap().getHeight());
            }else if(posX > maxX / 2){
                x = x - Math.min(jumpVel * 3, x - getBitmap().getHeight());
            }
        }
    }

    @Override
    public void update() {
        // Make the ball go down
        if(!isJumping && velY < maxVelY){
            velY += gravity;
        }
        y = y + Math.min(velY, y - getBitmap().getHeight());

        // if it jumps set jumping to false
        if(isJumping){
            stopJumping();
        }

        setRectHitBox();
    }
}
