package com.example.lab21.utils;

import android.hardware.Sensor;

public class SensorDataHelper {

    public static String stringifySensorInfo(Sensor s) {
        StringBuilder sb = new StringBuilder();
        sb.append("Identifier: ").append(s.getId()).append("\n");
        sb.append("Model: ").append(s.getName()).append("\n");
        sb.append("Manufacturer: ").append(s.getVendor()).append("\n");
        sb.append("Version: ").append(s.getVersion()).append("\n");
        sb.append("Category: ").append(s.getStringType()).append("\n");
        sb.append("Numeric Type: ").append(s.getType()).append("\n");
        sb.append("Resolution: ").append(s.getResolution()).append("\n");
        sb.append("Consumption: ").append(s.getPower()).append(" mA\n");
        sb.append("Range Limit: ").append(s.getMaximumRange()).append("\n");
        sb.append("Min Interval: ").append(s.getMinDelay()).append(" µs\n");
        return sb.toString();
    }
}
