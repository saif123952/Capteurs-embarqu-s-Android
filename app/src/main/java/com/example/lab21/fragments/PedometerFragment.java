package com.example.lab21.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PedometerFragment extends Fragment implements SensorEventListener {

    private SensorManager sm;
    private Sensor stepSensor;
    private TextView infoLabel;

    private float startCount = -1;

    private final ActivityResultLauncher<String> requestPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    initStepCounter();
                } else {
                    infoLabel.setText("Recognition permission needed.");
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        infoLabel = new TextView(requireContext());
        infoLabel.setTextSize(21);
        infoLabel.setPadding(30, 30, 30, 30);

        sm = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        return infoLabel;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stepSensor == null) {
            infoLabel.setText("Pedometer hardware not found.");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        } else {
            initStepCounter();
        }
    }

    private void initStepCounter() {
        sm.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float rawSteps = event.values[0];
        if (startCount < 0) startCount = rawSteps;

        int currentSession = (int) (rawSteps - startCount);
        infoLabel.setText("Total Boot Steps: " + (int) rawSteps + "\n\nCurrent Session: " + currentSession);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
