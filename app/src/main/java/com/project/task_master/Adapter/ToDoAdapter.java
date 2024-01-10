package com.project.task_master.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.task_master.AddTask;
import com.project.task_master.MainActivity;
import com.project.task_master.Model.ToDoModel;
import com.project.task_master.R;
import com.project.task_master.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB, MainActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel item = mList.get(position);

        if (item.getTask_description().trim().length() > 0) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(item.getTask_description());
        } else {
            holder.description.setVisibility(View.GONE);
        }
        holder.date.setText(item.getDue_date());
        holder.time.setText(item.getDue_time());
        holder.category.setText(item.getCategory());
        holder.priority.setText(item.getPriority());
        holder.task_status.setText(item.getTask_status());
        holder.mCheckBox.setText(item.getTask_name());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        if (toBoolean(item.getStatus())) {
            int color = Color.parseColor("#D1FFBD");
            holder.cv.setCardBackgroundColor(color);
        } else {
            int color = Color.parseColor("#ffffff");
            holder.cv.setCardBackgroundColor(color);
        }

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myDB.updateStatus(item.getId(), 1);
                    int color = Color.parseColor("#D1FFBD"); // Example: Parse color from hex code
                    holder.cv.setCardBackgroundColor(color);
                } else {
                    myDB.updateStatus(item.getId(), 0);
                    int color = Color.parseColor("#ffffff"); // Example: Parse color from hex code
                    holder.cv.setCardBackgroundColor(color);
                }
            }
        });
    }

    public void setTasks(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        if (mList.size() > 0) {
            MainActivity.imageView.setVisibility(View.INVISIBLE);
            MainActivity.m_textView.setVisibility(View.INVISIBLE);
        } else {
            MainActivity.imageView.setVisibility(View.VISIBLE);
            MainActivity.m_textView.setVisibility(View.VISIBLE);
        }
        notifyItemRemoved(position);

    }

    public void editItem(int position) {
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task_name", item.getTask_name());
        bundle.putInt("status", item.getStatus());
        bundle.putString("task_description", item.getTask_description());
        bundle.putString("due_date", item.getDue_date());
        bundle.putString("due_time", item.getDue_time());
        bundle.putString("priority", item.getPriority());
        bundle.putString("category", item.getCategory());
        bundle.putString("task_status", item.getTask_status());

        Intent intent = new Intent(activity, AddTask.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);

    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return activity;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;

        CardView cv;
        TextView description, date, time, priority, category, task_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            priority = itemView.findViewById(R.id.priority);
            category = itemView.findViewById(R.id.category);
            task_status = itemView.findViewById(R.id.task_status_text);
        }
    }

}
