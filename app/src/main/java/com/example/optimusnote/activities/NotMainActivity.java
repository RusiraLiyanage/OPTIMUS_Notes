package com.example.optimusnote.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.optimusnote.R;
import com.example.optimusnote.adapters.NotesAdapter;
import com.example.optimusnote.database.NotesDatabase;
import com.example.optimusnote.entities.Note;
import com.example.optimusnote.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class NotMainActivity extends AppCompatActivity implements NotesListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTE = 3;


    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_main);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(getApplicationContext(), AddNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        );

        noteList =new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);
        getNotes(REQUEST_CODE_SHOW_NOTE, false);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(noteList.size() != 0){
                    notesAdapter.searchNotes(editable.toString());
                }
            }
        });


    }

    @Override
    public void onNoteClicked(Note note, int position) {

            noteClickedPosition = position;
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            intent.putExtra("isViewOrUpdate", true);
            intent.putExtra("note",note);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);

    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted){
        @SuppressLint("StaticFieldLeak")
        class GetNotesTaks extends AsyncTask<Void, Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
            return NotesDatabase
                    .getDatabase(getApplicationContext())
                    .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
               if(requestCode == REQUEST_CODE_SHOW_NOTE){
                   noteList.addAll(notes);
                   notesAdapter.notifyDataSetChanged();
               }else if(requestCode == REQUEST_CODE_ADD_NOTE){
                   noteList.add(0,notes.get(0));
                   notesAdapter.notifyItemInserted(0);
                   notesRecyclerView.smoothScrollToPosition(0);
               }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                   noteList.remove(noteClickedPosition);


                   if(isNoteDeleted){
                       notesAdapter.notifyItemRemoved(noteClickedPosition);
                   }else{
                       noteList.add(noteClickedPosition,notes.get(noteClickedPosition));
                       notesAdapter.notifyItemChanged(noteClickedPosition);
                   }

               }
            }
        }
        new GetNotesTaks().execute();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){

            getNotes(REQUEST_CODE_ADD_NOTE, false);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if(data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }
    }
}
