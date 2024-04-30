package com.shubham79059.android.todolistapp.db.entity;

public class Work {

    // 1- Constants for Database
    public static final String TABLE_NAME = "works";
    public static final String COLUMN_ID = "work_id";
    public static final String COLUMN_NAME = "work_name";
    public static final String COLUMN_DESC = "work_desc";
    // 2 - Variables
    private String name;
    private String desc;
    private int id;

    // 3 - Constructor
    public Work(){

    }

    public Work(String name, String desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
    }

    // 4 - Getters band Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // SQL Query: Creating the Table
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_DESC + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

}
