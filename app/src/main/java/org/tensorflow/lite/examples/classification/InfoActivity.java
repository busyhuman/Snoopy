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
    String c_FoodName1, c_FoodName2, c_FoodName3;
    int foodNum;
    String fd_name;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_info);

        Intent intent = getIntent();
        fd_name = intent.getStringExtra("txtStr");

        ImageView back = (ImageView) findViewById(R.id.back);

        final String[] num = {"1","2", "3", "4"};

        c_FoodName1 = intent.getStringExtra("FoodName1");
        c_FoodName2 = intent.getStringExtra("FoodName2");
        c_FoodName3 = intent.getStringExtra("FoodName3");
        foodNum = intent.getIntExtra("foodNum", 0);

        TextView fd_nameView = (TextView) findViewById(R.id.fd_name);

        TextView add = (TextView) findViewById(R.id.add);


        final ImageView bookmark = (ImageView) findViewById(R.id.star);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, num);
        spinner.setAdapter(adapter);

        fd_nameView.setText(fd_name);

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
                if(foodNum == 1){
                    intent.putExtra("FoodName1", fd_name);
                    intent.putExtra("FoodName2", c_FoodName2);
                    intent.putExtra("FoodName3", c_FoodName3);
                }else if(foodNum == 2) {
                    intent.putExtra("FoodName1", c_FoodName1);
                    intent.putExtra("FoodName2", fd_name);
                    intent.putExtra("FoodName3", c_FoodName3);
                }else if(foodNum == 3) {
                    intent.putExtra("FoodName1", c_FoodName1);
                    intent.putExtra("FoodName2", c_FoodName2);
                    intent.putExtra("FoodName3", fd_name);
                }else {
                    intent.putExtra("FoodName1", c_FoodName1);
                    intent.putExtra("FoodName2", c_FoodName2);
                    intent.putExtra("FoodName3", c_FoodName3);
                }
                startActivity(intent);
            }
        });


    }
}
