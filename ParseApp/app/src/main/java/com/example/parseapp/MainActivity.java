package com.example.parseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        addData();
//        getData();
    }

    public void getData () {
        // Create a Parse Query instance to get data from Parse server
        // Verileri getirmek için Parse Query nesnesi oluşturma
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Fruits");

        // Get data which is given Id to the query
        // Id verilen nesnenin arka planda getirilmesini sağlama
        parseQuery.getInBackground("1CBy9ul7Wc", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    String name = object.getString("name");
                    int calory = object.getInt("calories");

                    System.out.println("object name: " + name);
                    System.out.println("object calory: " + calory);
                }
            }
        });
    }

    public void addData () {
        // Create a Parse Object instance to save data to Parse server
        // Verileri kaydetmek için Parse nesnesi örneği oluşturma
        ParseObject parseObject = new ParseObject("Fruits");

        // Add data to Parse Object
        // Parse nesnesine veri ekleme
        parseObject.put("name", "banana");
        parseObject.put("calories", 150);

        // Save data an get callback func
        // Veri kaydetme ve geri dönüş fonksiyonunu kullanma
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}