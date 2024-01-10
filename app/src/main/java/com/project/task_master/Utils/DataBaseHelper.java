package com.project.task_master.Utils;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.project.task_master.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "TASKMASTER";
    private static final String TABLE_NAME = "TASKMASTER_DATA";
    private static final String COL1 = "ID";
    private static final String COL2 = "TASK_NAME";
    private static final String COL3 = "TASK_DESCRIPTION";
    private static final String COL4 = "STATUS";
    private static final String COL5 = "DUE_DATE";
    private static final String COL6 = "DUE_TIME";
    private static final String COL7 = "PRIORITY";
    private static final String COL8 = "CATEGORY";

    private static final String COL9 = "TASK_STATUS";



    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK_NAME TEXT , TASK_DESCRIPTION TEXT , " +
                "STATUS INTEGER , DUE_DATE DATE ,DUE_TIME TIME , PRIORITY TEXT , CATEGORY TEXT ,TASK_STATUS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public void insertTask(ToDoModel model)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL2, model.getTask_name());
        values.put(COL3, model.getTask_description());
        values.put(COL4,0);
        values.put(COL5, model.getDue_date().toString());
        values.put(COL6, model.getDue_time().toString());
        values.put(COL7,model.getPriority());
        values.put(COL8, model.getCategory());
        values.put(COL9,model.getTask_status());
        db.insert(TABLE_NAME,null,values);
    }

    public void updateTask(int id,String task_name ,String task_description , int status , String due_date, String due_time , String priority, String category ,String task_status )
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL2, task_name);
        values.put(COL3, task_description);
        values.put(COL4,status);
        values.put(COL5, due_date);
        values.put(COL6, due_time);
        values.put(COL7,priority);
        values.put(COL8, category);
        values.put(COL9,task_status);
        db.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});
    }
    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL4,status);
        db.update(TABLE_NAME,values,"ID=?" , new String[]{String.valueOf(id)});

    }

    public void deleteTask(int id)
    {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME , null , null , null , null , null , null);
            if (cursor !=null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL1)));
                        task.setTask_name(cursor.getString(cursor.getColumnIndex(COL2)));
                        task.setTask_description(cursor.getString(cursor.getColumnIndex(COL3)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL4)));
                        task.setDue_date(cursor.getString(cursor.getColumnIndex(COL5)));
                        task.setDue_time(cursor.getString(cursor.getColumnIndex(COL6)));
                        task.setPriority(cursor.getString(cursor.getColumnIndex(COL7)));
                        task.setCategory(cursor.getString(cursor.getColumnIndex(COL8)));
                        task.setTask_status(cursor.getString(cursor.getColumnIndex(COL9)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}

