package com.example.lab21.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab21.views.CustomGraphView;

public class SensorMonitorFragment extends Fragment implements SensorEventListener {

    private static final String KEY_TYPE = "type_id";
    private static final String KEY_LABEL = "label";
    private static final String KEY_STRATEGY = "strategy";

    private SensorManager manager;
    private Sensor hardwareSensor;

    private TextView displayValue;
    private CustomGraphView graphComponent;

    private int sType;
    private String sLabel;
    private String sStrategy;

    private final Handler mockHandler = new Handler(Looper.getMainLooper());
    private float mockStep = 0f;

    public static SensorMonitorFragment create(int type, String label, String strategy) {
        SensorMonitorFragment frag = new SensorMonitorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putString(KEY_LABEL, label);
        args.putString(KEY_STRATEGY, strategy);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sType = requireArguments().getInt(KEY_TYPE);
        sLabel = requireArguments().getString(KEY_LABEL);
        sStrategy = requireArguments().getString(KEY_STRATEGY);

        manager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        hardwareSensor = manager.getDefaultSensor(sType);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);

        TextView header = new TextView(requireContext());
        header.setText(sLabel);
        header.setTextSize(24);
        header.setPadding(0, 0, 0, 30);

        displayValue = new TextView(requireContext());
        displayValue.setTextSize(20);
        displayValue.setPadding(0, 0, 0, 30);

        graphComponent = new CustomGraphView(requireContext());
        graphComponent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 700));

        root.addView(header);
        root.addView(displayValue);
        root.addView(graphComponent);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hardwareSensor != null) {
            manager.registerListener(this, hardwareSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            displayValue.setText("Device lacks this sensor. Starting simulation...");
            triggerSimulation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.unregisterListener(this);
        mockHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float res = processInput(event.values);
        refreshDisplay(res);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private float processInput(float[] vals) {
        if ("VECTOR_LENGTH".equals(sStrategy)) {
            return (float) Math.sqrt(vals[0] * vals[0] + vals[1] * vals[1] + vals[2] * vals[2]);
        }
        return vals[0];
    }

    private void refreshDisplay(float value) {
        displayValue.setText("Current: " + String.format("%.2f", value));
        graphComponent.appendData(value);
    }

    private void triggerSimulation() {
        mockHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mockStep += 0.2f;
                float fakeVal;
                if (sType == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    fakeVal = 22f + (float) Math.cos(mockStep / 4) * 5;
                } else if (sType == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    fakeVal = 50f + (float) Math.sin(mockStep / 6) * 10;
                } else {
                    fakeVal = (float) (Math.random() * 10);
                }
                refreshDisplay(fakeVal);
                mockHandler.postDelayed(this, 1200);
            }
        }, 1200);
    }
}
