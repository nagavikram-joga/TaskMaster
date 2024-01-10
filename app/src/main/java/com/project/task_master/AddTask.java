package com.project.task_master;


import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.task_master.Model.ToDoModel;
import com.project.task_master.Utils.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTask extends AppCompatActivity {

    private EditText task_name,task_description;
    RelativeLayout rldate,rltime,rlpriority,rlcategory,rlstatus;
    private TextView due_date,due_time,priority,category,task_status;
    private Button back_btn,save_btn;
    private DataBaseHelper myDB;
    ArrayList<String> priorityList = new ArrayList<>();

    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<String> categoriesList = new ArrayList<>();

    public static AddTask newInstance()
    {
        return new AddTask();
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        task_name = findViewById(R.id.task_heading);
        task_description = findViewById(R.id.task_description);
        due_date = findViewById(R.id.date_text_view);
        due_time = findViewById(R.id.time_text_view);
        priority = findViewById(R.id.priority_text_view);
        category = findViewById(R.id.category_text_view);
        save_btn = findViewById(R.id.save_btn);
        back_btn = findViewById(R.id.back_btn);
        rldate = findViewById(R.id.dateRL);
        rltime = findViewById(R.id.timeRL);
        rlpriority = findViewById(R.id.priorityRL);
        rlcategory = findViewById(R.id.categoryRL);
        rlstatus = findViewById(R.id.statusRL);
        task_status = findViewById(R.id.status_text_view);



        priorityList.add("Low Priority");
        priorityList.add("Medium Priority");
        priorityList.add("High Priority");

        categoriesList.add("Default");

        statusList.add("New");
        statusList.add("In Progress");
        statusList.add("Completed");

        myDB = new DataBaseHelper(AddTask.this);

        boolean isUpdate = false;


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isUpdate = true;
            String b_task_name = bundle.getString("task_name");
            String b_task_description = bundle.getString("task_description");
            String b_due_date = bundle.getString("due_date");
            String b_due_time = bundle.getString("due_time");
            String b_priority = bundle.getString("priority");
            String b_category = bundle.getString("category");
            String b_status = bundle.getString("task_status");

            task_name.setText(b_task_name);
            task_description.setText(b_task_description);
            due_date.setText(b_due_date);
            due_time.setText(b_due_time);
            priority.setText(b_priority);
            category.setText(b_category);
            task_status.setText(b_status);

        }
        else{
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String currentTime = timeFormat.format(calendar.getTime());

            // Get current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());

            // Set current time and date to TextViews
            due_time.setText(currentTime);
            due_date.setText(currentDate);
            priority.setText("Low");
            category.setText("Default");
            task_status.setText("New");
        }
        rldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        rltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        rlpriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemSelectionDialog(priorityList);
            }
        });

        rlstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusSelectionDialog(statusList);
            }
        });

        rlcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog(categoriesList);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final boolean finalIsUpdate = isUpdate;
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = task_name.getText().toString().trim();
                if(name.length()>0)
                {
                    String desc = task_description.getText().toString();
                    String date = due_date.getText().toString();
                    String time = due_time.getText().toString();
                    String s_priority = priority.getText().toString();
                    String s_category = category.getText().toString();
                    String s_task_status = task_status.getText().toString();

                    if(finalIsUpdate)
                    {
                        myDB.updateTask(bundle.getInt("id"), name,desc,bundle.getInt("status"),date,time,s_priority,s_category,s_task_status);
                    }
                    else {
                        ToDoModel item = new ToDoModel();
                        item.setTask_name(name);
                        item.setTask_description(desc);
                        item.setStatus(0);
                        item.setDue_date(date);
                        item.setDue_time(time);
                        item.setCategory(s_category);
                        item.setPriority(s_priority);
                        item.setTask_status(s_task_status);
                        myDB.insertTask(item);

                    }

                    finish();
                }
                else{
                    Toast.makeText(AddTask.this, "Please enter task name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Handle selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());
                        due_date.setText(formattedDate);
                        // TODO: Do something with the selected date
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }


    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle selected time
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);

                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String formattedTime = timeFormat.format(selectedTime.getTime());
                        due_time.setText(formattedTime);
                        // TODO: Do something with the selected time
                    }
                },
                hour, minute, false
        );

        timePickerDialog.show();
    }

    private void showItemSelectionDialog(ArrayList<String> itemsList) {
        CharSequence[] items = itemsList.toArray(new CharSequence[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Priority")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the item selection here
                        String selectedItem = itemsList.get(which);
                        String newSelectedItem = selectedItem.substring(0,selectedItem.length()-9);
                        // TODO: Use the selected item as needed
                        // For example, display it in a TextView or perform an action
                        //Toast.makeText(getApplicationContext(), "Selected Item: " + selectedItem, Toast.LENGTH_SHORT).show();
                        priority.setText(newSelectedItem);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showStatusSelectionDialog(ArrayList<String> itemsList) {
        CharSequence[] items = itemsList.toArray(new CharSequence[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Task Status")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the item selection here
                        String selectedItem = itemsList.get(which);
                        task_status.setText(selectedItem);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showCategoryDialog(final ArrayList<String> categoriesList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select or Add a Category");

        // Convert ArrayList to an array for the dialog items
        final CharSequence[] items = categoriesList.toArray(new CharSequence[0]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedItem = categoriesList.get(which);
                category.setText(selectedItem);
            }
        });

        builder.setPositiveButton("Add New Category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle adding a new category here
                showAddCategoryDialog(categoriesList);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showAddCategoryDialog(final ArrayList<String> categoriesList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Category");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCategory = input.getText().toString().trim();
                if (!newCategory.isEmpty()) {
                    categoriesList.add(newCategory);
                    // You can notify your adapter or update UI here with the new category
                    //Toast.makeText(getApplicationContext(), "New Category Added: " + newCategory, Toast.LENGTH_SHORT).show();
                    category.setText(newCategory);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



}







