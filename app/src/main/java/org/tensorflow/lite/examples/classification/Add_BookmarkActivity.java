package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Bundle;
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

    String c_FoodName1, c_FoodName2, c_FoodName3;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bookmark);

        Button bt1 = (Button) findViewById(R.id.detail_btn) ;
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView add = (TextView) findViewById(R.id.add);

        TabHost tabHost = (TabHost)findViewById(R.id.host);
        tabHost.setup();

        Intent intent = getIntent();
        c_FoodName1 = intent.getStringExtra("FoodName1");
        c_FoodName2 = intent.getStringExtra("FoodName2");
        c_FoodName3 = intent.getStringExtra("FoodName3");

        TabHost.TabSpec tabSpecDog = tabHost.newTabSpec("Dog").setIndicator("검색");
        tabSpecDog.setContent(R.id.search);
        tabHost.addTab(tabSpecDog);

        TabHost.TabSpec tabSpecCat = tabHost.newTabSpec("Cat").setIndicator("즐겨찾기");
        tabSpecCat.setContent(R.id.total);
        tabHost.addTab(tabSpecCat);


        tabHost.setCurrentTab(0);

        final String[] mid = {"밥", "김", "물", "김치", "라면"};

        ListView list = (ListView) findViewById(R.id.list1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_multiple_choice, mid);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), mid[arg2], Toast.LENGTH_SHORT).show();
            }
        });

        final String[] mid2 = {"밥", "김치", "라면"};

        ListView list1 = (ListView) findViewById(R.id.list2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_multiple_choice, mid2);
        list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list1.setAdapter(adapter1);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), mid2[arg2], Toast.LENGTH_SHORT).show();
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImgRecordActivity.class);
                intent.putExtra("FoodName1", c_FoodName1);
                intent.putExtra("FoodName2", c_FoodName2);
                intent.putExtra("FoodName3", c_FoodName3);
                startActivity(intent);
            }
        });
    }
}
