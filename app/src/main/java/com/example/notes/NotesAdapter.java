package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private Note lastArchivedNote;
    private int lastArchivedPosition;

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        LinearLayout containerView;
        TextView textView;

        NoteViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.note_row);
            textView = view.findViewById(R.id.note_row_text);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note current = (Note) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), NoteActivity.class);
                    intent.putExtra("id", current.id);
                    intent.putExtra("contents", current.contents);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private List<Note> notes = new ArrayList<>();

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note current = notes.get(position);
        holder.textView.setText(current.contents);
        holder.containerView.setTag(current);
    }

    public void archiveNote(NoteViewHolder viewHolder, RecyclerView recyclerView) {
        int position = viewHolder.getAdapterPosition();
        Note current = notes.get(position);
        MainActivity.database.noteDao().archive(current.id);
        lastArchivedNote = current;
        lastArchivedPosition = position;
        notes.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        showUndoSnackbar(viewHolder, recyclerView);
    }

    private void showUndoSnackbar(final NotesAdapter.NoteViewHolder viewHolder, RecyclerView recyclerView) {
        Snackbar snackbar = Snackbar.make(
                recyclerView,
                R.string.snackbar_archived,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesAdapter.this.undoArchiveNote(viewHolder);
            }
        });
        snackbar.show();
    }

    public void undoArchiveNote(NoteViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        MainActivity.database.noteDao().unarchive(lastArchivedNote.id);
        notes.add(lastArchivedPosition, lastArchivedNote);

        notifyItemInserted(lastArchivedPosition);
        notifyItemRangeChanged(lastArchivedPosition, getItemCount());
    }

    public void deleteNote(NoteViewHolder viewHolder, RecyclerView recyclerView) {
        int position = viewHolder.getAdapterPosition();
        Note current = notes.get(position);
        MainActivity.database.noteDao().delete(current.id);
        notes.remove(position);

        Snackbar snackbar = Snackbar.make(
                recyclerView,
                R.string.snackbar_deleted,
                Snackbar.LENGTH_LONG);
        snackbar.show();

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void reload() {
        notes = MainActivity.database.noteDao().getAllNotes();
    }
}
