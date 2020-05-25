package org.tensorflow.lite.examples.classification;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;
import org.tensorflow.lite.examples.classification.DAO;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button logbt, signbt;
    EditText id, pw;
    String id_str = "";
    String pw_str = "";
    String j_id, j_pw;
    TextView err;
    int login_chk = 0;

    int status;

    JSONArray jarray;
    JSONObject jsonObj;


    static SharedPreferences setting;
    static SharedPreferences.Editor editor;


    protected InputFilter filter= new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,

                                   Spanned dest, int dstart, int dend) {



            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches()) {

                return "";

            }
            return null;
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logbt = (Button) findViewById(R.id.login_bt);
        signbt = (Button) findViewById(R.id.sign_up_bt);
        err = (TextView) findViewById(R.id.errtxt);

        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);

        id.setFilters(new InputFilter[] {filter});
        pw.setFilters(new InputFilter[] {filter});




        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();


        id_str = setting.getString("ID", "");
        pw_str = setting.getString("PW", "");



        status = NetworkStatus.getConnectivityStatus(getApplicationContext());

        if(status == NetworkStatus.TYPE_NOT_CONNECTED){
            Toast.makeText(getApplicationContext(), "인터넷을 연결해 주세요.", Toast.LENGTH_LONG).show();
        }else if(status != NetworkStatus.TYPE_NOT_CONNECTED){
            if(!id_str.equals("") && !pw_str.equals("")) {
                Intent intent = new Intent(getApplicationContext(), Main_StatsActivity.class);
                intent.putExtra("ID", id_str);
                startActivity(intent);
            }
        }








        logbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = NetworkStatus.getConnectivityStatus(getApplicationContext());

                Handler mHandler = new Handler(Looper.getMainLooper());

                id_str = id.getText().toString();
                pw_str = pw.getText().toString();




                class NewRunnable implements Runnable {
                    @Override
                    public void run() {
                        String str = SnoopyHttpConnection.makeConnection("https://khd8593.pythonanywhere.com/users/?format=json&ID=" + id_str,
                                "GET", null);

                        mHandler.postDelayed(new Runnable() { public void run() {
                             if(id_str.equals("") || pw_str.equals("")){err.setVisibility(View.VISIBLE);}
                              else if(str.equals("[] ")){err.setVisibility(View.VISIBLE);}
                             else if(!str.equals("")) {
                                 try {
                                     jarray = new JSONArray(str); // JSONArray 생성
                                     jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                                     j_id = jsonObj.getString("ID");
                                     j_pw = jsonObj.getString("PW");

                                     if(id_str.equals(j_id ) && pw_str.equals(j_pw)) {

                                         editor.putString("ID", j_id);
                                         editor.putString("PW", j_pw);
                                         editor.putInt("chk", 1);

                                         editor.commit();

                                         Intent intent = new Intent(getApplicationContext(), Main_StatsActivity.class);
                                         intent.putExtra("ID", j_id);
                                         startActivity(intent);
                                     } else {
                                         err.setVisibility(View.VISIBLE);
                                     }

                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }

                        }
                        },0);
                    }
                }

                if(status == NetworkStatus.TYPE_NOT_CONNECTED){
                    Toast.makeText(getApplicationContext(), "인터넷을 연결해 주세요.", Toast.LENGTH_LONG).show();
                }else if(status != NetworkStatus.TYPE_NOT_CONNECTED){
                    Thread t = new Thread(new NewRunnable());
                    t.start();
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        signbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}