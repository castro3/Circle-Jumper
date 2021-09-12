/*
Oscar Castro
CS 4381
Circle Jumper - SoundManager
 */

package edu.utep.cs4381.circlejumper;

import android.content.Context;
import android.media.SoundPool;

public class SoundManager {

    private static SoundManager theInstance;
    private final SoundPool soundPool;

    public enum Sound {
        BUMP(R.raw.bump),
        SCORE(R.raw.point);

        public final int resourceId;
        private int soundId;

        Sound(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    private SoundManager(Context ctx) {
        soundPool = new SoundPool.Builder()
                .setMaxStreams(Sound.values().length).build();
        for (Sound sound: Sound.values()) {
            sound.soundId = soundPool.load(ctx, sound.resourceId, 1);
        }
    }

    public static SoundManager instance(Context context) {
        if (theInstance == null) {
            theInstance = new SoundManager(context);
        }
        return theInstance;
    }

    public void play(Sound sound) {
        soundPool.play(sound.soundId, 1, 1, 0, 0, 1);
    }
}
