package org.tensorflow.lite.examples.classification;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.classification.SnoopyConnection.SnoopyHttpConnection;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static androidx.core.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLICK;
import static org.tensorflow.lite.examples.classification.MainActivity.setting;
import static org.tensorflow.lite.examples.classification.Main_StatsActivity.setting1;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetActivity extends AppWidgetProvider {

    String ID;
    int setKcal, setCarbo, setPro, setFat, totalKcal;
    float totalCarbo, totalProtein, totalFat;

    int[] id;

    private static final String CLICK_ACTION = "org.tensorflow.lite.examples.classification.CLICK";




    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd");
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    String DATE;
    RemoteViews views;




    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        views = new RemoteViews(context.getPackageName(), R.layout.widget_activity);




                ID = setting.getString("ID", "");
                setKcal = setting1.getInt("setKcal", 0);
                setCarbo = setting1.getInt("setCarbo", 0);
                setPro = setting1.getInt("setPro", 0);
                setFat = setting1.getInt("setFat", 0);

                totalKcal = setting1.getInt("totalKcal", 0);
                totalCarbo = setting1.getFloat("totalCarbo", 0);
                totalProtein = setting1.getFloat("totalPro", 0);
               totalFat = setting1.getFloat("totalFat", 0);


                cal.setTime(date);
                DATE = format.format(cal.getTime());
                views.setTextViewText(R.id.today, DATE);
                views.setTextViewText(R.id.txtK1, String.valueOf(totalKcal) +"/"+String.valueOf(setKcal)+"kcal");
                views.setTextViewText(R.id.txtC, String.format("%.2f", totalCarbo) +"/"+String.valueOf(setCarbo)+"g");
                views.setTextViewText(R.id.txtP, String.format("%.2f", totalProtein) +"/"+String.valueOf(setPro)+"g");
                views.setTextViewText(R.id.txtF, String.format("%.2f", totalFat) +"/"+String.valueOf(setFat)+"g");

        if(totalCarbo == setCarbo){
            views.setImageViewResource(R.id.imgCar_1, R.drawable.green);
        }else if(totalCarbo < setCarbo){
            views.setImageViewResource(R.id.imgCar_1, R.drawable.orange);
        }else if(totalCarbo > setCarbo){
            views.setImageViewResource(R.id.imgCar_1, R.drawable.red);
        }

        if(totalProtein == setPro){
            views.setImageViewResource(R.id.imgPro_1, R.drawable.green);
        }else if(totalProtein < setPro){
            views.setImageViewResource(R.id.imgPro_1, R.drawable.orange);
        }else if(totalProtein > setPro){
            views.setImageViewResource(R.id.imgPro_1, R.drawable.red);
        }

        if(totalFat == setFat){
            views.setImageViewResource(R.id.imgFat_1, R.drawable.green);
        }else if(totalFat < setFat){
            views.setImageViewResource(R.id.imgFat_1, R.drawable.orange);
        }else if(totalFat > setFat){
            views.setImageViewResource(R.id.imgFat_1, R.drawable.red);
        }





        //앱 실행 버튼 클릭
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        intent1.setComponent(new ComponentName(context, MainActivity.class));
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
        views.setOnClickPendingIntent(R.id.record_btn, pendingIntent1);




        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}