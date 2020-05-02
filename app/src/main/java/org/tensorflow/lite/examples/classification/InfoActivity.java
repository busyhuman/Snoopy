package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    int i = 0;
    String[] c_FoodName = new String[3];
    int foodNum;
    String fd_name;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_info);

        Intent intent = getIntent();
        fd_name = intent.getStringExtra("txtStr");

        ImageView back = (ImageView) findViewById(R.id.back);

        final String[] num = {"1","2", "3", "4"};

        c_FoodName = intent.getStringArrayExtra("FoodName");
        foodNum = intent.getIntExtra("foodNum", 0);

        TextView fd_nameView = (TextView) findViewById(R.id.fd_name);

        TextView add = (TextView) findViewById(R.id.add);


        final ImageView bookmark = (ImageView) findViewById(R.id.star);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, num);
        spinner.setAdapter(adapter);

        fd_nameView.setText(fd_name);

        if(foodNum == 1){
            c_FoodName[0] = fd_name;
        }else if(foodNum == 2) {
            c_FoodName[1] = fd_name;
        }else if(foodNum == 3) {
            c_FoodName[2] = fd_name;
        }else { }

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImgRecordActivity.class);
                intent.putExtra("FoodName", c_FoodName);
                startActivity(intent);
            }
        });


    }
}
