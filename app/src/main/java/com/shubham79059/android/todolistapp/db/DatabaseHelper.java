package com.shubham79059.android.todolistapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shubham79059.android.todolistapp.db.entity.Work;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "work_db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(Work.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Work.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    // Insert Data into Database
    public long insertWork(String name, String desc){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Work.COLUMN_NAME, name);
        values.put(Work.COLUMN_DESC, desc);

        long id = db.insert(Work.TABLE_NAME, null, values);

        db.close();

        return id;

    }

    // Getting Work from Database
    public Work getWork(long id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Work.TABLE_NAME,
                new String[]{
                        Work.COLUMN_ID,
                        Work.COLUMN_NAME,
                        Work.COLUMN_DESC},
                Work.COLUMN_ID + " = ? ",
                new String[]{
                        String.valueOf(id)
                },
                null,
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();

        Work work = new Work(
                cursor.getString(cursor.getColumnIndexOrThrow(Work.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(Work.COLUMN_DESC)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Work.COLUMN_ID))
        );

        cursor.close();
        return work;

    }

    //Getting all Works
    public ArrayList<Work> getAllWorks(){
        ArrayList<Work> works = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Work.TABLE_NAME + " ORDER BY " +
                Work.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                Work work = new Work();
                work.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Work.COLUMN_ID)));
                work.setName(cursor.getString(cursor.getColumnIndexOrThrow(Work.COLUMN_NAME)));
                work.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(Work.COLUMN_DESC)));

                works.add(work);

            }while(cursor.moveToNext());
        }

        db.close();
        return works;

    }

    public int updateWork(Work work){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Work.COLUMN_NAME, work.getName());
        values.put(Work.COLUMN_DESC, work.getDesc());

        return db.update(Work.TABLE_NAME, values, Work.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(work.getId())});
    }

    public void deleteWork(Work work){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Work.TABLE_NAME, Work.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(work.getId())}
                );
        db.close();
    }

}
