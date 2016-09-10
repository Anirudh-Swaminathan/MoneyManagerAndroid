package com.anirudh.anirudhswami.personalassistant;

import android.os.AsyncTask;
import android.widget.TextView;

/**
 * Created by Anirudh Swami on 13-06-2016.
 */
public class LoadWeatherData extends AsyncTask<String,Void,String[]> {

    public LoadWeatherData(TextView a, TextView b, TextView c){
        this.temp=a;
        this.pres = b;
        this.hum = c;
    }

    private GetDataXML gdata;
    String[] data;
    String p = null;
    TextView temp,pres,hum;

    @Override
    protected String[] doInBackground(String... params) {
        //return new String[0];
        p= params[0];
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+p+"&mode=xml&APPID=0d307ddfc720ee6d1235886a6bad152a";


        gdata = new GetDataXML(url);

        gdata.getXML();
        while (!gdata.parsingComplete);
        data = gdata.return_data();
        return data;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        //super.onPostExecute(strings);
        double t = Double.parseDouble(strings[0]);
        t -= 273.15;
        String apk = String.format("%.5f",t);
        String msg = apk+" \u00B0C";

        temp.setText(msg);
        pres.setText(strings[1]+" hPa");
        hum.setText(strings[2]+" %");
    }
}
