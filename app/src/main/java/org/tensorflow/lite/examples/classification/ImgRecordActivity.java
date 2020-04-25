package org.tensorflow.lite.examples.classification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.tensorflow.lite.Interpreter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ImgRecordActivity extends AppCompatActivity {

    String FoodName1, FoodName2, FoodName3;
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_record);
        Intent intent = getIntent();

        FoodName1 = intent.getStringExtra("FoodName1");
        FoodName2 = intent.getStringExtra("FoodName2");
        FoodName3 = intent.getStringExtra("FoodName3");

        final Button endbtn;
        final TextView txtname1, txtname2, txtname3;
        final TextView txtfdcal1,txtfdcal2,txtfdcal3;
        final TextView totalCal;
        final LinearLayout fdl1, fdl2, fdl3;

        ImageView back = (ImageView) findViewById(R.id.back);

        txtfdcal1 = (TextView) findViewById(R.id.txtkcal1);
        txtfdcal2 = (TextView) findViewById(R.id.txtkcal2);
        txtfdcal3 = (TextView) findViewById(R.id.txtkcal3);

        txtname1 = (TextView) findViewById(R.id.food_name1);
        txtname2 = (TextView) findViewById(R.id.food_name2);
        txtname3 = (TextView) findViewById(R.id.food_name3);

        fdl1 = (LinearLayout) findViewById(R.id.food_sel1) ;
        fdl2 = (LinearLayout) findViewById(R.id.food_sel2) ;
        fdl3 = (LinearLayout) findViewById(R.id.food_sel3) ;

        endbtn = (Button)findViewById(R.id.end_btn);

        txtname1.setText(FoodName1);
        txtname2.setText(FoodName2);
        txtname3.setText(FoodName3);




        fdl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_BookmarkActivity.class);
                intent.putExtra("FoodName1", FoodName1);
                intent.putExtra("FoodName2", FoodName2);
                intent.putExtra("FoodName3", FoodName3);
                startActivity(intent);
            }
        });
        fdl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_BookmarkActivity.class);
                startActivity(intent);
            }
        });
        fdl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_BookmarkActivity.class);
                startActivity(intent);
            }
        });



        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main_StatsActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "음식 기록 완료!", Toast.LENGTH_LONG).show();
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
