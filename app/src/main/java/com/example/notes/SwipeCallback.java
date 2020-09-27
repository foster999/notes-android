package com.example.notes;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {
    NotesAdapter notesAdapter;
    RecyclerView recyclerView;

    public SwipeCallback(NotesAdapter adapter, RecyclerView rv) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        notesAdapter = adapter;
        recyclerView = rv;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // No action on up or down movement
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.RIGHT) {
            notesAdapter.deleteNote((NotesAdapter.NoteViewHolder) viewHolder, recyclerView);
        } else if (direction == ItemTouchHelper.LEFT) {
            notesAdapter.archiveNote((NotesAdapter.NoteViewHolder) viewHolder, recyclerView);
        }
    }

    @Override
    public void onChildDraw (Canvas c,
                             RecyclerView recyclerView,
                             RecyclerView.ViewHolder viewHolder,
                             float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (dX == 0) {
            return;
        }
        Drawable icon = null;
        ColorDrawable background = null;
        int iconHeight = 0;
        int iconMargin = 0;
        int iconTop = 0;
        int iconBottom = 0;
        int iconLeft = 0;
        int iconRight = 0;

        if (dX > 0) {
            icon = ResourcesCompat.getDrawable(recyclerView.getResources(),
                    R.drawable.ic_menu_delete, null);
            iconHeight = icon.getIntrinsicHeight();
            iconMargin = (viewHolder.itemView.getHeight() - iconHeight) / 2;
            iconTop = viewHolder.itemView.getTop() +
                    (viewHolder.itemView.getHeight() - iconHeight) / 2;
            iconBottom = iconTop + iconHeight;
            iconLeft = 0 + iconMargin;
            iconRight = 0 + iconMargin + iconHeight;

            background  = new ColorDrawable(recyclerView.getResources().getColor(R.color.delete));
            background.setBounds(0, viewHolder.itemView.getTop(),
                    (int) (viewHolder.itemView.getLeft() + dX), viewHolder.itemView.getBottom());
        } else if (dX < 0){
            icon = ResourcesCompat.getDrawable(recyclerView.getResources(),
                    R.drawable.stat_notify_sdcard, null);
            iconHeight = icon.getIntrinsicHeight();
            iconMargin = (viewHolder.itemView.getHeight() - iconHeight) / 2;
            iconTop = viewHolder.itemView.getTop() +
                    (viewHolder.itemView.getHeight() - iconHeight) / 2;
            iconBottom = iconTop + iconHeight;
            iconLeft = viewHolder.itemView.getRight() - iconMargin - iconHeight;
            iconRight = viewHolder.itemView.getRight() - iconMargin;

            background  = new ColorDrawable(recyclerView.getResources().getColor(R.color.archive));
            background.setBounds((int) (viewHolder.itemView.getRight() + dX),
                    viewHolder.itemView.getTop(),   viewHolder.itemView.getRight(),
                    viewHolder.itemView.getBottom());
        }

        background.draw(c);
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        icon.draw(c);
    }
}