package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class AddContact extends AppCompatActivity {

    private static String roll;
    EditText name;
    EditText number;
    DbHelper AniDb;

    Bitmap bitmap = null;
    byte[] photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        SharedPreferences spf = AddContact.this.getSharedPreferences(AddContact.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(AddContact.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddContact.this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb","");
        //Toast.makeText(AddContact.this,"Roll is "+roll,Toast.LENGTH_SHORT).show();

        name= (EditText) findViewById(R.id.nameAdd);
        number = (EditText) findViewById(R.id.numberAdd);
        AniDb = new DbHelper(this);
        ((Button) findViewById(R.id.addData)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String na = name.getText().toString();
                String nu = number.getText().toString();

                //the following is code for image-insertion
                if(bitmap == null) {

                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();

                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                photo = baos.toByteArray();
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap theImage= BitmapFactory.decodeStream(imageStream);
                ImageView img_here = (ImageView) findViewById(R.id.imageView2);
                img_here.setImageBitmap(theImage);
                //stops here

                if(na.equals("")||nu.equals("")){
                    Toast.makeText(AddContact.this,"Enter a name and number ",Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean inser = AniDb.insertContact(na, nu, photo, roll);
                    if(inser){
                        Toast.makeText(AddContact.this,"Data Inserted SCCESSFULLY!",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddContact.this,"Data Couldn't be inserted Successfully",Toast.LENGTH_SHORT).show();
                    }



                }
                //update();
                Intent i = new Intent(AddContact.this,Contacts.class);
                startActivity(i);
                finish();
            }
        });
        ((Button) findViewById(R.id.tak_pik)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bdn = data.getExtras();
            Bitmap btm = (Bitmap) bdn.get("data");
            bitmap = btm;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photo = baos.toByteArray();

            ImageView img_here = (ImageView) findViewById(R.id.imageView2);
            img_here.setImageBitmap(btm);
        }
    }

}
