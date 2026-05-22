package com.example.lab21.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab21.utils.SensorDataHelper;

import java.util.List;

public class SensorsOverviewFragment extends Fragment {

    private SensorManager sm;
    private LinearLayout mainList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scroll = new ScrollView(requireContext());
        mainList = new LinearLayout(requireContext());
        mainList.setOrientation(LinearLayout.VERTICAL);
        mainList.setPadding(30, 30, 30, 30);
        scroll.addView(mainList);

        sm = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        populateList();

        return scroll;
    }

    private void populateList() {
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s : allSensors) {
            TextView infoText = new TextView(requireContext());
            infoText.setText(SensorDataHelper.stringifySensorInfo(s));
            infoText.setTextSize(15);
            infoText.setPadding(10, 20, 10, 20);
            mainList.addView(infoText);

            View line = new View(requireContext());
            line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            line.setBackgroundColor(0xFFCCCCCC);
            mainList.addView(line);
        }
    }
}
