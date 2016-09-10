package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contacts extends AppCompatActivity {

    List<String> namesList = new ArrayList<>();
    List<String> numberList = new ArrayList<>();
    List<Bitmap> imageList = new ArrayList<>();

    String[] names;
    String[] numbers;
    Bitmap[] images;

    DbHelper AniDb = new DbHelper(this);
    private static String roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        SharedPreferences spf = Contacts.this.getSharedPreferences(Contacts.this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!spf.getBoolean("loginStat", false)) {
            Toast.makeText(Contacts.this, "Please Login to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Contacts.this, MainActivity.class);
            startActivity(i);
        }
        roll = spf.getString("rollNumb","");

        getNames();
        names=new String[namesList.size()];
        numbers = new String[numberList.size()];
        images = new Bitmap[imageList.size()];
        for(int i=0; i<namesList.size(); ++i){
            names[i]=namesList.get(i);
            numbers[i]=numberList.get(i);
            images[i] = imageList.get(i);
        }

        ListAdapter aniAdapter = new ContactCustomAdapter(Contacts.this,names,numbers,images,roll);
        ListView contList = (ListView) findViewById(R.id.contList);
        contList.setAdapter(aniAdapter);

        contList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = String.valueOf(parent.getItemAtPosition(position));
                ImageView curr_img = (ImageView) view.findViewById(R.id.imageView);
                String number = numbers[position];

                //Toast.makeText(MainActivity.this, "Hi. Thanks for clicking", Toast.LENGTH_SHORT).show();

                curr_img.buildDrawingCache();
                Bitmap curri = curr_img.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("curri", curri);
                Intent inta = new Intent(Contacts.this, Contact_Content.class);
                inta.putExtra("name", name).putExtra("number", number);
                inta.putExtras(extras);
                startActivity(inta);

            }
        });
        ((Button) findViewById(R.id.order)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(namesList);
                Collections.reverse(numberList);
                Collections.reverse(imageList);

                names = new String[namesList.size()];
                numbers = new String[numberList.size()];
                images=new Bitmap[imageList.size()];
                for (int i = 0; i < namesList.size(); ++i) {
                    names[i] = namesList.get(i);
                    numbers[i] = numberList.get(i);
                    images[i] = imageList.get(i);
                }

                ListAdapter aniAdapter = new ContactCustomAdapter(Contacts.this, names, numbers,images,roll);
                ListView contList = (ListView) findViewById(R.id.contList);
                contList.setAdapter(aniAdapter);
            }
        });

        ((Button) findViewById(R.id.ser_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numb = ((EditText) findViewById(R.id.ser_txt)).getText().toString();
                boolean fond = false;
                for(int i=0; i<numbers.length; i++){
                    if(numbers[i].equals(numb)){
                        Toast.makeText(Contacts.this,"Contact Found. Name: "+names[i],Toast.LENGTH_SHORT).show();
                        fond = true;
                        break;
                    }
                }
                if(!fond) Toast.makeText(Contacts.this,"Contact Missing",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contact_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.AddIt:
                Toast.makeText(Contacts.this,"Adding-Data",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Contacts.this,AddContact.class);
                startActivity(i);
                finish();
                break;
            default:
                //Toast.makeText(Contacts.this,"HELLO!!",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getNames(){
        Cursor res = AniDb.getAllContacts(roll);
        if(res.getCount()==0){
            return;
        }
        while (res.moveToNext()){
            namesList.add(res.getString(0));
            numberList.add(res.getString(1));
            //imageList.add(res.getBlob(2));
            //byte[] blob = res.getBlob(res.getInt(res.getColumnIndex("photo")));
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
            //bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            //byte[] blob = baos.toByteArray();

            byte[] blob = res.getBlob(2);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(blob);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);
            imageList.add(theImage);
        }
    }
}
