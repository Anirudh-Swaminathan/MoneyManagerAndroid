package com.anirudh.anirudhswami.personalassistant;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anirudh.anirudhswami.personalassistant.model.BudgetRow;
import com.anirudh.anirudhswami.personalassistant.model.MovieRow;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BudgetMain extends AppCompatActivity {

    String roll, fNameb, fNamec, fNamed;
    int numReg;
    DaoHelper anidb = null;
    FileHelper fi;

    TextView monText, yearText, budText, cosText, predTxt;
    Button sub, unsub, predict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        monText = (TextView) findViewById(R.id.budmon);
        yearText = (TextView) findViewById(R.id.budyear);
        budText = (TextView) findViewById(R.id.budbud);
        cosText = (TextView) findViewById(R.id.budcost);
        predTxt = (TextView) findViewById(R.id.predOut);
        //totText = (TextView) findViewById(R.id.budtot);
        sub = (Button) findViewById(R.id.budsub);
        unsub = (Button) findViewById(R.id.budunsub);
        predict = (Button) findViewById(R.id.predBtn);
        fi = new FileHelper(BudgetMain.this);

        SharedPreferences spf = BudgetMain.this.getSharedPreferences(BudgetMain.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(BudgetMain.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(BudgetMain.this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb", "");
        numReg = spf.getInt("budreg", 0);
        setTitle(roll);

        fNameb = roll + "b.txt";
        fNamec = roll + "c.txt";
        fNamed = roll + "d.txt";

        if (!registered()) {
            StringBuffer buff = new StringBuffer();
            buff.append("You need to register yourself to use the budget service.\n" +
                    "Do you wish to register for the free budget manager service?");
            AlertDialog.Builder abu = new AlertDialog.Builder(BudgetMain.this);
            abu.setMessage(buff.toString()).setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(BudgetMain.this,"Need to write registration code",Toast.LENGTH_SHORT).show();
                            registerNew();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(BudgetMain.this, "Your loss", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert = abu.create();
            alert.setTitle("REGISTER");
            alert.show();
        }
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BudgetMain.this, "Gonna subscribe for the daily notifications", Toast.LENGTH_SHORT).show();
                subscribeBu();
                sub.setVisibility(View.INVISIBLE);
                unsub.setVisibility(View.VISIBLE);
            }
        });
        unsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BudgetMain.this, "Gonna unsubscribe from daily notifs", Toast.LENGTH_SHORT).show();
                unsubscribeBu();
                unsub.setVisibility(View.INVISIBLE);
                sub.setVisibility(View.VISIBLE);
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO write some predict codes
                //For the checking of the app
                /*
                fi.write(fNameb, Integer.toString(0));
                fi.write(fNamec, Integer.toString(3));
                fi.appendLine(fNameb, Integer.toString(1));
                fi.appendLine(fNamec,Integer.toString(5));
                fi.appendLine(fNameb,budText.getText().toString());
                fi.appendLine(fNamec,cosText.getText().toString());
                */
                //end of the checking
                if (Integer.parseInt(budText.getText().toString()) != -1) {
                    List<Double> check = fi.read(fNameb);
                    if (check.size() == 0 || check.size() == 1) {
                        Toast.makeText(BudgetMain.this, "Not enough data to predict cost.\nNeed atleast 1 month data", Toast.LENGTH_SHORT).show();
                    } else {
                        MachineBudget mi = new MachineBudget(BudgetMain.this);
                        double ret = mi.predictCost(fNameb, fNamec, Double.parseDouble(budText.getText().toString()));
                        predTxt.setText("Using linear regression with gradient descent, the predicted cost for budget " + budText.getText().toString() +
                                " is " + String.format("%.3f", ret));
                    }
                } else {
                    Toast.makeText(BudgetMain.this, "Budget not set yet for this month", Toast.LENGTH_SHORT).show();
                }
                /*
                Checking of the app
                fi.deleteFile(fNameb);
                fi.deleteFile(fNamec);
                fi.write(fNameb, budText.getText().toString());
                fi.write(fNamec, cosText.getText().toString());
                */
            }
        });

        ((Button) findViewById(R.id.monthGra)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mon = new Intent(BudgetMain.this, MonthlyGraph.class);
                startActivity(mon);
            }
        });

        ((Button) findViewById(R.id.dailGra)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dai = new Intent(BudgetMain.this, DailyGraph.class);
                startActivity(dai);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.budup:
                //Toast.makeText(BudgetMain.this, "Update budget selected", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(BudgetMain.this, UpdateBudget.class);
                startActivity(in);
                break;
            case R.id.budun:
                //Toast.makeText(BudgetMain.this, "Unregister selected", Toast.LENGTH_SHORT).show();

                StringBuffer buff = new StringBuffer();
                buff.append("Are you sure you want to unregister?");
                AlertDialog.Builder abu = new AlertDialog.Builder(BudgetMain.this);
                abu.setMessage(buff.toString()).setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                unregister();
                                dialog.cancel();
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = abu.create();
                alert.setTitle("UNREGISTER");
                alert.show();
                break;
            case R.id.costup:
                //Toast.makeText(BudgetMain.this, "Update cost selected", Toast.LENGTH_SHORT).show();
                Intent cos = new Intent(BudgetMain.this, UpdateCost.class);
                startActivity(cos);
                break;
            case R.id.costhis:
                break;
            default:
                //Toast.makeText(BudgetMain.this, "You made the wrong choice", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void subscribeBu() {
        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            UpdateBuilder<BudgetRow, String> updateBuilder = budgetDao.updateBuilder();
            updateBuilder.where().eq("roll", roll);
            updateBuilder.updateColumnValue("subscribed", true);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unsubscribeBu() {
        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            UpdateBuilder<BudgetRow, String> updateBuilder = budgetDao.updateBuilder();
            updateBuilder.where().eq("roll", roll);
            updateBuilder.updateColumnValue("subscribed", false);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean registered() {
        List<BudgetRow> bud = new ArrayList<BudgetRow>();

        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            bud = budgetDao.queryForEq("roll", roll);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bud.size() == 0) {
            Toast.makeText(BudgetMain.this, "You haven't signed up for the budget manager yet", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            BudgetRow bu = bud.get(0);
            monText.setText(Integer.toString(bu.getMonth()));
            yearText.setText(Integer.toString(bu.getYear()));
            budText.setText(Integer.toString(bu.getBudget()));
            cosText.setText(Integer.toString(bu.getCost()));
            //totText.setText(bu.);
            if (bu.getSubscribed()) {
                sub.setVisibility(View.INVISIBLE);
            } else {
                unsub.setVisibility(View.INVISIBLE);
            }
        }
        return true;
    }

    public void registerNew() {
        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            Calendar cal = Calendar.getInstance();
            if (budgetDao.create(new BudgetRow(-1, 0, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), roll, false, cal.get(Calendar.YEAR))) == 1) {
                Toast.makeText(BudgetMain.this, "Registration successful", Toast.LENGTH_SHORT).show();
                if (numReg == 0) {
                    //Toast.makeText(BudgetMain.this, "Write some broadcast code", Toast.LENGTH_SHORT).show();

                    cal.set(Calendar.HOUR_OF_DAY, 21);
                    cal.set(Calendar.MINUTE, 30);//AM
                    cal.set(Calendar.SECOND, 0);

                    Intent budSet = new Intent(BudgetMain.this, BudgetReceiver.class);
                    PendingIntent pintent = PendingIntent.getBroadcast(BudgetMain.this, 3011, budSet, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
                    //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pintent);
                    //Toast.makeText(BudgetMain.this, "Broadcast set", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(BudgetMain.this, "Broadcast already available", Toast.LENGTH_SHORT).show();
                }
                numReg++;

                SharedPreferences spf = BudgetMain.this.getSharedPreferences(BudgetMain.this.getString(R.string.app_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                edit.putInt("budreg", numReg);
                edit.apply();

                monText.setText(Integer.toString(cal.get(Calendar.MONTH)));
                yearText.setText(Integer.toString(cal.get(Calendar.YEAR)));
                budText.setText(Integer.toString(-1));
                cosText.setText(Integer.toString(0));
                unsub.setVisibility(View.INVISIBLE);

                fi.write(fNameb, Integer.toString(-1));
                fi.write(fNamec, Integer.toString(0));
                int nd = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                //nd = 30;
                fi.write(fNamed, Integer.toString(-1));
                for (int i = 0; i < nd - 1; ++i) {
                    fi.appendLine(fNamed, Integer.toString(-1));
                }
                //List<Double> li = fi.read(fNamed);
                //Toast.makeText(BudgetMain.this,"Contents of d.txt is "+li.toString(),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BudgetMain.this, "Error in registration", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregister() {
        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            DeleteBuilder<BudgetRow, String> deleteBuilder = budgetDao.deleteBuilder();
            deleteBuilder.where().eq("roll", roll);
            if (deleteBuilder.delete() != 0) {
                numReg--;

                SharedPreferences spf = BudgetMain.this.getSharedPreferences(BudgetMain.this.getString(R.string.app_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                edit.putInt("budreg", numReg);
                edit.apply();

                if (numReg == 0) {
                    Intent budSet = new Intent(BudgetMain.this, BudgetReceiver.class);
                    PendingIntent pintent = PendingIntent.getBroadcast(BudgetMain.this, 3011, budSet, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pintent);
                    //Toast.makeText(BudgetMain.this, "Cancelled successfully", Toast.LENGTH_SHORT).show();
                }
                fi.deleteFile(fNameb);
                fi.deleteFile(fNamec);
                fi.deleteFile(fNamed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DaoHelper getHelper() {
        if (anidb == null) {
            anidb = OpenHelperManager.getHelper(this, DaoHelper.class);
        }
        return anidb;
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
