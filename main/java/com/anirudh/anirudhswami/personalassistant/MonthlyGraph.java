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
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthlyGraph extends AppCompatActivity{

    List<Double> costs = new ArrayList<>();
    List<Double> cost2 = new ArrayList<>();
    double[] cos = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    int cur;

    String roll;

    FileHelper fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_graph);

        SharedPreferences spf = this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb", "");

        Calendar cal = Calendar.getInstance();
        cur = cal.get(Calendar.MONTH);

        fi = new FileHelper(MonthlyGraph.this);

        cost2 = fi.read(roll + "c.txt");

        int ran = (int) Math.round(Math.random()*15);
        for(int i=0; i<ran; ++i){
            fi.appendLine(roll + "c.txt", Long.toString(Math.round(Math.random() * 1600)));
        }

        costs = fi.read(roll + "c.txt");
        int ind = costs.size() - 1;
        for (int i = cur; i >= 0; --i) {
            cos[i] = costs.get(ind);
            ind--;
            if (ind == -1) break;
        }

        DataPoint[] data = new DataPoint[12];
        /*This is the right code*/
        for (int i = 0; i < 12; ++i) {
            double x = i + 1;
            double y = cos[i] / 100.0;
            DataPoint v = new DataPoint(x, y);
            data[i] = v;
        }

        GraphView graph = (GraphView) findViewById(R.id.monthly);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(data);

        series.setSpacing(10);
        graph.addSeries(series);

        series.setDrawValuesOnTop(false);
        series.setValuesOnTopColor(Color.RED);

        graph.setBackgroundColor(Color.WHITE);
        graph.setTitle("Cost vs Month");

        //graph.getGridLabelRenderer().setNumHorizontalLabels(13);

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

        /*
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"});
        //staticLabelsFormatter.setVerticalLabels(new String[]{"500","1000","1500","2000"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        */
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(13);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);

        // legend
        series.setTitle("Year: " + cal.get(Calendar.YEAR) + "X: Month" + System.lineSeparator() + "Y: Cost in 100 Rs");
        //series2.setTitle("bar");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        fi.deleteFile(roll + "c.txt");
        fi.write(roll + "c.txt",Double.toString(cost2.get(0)));
        for(int i=1; i<cost2.size(); ++i){
            fi.appendLine(roll+"c.txt",Double.toString(cost2.get(i)));
        }

    }
}
