package com.anirudh.anirudhswami.personalassistant;

import com.anirudh.anirudhswami.personalassistant.model.BudgetRow;
import com.anirudh.anirudhswami.personalassistant.model.MovieRow;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Anirudh Swami on 07-07-2016.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{MovieRow.class, BudgetRow.class};

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt", classes);
    }
}
