package com.anirudh.anirudhswami.personalassistant;

/**
 * Created by Anirudh Swami on 04-06-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class Login extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    //private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Login newInstance() {
        Login fragment = new Login();
        //Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //fragment.setArguments(args);
        return fragment;
    }

    public Login() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.login_main, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        ((Button) rootView.findViewById(R.id.loginBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rollNo = ((EditText) rootView.findViewById(R.id.rollNoIn)).getText().toString();
                String passWord = ((EditText) rootView.findViewById(R.id.passIn)).getText().toString();
                if (rollNo.equals("")) {
                    Toast.makeText(getActivity(), "Enter a roll number", Toast.LENGTH_SHORT).show();
                } else {
                    if (passWord.equals("")) {
                        Toast.makeText(getActivity(), "Enter a password", Toast.LENGTH_SHORT).show();
                    } else {
                        ValidateData val = new ValidateData();
                        if (val.validateRoll(rollNo, getActivity())) {
                            DbHelper Anidb = new DbHelper(getActivity());
                            Cursor cursor = Anidb.getPass(rollNo);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                String p = cursor.getString(0);
                                if (passWord.equals(p)) {
                                    Intent i = new Intent(getActivity(), UserMain.class);
                                    i.putExtra("RollNo", rollNo);
                                    //i.putExtra("passWord", passWord);

                                    ((EditText) rootView.findViewById(R.id.rollNoIn)).setText("");
                                    ((EditText) rootView.findViewById(R.id.passIn)).setText("");

                                    final SharedPreferences loginInfo = getContext().getSharedPreferences(getContext().getString(R.string.app_name), Context.MODE_PRIVATE);
                                    final SharedPreferences.Editor edit = loginInfo.edit();
                                    edit.putBoolean("loginStat", true);
                                    edit.putString("rollNumb", rollNo);
                                    edit.apply();

                                    startActivity(i);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "Either the rollnumber or password is incorrect!!", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(getActivity(), "It seems that you haven't registered yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        SharedPreferences shared = getContext().getSharedPreferences(getContext().getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean lo = shared.getBoolean("loginStat", false);
        String us = shared.getString("rollNumb", "");

        if (lo && !us.equals("")) {
            Intent i = new Intent(getActivity(), UserMain.class);
            i.putExtra("RollNo", us);
            startActivity(i);
            getActivity().finish();
        }

        return rootView;
    }
}
