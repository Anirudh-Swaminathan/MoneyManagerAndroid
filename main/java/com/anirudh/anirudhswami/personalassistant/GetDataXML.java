package com.anirudh.anirudhswami.personalassistant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anirudh Swami on 13-06-2016.
 */
public class GetDataXML {
    private String temperature = "DEFAULT";
    private String pressure = "Press";
    private String humidity = "Humid";

    private String url;

    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = false;

    public GetDataXML(String a){
        this.url = a;
    }

    public String[] return_data(){
        String[] a = {temperature,pressure,humidity};
        return  a;
    }
    public void getXML(){
        Runnable r =new Runnable() {
            @Override
            public void run() {
                try{
                    URL url1 = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();

                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();

                    InputStream in = urlConnection.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser aniParser = xmlFactoryObject.newPullParser();

                    aniParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                    aniParser.setInput(in,null);
                    //readStream(in);

                    storeXML(aniParser);

                    in.close();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    public void storeXML(XmlPullParser aniParser){
        int event;
        String output = null;
        try{
            event = aniParser.getEventType();
            while (event!=XmlPullParser.END_DOCUMENT){
                String tag = aniParser.getName();

                if(event == XmlPullParser.START_TAG) {
                    if(tag.equals("temperature")){
                        temperature = aniParser.getAttributeValue(null,"value");
                    }
                    else if(tag.equals("humidity")){
                        humidity = aniParser.getAttributeValue(null,"value");
                    }
                    else if(tag.equals("pressure")){
                        pressure = aniParser.getAttributeValue(null,"value");
                    }
                }
                else if(event == XmlPullParser.TEXT){
                    output = aniParser.getText();
                }
                else if(event == XmlPullParser.END_TAG){

                }
                event = aniParser.next();
            }
            parsingComplete = true;

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
