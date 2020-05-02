package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



import androidx.appcompat.app.AppCompatActivity;

public class Main_StatsActivity extends AppCompatActivity {

    Spinner spin;
    Date date, date_char;
    SimpleDateFormat format, format_char;
    String nowtime;
    BarChart barchart;
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_stats);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");

        TextView txtcal = (TextView) findViewById(R.id.txtCalendar);
        Button bt1 = (Button) findViewById(R.id.bt1) ;
        Button bt2 = (Button) findViewById(R.id.bt2) ;
        Button bt3 = (Button) findViewById(R.id.bt3) ;

        Button before = (Button) findViewById(R.id.beforebt);
        Button next = (Button) findViewById(R.id.nextbt);
        barchart = (BarChart) findViewById(R.id.barchart);

        format = new SimpleDateFormat( "yyyy년 MM월 dd일");
        format_char = new SimpleDateFormat( "MM.dd");
        date = new Date();
        date_char = new Date();
        Calendar cal = Calendar.getInstance();
        Calendar cal_char = Calendar.getInstance();
        cal.setTime(date);
        cal_char.setTime(date_char);
        nowtime = format.format(cal.getTime());

        txtcal.setText(nowtime);

        ArrayList calo = new ArrayList();

        calo.add(new BarEntry(100, 0));
        calo.add(new BarEntry(200, 1));
        calo.add(new BarEntry(300, 2));
        calo.add(new BarEntry(400, 3));
        calo.add(new BarEntry(500, 4));

        ArrayList date1 = new ArrayList();

        date1.add("04/23");
        date1.add("04/22");
        date1.add("04/21");
        date1.add("04/20");
        date1.add("04/19");

        BarDataSet bardataset = new BarDataSet(calo, "칼로리");
        bardataset.setValueTextSize(15);
        bardataset.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData data = new BarData(date1, bardataset);      // MPAndroidChart v3.X 오류 발생
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barchart.setData(data);
        barchart.animateXY(7,3000);
        barchart.invalidate();


        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, -1);
                nowtime = format.format(cal.getTime());
                txtcal.setText(nowtime);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, 1);
                nowtime = format.format(cal.getTime());
                txtcal.setText(nowtime);
            }
        });

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
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                intent.putExtra("eatTime", "아침");
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                intent.putExtra("eatTime", "점심");
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                intent.putExtra("eatTime", "저녁");
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });


    }

}
