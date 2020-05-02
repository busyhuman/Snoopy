package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupendActivity extends AppCompatActivity {

    String t_name, t_kcal, ID;
    TextView name, kcal;


    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_end);
        name = (TextView) findViewById(R.id.tv_name);
        kcal = (TextView) findViewById(R.id.tv_kcal);
        Button bt = (Button) findViewById(R.id.bt1);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");

        Handler mHandler = new Handler(Looper.getMainLooper());

        class NewRunnable implements Runnable {


                @Override
                public void run() {
                    String str = SnoopyHttpConnection.makeConnection("http://busyhuman.pythonanywhere.com/users/?format=json&ID="+ ID,
                            "GET", null);
                    System.out.println(str);
                    mHandler.postDelayed(new Runnable() { public void run() {
                        try {
                            JSONArray jarray = new JSONArray(str); // JSONArray 생성
                            JSONObject jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                            t_name = jsonObj.getString("UserName");
                            t_kcal = jsonObj.getString("UserKcal");
                            System.out.println(t_name + " " + t_kcal);
                            name.setText(URLDecoder.decode(t_name, "Euckr"));
                            kcal.setText(t_kcal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                    },0);
                }
        }

        NewRunnable nr = new NewRunnable() ;
        Thread t = new Thread(nr) ;
        t.start();



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    }

