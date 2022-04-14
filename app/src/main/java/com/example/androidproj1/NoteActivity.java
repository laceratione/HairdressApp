package com.example.androidproj1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

//окно создания заявки
public class NoteActivity extends AppCompatActivity implements View.OnClickListener {
    String[] dataRus;
    String[] data;
    Spinner spinner;
    EditText etFullName;
    EditText etDateVisit;
    EditText etPhoneNumber;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle("Сделать заявку");

        dataRus = getResources().getStringArray(R.array.type_service_rus);
        data = getResources().getStringArray(R.array.type_service);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataRus);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        etFullName = findViewById(R.id.etFullName);
        etDateVisit = findViewById(R.id.etDateVisit);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (checkFields() == true)
                    saveNoteRequest();
                break;
        }
    }

    private void saveNoteRequest() {
        try {
            Note note = createNote();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonNote = objectMapper.writeValueAsString(note);
            CallRequest callRequest = (CallRequest) new CallRequest("post", "saveMobile") {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s != null) {
                        switch (s) {
                            case "Note saved":
                                Toast.makeText(getApplicationContext(), "Заявка сохранена", Toast.LENGTH_LONG).show();
                                finish();
                                break;
                        }
                    }
                }
            }.execute(jsonNote);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Note createNote() {
        Note note = new Note();
        note.setName(etFullName.getText().toString());
        note.setDateVisit(etDateVisit.getText().toString());
        note.setPhone(etPhoneNumber.getText().toString());
        note.setServiceType(data[spinner.getSelectedItemPosition()]);

        return note;
    }

    private boolean checkFields() {
        boolean isValid = true;
        String sError = "Поле должно быть заполнено";
        if (etFullName.getText().toString().equals("")) {
            etFullName.setError(sError);
            isValid = false;
        }
        if (etDateVisit.getText().toString().equals("")) {
            etDateVisit.setError(sError);
            isValid = false;
        }
        if (etPhoneNumber.getText().toString().equals("")) {
            etPhoneNumber.setError(sError);
            isValid = false;
        }
        return isValid;
    }
}