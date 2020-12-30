package com.example.four.SqliteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MemberInfo extends SQLiteOpenHelper {


    final static  String TAG = "MemberInfo";

    public MemberInfo(@Nullable Context context) {
        super(context, "memberInfo.db", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreate()");
        String query = "CREATE TABLE MEMBER(id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, userAddr TEXT, userTel TEXT)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "upgrade()");
        String query = "DROP TABLE IF EXISTS member";
        db.execSQL(query);
        onCreate(db);
    }
}
