package com.example.ribath.sodingassignment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ribath on 8/12/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    List<TaskClass> taskClasses;
    TaskClass taskClass;
    Context context;
    RelativeLayout relativeLayout;
    String TAG = "RecyclerAdapter";


    public RecyclerAdapter(Context context, List<TaskClass> taskClasses) {
        this.context = context;
        this.taskClasses = taskClasses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        taskClass = taskClasses.get(position);
        holder.name.setText(taskClass.getName());
        ////
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(taskClass.getDateCreated());
        String createdate = formatter.format(calendar.getTime());
        holder.createdAt.setText(createdate);

        String updatedate=null;
        if(taskClass.getDateUpdated()!=0)
        {
            calendar.setTimeInMillis(taskClass.getDateUpdated());
            updatedate = formatter.format(calendar.getTime());
            holder.updatedAt.setText(updatedate);
        }
        ////
    }

    @Override
    public int getItemCount() {
        if(taskClasses  != null) {
            return taskClasses.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView createdAt;
        public TextView updatedAt;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.textView_name);
            createdAt = (TextView) v.findViewById(R.id.textView_created);
            updatedAt = (TextView) v.findViewById(R.id.textView_updated);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.singleRow);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent intent = new Intent(context, ViewTaskActivity.class);
                                                      intent.putExtra("id",taskClasses.get(getAdapterPosition()).getId());
                                                      context.startActivity(intent);
                                                  }
                                              }
            );

            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("recy", "long click"+getAdapterPosition());
                    PopupMenu popup = new PopupMenu(context, v);
                    Menu m = popup.getMenu();
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.action_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_edit:
                                    Log.i(TAG, "edit pressed");
                                    Intent intent = new Intent(context, EditTaskActivity.class);
                                    intent.putExtra("id", taskClasses.get(getAdapterPosition()).getId());
                                    context.startActivity(intent);
                                    return true;
                                case R.id.action_delete:
                                    Log.i(TAG, "delete pressed");
                                    try {
                                        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
                                        Dao<TaskClass, String> TaskClassDao = dbHelper.getTaskClassDao();
                                        DeleteBuilder<TaskClass, String> deleteBuilder = TaskClassDao.deleteBuilder();
                                        deleteBuilder.where().eq("id", taskClasses.get(getAdapterPosition()).getId());
                                        deleteBuilder.delete();
                                        /////////////////
                                        Toast.makeText(context, "this task is deleted", Toast.LENGTH_LONG).show();
                                        /////////////////
                                        Intent intent1 = new Intent(context, MainActivity.class);
                                        context.startActivity(intent1);

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                            }
                            return true;
                        }
                    });
                    popup.show();
                    return false;
                }
            });
        }
    }
}