/*
Oscar Castro
CS 4381
Circle Jumper - JumperView
 */

package edu.utep.cs4381.circlejumper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.utep.cs4381.circlejumper.model.Background;
import edu.utep.cs4381.circlejumper.model.DoublePoints;
import edu.utep.cs4381.circlejumper.model.Obstacle;
import edu.utep.cs4381.circlejumper.model.Player;
import edu.utep.cs4381.circlejumper.model.Rectangle;


public class JumperView extends SurfaceView implements Runnable {

    // View settings
    private volatile boolean running;
    private boolean playing;
    boolean powerUp = false;
    private Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;
    private Context context;
    private int screenX;
    private int screenY;
    private int offset;
    private DoublePoints dp;

    // button settings
    private Rect pause;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonPadding;

    private Player player;
    SoundManager  sm;
    Random random = new Random();

    // game objects
    private List<Background> backgroundList = new ArrayList<>();
    private Rectangle leftRectangles;
    private Rectangle rightRectangles;
    private Obstacle obstacleR;
    private Obstacle obstacleL;

    private int score = 0;
    private float highScore;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public JumperView(Context context, int screenX, int screenY) {
        super(context);
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;

        // initialize all our variable classes
        holder = getHolder();
        paint = new Paint();
        sm = SoundManager.instance(context);

        // offset between rectangles
        offset = screenX / 3;

        // Store
        prefs = context.getSharedPreferences("highScore", context.MODE_PRIVATE);
        editor = prefs.edit();


        // Read
        highScore = prefs.getLong("highScore", 0);

        // button settings
        buttonWidth = screenX / 10;
        buttonHeight = screenY/ 20;
        buttonPadding = screenX / 50;

        playing = false;
        game();
    }

    private void game(){

        // create player
        player = new Player(context, screenX, screenY);

        // create rectangles
        int posX = offset + random.nextInt((int) (screenX - 1.6 * offset));
        rightRectangles = new Rectangle(context, screenX, screenY,
                0, posX, false);
        leftRectangles = new Rectangle(context, screenX, screenY,
                -(rightRectangles.getBitmap().getWidth() - posX + offset), 0, true);

        // create obstacles
        obstacleR = new Obstacle(context, screenX, screenY, true);
        obstacleL = new Obstacle(context, screenX, screenY, false);

        // create power up double points
        dp = new DoublePoints(screenX, screenY);

        // clear background list for when the game is restarted
        backgroundList.clear();
        // fill background list
        int numSpecs = 400;
        for (int i = 0; i < numSpecs; i++) {
            // background spawn
            Background spec = new Background(screenX, screenY);
            backgroundList.add(spec);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_BUTTON_PRESS:
                playing = true;
                player.move(event.getX());
                // Start new game if it ended
                if(!playing){
                    playing = true;
                    game();
                }
                break;
        }

        // detect pause button touch
        int pointerCounter = event.getPointerCount();
        for (int i = 0; i < pointerCounter; i++) {

            int x = (int) event.getX(i);
            int y = (int) event.getY(i);

            if(running){
                switch (event.getAction() & MotionEvent.ACTION_MASK){

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if(pause.contains(x,y)){
                            pause();
                        }
                        break;
                }
            }else{
                switch (event.getAction() & MotionEvent.ACTION_MASK){

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if(pause.contains(x, y)){
                            resume();
                        }
                        break;
                }
            }
        }
        return true;
    }

