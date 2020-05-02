package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context) {
        super(context, "foods", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE foods (Num INTEGER NOT NULL PRIMARY KEY,Category VARCHAR(20) NOT NULL,FoodName VARCHAR(50) NOT NULL,ServingSize FLOAT NOT NULL,Kcal FLOAT NOT NULL,Carbo FLOAT NOT NULL,Protein FLOAT NOT NULL,Fat FLOAT NOT NULL,Saccharide FLOAT NOT NULL,Natrium FLOAT NOT NULL );");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS foods");
        onCreate(db);
    }
}