package com.freechetwithyounme.chettingapp.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="BIRTH_DATE_DATABASE";
    private static final String DATABASE_TABLE="BIRTH_DATE";
    private static final String id="ID";
    private static final String birth_day="birth_day";

    public SQLiteDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create=("CREATE TABLE " +DATABASE_TABLE+"(" +id+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +birth_day+ " TEXT "+")");
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void SaveDate(String DBbirth_day){
        SQLiteDatabase sqLiteDatabase= getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(birth_day, DBbirth_day);
        sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public Cursor DisplayData(){
        SQLiteDatabase sqLiteDatabase= getReadableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM "+DATABASE_TABLE, null);
        return cursor;
    }
}
