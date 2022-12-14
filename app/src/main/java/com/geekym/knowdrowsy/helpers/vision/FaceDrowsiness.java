package com.geekym.knowdrowsy.helpers.vision;

import android.content.Context;
import android.media.MediaPlayer;


import com.geekym.knowdrowsy.R;

import com.google.mlkit.vision.face.Face;

import java.util.ArrayDeque;


public class FaceDrowsiness {
    private static final float DROWSINESS_THRESHOLD = 0.9f;
    private static final int MAX_HISTORY = 10;

    public long lastCheckedAt;
    private final ArrayDeque<Boolean> history = new ArrayDeque<>();
    private Context context;

    public FaceDrowsiness(Context context) {
        this.context = context;
    }

    public boolean isDrowsy(Face face) {
        boolean isDrowsy = true;
        MediaPlayer media = MediaPlayer.create(context,R.raw.siren);
        lastCheckedAt = System.currentTimeMillis();
        if (face.getLeftEyeOpenProbability() == null
            || face.getRightEyeOpenProbability() == null) {
            return false;
        }
        if (face.getLeftEyeOpenProbability() < DROWSINESS_THRESHOLD
            && face.getRightEyeOpenProbability() < DROWSINESS_THRESHOLD) {
            history.addLast(true);
        } else {
            history.addLast(false);
        }
        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
        if (history.size() == MAX_HISTORY) {
            for (boolean instance : history) {
                isDrowsy &= instance;
            }
        } else {
            return false;
        }
        if (isDrowsy) media.start();
        return isDrowsy;
    }
}
