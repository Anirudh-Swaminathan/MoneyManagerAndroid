package com.anirudh.anirudhswami.personalassistant;

import android.content.Intent;
//import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    //MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //mp = MediaPlayer.create(Splash.this,R.raw.splash_sound);
        //mp.start();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3500);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally{
                    Intent i = new Intent(Splash.this,MainActivity.class);
                    startActivity(i);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mp.stop();
        //mp.release();
        finish();
    }
}
