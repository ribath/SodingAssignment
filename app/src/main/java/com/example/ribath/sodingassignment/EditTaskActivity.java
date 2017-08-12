package com.example.ribath.sodingassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

    int id;
    DBHelper dbHelper;
    Dao<TaskClass, String> TaskClassDao;
    List<TaskClass> list;
    EditText editText_name, editText_description;
    Button buttonOk;
    String TAG="EditTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        editText_name =(EditText)findViewById(R.id.editText_name);
        editText_description =(EditText)findViewById(R.id.editText_description);
        buttonOk = (Button)findViewById(R.id.buttonOk);

        try {
            TaskClassDao = dbHelper.getTaskClassDao();
            QueryBuilder<TaskClass, String> queryBuilder = TaskClassDao.queryBuilder();
            list = queryBuilder.where().eq("id", id).query();
            editText_name.setText(list.get(0).getName());
            editText_description.setText(list.get(0).getDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();
                String description = editText_description.getText().toString();
                UpdateBuilder<TaskClass, String> updateBuilder = TaskClassDao.updateBuilder();
                // set the criteria like you would a QueryBuilder
                try {
                    updateBuilder.where().eq("id", id);
                    // update the value of your field(s)
                    updateBuilder.updateColumnValue("name", name);
                    updateBuilder.updateColumnValue("description", description);
                    updateBuilder.updateColumnValue("dateUpdated", System.currentTimeMillis());
                    updateBuilder.update();
                    /////////////////
                    Toast.makeText(EditTaskActivity.this, "this task is updated", Toast.LENGTH_LONG).show();
                    /////////////////
                    Intent intent1 = new Intent(EditTaskActivity.this, MainActivity.class);
                    startActivity(intent1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
