package com.example.projectmobile.database;

public class Course_deadline {
    private int id_course;
    private int id_event;
    private String name_event;
    private long time_start;

    public Course_deadline() {
    }

    public Course_deadline(int id_course, int id_event, String name_event, long time_start) {
        this.id_course = id_course;
        this.id_event = id_event;
        this.name_event = name_event;
        this.time_start = time_start;
    }

    public int getId_course() {
        return id_course;
    }
    public int getId_event() {
        return id_event;
    }
    public String getName_event() {
        return name_event;
    }
    public long getTime_Start() {
        return time_start;
    }
}
