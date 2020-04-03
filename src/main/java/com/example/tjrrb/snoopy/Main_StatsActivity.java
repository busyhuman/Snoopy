package com.example.tjrrb.snoopy;

import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;

public class Main_StatsActivity extends AppCompatActivity {


    Spinner spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_stats);

        Button bt1 = (Button) findViewById(R.id.bt1) ;
        Button bt2 = (Button) findViewById(R.id.bt2) ;
        Button bt3 = (Button) findViewById(R.id.bt3) ;
        Button bt4 = (Button) findViewById(R.id.bt4) ;

        spin = findViewById(R.id.spin);
        // Button calen = findViewById(R.id.txtCalendar);
        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpecRecord = tabHost.newTabSpec("RECORD").setIndicator("홈");
        tabSpecRecord.setContent(R.id.tabrecord);
        tabHost.addTab(tabSpecRecord);

        TabHost.TabSpec tabSpecStatistics = tabHost.newTabSpec("STATISTICS").setIndicator("통계");
        tabSpecStatistics.setContent(R.id.tabstatistics);

        final String[] sep = {"일간","주간","월간"};

        ArrayAdapter<String> adt = new ArrayAdapter<String>(this, R.layout.spinner_item, sep);
        spin.setAdapter(adt);
        tabHost.addTab(tabSpecStatistics);

        tabHost.setCurrentTab(0);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

    }
}
