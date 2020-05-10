package org.tensorflow.lite.examples.classification;

import org.json.JSONArray;
import org.json.JSONException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    Spinner spinner;
    Button bt1, bt2;
    View dialogView;
    EditText edtID, edtPW, edtPW2, edtName, edtAge, edtH, edtW;
    RadioGroup radioGr;
    int id_check=0;
    String id, id2, pw, pw2, name, age, hei, wei, sex, act;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        final String[] num = {"25", "30", "35", "40"};

        spinner = (Spinner) findViewById(R.id.spinner);
        ImageView img = (ImageView) findViewById(R.id.img);
        Button bt1 = (Button) findViewById(R.id.bt1);
        Button bt2 = (Button) findViewById(R.id.bt2);
        ImageView back = (ImageView) findViewById(R.id.back);

        radioGr = (RadioGroup) findViewById(R.id.radioGr);

        edtID = (EditText) findViewById(R.id.edtID);
        edtPW = (EditText) findViewById(R.id.edtPW);
        edtPW2 = (EditText) findViewById(R.id.edtPW2);
        edtName = (EditText) findViewById(R.id.edtName);
        edtAge = (EditText) findViewById(R.id.edtAge);
        edtH = (EditText) findViewById(R.id.edtH);
        edtW = (EditText) findViewById(R.id.edtW);




        dialogView = (View)View.inflate(SignupActivity.this, R.layout.activity_index, null);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, num);
        spinner.setAdapter(adapter);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setView(dialogView);
                alert.show();
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler mHandler = new Handler(Looper.getMainLooper());


                id = edtID.getText().toString();


                class NewRunnable implements Runnable {
                    @Override
                    public void run() {
                        id = edtID.getText().toString();

                        if(!id.equals("")){
                            String str = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/users/?format=json&ID=" + id,
                                    "GET", null);
                            System.out.println("회원가입: "+ str);
                            try {
                                JSONArray jarray = new JSONArray(str); // JSONArray 생성
                                JSONObject jsonObj = jarray.getJSONObject(0);  // JSONObject 추출
                                id2 = jsonObj.getString("ID");
                                System.out.println(id2);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(id.equals(id2)) {
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();     //닫기
                                            }
                                        });
                                        alert.setMessage("중복된 아이디입니다..");
                                        alert.show();
                                        id_check = 0;
                                    }
                                }, 0);
                                id_check = 1;
                            } else if(!id.equals(id2)){
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();     //닫기
                                            }
                                        });
                                        alert.setMessage("사용 가능한 아이디입니다.");
                                        alert.show();
                                        id_check = 1;
                                    }
                                }, 0);
                            }
                        } else if(id.equals("")){
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();     //닫기
                                        }
                                    });
                                    alert.setMessage("아이디를 입력해 주세요.");
                                    alert.show();
                                    id_check = 0;
                                }
                            }, 0);
                        }
                    }
                }


                NewRunnable nr = new NewRunnable() ;
                Thread t = new Thread(nr) ;
                t.start();

            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                id = edtID.getText().toString();
                pw = edtPW.getText().toString();
                pw2 = edtPW2.getText().toString();
                name = edtName.getText().toString();
                age = edtAge.getText().toString();
                hei = edtH.getText().toString();
                wei = edtW.getText().toString();

                act = spinner.getSelectedItem().toString();

                int rad_id = radioGr.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(rad_id);
                sex = rb.getText().toString();

                class NewRunnable implements Runnable {
                    @Override
                    public void run() {
                        try{
                            float kcal = ((Float.parseFloat(hei) - 100.0f ) * 0.9f) * Float.parseFloat(act);
                            String post = "ID="+id+"&PW="+pw+"&UserName="+URLEncoder.encode(name, "UTF-8")+"&Age="+age+"&Sex="+URLEncoder.encode(sex, "UTF-8")+"&Height="+hei+"&Weight="+wei+"&ActivityIndex="+act + "&UserKcal="+String.valueOf(kcal);
                            System.out.println("회원가입 성공: "+SnoopyHttpConnection.makeConnection("http://busyhuman.pythonanywhere.com/users/?format=json",
                                    "POST", post));
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                }


                if(id_check != 1) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("중복확인을 해 주세요.");
                    alert.show();
                }else if(!pw.equals(pw2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("패스워드 확인 값이 틀렸습니다.");
                    alert.show();
                } else if( id.equals("") || pw.equals("") || pw2.equals("") || name.equals("") || age.equals("") ||
                        hei.equals("") || wei.equals("") || sex.equals("") || act.equals("")){

                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("입력값을 확인해 주세요.");
                    alert.show();
                } else{

                    NewRunnable nr = new NewRunnable() ;
                    Thread t = new Thread(nr) ;
                    t.start() ;
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), SignupendActivity.class);
                    intent.putExtra("ID",id);
                    startActivity(intent);

                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

}