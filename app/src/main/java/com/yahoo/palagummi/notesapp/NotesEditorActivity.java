package com.yahoo.palagummi.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.HashSet;
import java.util.TreeSet;

public class NotesEditorActivity extends AppCompatActivity {


    EditText editText;
    int notesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        notesId = intent.getIntExtra("notesId", -1);
        if(notesId != -1) {
            editText.setText(MainActivity.notes.get(notesId));

        } else {
            MainActivity.notes.add(""); // initially an empty note is created
            notesId = MainActivity.notes.size() - 1;
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // update the notes array
                Log.i("OnChange ", String.valueOf(charSequence.toString().length()));
                if(charSequence.toString().length() != 0) {
                    MainActivity.notes.set(notesId, charSequence.toString());
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                } else {
                    MainActivity.notes.remove(notesId);
                }

                // permanent storage
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SAVED_NOTES", Context.MODE_PRIVATE);
                TreeSet<String> treeSet = new TreeSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", treeSet).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
