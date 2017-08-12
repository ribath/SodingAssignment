package com.example.ribath.sodingassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ViewTaskActivity extends AppCompatActivity {

    int id;
    DBHelper dbHelper;
    Dao<TaskClass, String> TaskClassDao;
    List<TaskClass> list;
    TextView textView_created, textView_updated, textView_name, textView_description;
    String name, TAG="ViewTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        textView_name =(TextView)findViewById(R.id.textView_name);
        textView_description =(TextView)findViewById(R.id.textView_description);
        textView_created =(TextView)findViewById(R.id.textView_created);
        textView_updated =(TextView)findViewById(R.id.textView_updated);

        try {
            TaskClassDao = dbHelper.getTaskClassDao();
            QueryBuilder<TaskClass, String> queryBuilder = TaskClassDao.queryBuilder();
            list = queryBuilder.where().eq("id", id).query();
            textView_name.setText(list.get(0).getName());
            textView_description.setText(list.get(0).getDescription());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(list.get(0).getDateCreated());
            textView_created.setText("created at "+formatter.format(calendar.getTime()));
            if(list.get(0).getDateUpdated()!=0)
            {
                calendar.setTimeInMillis(list.get(0).getDateUpdated());
                textView_updated.setText("updated at "+formatter.format(calendar.getTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_edit:
                Log.i(TAG, "edit pressed");
                Intent intent = new Intent(this, EditTaskActivity.class);
                intent.putExtra("id",list.get(0).getId());
                startActivity(intent);
                return true;
            case R.id.action_delete:
                Log.i(TAG, "delete pressed");
                try {
                    Dao<TaskClass, String> TaskClassDao = dbHelper.getTaskClassDao();
                    DeleteBuilder<TaskClass, String> deleteBuilder = TaskClassDao.deleteBuilder();
                    deleteBuilder.where().eq("id", list.get(0).getId());
                    deleteBuilder.delete();
                    /////////////////
                    Toast.makeText(ViewTaskActivity.this, "this task is deleted", Toast.LENGTH_LONG).show();
                    /////////////////
                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
