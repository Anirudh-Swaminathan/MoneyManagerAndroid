package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anirudh.anirudhswami.personalassistant.model.BudgetRow;
import com.anirudh.anirudhswami.personalassistant.model.MovieRow;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by Anirudh Swami on 07-07-2016.
 */
public class DaoHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<MovieRow, Integer> movieDao = null;
    private RuntimeExceptionDao<MovieRow, Integer> movieRunDao = null;

    private Dao<BudgetRow, String> budgetDao = null;
    private RuntimeExceptionDao<BudgetRow, String> budgetRunDao = null;

    public DaoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MovieRow.class);
            TableUtils.createTable(connectionSource, BudgetRow.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, MovieRow.class, true);
            TableUtils.dropTable(connectionSource, BudgetRow.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<MovieRow, Integer> getMovieDao() throws SQLException {
        if (movieDao == null) {
            movieDao = getDao(MovieRow.class);
        }
        return movieDao;
    }

    public RuntimeExceptionDao<MovieRow, Integer> getMovieRunDao() throws SQLException {
        if (movieRunDao == null) {
            movieRunDao = getRuntimeExceptionDao(MovieRow.class);
        }
        return movieRunDao;
    }

    public Dao<BudgetRow, String> getBudgetDao() throws SQLException {
        if (budgetDao == null) {
            budgetDao = getDao(BudgetRow.class);
        }
        return budgetDao;
    }

    public RuntimeExceptionDao<BudgetRow, String> getBudgetRunDao() throws SQLException {
        if (budgetRunDao == null) {
            budgetRunDao = getRuntimeExceptionDao(BudgetRow.class);
        }
        return budgetRunDao;
    }
}
