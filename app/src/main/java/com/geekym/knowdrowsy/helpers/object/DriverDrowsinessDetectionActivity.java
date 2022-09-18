package com.geekym.knowdrowsy.helpers.object;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.geekym.knowdrowsy.helpers.MLVideoHelperActivity;
import com.geekym.knowdrowsy.helpers.vision.FaceDetectorProcessor;

public class DriverDrowsinessDetectionActivity extends MLVideoHelperActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setProcessor() {
        cameraSource.setMachineLearningFrameProcessor(new FaceDetectorProcessor(this));
    }

}
