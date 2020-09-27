package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton add_button;

    public static NoteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "notes")
                .allowMainThreadQueries()  // Let in run in foreground
                .addMigrations(MIGRATION_1_2)
                .build();

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        notesAdapter = new NotesAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notesAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeCallback(notesAdapter, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        add_button = findViewById(R.id.add_note_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.noteDao().create();
                notesAdapter.reload();
                notesAdapter.notifyItemInserted(notesAdapter.getItemCount() - 1);
            }
        });

        notesAdapter.reload();
    }

    @Override
    protected void onResume() {
        super.onResume();

        notesAdapter.notifyDataSetChanged();
        notesAdapter.reload();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE notes "
                    +"ADD archived INT NOT NULL DEFAULT 0");

        }
    };
}