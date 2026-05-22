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

public class DirectionFinderFragment extends Fragment implements SensorEventListener {

    private SensorManager sm;
    private Sensor accel;
    private Sensor magnet;

    private TextView statusDisplay;

    private final float[] gravityBuffer = new float[3];
    private final float[] magneticBuffer = new float[3];

    private boolean accelReady = false;
    private boolean magnetReady = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        statusDisplay = new TextView(requireContext());
        statusDisplay.setTextSize(23);
        statusDisplay.setPadding(35, 35, 35, 35);

        sm = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return statusDisplay;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accel != null) sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        if (magnet != null) sm.registerListener(this, magnet, SensorManager.SENSOR_DELAY_UI);

        if (accel == null || magnet == null) {
            statusDisplay.setText("Orientation sensors unavailable.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, gravityBuffer, 0, 3);
            accelReady = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magneticBuffer, 0, 3);
            magnetReady = true;
        }

        if (accelReady && magnetReady) {
            float[] rMat = new float[9];
            float[] orient = new float[3];

            if (SensorManager.getRotationMatrix(rMat, null, gravityBuffer, magneticBuffer)) {
                SensorManager.getOrientation(rMat, orient);

                float deg = (float) Math.toDegrees(orient[0]);
                if (deg < 0) deg += 360;

                String cardinal = resolveCardinal(deg);
                statusDisplay.setText("Heading: " + String.format("%.1f", deg) + "°\n\nDirection: " + cardinal);
            }
        }
    }

    private String resolveCardinal(float angle) {
        if (angle >= 337.5 || angle < 22.5) return "North";
        if (angle < 67.5) return "North-East";
        if (angle < 112.5) return "East";
        if (angle < 157.5) return "South-East";
        if (angle < 202.5) return "South";
        if (angle < 247.5) return "South-West";
        if (angle < 292.5) return "West";
        return "North-West";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
