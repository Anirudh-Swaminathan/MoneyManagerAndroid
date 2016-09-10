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

import java.sql.SQLException;
import java.util.List;

public class UpdateBudget extends AppCompatActivity {

    String roll,fNameb;
    int numReg, mbudget;

    DaoHelper anidb = null;
    FileHelper fi;

    TextView days;
    EditText budg;
    Button upda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget);

        days = (TextView) findViewById(R.id.upbuddatxt);
        budg = (EditText) findViewById(R.id.upbudedt);
        upda = (Button) findViewById(R.id.upbudbtn);
        fi = new FileHelper(UpdateBudget.this);

        SharedPreferences spf = UpdateBudget.this.getSharedPreferences(UpdateBudget.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(UpdateBudget.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UpdateBudget.this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb", "");
        numReg = spf.getInt("budreg", 0);
        fNameb = roll+"b.txt";

        setTitle(roll);

        mbudget = getBudget();
        if (mbudget != -1) {
            Toast.makeText(UpdateBudget.this, "Already set the budget for this month", Toast.LENGTH_SHORT).show();
            finish();
        }

        upda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateBud()) {
                    Toast.makeText(UpdateBudget.this, "Update was successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateBudget.this, "Sorry, Error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean updateBud() {
        String bu = budg.getText().toString();
        int budget = -1;
        if (bu.equals("")) {
            Toast.makeText(UpdateBudget.this, "Please enter a valid budget in rupees", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            budget = Integer.parseInt(bu);
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            UpdateBuilder<BudgetRow, String> updateBuilder = budgetDao.updateBuilder();
            updateBuilder.where().eq("roll", roll);
            updateBuilder.updateColumnValue("budget", budget);
            if (updateBuilder.update() != 0) {
                fi.delLastLine(fNameb);
                fi.appendLine(fNameb,Integer.toString(budget));
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getBudget() {
        int ret = 0;
        try {
            final RuntimeExceptionDao<BudgetRow, String> budgetDao = getHelper().getBudgetRunDao();
            List<BudgetRow> buds = budgetDao.queryForEq("roll", roll);
            if (buds.size() != 0) {
                ret = buds.get(0).getBudget();
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
