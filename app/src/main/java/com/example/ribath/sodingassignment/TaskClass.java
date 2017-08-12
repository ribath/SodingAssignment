package com.example.ribath.sodingassignment;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ribath on 8/11/2017.
 */

@DatabaseTable(tableName = "TaskTable")
public class TaskClass {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String description;

    @DatabaseField
    private long dateCreated;

    @DatabaseField
    private long dateUpdated;

    public TaskClass(String name, String description, long dateCreated, long dateUpdated) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public TaskClass() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }
}
