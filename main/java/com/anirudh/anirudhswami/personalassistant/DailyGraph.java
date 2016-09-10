package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyGraph extends AppCompatActivity {

    List<Double> costs = new ArrayList<>();
    int cur;

    String roll;

    FileHelper fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_graph);

        SharedPreferences spf = this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb", "");

        Calendar cal = Calendar.getInstance();
        cur = cal.get(Calendar.MONTH);

        fi = new FileHelper(DailyGraph.this);
        costs = fi.read(roll + "d.txt");
        double[] cos = new double[costs.size()];
        for(int i=0; i<costs.size(); ++i){
            cos[i] = costs.get(i);
        }
        //Toast.makeText(DailyGraph.this,"Contents of cost is "+costs.toString(),Toast.LENGTH_SHORT).show();
        DataPoint[] dataPoint = new DataPoint[costs.size()];
        for (int i = 0; i < cos.length; ++i) {
            double x = i + 1;
            double y = cos[i] / 100.0;
            DataPoint v = new DataPoint(x, y);
            dataPoint[i] = v;
        }


        GraphView graph = (GraphView) findViewById(R.id.daily);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoint);

        series.setSpacing(10);
        graph.addSeries(series);

        series.setDrawValuesOnTop(false);
        series.setValuesOnTopColor(Color.RED);

        graph.setBackgroundColor(Color.WHITE);
        graph.setTitle("Cost vs Date");


        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                int col;
                if (data.getY() > 10) {
                    col = Color.rgb(255, 0, 0);
                } else {
                    col = Color.rgb(0, 255, 0);
                }
                return col;
                //return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(32);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);

        // legend
        series.setTitle("Month: " + cur + "X: Date" + System.lineSeparator() + "Y: Cost in 100 Rs");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    }
}
