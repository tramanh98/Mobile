package com.example.projectmobile.database;

public class Course_content {
    private int id_course;
    private int id_topic;
    private String name_topic;
    private int id_module;
    private String name_module;

    public Course_content() {
    }

    public Course_content(int id_course, int id_topic, String name_topic, int id_module, String name_module) {
        this.id_course = id_course;
        this.id_topic = id_topic;
        this.name_topic = name_topic;
        this.id_module = id_module;
        this.name_module = name_module;
    }

    public int getId_course() {
        return id_course;
    }
    public int getId_topic() {
        return id_topic;
    }
    public int getIdmodule() {
        return id_module;
    }
    public String getName_topic() {
        return name_topic;
    }
    public String getName_module() {
        return name_module;
    }

//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
