package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    int i = 0;
    String[] c_FoodName = new String[3];
    String spi;
    int foodNum, eatTime, fdID, bookbool, booknum;
    String fd_name, ID, nowtime;
    TextView txtsize, txtkcal, txtcar, txtpro, txtfat, txtNa;
    JSONArray jarray;
    JSONObject jsonObj;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    Cursor cursor;

    int size;
    float[] Serving = new float[3];
    float f_kcal,carbo,pro,fat,Na;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_info);

        Intent intent = getIntent();
        fd_name = intent.getStringExtra("txtStr");

        ImageView back = (ImageView) findViewById(R.id.back);

        final String[] num = {"1","2", "3", "4"};

        Serving = intent.getFloatArrayExtra("Serving");
        c_FoodName = intent.getStringArrayExtra("FoodName");
        foodNum = intent.getIntExtra("foodNum", 0);
        fdID = intent.getIntExtra("FoodID", 0);
        bookbool = intent.getIntExtra("bookbool", 0);
        ID = intent.getStringExtra("ID");
        eatTime = intent.getIntExtra("eatTime",0);
        nowtime = intent.getStringExtra("DATE");

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

        if( bookbool == 1) {bookmark.setImageResource(R.drawable.star_on);}
        else if(bookbool == 0) {bookmark.setImageResource(R.drawable.star_off);}



        String Query = "SELECT ServingSize, Kcal, Carbo, Protein, Fat, Natrium FROM foods WHERE FoodName='"+ fd_name +"'";
        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery(Query, null);

        while(cursor.moveToNext()){
            size = cursor.getInt(0);
            f_kcal = cursor.getFloat(1);
            carbo = cursor.getFloat(2);
            pro = cursor.getFloat(3);
            fat = cursor.getFloat(4);
            Na = cursor.getFloat(5);
            System.out.println(size + " " + f_kcal + " " +carbo + " " +pro + " " +fat + " "+Na + " ");
        }

        sqlDB.close();
        cursor.close();

        if(carbo==-1) {carbo=0;}
        if(pro==-1) {pro=0;}
        if(fat==-1) {fat=0;}
        if(Na==-1) {Na=0;}

        txtsize = (TextView) findViewById(R.id.txtsize);
        txtkcal = (TextView) findViewById(R.id.txtkcal);
        txtcar = (TextView) findViewById(R.id.txtcar);
        txtpro = (TextView) findViewById(R.id.txtpro);
        txtfat = (TextView) findViewById(R.id.txtfat);
        txtNa = (TextView) findViewById(R.id.txtNa);


        txtsize.setText(String.valueOf(size) + "g");
        txtkcal.setText(String.valueOf(f_kcal) + "kcal");
        txtcar.setText(String.format("%.2f", carbo)+"g");
        txtpro.setText(String.format("%.2f", pro)+"g");
        txtfat.setText(String.format("%.2f", fat)+"g");
        txtNa.setText(String.format("%.2f", Na)+"mg");



        Handler mHandler = new Handler(Looper.getMainLooper());

        class NewRunnable implements Runnable {


            @Override
            public void run() {
                String str = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/bookmarks/?format=json&ID="+ ID + "&FoodNum=" + String.valueOf(fdID),
                        "GET", null);
                System.out.println("북마크 검색: "+str);
                mHandler.postDelayed(new Runnable() { public void run() {
                    try {
                        jarray = new JSONArray(str); // JSONArray 생성
                        jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                        booknum = jsonObj.getInt("BMNum");
                        System.out.println("북마크 num: " + booknum);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                },0);
            }
        }
        if( bookbool == 1) {
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();
        }


        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                class add_Runnable implements Runnable {
                    @Override
                    public void run() {

                        if ( bookbool == 1 ){
                            System.out.println("삭제" + SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/bookmarks/" + String.valueOf(booknum) + "/", "DELETE", null));
                            bookmark.setImageResource(R.drawable.star_off);
                            System.out.println("삭제 완료");
                            bookbool = 0;
                        }
                        else if ( bookbool == 0 ){
                            String post = "user=" + ID + "&FoodNum=" + String.valueOf(fdID);
                            System.out.println("삽입: " + SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/bookmarks/?format=json",
                                    "POST", post));
                            System.out.println("삽입 완료");
                            bookmark.setImageResource(R.drawable.star_on);
                            bookbool = 1;

                            String str = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/bookmarks/?format=json&ID="+ ID + "&FoodNum=" + String.valueOf(fdID),
                                    "GET", null);
                            System.out.println(str);

                            mHandler.postDelayed(new Runnable() { public void run() {
                                try {
                                    jarray = new JSONArray(str); // JSONArray 생성
                                    jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                                    booknum = jsonObj.getInt("BMNum");
                                    System.out.println("북마크 num: " + booknum);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            },0);

                        }
                    }
                }

                add_Runnable anr = new add_Runnable();
                Thread ta = new Thread(anr);
                ta.start();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spi = spinner.getSelectedItem().toString();
                Serving[foodNum-1] = Float.parseFloat(spi);

                txtsize.setText(String.valueOf(size*Serving[foodNum-1]) + "g");
                txtkcal.setText(String.valueOf(f_kcal*Serving[foodNum-1]) + "kcal");
                txtcar.setText(String.valueOf(carbo*Serving[foodNum-1])+"g");
                txtpro.setText(String.valueOf(pro*Serving[foodNum-1])+"g");
                txtfat.setText(String.valueOf(fat*Serving[foodNum-1])+"g");
                txtNa.setText(String.valueOf(Na*Serving[foodNum-1])+"mg");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                intent.putExtra("Serving", Serving);
                intent.putExtra("FoodName", c_FoodName);
                intent.putExtra("ID", ID);
                intent.putExtra("eatTime", eatTime);
                intent.putExtra("DATE", nowtime);
                intent.putExtra("foodNum", foodNum);
                startActivity(intent);
            }
        });


    }
}

