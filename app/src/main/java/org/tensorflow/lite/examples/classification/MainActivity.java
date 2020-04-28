package org.tensorflow.lite.examples.classification;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

public class MainActivity extends AppCompatActivity {

    Button logbt, signbt;
    EditText id, pw;
    String id_str = "";
    String pw_str = "";
    String j_id, j_pw;
    TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logbt = (Button) findViewById(R.id.login_bt);
        signbt = (Button) findViewById(R.id.sign_up_bt);
        err = (TextView) findViewById(R.id.errtxt);

        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }


        logbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler mHandler = new Handler(Looper.getMainLooper());

                id_str = id.getText().toString();
                pw_str = pw.getText().toString();

                class NewRunnable implements Runnable {
                    @Override
                    public void run() {
                        String str = SnoopyHttpConnection.makeConnection("http://busyhuman.pythonanywhere.com/users/?format=json&ID=" + id_str,
                                "GET", null);
                        System.out.println(str);
                        mHandler.postDelayed(new Runnable() { public void run() {
                            try {
                                JSONArray jarray = new JSONArray(str); // JSONArray 생성
                                JSONObject jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                                j_id = jsonObj.getString("ID");
                                j_pw = jsonObj.getString("PW");
                                System.out.println(j_id  + " " + j_pw);

                                if(id_str.equals(j_id) && pw_str.equals(j_pw))
                                {
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
                        },0);
                    }
                }

                NewRunnable nr = new NewRunnable() ;
                Thread t = new Thread(nr) ;
                t.start();


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