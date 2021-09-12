package edu.utep.cs4381.circlejumper.model;

public class DoublePoints extends GameObject{

    private int spawnRate;

    public DoublePoints(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        // spawn rate
        spawnRate = 500;

        // set a fixed speed of 20
        speed = 15;

        //set starting coordinates
        x = generate(maxX);
        y = minY - 10;
    }

    public void update(){
        y += speed;

        if(y > maxY){
            spawnRate--;
            if(spawnRate == 0) {
                x = generate(maxX);
                y = minY;
                spawnRate = 500;
            }
        }
    }
}
