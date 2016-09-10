package com.anirudh.anirudhswami.personalassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.anirudh.anirudhswami.personalassistant.model.BudgetRow;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

public class BudgetReceiver extends BroadcastReceiver {

    Context ctx;
    DaoHelper anidb = null;

    NotificationCompat.Builder notification;
    private static final int UNIQUE_ID = 2461;

    public BudgetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        this.ctx = context;
        List<BudgetRow> users = getAllUsers();
        for (ListIterator<BudgetRow> iter = users.listIterator(); iter.hasNext(); ) {
            BudgetRow user = iter.next();
            String roll = user.getRoll();
            int numDs = user.getNumDays();
            numDs++;
            try {
                final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
                UpdateBuilder<BudgetRow, String> updateBuilder = budgetDao.updateBuilder();
                updateBuilder.where().eq("roll", roll);
                updateBuilder.updateColumnValue("numDays", numDs);
                if (updateBuilder.update() != 0) {
                    //Toast.makeText(this.ctx, "Num Days updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "Num Days updation unsuccessful", Toast.LENGTH_SHORT).show();
                }
                if (anidb != null) {
                    OpenHelperManager.releaseHelper();
                    anidb = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            int month, year, budget, cost;
            int curm, cury;

            Calendar cal = Calendar.getInstance();
            curm = cal.get(Calendar.MONTH);
            cury = cal.get(Calendar.YEAR);

            month = user.getMonth();
            year = user.getYear();

            if (month != curm || year != cury) {
                month = curm;
                year = cury;
                budget = -1;
                cost = 0;
                try {
                    final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
                    UpdateBuilder<BudgetRow, String> updateBuilder = budgetDao.updateBuilder();
                    updateBuilder.where().eq("roll", roll);
                    updateBuilder.updateColumnValue("month", month);
                    updateBuilder.updateColumnValue("year", year);
                    updateBuilder.updateColumnValue("budget", budget);
                    updateBuilder.updateColumnValue("cost", cost);
                    if (updateBuilder.update() != 0) {
                        //Toast.makeText(this.ctx, "Month,year,budget,cost updated successfully", Toast.LENGTH_SHORT).show();
                        FileHelper fi = new FileHelper(context);
                        String fB = roll+"b.txt";
                        String fC = roll+"c.txt";
                        String fD = roll + "d.txt";
                        fi.appendLine(fB,Integer.toString(-1));
                        fi.appendLine(fC, Integer.toString(0));
                        fi.deleteFile(fD);
                        fi.write(fD,Integer.toString(-1));
                        int nd = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        //nd = 30;
                        for(int i=0; i<nd-1; ++i){
                            fi.appendLine(fD,Integer.toString(-1));
                        }
                        //List<Double> li = fi.read(fD);
                        //Toast.makeText(this.ctx,"List of values is "+li.toString(),Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(this.ctx, "Month,year,budget,cost updation unsuccessful", Toast.LENGTH_SHORT).show();

                    }
                    if (anidb != null) {
                        OpenHelperManager.releaseHelper();
                        anidb = null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            SharedPreferences spf = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
            if (spf.getBoolean("loginStat", false)) {
                if (user.getSubscribed() && roll.equals(spf.getString("rollNumb", ""))) {
                    notification = new NotificationCompat.Builder(context);
                    notification.setAutoCancel(true);
                    notification.setSmallIcon(R.drawable.notif2);

                    notification.setTicker("Update cost today");
                    //set the time
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle("COST");
                    notification.setContentText("Update the money you spent today");

                    Intent sendTo = new Intent(context, UpdateCost.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, sendTo, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);

                    //Builds Notification and issues it
                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(UNIQUE_ID, notification.build());

                    Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    vib.vibrate(500);

                }
            }
        }
    }

    private DaoHelper getHelper() {
        if (anidb == null) {
            anidb = OpenHelperManager.getHelper(this.ctx, DaoHelper.class);
        }
        return anidb;
    }

    private List<BudgetRow> getAllUsers() {
        List<BudgetRow> buds = new ArrayList<BudgetRow>();
        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            buds = budgetDao.queryForAll();
            if (anidb != null) {
                OpenHelperManager.releaseHelper();
                anidb = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buds;
    }
}
