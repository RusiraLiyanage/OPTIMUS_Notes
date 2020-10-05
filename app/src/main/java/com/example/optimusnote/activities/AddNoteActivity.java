package com.example.optimusnote.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.optimusnote.R;
import com.example.optimusnote.database.NotesDatabase;
import com.example.optimusnote.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteSubtitle,inputNoteText;
    private TextView textDateTime;

    private String selectedNoteMore;

    private AlertDialog dialogDeleteNote;
    private Note alreadyAvailableNote;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ImageView imageBack =findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              saveNote();
              Intent intent = new Intent(getApplicationContext(),NotMainActivity.class);
              startActivity(intent);
            }
        });



        iniDeleteLayout();

        if(getIntent().getBooleanExtra("isViewOrUpdate",false)){
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }
    }



    private void setViewOrUpdateNote(){

        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(alreadyAvailableNote.getDateTime());
    }

     private void saveNote(){
        if(inputNoteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Plz enter the note title", Toast.LENGTH_SHORT).show();
            return;
        }else if(inputNoteSubtitle.getText().toString().trim().isEmpty()
            && inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note can't be Empty!!!", Toast.LENGTH_SHORT).show();
            return;
        }
         final Note note = new Note();

        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubtitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());

        if(alreadyAvailableNote != null){
            note.setId(alreadyAvailableNote.getId());

        }

        class SaveNoteTask extends AsyncTask<Void ,Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
            NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

           // @Override
            /*protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
               /* Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();

            }*/

        }
        new SaveNoteTask().execute();
        }

        private  void  iniDeleteLayout(){

        final LinearLayout layoutDelete =findViewById(R.id.layout_m_delete);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutDelete);

        layoutDelete.findViewById(R.id.text_M_Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                if (alreadyAvailableNote != null){
                    layoutDelete.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
                    layoutDelete.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            showDeleteNoteDialog();

                        }
                    });
                }
            }
        });

        }
        private void showDeleteNoteDialog(){
            if(dialogDeleteNote == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                View view = LayoutInflater.from(this).inflate(
                        R.layout.layout_delete_note,
                        (ViewGroup) findViewById(R.id.layoutDeletedNoteContainer)
                );
                builder.setView(view);
                dialogDeleteNote = builder.create();
                if(dialogDeleteNote.getWindow() != null){
                    dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        @SuppressLint("StaticFieldLeak")
                        class DeleteNoteTask extends AsyncTask<Void, Void, Void>{

                            @Override
                            protected Void doInBackground(Void... voids) {
                                NotesDatabase.getDatabase(getApplicationContext()).noteDao()
                                        .deleteNote(alreadyAvailableNote);
                                return null;
                            }

                            /*@Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                Intent intent = new Intent(getApplicationContext(),NotMainActivity.class);
                                intent.putExtra("isNoteDeleted",true);
                                setResult(RESULT_OK,intent);
                                finish();
                            }*/

                        }
                        Intent intent = new Intent(getApplicationContext(),NotMainActivity.class);
                        startActivity(intent);
                        new DeleteNoteTask().execute();
                    }
                });

                view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDeleteNote.dismiss();
                    }
                });
            }
            dialogDeleteNote.show();
        }
     }
