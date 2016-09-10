package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherUpdates extends AppCompatActivity {

    TextView temp,pres,hum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_updates);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        android.support.v7.app.ActionBar actionBar = getActionBar();
        try{
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        */
        SharedPreferences spf = WeatherUpdates.this.getSharedPreferences(WeatherUpdates.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(WeatherUpdates.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(WeatherUpdates.this, MainActivity.class);
            startActivity(i);
        }

        temp = (TextView) findViewById(R.id.temp);
        pres = (TextView) findViewById(R.id.pres);
        hum = (TextView) findViewById(R.id.hum);

        ((Button) findViewById(R.id.getData)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String place = ((EditText) findViewById(R.id.place)).getText().toString();
                if (place.equals("")) place = "chennai";

                //Checking for wifi connectivity
                ConnectivityManager connMana = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                //boolean is3g = connMana.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
                //boolean isWifi = connMana.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                boolean is3g = false;
                boolean isWifi = false;
                NetworkInfo netac = connMana.getActiveNetworkInfo();
                if (netac != null) {
                    if (netac.getType() == ConnectivityManager.TYPE_MOBILE) is3g = true;
                    if (netac.getType() == ConnectivityManager.TYPE_WIFI) isWifi = true;

                    if (is3g) {
                        Toast.makeText(WeatherUpdates.this, "Fetching data for " + place + " using " + netac.getTypeName(), Toast.LENGTH_SHORT).show();
                        //loadSomeStuff loadSome = new loadSomeStuff();
                        new LoadWeatherData(temp, pres, hum).execute(place);
                        //Toast.makeText(WeatherUpdates.this, "Executed!!", Toast.LENGTH_SHORT).show();
                    } else if (isWifi) {

                        WifiManager wii = (WifiManager) WeatherUpdates.this.getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wii.getConnectionInfo();

                        String wifiName = null;

                        if (wifiInfo != null) {
                            NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                            if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                                wifiName = wifiInfo.getSSID();
                            }
                        }

                        Toast.makeText(WeatherUpdates.this, "Fetching data for " + place + " using " + netac.getTypeName() + wifiName, Toast.LENGTH_SHORT).show();
                        new LoadWeatherData(temp, pres, hum).execute(place);
                        //Toast.makeText(WeatherUpdates.this, "Executed!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WeatherUpdates.this, "Not Connected to any Network!!!", Toast.LENGTH_SHORT).show();
                    WifiManager wii = (WifiManager) WeatherUpdates.this.getSystemService(Context.WIFI_SERVICE);
                    //WifiInfo wifiInfo = wii.getConnectionInfo();
                    /*
                        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                        if (!(state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR)) {
                            wii.setWifiEnabled(true);
                        }
                    */
                    boolean wifiEnabled = wii.isWifiEnabled();
                    if (!wifiEnabled) {
                        wii.setWifiEnabled(true);
                        Toast.makeText(WeatherUpdates.this, "Wifi has been enabled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


}
