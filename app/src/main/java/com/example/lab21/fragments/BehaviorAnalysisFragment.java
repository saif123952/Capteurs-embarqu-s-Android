package com.example.lab21.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.Queue;

public class BehaviorAnalysisFragment extends Fragment implements SensorEventListener {

    private SensorManager sm;
    private Sensor accel;

    private TextView logView;

    private final float[] gravityEst = new float[3];
    private final Queue<Float> dataWindow = new LinkedList<>();

    private static final int LIMIT = 40;
    private static final float SMOOTHING = 0.75f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logView = new TextView(requireContext());
        logView.setTextSize(20);
        logView.setPadding(40, 40, 40, 40);

        sm = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return logView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accel != null) {
            sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
        } else {
            logView.setText("No accelerometer detected.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        gravityEst[0] = SMOOTHING * gravityEst[0] + (1 - SMOOTHING) * x;
        gravityEst[1] = SMOOTHING * gravityEst[1] + (1 - SMOOTHING) * y;
        gravityEst[2] = SMOOTHING * gravityEst[2] + (1 - SMOOTHING) * z;

        float lx = x - gravityEst[0];
        float ly = y - gravityEst[1];
        float lz = z - gravityEst[2];

        float force = (float) Math.sqrt(lx * lx + ly * ly + lz * lz);

        pushValue(force);

        String state = analyzeMotion(x, y, z);

        String out = "X: " + String.format("%.2f", x) + "\n"
                   + "Y: " + String.format("%.2f", y) + "\n"
                   + "Z: " + String.format("%.2f", z) + "\n\n"
                   + "Linear Force: " + String.format("%.2f", force) + "\n\n"
                   + "Detected State: " + state;

        logView.setText(out);
    }

    private void pushValue(float val) {
        if (dataWindow.size() >= LIMIT) dataWindow.poll();
        dataWindow.add(val);
    }

    private String analyzeMotion(float x, float y, float z) {
        if (dataWindow.size() < LIMIT) return "Calibrating...";

        float sum = 0f;
        float peak = 0f;
        for (float v : dataWindow) {
            sum += v;
            peak = Math.max(peak, v);
        }
        float avg = sum / dataWindow.size();

        float sqDiffSum = 0f;
        for (float v : dataWindow) sqDiffSum += (v - avg) * (v - avg);
        float dev = (float) Math.sqrt(sqDiffSum / dataWindow.size());

        if (peak > 12f) return "Jumping / Impact";
        if (dev > 1.5f) return "Walking / Moving";
        if (Math.abs(z) > 8.5f) return "Flat / Stationary";
        if (Math.abs(y) > 7.5f || Math.abs(x) > 7.5f) return "Upright / Tilted";

        return "Idle";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
