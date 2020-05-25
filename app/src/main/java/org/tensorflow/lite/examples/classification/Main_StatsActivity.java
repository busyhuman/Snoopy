package org.tensorflow.lite.examples.classification;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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

import static org.tensorflow.lite.examples.classification.MainActivity.editor;
import static org.tensorflow.lite.examples.classification.MainActivity.setting;

public class Main_StatsActivity extends AppCompatActivity {

    Date date, date_char, date_char1, today;
    int chk = 0;
    int update_chk = 0;
    SimpleDateFormat format, format_char, format_char1;
    String nowtime, todaytime;
    int eatTime;
    String[] cal_time = new String[5];
    String[] cal_time1 = new String[5];
    BarChart barchart;
    String ID;
    TextView txtTotalCal, txtTotalCarbo, txtTotalProtein, txtTotalFat, txtCarboPercent, txtProteinPercent, txtFatPercent;
    TextView txtCar, txtPro, txtFat, txtCar1, txtPro1, txtFat1;
    TextView timekcal1, timekcal2, timekcal3, txteat1, txteat2, txteat3;
    Button fd_update, logout;

    float[] Serving = new float[3];
    float[] mykcal = new float[3];
    float[] dateKcal = new float[5];
    String[] stat = new String[5];

    String[] f_sate = new String[3];

    String[] FoodName = new String[3];


    TextView text;
    View layout;
    LayoutInflater inflater;

    int[] record = new int[3];

    TextView txtMsg;



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
    int Protein, Fat, Carbo;

    ImageView imgCar1, imgPro1, imgFat1, imgCar2, imgPro2, imgFat2;

    static SharedPreferences setting1;
    static SharedPreferences.Editor editor1;



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

                for(int i=0;i<5;i++){
                    dateKcal[i] = 0;
                }

                for(int i=0; i<5; i++){
                    stat[i] = SnoopyHttpConnection.makeConnection("https://khd8593.pythonanywhere.com/stats/?format=json&Date=" + cal_time[i] + "&user=" + ID,
                            "GET", null);
                }
                String user = SnoopyHttpConnection.makeConnection("https://khd8593.pythonanywhere.com/users/?format=json&ID=" + ID,
                        "GET", null);
                for(int i=0; i<3; i++) {
                    f_sate[i] = SnoopyHttpConnection.makeConnection("https://khd8593.pythonanywhere.com/stats/?format=json&Date=" + cal_time[0] + "&user=" + ID + "&Timeslot=" + String.valueOf(i),
                            "GET", null);
                }



