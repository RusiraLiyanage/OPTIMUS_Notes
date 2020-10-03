package com.example.optimusnote.listeners;

import com.example.optimusnote.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note,int position);
}
