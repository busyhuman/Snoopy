package com.example.tjrrb.snoopy;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import static android.icu.text.DisplayContext.LENGTH_SHORT;

public class Add_BookmarkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bookmark);

        Button bt1 = (Button) findViewById(R.id.detail_btn) ;
        Button bt2 = (Button) findViewById(R.id.detail_btn2) ;
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView add = (TextView) findViewById(R.id.add);

        TabHost tabHost = (TabHost)findViewById(R.id.host);
        tabHost.setup();

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

        bt2.setOnClickListener(new View.OnClickListener() {
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
                startActivity(intent);
            }
        });
    }
}
