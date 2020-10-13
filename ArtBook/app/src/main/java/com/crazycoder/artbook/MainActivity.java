package com.crazycoder.artbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listArts;
    SQLiteDatabase DB;
    ArrayList<String> artNames;
    ArrayList<Integer> idS;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listArts = findViewById(R.id.listItems);
        artNames = new ArrayList<String>();
        idS = new ArrayList<Integer>();

        try {
            DB = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, artNames);
        listArts.setAdapter(arrayAdapter);

        listArts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentToDetail = new Intent(MainActivity.this, DetailActivity.class);
                intentToDetail.putExtra("artId", idS.get(i));
                intentToDetail.putExtra("activityType", 2);
                startActivity(intentToDetail);
            }
        });

        getData();
    }

    public void getData() {
        try {
            Cursor cursor = DB.rawQuery("Select * from arts", null);
            int nameIx = cursor.getColumnIndex("artName");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                artNames.add(cursor.getString(nameIx));
                idS.add(cursor.getInt(idIx));
            }

            arrayAdapter.notifyDataSetChanged();

            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_art, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_art) {
            Intent detail_intent = new Intent(MainActivity.this, DetailActivity.class);
            detail_intent.putExtra("activityType", 1);
            startActivity(detail_intent);
        }

        return super.onOptionsItemSelected(item);
    }
}