package com.example.androidproj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

//окно с бизнес-логикой для пользователя
public class UserPage extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<>();
    final String ATTRIBUTE_NAME_TEXT = "text";
    ListView lvUserPage;
    UserPageAdapter userPageAdapter;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        items = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.list_user_page)));
        userPageAdapter = new UserPageAdapter(this, items);
        lvUserPage = (ListView) findViewById(R.id.lvUserPage);
        lvUserPage.setAdapter(userPageAdapter);
        lvUserPage.setOnItemClickListener(lvUserPageListener);

        Intent intent = getIntent();
        name = intent.getStringExtra("user_name");
    }

    AdapterView.OnItemClickListener lvUserPageListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CallRequest callRequest;
            switch (position) {
                case 0:
                    //ТУТ ДОДЕЛАТЬ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    Intent noteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    noteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(noteIntent);
                    break;
                case 1:
                    //по красоте нужно добавить id пользателя в Note
                    String page = "getHistoryNotesJson?username=" + name;
                    callRequest = (CallRequest) new CallRequest("get", page) {
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            if (s.equals("[]"))
                                Toast.makeText(getApplicationContext(), "У Вас нет заявок", Toast.LENGTH_LONG).show();
                            else
                                startMyActivity(NotesActivity.class, s);
                        }
                    }.execute(page);
                    break;
                case 2:
                    finish();
                    break;
            }
        }
    };

    private void startMyActivity(Class<?> activity, String json) {
        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("response", json);
        startActivity(intent);
    }
}