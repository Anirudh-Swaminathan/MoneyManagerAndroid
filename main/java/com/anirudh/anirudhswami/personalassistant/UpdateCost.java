package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anirudh.anirudhswami.personalassistant.model.BudgetRow;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.util.Calendar;
import java.util.List;

public class UpdateCost extends AppCompatActivity {

    String roll, fNamec, fNamed;
    int numDays, mcost, mbudget;

    DaoHelper anidb = null;
    FileHelper fi;

    TextView days;
    EditText costs;
    Button upda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cost);


        days = (TextView) findViewById(R.id.upctxt);
        costs = (EditText) findViewById(R.id.upcedt);
        upda = (Button) findViewById(R.id.upcbtn);
        fi = new FileHelper(UpdateCost.this);

        SharedPreferences spf = UpdateCost.this.getSharedPreferences(UpdateCost.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(UpdateCost.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UpdateCost.this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb", "");

        setTitle(roll);
        fNamec = roll + "c.txt";
        fNamed = roll + "d.txt";

        numDays = getNumDays();
        if (mbudget == -1) {
            Toast.makeText(UpdateCost.this, "Budget not set for this month!!", Toast.LENGTH_SHORT).show();
            Intent sh = new Intent(UpdateCost.this, UpdateBudget.class);
            startActivity(sh);
        }
        if (numDays == 0) {
            Toast.makeText(UpdateCost.this, "Already updated cost today", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (numDays == 1) {
            days.setText("Enter today's cost");
        } else {
            days.setText("Enter cost for " + numDays + " days");
        }

        upda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateBud()) {
                    Toast.makeText(UpdateCost.this, "Update was successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateCost.this, "Sorry, Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean updateBud() {
        String bu = costs.getText().toString();
        int budget = -1;
        if (bu.equals("")) {
            Toast.makeText(UpdateCost.this, "Please enter a valid cost in rupees", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            budget = Integer.parseInt(bu);
            int cd = budget;
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            UpdateBuilder<BudgetRow, String> updateBuilder = budgetDao.updateBuilder();
            updateBuilder.where().eq("roll", roll);
            budget = budget + mcost;
            updateBuilder.updateColumnValue("cost", budget);
            updateBuilder.updateColumnValue("numDays", 0);
            if (updateBuilder.update() != 0) {
                fi.delLastLine(fNamec);
                fi.appendLine(fNamec, Integer.toString(budget));
                List<Double> costM = fi.read(fNamed);
                double[] coss = new double[costM.size()];
                for (int i = 0; i < costM.size(); ++i)
                    coss[i] = costM.get(i);
                Calendar cal = Calendar.getInstance();
                coss[cal.get(Calendar.DAY_OF_MONTH) - 1] = cd;
                fi.deleteFile(fNamed);
                fi.write(fNamed, Double.toString(coss[0]));
                for (int i = 1; i < coss.length; ++i) {
                    fi.appendLine(fNamed, Double.toString(coss[i]));
                }
                //List<Double> li = fi.read(fNamed);
                //Toast.makeText(UpdateCost.this,"Contents of coss is now "+li.toString(),Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getNumDays() {
        int ret = 0;
        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            List<BudgetRow> buds = budgetDao.queryForEq("roll", roll);
            if (buds.size() != 0) {
                ret = buds.get(0).getNumDays();
                mcost = buds.get(0).getCost();
                mbudget = buds.get(0).getBudget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
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
        //finish();
    }
}
