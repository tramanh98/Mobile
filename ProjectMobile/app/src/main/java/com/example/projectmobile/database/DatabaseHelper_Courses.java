package com.example.projectmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper_Courses extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EVENTS_COURSES.db";
    private static final String DATABASE_TABLE = "LIST_COURSES";  // lấy danh mục các chủ đề của 1 môn học
    private static final String COL_0 = "ID";
    private static final String COL_1 = "ID_Course";
    private static final String COL_2 = "NAME_Course";
    public DatabaseHelper_Courses(Context context) {

        super(context, DATABASE_TABLE, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + COL_0 + " INTEGER PRIMARY KEY,"
                + COL_1 + " INTEGER," + COL_2 + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }


    public void addCourse(Integer id_course, String name_course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, id_course);
        values.put(COL_2, name_course);
        Log.d("id ",id_course.toString());
        db.insertWithOnConflict(DATABASE_TABLE,null,values,4);
        db.close();
    }
    public List<Integer> getAllCourses() {
        List<Integer> listcontacts = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                listcontacts.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listcontacts;
    }
}
