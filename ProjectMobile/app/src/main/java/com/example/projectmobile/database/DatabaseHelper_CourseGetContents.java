package com.example.projectmobile.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper_CourseGetContents extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EVENTS_COURSES.db";
    private static final String DATABASE_TABLE = "core_course_get_contents";  // lấy danh mục các chủ đề của 1 môn học
    private static final String COL_1 = "ID_Course";
    private static final String COL_2 = "ID_Topic";
    private static final String COL_3 = "NAME_Topic";
    private static final String COL_4 = "ID_Module";
    private static final String COL_5 = "NAME_Module";



    public DatabaseHelper_CourseGetContents(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + COL_1 + " INTEGER," + COL_2 + " INTERGER,"
                + COL_3 + " TEXT," + COL_4 + " INTERGER," + COL_5 + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addContent(Course_content content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, content.getId_course());
        values.put(COL_2, content.getId_topic());
        values.put(COL_3, content.getName_topic());
        values.put(COL_4, content.getIdmodule());
        values.put(COL_5, content.getName_module());
        //Neu de null thi khi value bang null thi loi

        db.insert(DATABASE_TABLE,null,values);
        db.close();
    }
}
