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
    private static final String DATABASE_NAME = "STUDENT_COURSE";
    private static final String DATABASE_TABLE1 = "LIST_COURSES";  // lấy danh mục các chủ đề của 1 môn học
    private static final String DATABASE_TABLE2 = "DEADLINE_COURSE";
    private static final String COL_0 = "ID";
    private static final String COL_1 = "ID_Course";
    private static final String COL_2 = "NAME_Course";

    private static final String COL_00 = "ID";  // buộc phải có
    private static final String COL_11 = "ID_COURSE";
    private static final String COL_22 = "ID_Event";
    private static final String COL_3 = "NAME_Event";
    private static final String COL_4 = "TIME_Start";
    public DatabaseHelper_Courses(Context context) {

        super(context, DATABASE_NAME, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSES_TABLE = "CREATE TABLE " + DATABASE_TABLE1 + "("
                + COL_0 + " INTEGER PRIMARY KEY,"
                + COL_1 + " INTEGER," + COL_2 + " TEXT" + ")";

        String CREATE_DEADLINE_TABLE = "CREATE TABLE " + DATABASE_TABLE2 + "("
                + COL_00 + " INTEGER PRIMARY KEY,"
                + COL_11 + " INTEGER,"
                + COL_22 + " INTEGER,"
                + COL_3 + " TEXT," + COL_4 + " LONG" + ")";
        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_DEADLINE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
        onCreate(db);
    }

    public void addCourse(Integer id_course, String name_course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, id_course);
        values.put(COL_2, name_course);
        Log.d("id ",id_course.toString());
        db.insert(DATABASE_TABLE1,null,values);
        db.close();
    }

    public void addDeadline(int id_course, int id_event, String name, Long timeStart){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_11, id_course);
        values.put(COL_22, id_event);
        values.put(COL_3, name);
        values.put(COL_4, timeStart);
        db.insert(DATABASE_TABLE2,null,values);
        db.close();
    }
    public List<Integer> getAllCourses() {
        List<Integer> listcontacts = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE1;

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
    public List<Courses> getListCourses() {
        List<Courses> listcourses = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                Courses cs = new Courses(cursor.getInt(1), cursor.getString(2));
                listcourses.add(cs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listcourses;
    }

    public List<Long> getDeadline() {
        List<Long> listTIME = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE2;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                listTIME.add(cursor.getLong(4));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listTIME;
    }
    public List<Course_deadline> getTableDeadline() {
        List<Course_deadline> listTIME = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE2;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                Course_deadline cdl = new Course_deadline(cursor.getInt(1), cursor.getInt(2), cursor.getString(3),cursor.getLong(4));
                listTIME.add(cdl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listTIME;
    }
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE1,null,null);
        db.delete(DATABASE_TABLE2,null,null);
        db.close();
    }
}
