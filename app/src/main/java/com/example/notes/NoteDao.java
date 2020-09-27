package com.example.notes;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("INSERT INTO notes (contents, archived) VALUES ('New note', 0)")
    void create();

    @Query("SELECT * FROM notes WHERE archived = 0")
    List<Note> getAllNotes();

    @Query("UPDATE notes SET contents = :contents WHERE id = :id")
    void save(String contents, int id);

    @Query("UPDATE notes SET archived = 1 WHERE id = :id")
    void archive(int id);

    @Query("UPDATE notes SET archived = 0 WHERE id = :id")
    void unarchive(int id);

    @Query("DELETE FROM notes WHERE id = :id")
    void delete(int id);
}
