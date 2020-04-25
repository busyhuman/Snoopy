package org.tensorflow.lite.examples.classification;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button logbt, signbt;
    EditText id, pw;
    String id_str = "";
    String pw_str = "";
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

                id_str = id.getText().toString();
                pw_str = pw.getText().toString();

                if(id_str.equals("admin") && pw_str.equals("admin"))
                {
                    Intent intent = new Intent(getApplicationContext(), Main_StatsActivity.class);
                    startActivity(intent);
                } else {
                    err.setVisibility(View.VISIBLE);
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