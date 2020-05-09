package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

import java.net.URLDecoder;


public class myDBHelper extends SQLiteOpenHelper {
    Context con;
    public myDBHelper(Context context) {

        super(context, "foods", null, 1);
        con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS foods");
        db.execSQL("CREATE TABLE IF NOT EXISTS foods (Num INTEGER NOT NULL PRIMARY KEY,FoodName VARCHAR(25) NOT NULL,Category VARCHAR(10) NOT NULL, ServingSize FLOAT NOT NULL,Kcal FLOAT NOT NULL,Carbo FLOAT NOT NULL,Protein FLOAT NOT NULL,Fat FLOAT NOT NULL,Natrium FLOAT NOT NULL );");
        class NewRunnable implements Runnable {
            @Override
            public void run() {

                String str = SnoopyHttpConnection.makeConnection("https://busyhuman.pythonanywhere.com/foods/?format=json", "GET", null);


                try {
                    JSONArray jarray = new JSONArray(str);
                    int len = jarray.length();
                    for(int i=0;i<len;i++){
                        JSONObject jsonObj = jarray.getJSONObject(i);  // JSONObject 추출

                        System.out.println(jsonObj);
                        int t_Num = Integer.parseInt(jsonObj.getString("Num"));
                        String t_FoodName = jsonObj.getString("FoodName");
                        String t_Category = jsonObj.getString("Category");
                        float t_ServingSize = Float.parseFloat(jsonObj.getString("ServingSize"));
                        float t_Kcal = Float.parseFloat(jsonObj.getString("Kcal"));
                        float t_Carbo = Float.parseFloat(jsonObj.getString("Carbo"));
                        float t_Protein = Float.parseFloat(jsonObj.getString("Protein"));
                        float t_Fat = Float.parseFloat(jsonObj.getString("Fat"));
                        float t_Natrium = Float.parseFloat(jsonObj.getString("Natrium"));
                        try {
                            db.execSQL("INSERT INTO foods (Num, FoodName, Category, ServingSize, Kcal, Carbo, Protein, Fat, Natrium) VALUES ('" + t_Num + "', '" + t_FoodName + "', '" + t_Category + "','" + t_ServingSize + "','" + t_Kcal + "','" + t_Carbo + "','" + t_Protein + "','" + t_Fat + "','" + t_Natrium + "');");
                        } catch (SQLiteException e){
                            continue;
                        }
                    }
                } catch (JSONException e) {
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

     //   db.execSQL("INSERT INTO foods ( Num, FoodName, ServingSize, col4 ) SELECT 'col1Val','col2Val','col3Val','col4Val' FROM foods WHERE NOT EXISTS ( SELECT *  FROM `테이블명` WHERE  col1 =  'col1Val' AND  col2 =  'col2Val' AND col3 =  'col3Val');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }
}