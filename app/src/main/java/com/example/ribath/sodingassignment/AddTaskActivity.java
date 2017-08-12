package com.example.ribath.sodingassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class AddTaskActivity extends AppCompatActivity {

    EditText editText_name, editText_description;
    Button buttonOk;
    String name, description, TAG="AddTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editText_name = (EditText)findViewById(R.id.editText_name);
        editText_description =(EditText)findViewById(R.id.editText_description);
        buttonOk = (Button)findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText_name.getText().toString();
                description = editText_description.getText().toString();
                Log.i(TAG, name+description);
                saveToDB(name, description);
                Toast.makeText(AddTaskActivity.this, "your data is saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void saveToDB(String name, String description)
    {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        TaskClass task = new TaskClass(name, description, System.currentTimeMillis(), 0);
        try {
            Dao<TaskClass, String> TaskClassDao = dbHelper.getTaskClassDao();
            TaskClassDao.create(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
