package com.anirudh.anirudhswami.personalassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anirudh Swami on 12-06-2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "personal.db";

    //Table names here
    private static final String USER_TABLE = "user";
    private static final String CONTACT_TABLE = "contact";

    //Column names for table USER_TABLE here
    private static final String USER_COL1 = "Roll";
    private static final String USER_COL2 = "Name";
    private static final String USER_COL3 = "Dept";
    private static final String USER_COL4 = "Mail";
    private static final String USER_COL5 = "Pass";
    private static final String USER_COL6 = "Image";

    //Column names for table CONTACT_TABLE here
    public static final String CONT_COL_1 = "Name";
    public static final String CONT_COL_2 = "Number";
    public static final String CONT_COL_3 = "Img";
    public static final String CONT_COL_4 = "Roll";

    public DbHelper(Context context) { //}, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "create table "+USER_TABLE+" ("+USER_COL1+" TEXT PRIMARY KEY, "+USER_COL2+" TEXT NOT NULL, "+USER_COL3+" TEXT NOT NULL, "
                +USER_COL4+" TEXT NOT NULL, "+USER_COL5+" TEXT NOT NULL, "+USER_COL6+" TEXT)";
        String query2 = "create table "+CONTACT_TABLE+" ("+CONT_COL_1+" TEXT NOT NULL, "+CONT_COL_2+" TEXT NOT NULL,"+CONT_COL_3+" BLOB,"+
                CONT_COL_4+" TEXT NOT NULL, UNIQUE("+CONT_COL_1+", "+CONT_COL_4+") ON CONFLICT REPLACE)";
        db.execSQL(query2);
        db.execSQL(query1);
        //db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CONTACT_TABLE);
        onCreate(db);
    }

    //All table inserts here
    //Table 1
    public boolean registerInsert(String roll,String name,String dept,String mail,String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL1,roll);
        contentValues.put(USER_COL2,name);
        contentValues.put(USER_COL3,dept);
        contentValues.put(USER_COL4,mail);
        contentValues.put(USER_COL5,pass);
        contentValues.put(USER_COL6, "");
        long result = db.insert(USER_TABLE,null,contentValues);
        if(result==-1) return false;
        return true;
    }
    //Table CONTACT_TABLE
    public boolean insertContact(String name, String number,byte[] img,String roll){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONT_COL_1,name);
        contentValues.put(CONT_COL_2,number);
        contentValues.put(CONT_COL_3,img);
        contentValues.put(CONT_COL_4,roll);
        long result = db.insert(CONTACT_TABLE,null,contentValues);
        if(result==-1) return false;
        return true;
    }

    //All Table selects here
    //Table 1
    public Cursor getPass(String roll){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = USER_TABLE;
        String[] columns = {USER_COL5,USER_COL2,USER_COL3,USER_COL4};
        String selection = USER_COL1+"=?";
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor res = db.query(table, columns, selection, new String[]{roll}, groupBy, having, orderBy);
        return res;
    }

    //Table 2
    public Cursor getAllContacts(String roll){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("select * from " + CONTACT_TABLE + " order by " + CONT_COL_1+" where Roll = "+roll, null);
        String table = CONTACT_TABLE;
        String[] columns = {CONT_COL_1,CONT_COL_2,CONT_COL_3};
        String selection = CONT_COL_4+"=?";
        String groupBy = null;
        String having = null;
        String orderBy = CONT_COL_1;
        Cursor res = db.query(table, columns, selection, new String[]{ roll },groupBy,having,orderBy);
        return res;
    }

    //All table updates here
    //Table 2
    public boolean update_contact(String name, String number,byte[] img,String roll){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONT_COL_1,name);
        contentValues.put(CONT_COL_2,number);
        contentValues.put(CONT_COL_3,img);
        contentValues.put(CONT_COL_4,roll);
        long res = db.update(CONTACT_TABLE, contentValues, CONT_COL_1+" = ? and "+CONT_COL_4+" = ?", new String[]{name,roll});
        if(res == 0) return false;
        return true;
    }

    //All table deletes here
    //Table 2
    public Integer delete_contact(String name,String roll){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACT_TABLE,CONT_COL_1+" = ? and "+CONT_COL_4+" = ?",new String[]{ name , roll });
    }
}
