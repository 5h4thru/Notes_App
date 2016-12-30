package com.yahoo.palagummi.notesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {


    static ListView listView;
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SAVED_NOTES", Context.MODE_PRIVATE);
        HashSet<String> treeSet = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if(treeSet == null) {
            notes.add("Sample note!");
        } else {
            notes = new ArrayList<>(treeSet);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NotesEditorActivity.class);
                intent.putExtra("notesId", i);
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int notesIdToDelete = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(notesIdToDelete);
                                arrayAdapter.notifyDataSetChanged();

                                // permanent storage
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SAVED_NOTES", Context.MODE_PRIVATE);
                                TreeSet<String> treeSet = new TreeSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", treeSet).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add a new note
                Intent intent = new Intent(getApplicationContext(), NotesEditorActivity.class);
                intent.putExtra("notesId", -1); // not necessary since the default is -1
                startActivity(intent);
            }
        });
    }

}
