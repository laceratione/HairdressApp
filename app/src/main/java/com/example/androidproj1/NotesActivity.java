package com.example.androidproj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//окно отображения заявок
public class NotesActivity extends AppCompatActivity {
    ListView lvNotes;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle("Заявки");

        try{
            Intent intent = getIntent();
            String json = intent.getStringExtra("response");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Note> notes = objectMapper.readValue(json, new TypeReference<List<Note>>(){});
            ArrayList<Note> arrayNotes = new ArrayList<Note>(notes);

            notesAdapter = new NotesAdapter(this, this, arrayNotes);
            lvNotes = (ListView) findViewById(R.id.lvNotes);
            lvNotes.setAdapter(notesAdapter);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}