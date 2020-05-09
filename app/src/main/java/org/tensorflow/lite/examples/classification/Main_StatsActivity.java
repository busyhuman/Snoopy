package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    Date date, date_char, date_char1;
    SimpleDateFormat format, format_char, format_char1;
    String nowtime;
    String[] cal_time = new String[5];
    String[] cal_time1 = new String[5];
    BarChart barchart;
    String ID;
    TextView txtTotalCal, txtTotalCarbo, txtTotalProtein, txtTotalFat, txtCarboPercent, txtProteinPercent, txtFatPercent;
    TextView txtCar, txtPro, txtFat, txtCar1, txtPro1, txtFat1;
    TextView timekcal1, timekcal2, timekcal3, txteat1, txteat2, txteat3;
    float[] kcal = new float[3];

    ProgressBar pgbCarbo, pgbProtein, pgbFat, pbCarboPro, pbProtenPro, pbFatPro;
    float totalKcal = 0.0f, userKcal = 0.0f, totalProtein = 0.0f, totalCarbo = 0.0f, totalFat=0.0f, userWei = 0.0f;
    char userSex = 'M';
    int userAge = 0;
    int carboPercnt=0, proteinPercent=0, fatPercent=0;

    BarData data;
    BarDataSet bardataset;
    ArrayList calo;
    ArrayList date1;
    Calendar cal_char, cal_char1;

    ImageView imgCar1, imgPro1, imgFat1, imgCar2, imgPro2, imgFat2;

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
                float[] dateKcal = new float[5];
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
                String[] stat = new String[5];
                for(int i=0; i<5; i++){
                    stat[i] = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/stats/?format=json&Date=" + cal_time[i] + "&user=" + ID,
                            "GET", null);
                    System.out.println("날짜: "+ cal_time[i]);
                }
                String user = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/users/?format=json&ID=" + ID,
                        "GET", null);



                try {
                    JSONArray statArray = new JSONArray(stat[0]);

                    int len = statArray.length();
                    for(int i=0;i<len;i++){
                        try{
                            JSONObject statObj = statArray.getJSONObject(i);
                            totalKcal += Float.parseFloat(statObj.getString("Kcal"));
                            totalCarbo += Float.parseFloat(statObj.getString("Carbo"));
                            totalProtein += Float.parseFloat(statObj.getString("Protein"));
                            totalFat += Float.parseFloat(statObj.getString("Fat"));


                            if(statObj.getString("Timeslot").equals("0")){
                                kcal[0] += Float.parseFloat(statObj.getString("Kcal"));
                            }else if(statObj.getString("Timeslot").equals("1")){
                                kcal[1] += Float.parseFloat(statObj.getString("Kcal"));
                            }else if(statObj.getString("Timeslot").equals("2")){
                                kcal[2] += Float.parseFloat(statObj.getString("Kcal"));
                            }

                        } catch (JSONException e){
                            continue;
                        }
                    }
                    carboPercnt = Math.round((totalCarbo / (totalCarbo + totalProtein + totalFat)) * 100.0f);
                    proteinPercent = Math.round((totalProtein / (totalCarbo + totalProtein + totalFat)) * 100.0f);
                    fatPercent = Math.round((totalFat / (totalCarbo + totalProtein + totalFat)) * 100.0f);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ArithmeticException e){
                    e.printStackTrace();
                }

                try{
                    JSONArray userArray = new JSONArray(user);
                    JSONObject userObj = userArray.getJSONObject(0);
                    userKcal = Float.parseFloat(userObj.getString("UserKcal"));
                    userWei = Float.parseFloat(userObj.getString("Weight"));
                    userSex =  userObj.getString("Sex").charAt(0);
                    userAge = Integer.parseInt(userObj.getString(("Age")));
                } catch (JSONException e){
                    e.printStackTrace();
                }



                try {
                    for(int i =0; i<5; i++) {
                    JSONArray statArray = new JSONArray(stat[i]);
                    int len = statArray.length();
                    System.out.println("길이: "+ String.valueOf(len));
                    for(int j=0;j<len;j++){
                        try{
                            JSONObject statObj = statArray.getJSONObject(j);
                            if(len != 0){
                            dateKcal[i] += Float.parseFloat(statObj.getString("Kcal"));}
                            else if (len == 0){dateKcal[i] = 0;}
                        } catch (JSONException e){
                            continue;
                        }
                    }
                        System.out.println(cal_time[i] + " kcal: "+ String.valueOf(dateKcal[i]));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ArithmeticException e){
                    e.printStackTrace();
                }



                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        float userlb = userWei *2.2f;
                        int Protein = (int)(userlb*0.9f);
                        int Fat = (int)(userlb*0.4f);
                        int Carbo = (int)((userKcal-(Protein*4 + Fat*9))/4);


                        txtCarboPercent.setText("" + carboPercnt +"%");
                        txtProteinPercent.setText(""  + proteinPercent +"%");
                        txtFatPercent.setText("" + fatPercent +"%");

                        txtCar1.setText("" + carboPercnt +"%");
                        txtPro1.setText(""  + proteinPercent +"%");
                        txtFat1.setText("" + fatPercent +"%");

                        pgbCarbo.setProgress((int)totalCarbo);
                        pgbProtein.setProgress((int)totalProtein);
                        pgbFat.setProgress((int)totalFat);

                        pbCarboPro.setProgress((int)totalCarbo);
                        pbProtenPro.setProgress((int)totalProtein);
                        pbFatPro.setProgress((int)totalFat);

                        txteat1.setText((int)kcal[0] + "kcal");
                        txteat2.setText((int)kcal[1] + "kcal");
                        txteat3.setText((int)kcal[2] + "kcal");

                        txtTotalCal.setText((int)totalKcal + " / " + (int)userKcal + " kcal");
                        txtTotalCarbo.setText(totalCarbo + " / " + Carbo + "g" );
                        txtTotalProtein.setText(totalProtein + " / " + Protein + "g");
                        txtTotalFat.setText(totalFat + " / " + Fat + "g");

                        txtCar.setText(totalCarbo + " / " + Carbo + "g" );
                        txtPro.setText(totalProtein + " / " + Protein + "g");
                        txtFat.setText(totalFat + " / " + Fat + "g");

                        timekcal1.setText(String.valueOf((int)kcal[0] +"kcal"));
                        timekcal2.setText(String.valueOf((int)kcal[1] +"kcal"));
                        timekcal3.setText(String.valueOf((int)kcal[2] +"kcal"));

                        kcal[0]=0;
                        kcal[1]=0;
                        kcal[2]=0;

                        if(totalCarbo == Carbo){
                            imgCar1.setImageResource(R.drawable.green);
                            imgCar2.setImageResource(R.drawable.green);
                        }else if(totalCarbo < Carbo){
                            imgCar1.setImageResource(R.drawable.orange);
                            imgCar2.setImageResource(R.drawable.orange);
                        }else if(totalCarbo > Carbo){
                            imgCar1.setImageResource(R.drawable.red);
                            imgCar2.setImageResource(R.drawable.red);
                        }

                        if(totalProtein == Protein){
                            imgPro1.setImageResource(R.drawable.green);
                            imgPro2.setImageResource(R.drawable.green);
                        }else if(totalProtein < Protein){
                            imgPro1.setImageResource(R.drawable.orange);
                            imgPro2.setImageResource(R.drawable.orange);
                        }else if(totalProtein > Protein){
                            imgPro1.setImageResource(R.drawable.red);
                            imgPro2.setImageResource(R.drawable.red);
                        }

                        if(totalFat == Fat){
                            imgFat1.setImageResource(R.drawable.green);
                            imgFat2.setImageResource(R.drawable.green);
                        }else if(totalFat < Fat){
                            imgFat1.setImageResource(R.drawable.orange);
                            imgFat2.setImageResource(R.drawable.orange);
                        }else if(totalFat > Fat){
                            imgFat1.setImageResource(R.drawable.red);
                            imgFat2.setImageResource(R.drawable.red);
                        }





                        calo = new ArrayList();

                        calo.add(new BarEntry((int)dateKcal[4], 0));
                        calo.add(new BarEntry((int)dateKcal[3], 1));
                        calo.add(new BarEntry((int)dateKcal[2], 2));
                        calo.add(new BarEntry((int)dateKcal[1], 3));
                        calo.add(new BarEntry((int)totalKcal, 4));

                        date1 = new ArrayList();

                        date1.add(cal_time1[4]);
                        date1.add(cal_time1[3]);
                        date1.add(cal_time1[2]);
                        date1.add(cal_time1[1]);
                        date1.add(cal_time1[0]);

                        bardataset = new BarDataSet(calo, "칼로리");
                        bardataset.setValueTextSize(15);
                        bardataset.setAxisDependency(YAxis.AxisDependency.LEFT);

                        data = new BarData(date1, bardataset);      // MPAndroidChart v3.X 오류 발생
                        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        barchart.setData(data);
                        barchart.animateXY(7,3000);
                        barchart.setMinimumHeight(0);
                        barchart.invalidate();

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
        LinearLayout bt1 = (LinearLayout) findViewById(R.id.bt1) ;
        LinearLayout bt2 = (LinearLayout) findViewById(R.id.bt2) ;
        LinearLayout bt3 = (LinearLayout) findViewById(R.id.bt3) ;

        Button before = (Button) findViewById(R.id.beforebt);
        Button next = (Button) findViewById(R.id.nextbt);
        barchart = (BarChart) findViewById(R.id.barchart);

        txtTotalCal = (TextView) findViewById(R.id.txtTotalCal);
        txtTotalCarbo = (TextView) findViewById(R.id.txtTotalCarbo);
        txtTotalProtein = (TextView) findViewById(R.id.txtTotalProtein);
        txtTotalFat = (TextView) findViewById(R.id.txtTotalFat);

        timekcal1 = (TextView) findViewById(R.id.timekcal1);
        timekcal2 = (TextView) findViewById(R.id.timekcal2);
        timekcal3 = (TextView) findViewById(R.id.timekcal3);

        txtCarboPercent = (TextView) findViewById(R.id.txtCarboPercent);
        txtProteinPercent = (TextView) findViewById(R.id.txtProteinPercent);
        txtFatPercent = (TextView) findViewById(R.id.txtFatPercent);

        pgbCarbo = (ProgressBar) findViewById(R.id.pgbCarbo);
        pgbProtein = (ProgressBar) findViewById(R.id.pgbProtein);
        pgbFat = (ProgressBar) findViewById(R.id.pgbFat);

        txteat1 = (TextView) findViewById(R.id.txteat1);
        txteat2 = (TextView) findViewById(R.id.txteat2);
        txteat3 = (TextView) findViewById(R.id.txteat3);

        txtCar = (TextView) findViewById(R.id.txtCarbo);
        txtPro = (TextView) findViewById(R.id.txtProtein);
        txtFat = (TextView) findViewById(R.id.txtFat);

        txtCar1 = (TextView) findViewById(R.id.txtCarbo1);
        txtPro1 = (TextView) findViewById(R.id.txtProtein1);
        txtFat1 = (TextView) findViewById(R.id.txtFat1);

        pbCarboPro = (ProgressBar) findViewById(R.id.pbCarboPro);
        pbProtenPro = (ProgressBar) findViewById(R.id.pbProtenPro);
        pbFatPro = (ProgressBar) findViewById(R.id.pbFatPro);

        imgCar1 = (ImageView)findViewById(R.id.imgCar1);
        imgPro1 = (ImageView)findViewById(R.id.imgPro1);
        imgFat1 = (ImageView)findViewById(R.id.imgFat1);
        imgCar2 = (ImageView)findViewById(R.id.imgCar2);
        imgPro2 = (ImageView)findViewById(R.id.imgPro2);
        imgFat2 = (ImageView)findViewById(R.id.imgFat2);

        format = new SimpleDateFormat( "yyyy-MM-dd");
        date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        nowtime = format.format(cal.getTime());

        txtcal.setText(nowtime);

        format_char = new SimpleDateFormat( "yyyy-MM-dd");
        date_char = new Date();
        cal_char = Calendar.getInstance();
        cal_char.setTime(date_char);
        cal_time[0] = format_char.format(cal_char.getTime());

        for(int i=1; i<5; i++) {
            cal_char.add(Calendar.DATE, -1);
            cal_time[i] = format_char.format(cal_char.getTime());
        }


        format_char1 = new SimpleDateFormat( "MM/dd");
        date_char1 = new Date();
        cal_char1 = Calendar.getInstance();
        cal_char1.setTime(date_char);
        cal_time1[0] = format_char1.format(cal_char1.getTime());

        for(int i=1; i<5; i++) {
            cal_char1.add(Calendar.DATE, -1);
            cal_time1[i] = format_char1.format(cal_char1.getTime());
        }


        renew();

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, -1);
                nowtime = format.format(cal.getTime());
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(cal.getTime());
                txtcal.setText(nowtime);

                cal_time[0] = format_char.format(cal1.getTime());
                cal_time1[0] = format_char1.format(cal1.getTime());

                for(int i=1; i<5; i++) {
                    cal1.add(Calendar.DATE, -1);
                    cal_time1[i] = format_char1.format(cal1.getTime());
                }
                cal1.add(Calendar.DATE, 4);
                for(int i=1; i<5; i++) {
                    cal1.add(Calendar.DATE, -1);
                    cal_time[i] = format_char.format(cal1.getTime());
                }

                renew();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, 1);
                nowtime = format.format(cal.getTime());
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(cal.getTime());
                txtcal.setText(nowtime);

                cal_time[0] = format_char.format(cal1.getTime());
                cal_time1[0] = format_char1.format(cal1.getTime());

                for(int i=1; i<5; i++) {
                    cal1.add(Calendar.DATE, -1);
                    cal_time1[i] = format_char1.format(cal1.getTime());
                }
                cal1.add(Calendar.DATE, 4);
                for(int i=1; i<5; i++) {
                    cal1.add(Calendar.DATE, -1);
                    cal_time[i] = format_char.format(cal1.getTime());
                }

                renew();
            }
        });

        // Button calen = findViewById(R.id.txtCalendar);
        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpecRecord = tabHost.newTabSpec("RECORD").setIndicator("홈");
        tabSpecRecord.setContent(R.id.tabrecord);
        tabHost.addTab(tabSpecRecord);

        TabHost.TabSpec tabSpecStatistics = tabHost.newTabSpec("STATISTICS").setIndicator("통계");
        tabSpecStatistics.setContent(R.id.tabstatistics);


        tabHost.addTab(tabSpecStatistics);

        tabHost.setCurrentTab(0);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                intent.putExtra("eatTime", "아침");
                intent.putExtra("DATE", nowtime);
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                intent.putExtra("eatTime", "점심");
                intent.putExtra("DATE", nowtime);
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                intent.putExtra("eatTime", "저녁");
                intent.putExtra("DATE", nowtime);
                intent.putExtra("ID", ID);
                startActivity(intent);
            }
        });
    }
}
