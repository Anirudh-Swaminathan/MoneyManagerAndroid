package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anirudh.anirudhswami.personalassistant.model.Movie;
import com.anirudh.anirudhswami.personalassistant.model.MovieRow;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class Movies extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    DaoHelper anidb = null;

    private static AutoCompleteTextView actv;
    ArrayAdapter adapter;
    EditText reactHere;
    boolean searched = false;
    private Subscription subscription;
    String[] comle;
    List<String> titles = new ArrayList<>();
    String myTitle = "MOVIES";
    String roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        SharedPreferences spf = Movies.this.getSharedPreferences(Movies.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if(!spf.getBoolean("loginStat",false)){
            Toast.makeText(Movies.this,"Please Login to continue",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Movies.this,MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb","");

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setTitle(myTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (java.lang.NullPointerException e){
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initializeAdapter();
        comle = titles.toArray(new String[0]);

        actv = (AutoCompleteTextView) findViewById(R.id.rxAutoTxt);
        actv.setVisibility(View.VISIBLE);
        adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, comle);
        actv.setThreshold(1);
        actv.setAdapter(adapter);

        reactHere = (EditText) findViewById(R.id.rxTxt);
        reactHere.setVisibility(View.INVISIBLE);

        subscription = RxTextView.textChangeEvents(reactHere)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        //return null;
                        //actv.setText(reactHere.getText().toString());
                        if (reactHere.getText().toString().equals("")) return false;
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSearchObserver());
        actv.addTextChangedListener(new TextWatcher() {
            String beffore;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
                beffore = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(beffore.equals(s.toString()))) {
                    reactHere.setText(s.toString());
                    reactHere.setSelection(reactHere.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }
        });
    }

    private DaoHelper getHelper() {
        if (anidb == null) {
            anidb = OpenHelperManager.getHelper(this, DaoHelper.class);
        }
        return anidb;
    }

    public void initializeAdapter() {
        try {
            final RuntimeExceptionDao<MovieRow, Integer> moviesDao = getHelper().getMovieRunDao();
            List<MovieRow> movies = moviesDao.queryForEq("roll", roll);
            int i = 0;
            //Toast.makeText(ViewAllResults.this,"Number of movies is "+movies.size(),Toast.LENGTH_SHORT).show();
            while (i < movies.size()) {
                titles.add(movies.get(i).getTitle());
                //Toast.makeText(ViewAllResults.this,"Title is "+movies.get(i).getTitle(),Toast.LENGTH_SHORT).show();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<MovieRow> getSingleMovie(String ti) {
        List<MovieRow> movie = new ArrayList<MovieRow>();
        try {
            final RuntimeExceptionDao<MovieRow, Integer> moviesDao = getHelper().getMovieRunDao();
            QueryBuilder<MovieRow,Integer> qb = moviesDao.queryBuilder();
            qb.where().eq("title",ti).and().eq("roll",roll);
            movie = qb.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;
    }

    private Observer<TextViewTextChangeEvent> getSearchObserver() {
        return new Observer<TextViewTextChangeEvent>() {

            @Override
            public void onCompleted() {
                Toast.makeText(Movies.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(Movies.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                if (!(actv.getText().toString().equals(textViewTextChangeEvent.text().toString()))) {
                    actv.setText(textViewTextChangeEvent.text().toString());
                    actv.showDropDown();
                }
                //Toast.makeText(ViewAllResults.this, "Searching for " + textViewTextChangeEvent.text().toString(), Toast.LENGTH_SHORT).show();
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.Search:
                if (!searched) {
                    searched = true;
                    reactHere.setVisibility(View.VISIBLE);
                } else {
                    if (reactHere.getText().toString().equals("")) {
                        searched = false;
                        Toast.makeText(Movies.this, "No Prompt entered to search", Toast.LENGTH_SHORT).show();
                        reactHere.setVisibility(View.INVISIBLE);
                    } else {
                        //Search
                        searched = false;
                        String query = reactHere.getText().toString();
                        Toast.makeText(Movies.this, "Searching for " + query, Toast.LENGTH_SHORT).show();
                        List<MovieRow> movi = getSingleMovie(query);
                        if (movi.size() == 0) {
                            Toast.makeText(Movies.this, "Movie " + query + " does not exist in the database", Toast.LENGTH_SHORT).show();
                        } else {
                            String tit = movi.get(0).getTitle();
                            String pl = movi.get(0).getPlot();
                            String po = movi.get(0).getImage();
                            String ge = movi.get(0).getGenre();
                            String ty = movi.get(0).getType();
                            String ra = movi.get(0).getRating();

                            Intent i = new Intent(Movies.this, SingleMovie.class);
                            i.putExtra("title", tit);
                            i.putExtra("plot", pl);
                            i.putExtra("poster", po);
                            i.putExtra("ge", ge);
                            i.putExtra("rating", ra);
                            i.putExtra("type", ty);

                            startActivity(i);
                        }
                    }
                }
                break;
            case R.id.AddMovMenu:
                Intent addMov = new Intent(Movies.this, AddMovie.class);
                startActivity(addMov);
                break;
            case R.id.Sort:
                break;
            default:
                //Toast.makeText(Movies.this, "You made the wrong choice", Toast.LENGTH_SHORT).show();
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    setTitle("MOVIES");
                    myTitle = "MOVIES";
                    getSupportActionBar().setTitle(myTitle);
                    return Moviesfrag.newInstance(Movies.this);
                case 1:
                    setTitle("SERIES");
                    myTitle = "SERIES";
                    getSupportActionBar().setTitle(myTitle);
                    return Seriesfrag.newInstance(Movies.this);
                default:
                    Toast.makeText(Movies.this, "No Such Fragment", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MOVIES";
                case 1:
                    return "SERIES";
            }
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (anidb != null) {
            OpenHelperManager.releaseHelper();
            anidb = null;
        }
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
