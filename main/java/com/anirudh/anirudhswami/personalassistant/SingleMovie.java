package com.anirudh.anirudhswami.personalassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SingleMovie extends AppCompatActivity {

    TextView gen, plo, typ, rat;
    ImageView pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        gen = (TextView) findViewById(R.id.singleGenre);
        plo = (TextView) findViewById(R.id.singlePlot);
        pos = (ImageView) findViewById(R.id.singleImg);
        typ = (TextView) findViewById(R.id.singleType);
        rat = (TextView) findViewById(R.id.singleRating);

        Intent a = getIntent();
        if (a == null) {
            setTitle("TITLE");
            pos.setImageResource(R.mipmap.ic_launcher);
        } else {
            gen.setText(a.getStringExtra("ge"));
            plo.setText(a.getStringExtra("plot"));
            setTitle(a.getStringExtra("title"));
            Picasso.with(SingleMovie.this)
                    .load(a.getStringExtra("poster"))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.error)
                    .into(pos);
            typ.setText(a.getStringExtra("type"));
            rat.setText(a.getStringExtra("rating"));
            //Toast.makeText(SingleMovie.this,"Poster is \n"+a.getStringExtra("poster"),Toast.LENGTH_SHORT).show();
        }
    }
}
