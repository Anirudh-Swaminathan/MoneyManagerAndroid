package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anirudh.anirudhswami.personalassistant.model.Movie;
import com.anirudh.anirudhswami.personalassistant.model.MovieRow;
import com.anirudh.anirudhswami.personalassistant.rest.ApiClient;
import com.anirudh.anirudhswami.personalassistant.rest.ApiInterface;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovie extends AppCompatActivity {

    TextView genr, plot, ratin, typ;
    ImageView post;
    EditText titl;
    Button add;
    String roll;

    DaoHelper anidb;
    ApiInterface apiService;

    boolean got;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        SharedPreferences spf = AddMovie.this.getSharedPreferences(AddMovie.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(AddMovie.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddMovie.this, MainActivity.class);
            startActivity(i);
        }

        roll = spf.getString("rollNumb", "");
        genr = (TextView) findViewById(R.id.addGenre);
        plot = (TextView) findViewById(R.id.addPlot);
        post = (ImageView) findViewById(R.id.addImg);
        titl = (EditText) findViewById(R.id.addTxt);
        add = (Button) findViewById(R.id.addBtn);
        ratin = (TextView) findViewById(R.id.addRat);
        typ = (TextView) findViewById(R.id.addType);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = titl.getText().toString();
                getMovie(query);
            }
        });
    }

    private DaoHelper getHelper() {
        if (anidb == null) {
            anidb = OpenHelperManager.getHelper(this, DaoHelper.class);
        }
        return anidb;
    }

    public boolean addMovie(String im, String ti, String ge, String pl, String ra, String ty) {
        try {
            final RuntimeExceptionDao<MovieRow, Integer> movieDao = getHelper().getMovieRunDao();

            if (movieDao.create(new MovieRow(im, ti, ge, pl, ra, ty, roll)) == 1) {
                got = true;
                Toast.makeText(AddMovie.this, "Addition Successful", Toast.LENGTH_SHORT).show();
                Intent ani = new Intent(AddMovie.this, Movies.class);
                startActivity(ani);
            } else {
                got = false;
                Toast.makeText(AddMovie.this, "Addition Unsuccessful", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(AddMovie.this, "Addition successful", Toast.LENGTH_SHORT).show();
            //OpenHelperManager.releaseHelper();
        } catch (Exception e) {
            //Toast.makeText(AddOrmLite.this,"SQLException in ADD.\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return got;
    }

    public void getMovie(String title) {
        got = false;
        Map<String, String> data = new HashMap<>();
        data.put("t", title);
        data.put("r", "json");
        Call<Movie> call = apiService.getMovieTitle(data);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.body().getResponse().equals("True")) {
                    setTitle(response.body().getTitle());
                    plot.setText(response.body().getPlot());
                    //posterText.setText(response.body().getPoster());
                    Picasso.with(AddMovie.this)
                            .load(response.body().getPoster())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.error)
                            .into(post);

                    genr.setText(response.body().getGenre());
                    ratin.setText("IMDBRating: " + response.body().getImdbRating());
                    typ.setText(response.body().getType());
                    if (addMovie(response.body().getPoster(), response.body().getTitle(), response.body().getGenre(),
                            response.body().getPlot(), response.body().getImdbRating(), response.body().getType())) {
                        got = true;
                    }
                } else {
                    //Toast.makeText(AddMovie.this, "Response was false", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddMovie.this, "Error: " + response.body().getError(), Toast.LENGTH_SHORT).show();
                    got = false;
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                //Toast.makeText(AddMovie.this, "Failure is the next step to success", Toast.LENGTH_SHORT).show();
                Toast.makeText(AddMovie.this, "The error was " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (anidb != null) {
            OpenHelperManager.releaseHelper();
            anidb = null;
        }
        finish();
    }
}
