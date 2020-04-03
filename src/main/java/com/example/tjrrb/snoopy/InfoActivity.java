package com.example.tjrrb.snoopy;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;


public class InfoActivity extends AppCompatActivity {

    int i = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_info);

        ImageView back = (ImageView) findViewById(R.id.back);

        final String[] num = {"1","2", "3", "4"};


        final ImageView bookmark = (ImageView) findViewById(R.id.star);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, num);
        spinner.setAdapter(adapter);

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 - i;

                if ( i == 0 ){
                    bookmark.setImageResource(R.drawable.star_off);
                }
                else{
                    bookmark.setImageResource(R.drawable.star_on);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
