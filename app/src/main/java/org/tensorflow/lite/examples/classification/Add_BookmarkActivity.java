package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Add_BookmarkActivity extends AppCompatActivity {

    String[] c_FoodName = new String[3];
    String[] list_name = new String[15];
    String[] bookmark, bookmark_name;
    String ID, nowtime, Query;
    Button search_bt;
    EditText search_txt;
    int foodNum, eatTime, fdID;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    Cursor cursor;
    ListView list,list1;
    JSONArray jarray;
    JSONObject jsonObj;


    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bookmark);

        ImageView back = (ImageView) findViewById(R.id.back);
        search_bt = (Button) findViewById(R.id.search_btn);
        search_txt = (EditText) findViewById(R.id.search_txt);

        TabHost tabHost = (TabHost)findViewById(R.id.host);
        tabHost.setup();

        Intent intent = getIntent();

        c_FoodName = intent.getStringArrayExtra("FoodName");
        foodNum = intent.getIntExtra("foodNum", 0);
        ID = intent.getStringExtra("ID");
        eatTime = intent.getIntExtra("eatTime",0);
        nowtime = intent.getStringExtra("DATE");


        TabHost.TabSpec tabSpecDog = tabHost.newTabSpec("search").setIndicator("검색");
        tabSpecDog.setContent(R.id.search);
        tabHost.addTab(tabSpecDog);

        TabHost.TabSpec tabSpecCat = tabHost.newTabSpec("bookmark").setIndicator("즐겨찾기");
        tabSpecCat.setContent(R.id.total);
        tabHost.addTab(tabSpecCat);


        tabHost.setCurrentTab(0);

        list = (ListView) findViewById(R.id.list1);
        list1 = (ListView) findViewById(R.id.list2);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String txtStr = list_name[i];

                Handler mHandler = new Handler(Looper.getMainLooper());

                class NewRunnable implements Runnable {


                    @Override
                    public void run() {
                        String str = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/foods/?format=json&FoodName="+ txtStr,
                                "GET", null);
                        System.out.println(str);
                        mHandler.postDelayed(new Runnable() { public void run() {
                            try {
                                JSONArray jarray = new JSONArray(str); // JSONArray 생성
                                JSONObject jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                                fdID = jsonObj.getInt("Num");
                                System.out.println("검색: " + fdID);

                                Intent intent = new Intent(Add_BookmarkActivity.this, InfoActivity.class);
                                intent.putExtra("FoodName", c_FoodName);
                                intent.putExtra("ID", ID);
                                intent.putExtra("bookbool", 0);
                                intent.putExtra("FoodID", fdID);
                                intent.putExtra("eatTime", eatTime);
                                intent.putExtra("DATE", nowtime);
                                intent.putExtra("foodNum", foodNum);
                                intent.putExtra("txtStr", txtStr);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        },0);
                    }
                }

                NewRunnable nr = new NewRunnable() ;
                Thread t = new Thread(nr) ;
                t.start();

            }
        });


        Handler mHandler = new Handler(Looper.getMainLooper());

        class NewRunnable implements Runnable {


            @Override
            public void run() {
                String str = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/bookmarks/?format=json&user="+ ID,
                        "GET", null);
                System.out.println("북바크 검색: " + str);
                mHandler.postDelayed(new Runnable() { public void run() {
                    try {
                        JSONArray jarray = new JSONArray(str); // JSONArray 생성
                        bookmark = new String[jarray.length()];
                        System.out.println("북바크 길이: " + jarray.length());
                        for(int i=0; i<jarray.length(); i++) {
                            JSONObject jsonObj = jarray.getJSONObject(i);  // JSONObject 추출
                            bookmark[i] = jsonObj.getString("FoodNum");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                },0);
            }
        }



        NewRunnable nr = new NewRunnable() ;
        Thread t = new Thread(nr) ;
        t.start();



        tabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class book_Runnable implements Runnable {
                    @Override
                    public void run() {
                        String[] str = new String[bookmark.length];
                        for(int i=0; i<bookmark.length; i++) {
                            str[i] = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/foods/?format=json&Num="+ bookmark[i],
                                    "GET", null);
                            System.out.println("푸드 검색: "+str[i]);
                        }
                        bookmark_name = new String[str.length];
                        mHandler.postDelayed(new Runnable() { public void run() {
                            try {
                                for(int i=0; i<str.length; i++){
                                    jarray = new JSONArray(str[i]); // JSONArray 생성
                                    jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                                    bookmark_name[i] =jsonObj.getString("FoodName");
                                    System.out.println("북마크 네임: " + bookmark[i]);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            tabHost.setCurrentTab(1);
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Add_BookmarkActivity.this,  android.R.layout.simple_list_item_1, bookmark_name);
                            list1.setAdapter(adapter1);
                        }
                        },0);
                    }
                }

                book_Runnable bnr = new book_Runnable() ;
                Thread tb = new Thread(bnr) ;
                tb.start();
            }
        });

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String txtStr = bookmark_name[i];

                Handler mHandler = new Handler(Looper.getMainLooper());

                class NewRunnable implements Runnable {


                    @Override
                    public void run() {
                        String str = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/foods/?format=json&FoodName="+ txtStr,
                                "GET", null);
                        System.out.println(str);
                        mHandler.postDelayed(new Runnable() { public void run() {
                            try {
                                JSONArray jarray = new JSONArray(str); // JSONArray 생성
                                JSONObject jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                                fdID = jsonObj.getInt("Num");
                                System.out.println("검색: " + fdID);

                                System.out.println("인텐트 푸드 아이디: " + fdID);
                                Intent intent = new Intent(Add_BookmarkActivity.this, InfoActivity.class);
                                intent.putExtra("FoodName", c_FoodName);
                                intent.putExtra("ID", ID);
                                intent.putExtra("FoodID", fdID);
                                intent.putExtra("bookbool", 1);
                                intent.putExtra("eatTime", eatTime);
                                intent.putExtra("DATE", nowtime);
                                intent.putExtra("foodNum", foodNum);
                                intent.putExtra("txtStr", txtStr);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        },0);
                    }
                }
                NewRunnable nr = new NewRunnable() ;
                Thread t = new Thread(nr) ;
                t.start();
            }
        });



        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_str = search_txt.getText().toString();

                if(!search_str.equals("")){
                    Query = "SELECT FoodName FROM foods WHERE FoodName LIKE '%"+ search_str +"%' LIMIT 15";
                    myHelper = new myDBHelper(Add_BookmarkActivity.this);
                    sqlDB = myHelper.getReadableDatabase();
                    cursor = sqlDB.rawQuery(Query, null);
                    cursor.moveToFirst();
                    String[] fdlist = new String[cursor.getCount()];
                    for(int i = 0; i<cursor.getCount(); i++) {
                        fdlist[i] = cursor.getString(cursor.getColumnIndex("FoodName"));
                        list_name[i] = fdlist[i];
                        cursor.moveToNext();
                    }
                    sqlDB.close();
                    cursor.close();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_BookmarkActivity.this,  android.R.layout.simple_list_item_1, fdlist);

                    list.setAdapter(adapter);
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
