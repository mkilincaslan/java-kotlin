package com.example.parseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        addData();
//        getOneData();
//        getListData();
//        userCreate();
//        userLogin();
    }

    public void getOneData () {
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

    public void getListData() {
        // Create a Parse Query instance to get data from Parse server
        // Verileri getirmek için Parse Query nesnesi oluşturma
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Fruits");

        // More filter options
        // Filteleme senecekleri

        /*
         * parseQuery.whereEqualTo("name", "banana");
        */

        // Get all data
        // Tüm verileri getirme

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            String name = object.getString("name");
                            int calory = object.getInt("calories");

                            System.out.println("object name: " + name);
                            System.out.println("object calory: " + calory);
                        }
                    }
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

    // User process
    // Kullanici Islemleri

    public void userCreate() {
        ParseUser user = new ParseUser();
        user.setUsername("username");
        user.setPassword("password");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    Toast.makeText(MainActivity.this, "user created", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void userLogin() {
        ParseUser.logInInBackground("username", "password", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "user logged in", Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "welcome " + user.getUsername(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}