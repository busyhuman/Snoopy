package org.tensorflow.lite.examples.classification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ImgRecordActivity extends AppCompatActivity {

    String ID, eatTime, Query;
    String[] FoodName, c_fn = new String[3];
    float[] f_kcal = new float[3];
    int foodNum, totalKcal;
    TextView t_eatTime;
    TextView txtfdcal1,txtfdcal2,txtfdcal3;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    Cursor cursor;


    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_record);
        Intent intent = getIntent();


        FoodName = intent.getStringArrayExtra("FoodName");
        foodNum = intent.getIntExtra("foodNum", 0);
        ID = intent.getStringExtra("ID");
        eatTime = intent.getStringExtra("eatTime");

        final Button endbtn;
        final TextView txtname1, txtname2, txtname3;
        final TextView totalCal;
        final LinearLayout fdl1, fdl2, fdl3;
        t_eatTime = (TextView) findViewById(R.id.eatTime);

        t_eatTime.setText(eatTime);


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

        txtname1.setText(FoodName[0]);
        txtname2.setText(FoodName[1]);
        txtname3.setText(FoodName[2]);



            Query = "SELECT Kcal, FoodName FROM foods WHERE FoodName='"+ FoodName[0] +"'";
            myHelper = new myDBHelper(this);
            sqlDB = myHelper.getReadableDatabase();
            cursor = sqlDB.rawQuery(Query, null);

            while(cursor.moveToNext()){
                c_fn[0]  = cursor.getString(1);
                f_kcal[0] = cursor.getFloat(0);
            }
            System.out.println(c_fn[0]+ " " + f_kcal[0]);

             sqlDB.close();
             cursor.close();


        txtfdcal1.setText(String.valueOf(f_kcal[0]));
        txtfdcal2.setText(String.valueOf(f_kcal[1]));
        txtfdcal3.setText(String.valueOf(f_kcal[2]));

        fdl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_BookmarkActivity.class);
                intent.putExtra("FoodName", FoodName);
                intent.putExtra("f_kcal", f_kcal);
                intent.putExtra("ID", ID);
                intent.putExtra("foodNum", 1);
                startActivity(intent);
            }
        });
        fdl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_BookmarkActivity.class);
                intent.putExtra("FoodName", FoodName);
                intent.putExtra("ID", ID);
                intent.putExtra("foodNum", 2);
                startActivity(intent);
            }
        });
        fdl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_BookmarkActivity.class);
                intent.putExtra("FoodName", FoodName);
                intent.putExtra("ID", ID);
                intent.putExtra("foodNum", 3);
                startActivity(intent);
            }
        });



        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main_StatsActivity.class);
                intent.putExtra("ID", ID);
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
