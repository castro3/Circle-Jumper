/*
Oscar Castro
CS 4381
Circle Jumper - MainActivity
 */

package edu.utep.cs4381.circlejumper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("highScore", MODE_PRIVATE);

        // Reference to button on layout
        final Button buttonPlay = findViewById(R.id.playButton);

        // Get reference to the textView layout
        final TextView textHighScore = findViewById(R.id.highScore);

        buttonPlay.setOnClickListener(this);

        //  load high score if not available we use 0
        long highScore = prefs.getLong("highScore", 0);

        // put high score on the text
        textHighScore.setText("High Score: " + highScore);
    }

    // Start the activity when the button play is pressed
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();

    }
}
