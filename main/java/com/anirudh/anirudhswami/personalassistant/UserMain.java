package com.anirudh.anirudhswami.personalassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class UserMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Intent a = getIntent();
        String rollNo = a.getStringExtra("RollNo");

        String n, d, w;
        TextView name, dept, webmail;
        name = (TextView) findViewById(R.id.nameUser);
        dept = (TextView) findViewById(R.id.deptUser);
        webmail = (TextView) findViewById(R.id.mailUser);
        DbHelper Anidb = new DbHelper(UserMain.this);
        Cursor cursor = Anidb.getPass(rollNo);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            n = cursor.getString(1);
            d = cursor.getString(2);
            w = cursor.getString(3);

            name.setText(n);
            dept.setText(d);
            webmail.setText(w);
        }

        //String pass = a.getStringExtra("passWord");
        Toast.makeText(UserMain.this, "Hello There " + rollNo, Toast.LENGTH_SHORT).show();
        setTitle(rollNo);

        SharedPreferences spf = UserMain.this.getSharedPreferences(UserMain.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(UserMain.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UserMain.this, MainActivity.class);
            startActivity(i);
        }

        //MachineBudget mb = new MachineBudget(UserMain.this);
        //Toast.makeText(UserMain.this,"Budget is "+mb.predictCost(rollNo+"b.txt",rollNo+"c.txt",5.0),Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /*
            case R.id.UpdateUser:
                Toast.makeText(UserMain.this, "Update Profile selected", Toast.LENGTH_SHORT).show();
                break;
            */
            case R.id.Weather:
                //Toast.makeText(UserMain.this, "Weather updates selected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(UserMain.this, WeatherUpdates.class);
                startActivity(i);
                break;
            /*
            case R.id.Calculator:
                Toast.makeText(UserMain.this, "Calculator selected", Toast.LENGTH_SHORT).show();
                break;
            */
            case R.id.Contacts:
                //Toast.makeText(UserMain.this, "Contacts selected", Toast.LENGTH_SHORT).show();
                Intent con = new Intent(UserMain.this, Contacts.class);
                startActivity(con);
                break;
            case R.id.Budget:
                //Toast.makeText(UserMain.this, "Budget selected", Toast.LENGTH_SHORT).show();
                Intent bud = new Intent(UserMain.this,BudgetMain.class);
                startActivity(bud);
                break;
            /*
            case R.id.Marks:
                Toast.makeText(UserMain.this, "Marks manager selected", Toast.LENGTH_SHORT).show();
                break;
            */
            case R.id.Movies:
                //Toast.makeText(UserMain.this, "Movie details selected", Toast.LENGTH_SHORT).show();
                Intent movs = new Intent(UserMain.this, Movies.class);
                startActivity(movs);
                break;
            case R.id.Logout:
                AlertDialog.Builder abu = new AlertDialog.Builder(UserMain.this);
                abu.setMessage("Do You Wish to LOGOUT??").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences spf = UserMain.this.getSharedPreferences(UserMain.this.getString(R.string.app_name), Context.MODE_PRIVATE);
                        SharedPreferences.Editor spfe = spf.edit();
                        spfe.putBoolean("loginStat", true);
                        spfe.putString("rollNumb", null);
                        spfe.apply();
                        Intent i = new Intent(UserMain.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = abu.create();
                alert.setTitle("LOGOUT CONFIRMATION");
                alert.show();
                break;
            default:
                //Toast.makeText(UserMain.this, "You made the wrong choice", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    */

}
