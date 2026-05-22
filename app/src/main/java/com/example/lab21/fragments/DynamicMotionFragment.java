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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab21.views.CustomGraphView;

public class DynamicMotionFragment extends Fragment implements SensorEventListener {

    private static final String PARAM_TYPE = "sensor_id";
    private static final String PARAM_LABEL = "display_title";

    private SensorManager sensorOps;
    private Sensor activeSensor;

    private TextView detailsText;
    private CustomGraphView motionGraph;

    private int sType;
    private String sLabel;

    public static DynamicMotionFragment build(int type, String label) {
        DynamicMotionFragment f = new DynamicMotionFragment();
        Bundle b = new Bundle();
        b.putInt(PARAM_TYPE, type);
        b.putString(PARAM_LABEL, label);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sType = requireArguments().getInt(PARAM_TYPE);
        sLabel = requireArguments().getString(PARAM_LABEL);

        sensorOps = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        activeSensor = sensorOps.getDefaultSensor(sType);

        LinearLayout box = new LinearLayout(requireContext());
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(35, 35, 35, 35);

        TextView head = new TextView(requireContext());
        head.setText(sLabel);
        head.setTextSize(22);

        detailsText = new TextView(requireContext());
        detailsText.setTextSize(17);
        detailsText.setPadding(0, 30, 0, 30);

        motionGraph = new CustomGraphView(requireContext());
        motionGraph.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 650));

        box.addView(head);
        box.addView(detailsText);
        box.addView(motionGraph);

        return box;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activeSensor != null) {
            sensorOps.registerListener(this, activeSensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            detailsText.setText("Hardware component missing.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorOps.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xVal = event.values[0];
        float yVal = event.values[1];
        float zVal = event.values[2];

        float totalMagnitude = (float) Math.sqrt(xVal * xVal + yVal * yVal + zVal * zVal);

        String readout = "X-Axis: " + xVal + "\n"
                       + "Y-Axis: " + yVal + "\n"
                       + "Z-Axis: " + zVal + "\n"
                       + "Magnitude: " + totalMagnitude;
        
        detailsText.setText(readout);
        motionGraph.appendData(totalMagnitude);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
