package com.example.projectmobile.database;

public class Courses {
    private int id_course;
    private String name_course;

    public Courses() {
    }

    public Courses(int id_course, String name_course) {
        this.id_course = id_course;
        this.name_course = name_course;
    }

    public int getId_course() {
        return id_course;
    }
    public String getName_course() {
        return name_course;
    }
}
