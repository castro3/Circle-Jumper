/*
Oscar Castro
CS 4381
Circle Jumper - GameObject
 */

package edu.utep.cs4381.circlejumper.model;

import android.graphics.Bitmap;

import java.util.Random;

import edu.utep.cs4381.circlejumper.RectHitBox;

public abstract class GameObject {
    private RectHitBox rectHitbox = new RectHitBox();
    protected int x;
    protected int y;
    protected int speed;
    protected Random generator = new Random();

    // boundaries
    protected int maxY;
    protected int maxX;
    protected int minX;
    protected int minY;

    private Bitmap bitmap;

    public abstract void update();

    public void setRectHitBox() {
        rectHitbox.setTop(y);
        rectHitbox.setLeft(x);
        rectHitbox.setBottom(y + bitmap.getHeight());
        rectHitbox.setRight(x + bitmap.getWidth());
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap scaleBitmap(Bitmap bitmap, int screenX) {

        float scale = screenX < 1000 ? 3 : (screenX < 1200 ? 2 : 1.5f);
        return Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() / scale),
                (int) (bitmap.getHeight() / scale),
                false);

    }

    public RectHitBox getRectHitbox() {
        return rectHitbox;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int generate(int number){
        return  generator.nextInt(number);
    }
}
