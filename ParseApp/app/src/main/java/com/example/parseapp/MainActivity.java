package com.example.parseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an Parse Object instance to save data to Parse Object
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