    private void update() {
        if(playing) {
            // player update
            player.update();

            // power up update
            dp.update();

            // check if player collected powerUp
            if(dp.getX() > player.getX() && dp.getX() < player.getRectHitbox().right){
                if(dp.getY() >= player.getY() && dp.getY() < player.getRectHitbox().bottom) {
                    powerUp = true;
                    dp.setX(-player.getBitmap().getWidth());
                }
            }

            System.out.println("Player height: " + -player.getBitmap().getHeight());
            System.out.println("Player y: " + player.getY());
            // if player goes out of screen we end game
            if(player.getY() > screenY + player.getBitmap().getHeight() + 10 ||
                    player.getY() < -player.getBitmap().getHeight()
                    || player.getX() > screenX - player.getBitmap().getWidth() + 10 ||
                    player.getX() < -player.getBitmap().getWidth()){
                player.setY(screenY / 4 + screenX + 200 - player.getBitmap().getHeight()/2);
                player.setX(screenX / 2 - player.getBitmap().getWidth()/2);
                player.stopJumping();
                playing = false;
                // restart game and score
                score = 0;
                game();
            }

            // check score
            int playerMidPos = player.getY() + player.getBitmap().getHeight()/2;
            int rectMidPos = leftRectangles.getY() + leftRectangles.getBitmap().getHeight()/2;
            if(rectMidPos <= playerMidPos && playerMidPos < rectMidPos + 10){
                sm.play(SoundManager.Sound.SCORE);
                score++;
                if(powerUp){
                    score++;
                    powerUp = false;
                }
            }

            if(score > highScore){
                editor.putLong("highScore", score);
                editor.commit();
                highScore = score;
            }

            // check for collisions and end game
            if(player.getRectHitbox().intersects(leftRectangles.getRectHitbox()) ||
            player.getRectHitbox().intersects(rightRectangles.getRectHitbox()) ||
            player.getRectHitbox().intersects(obstacleL.getRectHitbox()) ||
            player.getRectHitbox().intersects(obstacleR.getRectHitbox())){
                sm.play(SoundManager.Sound.BUMP);
                playing = false;
                // restart game and score
                score = 0;
                game();
            }


            // spacing between rectangles to make sure ball
            // can go through it
            int posX = getOffSet();
            // rectangles update
            leftRectangles.update();
            if(leftRectangles.getY() > screenY) {
                leftRectangles.setY(-leftRectangles.getBitmap().getHeight());
                leftRectangles.setX(-(leftRectangles.getBitmap().getWidth() - posX + offset));
            }

            rightRectangles.update();
            if(rightRectangles.getY() > screenY){
                rightRectangles.setY(-rightRectangles.getBitmap().getHeight());
                rightRectangles.setX(posX);
            }

            // obstacles update
            obstacleL.update();
            obstacleR.update();

            // background update
            for (Background bg : backgroundList) {
                bg.update();
            }
        }
    }

    private int getOffSet() {
        return offset + leftRectangles.generate((int) (screenX - 1.6 * offset));
    }

    private void draw(){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 255, 255, 255));

            // Create a background
            paint.setColor(Color.BLACK);
            for (Background bg : backgroundList){
                canvas.drawCircle(bg.getX(),bg.getY(),3, paint);
            }


            // Draw Player
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);

            //draw power up
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(255, 0,206,0));
            canvas.drawCircle(dp.getX(), dp.getY(), 20, paint);

            // draw obstacles
            canvas.drawBitmap(obstacleL.getBitmap(), obstacleL.getX(),
                    obstacleL.getY(), paint);
            canvas.drawBitmap(obstacleR.getBitmap(), obstacleR.getX(),
                    obstacleR.getY(), paint);

            // draw rectangles
            canvas.drawBitmap(leftRectangles.getBitmap(),
                    leftRectangles.getX(),
                    leftRectangles.getY(), paint);
            canvas.drawBitmap(rightRectangles.getBitmap(),
                    rightRectangles.getX(),
                    rightRectangles.getY(), paint);

            // HUD
            paint.setColor(Color.argb(255, 0,0,0));
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(40);
            canvas.drawText("Score: " + score, 20, 50, paint);

            // draw pause button
            pause = new Rect(screenX - buttonPadding - buttonWidth,
                    buttonPadding,
                    screenX - buttonPadding,
                    buttonPadding + buttonHeight);
            paint.setColor(Color.argb(80, 0, 0,255));
            RectF rf = new RectF(pause.left, pause.top, pause.right, pause.bottom);
            canvas.drawRoundRect(rf, 15f, 15f, paint);

            // draw paused screen
            if(!running){
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255,255,0,0));
                paint.setTextSize(120);
                canvas.drawColor(Color.argb(120, 255, 255, 255));
                canvas.drawText("PAUSED",
                        screenX / 2, screenY / 2, paint);
            }

            if(!playing){
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255,0,0,0));
                paint.setTextSize(100);
                canvas.drawColor(Color.argb(120, 255, 255, 255));
                canvas.drawText("Tap left to move left",
                        screenX / 2, screenY / 2, paint);
                canvas.drawText("Tap right to move right",
                        screenX / 2, screenY / 2 + 100, paint);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "failed to pause thread");
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            update();
            draw();
        }
    }
}
