package com.example.projectmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper_GetDeadline extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EVENTS_COURSES.db";
    private static final String DATABASE_TABLE = "DEADLINE";  // lấy danh mục các chủ đề của 1 môn học
    private static final String COL_0 = "ID";
    private static final String COL_1 = "ID_Course";  // buộc phải có
    private static final String COL_2 = "ID_Event";
    private static final String COL_3 = "NAME_Event";
    private static final String COL_4 = "TIME_Start";



    public DatabaseHelper_GetDeadline(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + COL_0 + " INTERGER PRIMARY KEY,"
                + COL_1 + " INTEGER," + COL_2 + " INTERGER,"
                + COL_3 + " TEXT," + COL_4 + " LONG" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addDeadline(Course_deadline deadline) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, deadline.getId_course());
        values.put(COL_2, deadline.getId_event());
        values.put(COL_3, deadline.getName_event());
        values.put(COL_4, deadline.getTime_Start());
        //Neu de null thi khi value bang null thi loi
        db.insertWithOnConflict(DATABASE_TABLE,null,values,4);
        db.close();
    }
    public boolean isOpenDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.isOpen();
    }
    public List<Long> getDeadline() {
        List<Long> listTIME = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                listTIME.add(cursor.getLong(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listTIME;
    }

}
