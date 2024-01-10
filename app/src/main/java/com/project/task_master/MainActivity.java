package com.project.task_master;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.task_master.Adapter.ToDoAdapter;
import com.project.task_master.Model.ToDoModel;
import com.project.task_master.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    public static TextView m_textView;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;

    private List<ToDoModel> mList;
    private ToDoAdapter toDoAdapter;
    private DataBaseHelper myDB;
    public static ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.img_view);
        mRecyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab_todo);
        m_textView=findViewById(R.id.text_view);
        myDB = new DataBaseHelper(MainActivity.this);

        mList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(myDB,MainActivity.this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(toDoAdapter);

        mList =  myDB.getAllTasks();
        Collections.reverse(mList);
        toDoAdapter.setTasks(mList);

        if(mList.size()>0) {
            imageView.setVisibility(View.INVISIBLE);
            m_textView.setVisibility(View.INVISIBLE);
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            m_textView.setVisibility(View.VISIBLE);

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mList =  myDB.getAllTasks();
        Collections.reverse(mList);
        toDoAdapter.setTasks(mList);
        toDoAdapter.notifyDataSetChanged();
        if(mList.size()!=0) {
            imageView.setVisibility(View.INVISIBLE);
            m_textView.setVisibility(View.INVISIBLE);
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            m_textView.setVisibility(View.VISIBLE);
        }

    }


}