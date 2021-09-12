/*
Oscar Castro
CS 4381
Circle Jumper - Background
 */

package edu.utep.cs4381.circlejumper.model;

public class Background extends GameObject {

    //constructor
    public Background(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        // set a speed between 0 and 9
        speed = generate(10) + 1;

        //set starting coordinates
        x = generate(maxX);
        y = generate(maxY);

    }

    @Override
    public void update(){
        // falling specs from top to bottom
        y += speed;

        if(y > maxY){
            y = minY;
            x = generate(maxX);
            speed = generate(15) + 1;
        }
    }
}
