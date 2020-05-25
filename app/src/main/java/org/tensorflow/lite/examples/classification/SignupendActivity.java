package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.content.SharedPreferences;
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

import static org.tensorflow.lite.examples.classification.MainActivity.editor;


public class SignupendActivity extends AppCompatActivity {

    String t_name, t_kcal, ID, PW;
    TextView name, kcal;
    String str;
    JSONArray userArray;
    JSONObject userObj;

    private void renew() {

        class NewRunnable implements Runnable {
            @Override
            public void run() {

                str = SnoopyHttpConnection.makeConnection("https://khd8593.pythonanywhere.com/users/?format=json&ID="+ ID,
                        "GET", null);


                try{
                    userArray = new JSONArray(str);
                    userObj = userArray.getJSONObject(0);
                    t_name = userObj.getString("UserName");
                    t_kcal = userObj.getString("UserKcal");

                } catch (JSONException e){
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

            name.setText(t_name);
            kcal.setText(t_kcal);

    }


    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_end);
        name = (TextView) findViewById(R.id.tv_name);
        kcal = (TextView) findViewById(R.id.tv_kcal);
        Button bt = (Button) findViewById(R.id.bt1);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        PW = intent.getStringExtra("PW");


        renew();


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                editor.putString("ID", "");
                editor.putString("PW", "");
                editor.putInt("chk", 0);

                editor.commit();

                intent.putExtra("ID", ID);

                startActivity(intent);
            }
        });
    }
}