                try {
                    JSONArray statArray = new JSONArray(stat[0]);
                    int len = statArray.length();


                    try{

                        mykcal[0] = 0;
                        mykcal[1] = 0;
                        mykcal[2] = 0;
                        totalKcal = 0;
                        totalCarbo = 0;
                        totalProtein = 0;
                        totalFat = 0;



                        for(int i=0;i<len;i++) {

                            JSONObject statObj = statArray.getJSONObject(i);
                            totalKcal += Float.parseFloat(statObj.getString("Kcal"));
                            totalCarbo += Float.parseFloat(statObj.getString("Carbo"));
                            totalProtein += Float.parseFloat(statObj.getString("Protein"));
                            totalFat += Float.parseFloat(statObj.getString("Fat"));


                            if (statObj.getString("Timeslot").equals("0")) {
                                mykcal[0] += Float.parseFloat(statObj.getString("Kcal"));
                            } else if (statObj.getString("Timeslot").equals("1")) {
                                mykcal[1] += Float.parseFloat(statObj.getString("Kcal"));
                            } else if (statObj.getString("Timeslot").equals("2")) {
                                mykcal[2] += Float.parseFloat(statObj.getString("Kcal"));
                            }
                        }




                    } catch (JSONException e){

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
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ArithmeticException e){
                    e.printStackTrace();
                }


            }
        }
        Thread t = new Thread(new NewRunnable());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        float userlb = userWei *2.2f;
        Protein = (int)(userlb*0.9f);
        Fat = (int)(userlb*0.4f);
        Carbo = (int)((userKcal-(Protein*4 + Fat*9))/4);


        txtCarboPercent.setText("" + carboPercnt +"%");
        txtProteinPercent.setText(""  + proteinPercent +"%");
        txtFatPercent.setText("" + fatPercent +"%");

        txtCar1.setText("" + carboPercnt +"%");
        txtPro1.setText(""  + proteinPercent +"%");
        txtFat1.setText("" + fatPercent +"%");

        txteat1.setText(String.valueOf((int)mykcal[0]) + "kcal");
        txteat2.setText(String.valueOf((int)mykcal[1]) + "kcal");
        txteat3.setText(String.valueOf((int)mykcal[2]) + "kcal");

        timekcal1.setText(String.valueOf((int)mykcal[0]) + "kcal");
        timekcal2.setText(String.valueOf((int)mykcal[1]) + "kcal");
        timekcal3.setText(String.valueOf((int)mykcal[2]) + "kcal");

        pgbCarbo.setProgress(carboPercnt);
        pgbProtein.setProgress(proteinPercent);
        pgbFat.setProgress(fatPercent);

        pbCarboPro.setProgress(carboPercnt);
        pbProtenPro.setProgress(proteinPercent);
        pbFatPro.setProgress(fatPercent);


        txtTotalCal.setText((int)totalKcal + " / " + (int)userKcal + " kcal");
        txtTotalCarbo.setText( String.format("%.2f", totalCarbo) + " / " + Carbo + "g" );
        txtTotalProtein.setText(String.format("%.2f", totalProtein) + " / " + Protein + "g");
        txtTotalFat.setText(String.format("%.2f", totalFat) + " / " + Fat + "g");

        txtCar.setText(String.format("%.2f", totalCarbo) + " / " + Carbo + "g" );
        txtPro.setText(String.format("%.2f", totalProtein) + " / " + Protein + "g");
        txtFat.setText(String.format("%.2f", totalFat) + " / " + Fat + "g");


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

        editor1.putInt("setKcal", (int)userKcal);
        editor1.putInt("setCarbo", Carbo);
        editor1.putInt("setPro", Protein);
        editor1.putInt("setFat", Fat);

        editor1.putInt("totalKcal", (int)totalKcal);
        editor1.putFloat("totalCarbo", totalCarbo);
        editor1.putFloat("totalPro", totalProtein);
        editor1.putFloat("totalFat", totalFat);

        editor1.commit();



    }

    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 하시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_stats);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");

        setting1 = getSharedPreferences("setting1", 0);
        editor1= setting1.edit();

        TextView txtcal = (TextView) findViewById(R.id.txtCalendar);
        LinearLayout bt1 = (LinearLayout) findViewById(R.id.bt1) ;
        LinearLayout bt2 = (LinearLayout) findViewById(R.id.bt2) ;
        LinearLayout bt3 = (LinearLayout) findViewById(R.id.bt3) ;

        fd_update = (Button) findViewById(R.id.fd_update1);

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

        logout = (Button)findViewById(R.id.logout);

        format = new SimpleDateFormat( "yyyy-MM-dd");
        date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        nowtime = format.format(cal.getTime());

        today = new Date();
        Calendar cal_today = Calendar.getInstance();
        cal_today.setTime(today);
        todaytime = format.format(cal_today.getTime());


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

        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_border, (ViewGroup)findViewById(R.id.toast_layout_root));
        text = (TextView) layout.findViewById(R.id.text);

        for(int i=1; i<5; i++) {
            cal_char1.add(Calendar.DATE, -1);
            cal_time1[i] = format_char1.format(cal_char1.getTime());
        }

        mykcal[0] = 0;
        mykcal[1] = 0;
        mykcal[2] = 0;
        totalKcal = 0;
        totalCarbo = 0;
        totalProtein = 0;
        totalFat = 0;
        for(int i=0; i<3; i++) {
            Serving[i] = 1;
        }


        renew();


        chk = setting.getInt("chk", 0);
        update_chk = setting1.getInt("update_chk",0);


        if(update_chk == 0) {
            text.setText("첫 사용자는 하단의 음식 업데이트 버튼을 눌러 주세요");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 550);
            toast.setView(layout);
            toast.show();
        }



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("ID", "");
                editor.putString("PW", "");

                editor1.putInt("setKcal", 0);
                editor1.putInt("setCarbo", 0);
                editor1.putInt("setPro", 0);
                editor1.putInt("setFat", 0);

                editor1.putInt("totalKcal", 0);
                editor1.putFloat("totalCarbo", 0);
                editor1.putFloat("totalPro", 0);
                editor1.putFloat("totalFat", 0);

                editor1.commit();
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }
        });

        fd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor1.putInt("update_chk", 1);
                editor1.commit();

                myDBHelper dbHelper1 = new myDBHelper(Main_StatsActivity.this);
                DAO.myDB = dbHelper1.getWritableDatabase();
                //dbHelper1.onCreate(DAO.myDB);
                dbHelper1.fiilFoodTable(DAO.myDB);

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_border, (ViewGroup)findViewById(R.id.toast_layout_root));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("음식 업데이트 완료");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 550);
                toast.setView(layout);
                toast.show();

                update_chk = setting1.getInt("update_chk",0);
            }
        });

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
                if(!nowtime.equals(todaytime)) {
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

        FoodName[0] = "음식 없음";
        FoodName[1] = "음식 없음";
        FoodName[2] = "음식 없음";

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(update_chk == 0){
                    text.setText("음식 업데이트를 해 주세요.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 550);
                    toast.setView(layout);
                    toast.show();
                } else if(update_chk == 1) {
                    if(mykcal[0]!=0) {
                        Intent intent = new Intent(getApplicationContext(), ImgRecordActivity.class);
                        intent.putExtra("eatTime", 0);
                        intent.putExtra("DATE", nowtime);

                        editor1.putInt("record", 1);
                        editor1.commit();

                        intent.putExtra("FoodName", FoodName);
                        intent.putExtra("Serving", Serving);
                        intent.putExtra("ID", ID);
                        startActivity(intent);
                    } else if(mykcal[0] == 0) {
                        Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                        intent.putExtra("eatTime", 0);

                        editor1.putInt("record", 0);
                        editor1.commit();

                        intent.putExtra("DATE", nowtime);
                        intent.putExtra("ID", ID);
                        startActivity(intent);
                    }
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(update_chk == 0){
                    text.setText("음식 업데이트를 해 주세요.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 550);
                    toast.setView(layout);
                    toast.show();
                } else if(update_chk == 1) {
                    if(mykcal[1]!=0) {
                        Intent intent = new Intent(getApplicationContext(), ImgRecordActivity.class);
                        intent.putExtra("eatTime", 1);
                        intent.putExtra("DATE", nowtime);

                        editor1.putInt("record", 1);
                        editor1.commit();

                        intent.putExtra("FoodName", FoodName);
                        intent.putExtra("Serving", Serving);
                        intent.putExtra("ID", ID);
                        startActivity(intent);
                    } else if(mykcal[1] == 0) {
                        Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                        intent.putExtra("eatTime", 1);
                        intent.putExtra("DATE", nowtime);
                        intent.putExtra("ID", ID);

                        editor1.putInt("record", 0);
                        editor1.commit();

                        startActivity(intent);
                    }
                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(update_chk == 0){
                    text.setText("음식 업데이트를 해 주세요.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 550);
                    toast.setView(layout);
                    toast.show();
                } else if(update_chk == 1) {
                    if(mykcal[2]!=0) {
                        Intent intent = new Intent(getApplicationContext(), ImgRecordActivity.class);
                        intent.putExtra("eatTime", 2);
                        intent.putExtra("DATE", nowtime);

                        editor1.putInt("record", 1);
                        editor1.commit();

                        intent.putExtra("FoodName", FoodName);
                        intent.putExtra("Serving", Serving);
                        intent.putExtra("ID", ID);
                        startActivity(intent);
                    } else if(mykcal[2] == 0) {
                        Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                        intent.putExtra("eatTime", 2);
                        intent.putExtra("DATE", nowtime);
                        intent.putExtra("ID", ID);

                        editor1.putInt("record", 0);
                        editor1.commit();

                        startActivity(intent);
                    }
                }
            }
        });
    }
}
