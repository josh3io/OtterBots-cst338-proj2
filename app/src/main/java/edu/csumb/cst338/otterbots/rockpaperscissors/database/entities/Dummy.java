package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

//TODO: delete this class once we have actual classes for our database

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DUMMY")
public class Dummy {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private Integer id;
}
