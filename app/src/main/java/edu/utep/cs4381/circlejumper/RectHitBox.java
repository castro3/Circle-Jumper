/*
Oscar Castro
CS 4381
Circle Jumper - RectHitBox
 */

package edu.utep.cs4381.circlejumper;

public class RectHitBox {
    public float top;
    public float left;
    public float bottom;
    public float right;

    public boolean intersects(RectHitBox rectHitbox) {
        return (this.right > rectHitbox.left
                && this.left < rectHitbox.right )
                && (this.top < rectHitbox.bottom
                && this.bottom > rectHitbox.top);
    }

    public void setTop(float top) {
        this.top = top;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void setRight(float right) {
        this.right = right;
    }
}
