package com.anirudh.anirudhswami.personalassistant;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.util.Log;
import android.widget.Toast;

import com.anirudh.anirudhswami.personalassistant.model.BudgetRow;
import com.anirudh.anirudhswami.personalassistant.model.MovieRow;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anirudh Swami on 19-07-2016 for the project PersonalAssistant.
 */
public class AniBackUpAgent extends BackupAgentHelper {
    static final String USER_PREFS = "PersonalAssistant";

    static final String PREFS_BACKUP_KEY = "myprefs";
    static final String FILES_BACKUP_KEY = "userfiles";
    static final String DB_BACKUP_KEY = "userdbs";

    static final String TAG = "MyDb";

    DaoHelper anidb = null;
    List<String> rolls = new ArrayList<>();
    List<String> fil = new ArrayList<>();
    String files[], dataFiles[];

    public void onCreate() {
        SharedPreferencesBackupHelper helper =
                new SharedPreferencesBackupHelper(this, USER_PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);

        //FileBackupHelper fileBackupHelper = new FileBackupHelper(this, "102115004b.txt", "102115004c.txt");
        //addHelper(FILES_BACKUP_KEY,fileBackupHelper);

        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            List<BudgetRow> budgets = budgetDao.queryForAll();
            int i = 0;
            //Toast.makeText(ViewAllResults.this,"Number of movies is "+movies.size(),Toast.LENGTH_SHORT).show();
            while (i < budgets.size()) {
                rolls.add(budgets.get(i).getRoll());
                //Toast.makeText(ViewAllResults.this,"Title is "+movies.get(i).getTitle(),Toast.LENGTH_SHORT).show();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        files = rolls.toArray(new String[rolls.size()]);
        for (int i = 0; i < files.length; ++i) {
            fil.add(files[i] + "b.txt");
            fil.add(files[i] + "c.txt");
        }
        dataFiles = fil.toArray(new String[fil.size()]);
        FileBackupHelper helper1 = new FileBackupHelper(this, dataFiles);
        addHelper(FILES_BACKUP_KEY, helper1);

        //FileBackupHelper dbs = new FileBackupHelper(this,"../databases/movies.db","../databases/personal.db");
        FileBackupHelper dbs = new FileBackupHelper(this, this.getDatabasePath("movies").getAbsolutePath(),this.getDatabasePath("personal").getAbsolutePath());
        addHelper(DB_BACKUP_KEY,dbs);
        //Toast.makeText(this.getApplicationContext(),"dbList is "+this.databaseList(),Toast.LENGTH_SHORT).show();
        //Log.d(TAG,"db list is "+this.databaseList());

    }


    private DaoHelper getHelper() {
        if (anidb == null) {
            anidb = OpenHelperManager.getHelper(this, DaoHelper.class);
        }
        return anidb;
    }

}
