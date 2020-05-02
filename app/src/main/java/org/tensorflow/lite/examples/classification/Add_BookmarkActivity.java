package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Add_BookmarkActivity extends AppCompatActivity {

    String[] c_FoodName = new String[3];
    String ID;
    int foodNum;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bookmark);

        Button bt1 = (Button) findViewById(R.id.detail_btn) ;
        ImageView back = (ImageView) findViewById(R.id.back);


        TabHost tabHost = (TabHost)findViewById(R.id.host);
        tabHost.setup();

        Intent intent = getIntent();
        c_FoodName = intent.getStringArrayExtra("FoodName");
        foodNum = intent.getIntExtra("foodNum", 0);

        TabHost.TabSpec tabSpecDog = tabHost.newTabSpec("Dog").setIndicator("검색");
        tabSpecDog.setContent(R.id.search);
        tabHost.addTab(tabSpecDog);

        TabHost.TabSpec tabSpecCat = tabHost.newTabSpec("Cat").setIndicator("즐겨찾기");
        tabSpecCat.setContent(R.id.total);
        tabHost.addTab(tabSpecCat);


        tabHost.setCurrentTab(0);

        final String[] mid = {"밥", "김", "물", "배추김치", "라면"};

        ListView list = (ListView) findViewById(R.id.list1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, mid);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String txtStr = mid[i];
                    Intent intent = new Intent(Add_BookmarkActivity.this, InfoActivity.class);
                    intent.putExtra("FoodName", c_FoodName);
                    intent.putExtra("ID", ID);
                    intent.putExtra("foodNum", foodNum);
                    intent.putExtra("txtStr", txtStr);
                    startActivity(intent);

            }
        });

        final String[] mid2 = {"밥", "배추김치", "라면"};

        ListView list1 = (ListView) findViewById(R.id.list2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, mid2);
        list1.setAdapter(adapter1);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String txtStr = mid2[i];
                Intent intent = new Intent(Add_BookmarkActivity.this, InfoActivity.class);
                intent.putExtra("FoodName", c_FoodName);
                intent.putExtra("ID", ID);
                intent.putExtra("foodNum", foodNum);
                intent.putExtra("txtStr", txtStr);
                startActivity(intent);

            }
        });



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
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
