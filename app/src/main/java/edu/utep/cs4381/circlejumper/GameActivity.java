/*
Oscar Castro
CS 4381
Circle Jumper - GameActivity
 */

package edu.utep.cs4381.circlejumper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {

    private JumperView jumperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point resolution = new Point();
        display.getSize(resolution);
        jumperView = new JumperView(this, resolution.x, resolution.y);
        setContentView(jumperView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jumperView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        jumperView.resume();
    }
}