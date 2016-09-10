package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CostHistory extends AppCompatActivity {

    List<Double> budgets = new ArrayList<>();
    List<Double> costs = new ArrayList<>();
    List<Double> months = new ArrayList<>();
    List<Double> years = new ArrayList<>();

    String[] budgs,cost,monts,yers;
    private static String roll;

    String fileB,fileC;

    FileHelper fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_history);
        SharedPreferences spf = this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb","");

        fileB = roll+"b.txt";
        fileC = roll+"c.txt";

        fi = new FileHelper(CostHistory.this);

        budgets = fi.read(fileB);
        costs = fi.read(fileC);

    }
}
