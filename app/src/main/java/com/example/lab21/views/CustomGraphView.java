package com.example.lab21.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CustomGraphView extends View {

    private final List<Float> dataPoints = new ArrayList<>();
    private final int limit = 100;

    private final Paint gridPaint = new Paint();
    private final Paint curvePaint = new Paint();
    private final Paint infoPaint = new Paint();

    public CustomGraphView(Context context) {
        super(context);

        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(2);

        curvePaint.setColor(Color.rgb(255, 87, 34)); // Deep Orange
        curvePaint.setStrokeWidth(6);
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setAntiAlias(true);

        infoPaint.setColor(Color.BLACK);
        infoPaint.setTextSize(35);
    }

    public void appendData(float val) {
        if (dataPoints.size() >= limit) {
            dataPoints.remove(0);
        }
        dataPoints.add(val);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        float margin = 50f;

        canvas.drawLine(margin, h - margin, w - margin, h - margin, gridPaint);
        canvas.drawLine(margin, margin, margin, h - margin, gridPaint);

        if (dataPoints.size() < 2) {
            canvas.drawText("Waiting for sensor input...", margin + 20, h / 2, infoPaint);
            return;
        }

        float low = Float.MAX_VALUE;
        float high = -Float.MAX_VALUE;

        for (float p : dataPoints) {
            low = Math.min(low, p);
            high = Math.max(high, p);
        }

        if (high == low) high = low + 1;

        Path track = new Path();

        for (int i = 0; i < dataPoints.size(); i++) {
            float posX = margin + i * ((w - 2 * margin) / (limit - 1));
            float normalized = (dataPoints.get(i) - low) / (high - low);
            float posY = h - margin - normalized * (h - 2 * margin);

            if (i == 0) {
                track.moveTo(posX, posY);
            } else {
                track.lineTo(posX, posY);
            }
        }

        canvas.drawPath(track, curvePaint);
        canvas.drawText("Range: [" + low + " - " + high + "]", margin + 20, margin + 20, infoPaint);
    }
}
