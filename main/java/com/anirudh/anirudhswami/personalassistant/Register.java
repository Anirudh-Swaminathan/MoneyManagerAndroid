package com.anirudh.anirudhswami.personalassistant;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;


/**
 * Created by Anirudh Swami on 04-06-2016.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class Register extends Fragment implements AdapterView.OnItemSelectedListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    //private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Register newInstance(){
        Register fragment = new Register();
        return fragment;
    }
    public Register(){

    }


    String dept;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.register_main, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        final ValidateData aniVal = new ValidateData();

        Spinner spinner = (Spinner) rootView.findViewById(R.id.deptInReg);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.Departments,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ((Button) rootView.findViewById(R.id.regBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam, rol, dep, web, pas, confp;
                nam = ((EditText) rootView.findViewById(R.id.nameInReg)).getText().toString();
                rol = ((EditText) rootView.findViewById(R.id.rollNoReg)).getText().toString();
                web = ((EditText) rootView.findViewById(R.id.mailInReg)).getText().toString();
                pas = ((EditText) rootView.findViewById(R.id.passInReg)).getText().toString();
                confp = ((EditText) rootView.findViewById(R.id.confpInReg)).getText().toString();
                dep = dept;
                if(aniVal.validateRoll(rol,getActivity()) && aniVal.validateName(nam,getActivity()) && aniVal.validateDept(dep,rol,getActivity())
                        && aniVal.validateMail(web,rol,getActivity())){
                    if(!(pas.equals("") || confp.equals(""))){
                        if(pas.equals(confp)){
                            Toast.makeText(getActivity(),"All data is correct, and passwords match",Toast.LENGTH_SHORT).show();
                            DbHelper Anidb = new DbHelper(getActivity());
                            boolean inser = Anidb.registerInsert(rol,nam,dep,web,pas);
                            if(inser){
                                Toast.makeText(getActivity(),"Registration Successful!!",Toast.LENGTH_SHORT).show();
                                ((EditText) rootView.findViewById(R.id.rollNoReg)).setText("");
                                ((EditText) rootView.findViewById(R.id.nameInReg)).setText("");
                                ((EditText) rootView.findViewById(R.id.mailInReg)).setText("");
                                ((EditText) rootView.findViewById(R.id.passInReg)).setText("");
                                ((EditText) rootView.findViewById(R.id.confpInReg)).setText("");
                            }
                            else{
                                Toast.makeText(getActivity(),"Could not insert data!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getActivity(),"Passwords don't match",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(),"Either password or the confirm password field is null",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Sorry, but entered data is not valid",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dept = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(),"You haven't selected any department",Toast.LENGTH_SHORT).show();
    }
}
