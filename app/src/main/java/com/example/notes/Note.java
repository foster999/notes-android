package com.example.notes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "contents")  // Can name col different to field
    public String contents;
    @ColumnInfo(name = "archived")
    public int  archived;
}
