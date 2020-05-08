package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

public class Main_StatsActivity extends AppCompatActivity {

    Spinner spin;
    Date date, date_char;
    SimpleDateFormat format, format_char;
    String nowtime;
    BarChart barchart;
    String ID;
    TextView txtTotalCal, txtTotalCarbo, txtTotalProtein, txtTotalFat, txtCarboPercent, txtProteinPercent, txtFatPercent;

    ProgressBar pgbCarbo, pgbProtein, pgbFat;
    float totalKcal = 0.0f, userKcal = 0.0f, totalProtein = 0.0f, totalCarbo = 0.0f, totalFat=0.0f;
    char userSex = 'M';
    int userAge = 0;
    int carboPercnt=0, proteinPercent=0, fatPercent=0;

    private int checkRecommendedProtein(char gender, int age){
        int[] ageRange = {2, 5, 8, 11, 14, 18, 29 ,49, 64, 74};
        int[][] korProtein = {
                {15,15,30,40,55,55,55,50,50,50},
                {15,20,25,40,45,50,45,45,45,45}
        };
        int _gender = gender=='M' ? 0 : 1;
        for(int i=0;i<10;i++){
            if(age <= ageRange[i]){
                return korProtein[_gender][i];
            }
        }
        return korProtein[_gender][korProtein[0].length-1];
    }

    private void renew() {
        class NewRunnable implements Runnable {
            @Override
            public void run() {
                totalKcal = 0.0f;
                userKcal = 0.0f;
                totalProtein = 0.0f;
                totalCarbo = 0.0f;
                totalFat=0.0f;
                userSex = 'M';
                userAge = 0;
                carboPercnt=0;
                proteinPercent=0;
                fatPercent=0;
                String stat = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/stats/?format=json&Date=" + nowtime + "&user=" + ID,
                        "GET", null);
                String user = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/users/?format=json&ID=" + ID,
                        "GET", null);

                try {
                    JSONArray statArray = new JSONArray(stat);

                    int len = statArray.length();
                    for(int i=0;i<len;i++){
                        try{
                            JSONObject statObj = statArray.getJSONObject(i);
                            totalKcal += Float.parseFloat(statObj.getString("Kcal"));
                            totalCarbo = Float.parseFloat(statObj.getString("Carbo"));
                            totalProtein = Float.parseFloat(statObj.getString("Protein"));
                            totalFat = Float.parseFloat(statObj.getString("Fat"));
                        } catch (JSONException e){
                            continue;
                        }
                    }
                    carboPercnt = Math.round((totalCarbo / (totalCarbo + totalProtein + totalFat)) * 100.0f);
                    proteinPercent = Math.round((totalProtein / (totalCarbo + totalProtein + totalFat)) * 100.0f);
                    fatPercent = Math.round((totalFat / (totalCarbo + totalProtein + totalFat)) * 100.0f);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try{
                    JSONArray userArray = new JSONArray(user);
                    JSONObject userObj = userArray.getJSONObject(0);
                    userKcal = Float.parseFloat(userObj.getString("UserKcal"));
                    userSex =  userObj.getString("Sex").charAt(0);
                    userAge = Integer.parseInt(userObj.getString(("Age")));
                } catch (JSONException e){
                    e.printStackTrace();
                }

                txtTotalCal.setText(totalKcal + " / " + userKcal);


                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            txtCarboPercent.setText("" + carboPercnt);
                            txtProteinPercent.setText(""  + proteinPercent);
                            txtFatPercent.setText("" + fatPercent);
                        } catch (ArithmeticException e){
                            e.printStackTrace();
                        }
                        pgbCarbo.setProgress((int)totalCarbo);
                        pgbProtein.setProgress((int)totalProtein);
                        pgbFat.setProgress((int)totalFat);

                        txtTotalCarbo.setText(totalCarbo + " / " + "90g" );
                        txtTotalProtein.setText(totalProtein + " / " + checkRecommendedProtein(userSex, userAge) + "g");
                        txtTotalFat.setText(totalFat + " / " + "25g");
                    }
                });

            }
        }
        new Thread(new NewRunnable()).start();
    }

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

        txtTotalCal = (TextView) findViewById(R.id.txtTotalCal);
        txtTotalCarbo = (TextView) findViewById(R.id.txtTotalCarbo);
        txtTotalProtein = (TextView) findViewById(R.id.txtTotalProtein);
        txtTotalFat = (TextView) findViewById(R.id.txtTotalFat);

        txtCarboPercent = (TextView) findViewById(R.id.txtCarboPercent);
        txtProteinPercent = (TextView) findViewById(R.id.txtProteinPercent);
        txtFatPercent = (TextView) findViewById(R.id.txtFatPercent);

        pgbCarbo = (ProgressBar) findViewById(R.id.pgbCarbo);
        pgbProtein = (ProgressBar) findViewById(R.id.pgbProtein);
        pgbFat = (ProgressBar) findViewById(R.id.pgbFat);

        format = new SimpleDateFormat( "yyyy-MM-dd");
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


        renew();

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, -1);
                nowtime = format.format(cal.getTime());
                txtcal.setText(nowtime);
                renew();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, 1);
                nowtime = format.format(cal.getTime());
                txtcal.setText(nowtime);
                renew();
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
