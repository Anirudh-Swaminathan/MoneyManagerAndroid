package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by Anirudh Swami on 12-06-2016.
 */
public class ValidateData {
    public boolean validateRoll(String roll,Context ctx){
        if(roll.equals("")){
            Toast.makeText(ctx,"Enter a roll number",Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            long r = Long.parseLong(roll);
            if(Math.floor(r / Math.pow(10, 8))!=1){
                Toast.makeText(ctx,"Incorrect roll number",Toast.LENGTH_SHORT).show();
                return  false;
            }
            if(!(Math.floor(r/Math.pow(10,6)) ==102 || Math.floor(r/Math.pow(10,6)) ==101 || Math.floor(r/Math.pow(10,6)) ==103 || Math.floor(r/Math.pow(10,6)) ==106
                    || Math.floor(r/Math.pow(10,6)) ==107 || Math.floor(r/Math.pow(10,6)) ==108 || Math.floor(r/Math.pow(10,6)) ==110 || Math.floor(r/Math.pow(10,6)) ==111
                    || Math.floor(r/Math.pow(10,6)) ==112 || Math.floor(r/Math.pow(10,6)) ==114)){
                Toast.makeText(ctx,"Incorrect roll number",Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean validateName(String name,Context ctx){
        if(name.equals("")){
            Toast.makeText(ctx,"Name must not be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern aniPat = Pattern.compile("[a-zA-Z][a-zA-Z ]+|[a-zA-Z]");
        if(!aniPat.matcher(name).matches()){
            Toast.makeText(ctx,"Name must have only alphabets and spaces",Toast.LENGTH_SHORT).show();
            return false;
        }
        aniPat = Pattern.compile("[0-9]");
        if(aniPat.matcher(name).matches()){
            Toast.makeText(ctx,"Name must not contain any digits",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validateMail(String mail,String roll,Context ctx){
        if(mail.length()!=18){
            Toast.makeText(ctx,"Invalid webmail. It must be 'your-roll@nitt.edu'",Toast.LENGTH_SHORT).show();
            return false;
        }
        String ro = mail.substring(0,9);
        String  en = mail.substring(9, 18);
        if(!ro.equals(roll)){
            Toast.makeText(ctx,"Invalid webmail. It must be 'your-roll@nitt.edu'",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!en.equals("@nitt.edu")){
            Toast.makeText(ctx,"Invalid webmail. It must be 'your-roll@nitt.edu'",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean validateDept(String d,String roll,Context ctx){
        if(d.equals("") || roll.equals("")){
            Toast.makeText(ctx,"Department must not be null",Toast.LENGTH_SHORT).show();
            return false;
        }
        try{
            Long r = Long.parseLong(roll);
            if(!(Math.floor(r/Math.pow(10,6)) ==102 && d.equals("Chemical")||Math.floor(r/Math.pow(10,6)) ==106 && d.equals("CSE")
                    ||Math.floor(r/Math.pow(10,6)) ==107 && d.equals("EEE")||Math.floor(r/Math.pow(10,6)) ==101 && d.equals("Architecture")
                    ||Math.floor(r/Math.pow(10,6)) ==103 && d.equals("Civil")||Math.floor(r/Math.pow(10,6)) ==108 && d.equals("ECE")
                    ||Math.floor(r/Math.pow(10,6)) ==110 && d.equals("ICE")||Math.floor(r/Math.pow(10,6)) ==111 && d.equals("Mechanical")
                    ||Math.floor(r/Math.pow(10,6)) ==112 && d.equals("MME")||Math.floor(r/Math.pow(10,6)) ==114 && d.equals("Production"))){
                Toast.makeText(ctx,"Incorrect department for entered roll Number",Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
