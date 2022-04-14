package com.example.androidproj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

//окно с бизнес-логикой для администратора
public class AdminActivity extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<String>();
    ListView lvAdminPage;
    AdminAdaper adminAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        items = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.list_admin_page)));
        adminAdaper = new AdminAdaper(this, items);
        lvAdminPage = (ListView) findViewById(R.id.lvAdminPage);
        lvAdminPage.setAdapter(adminAdaper);
        lvAdminPage.setOnItemClickListener(lvClickListener);
    }

    AdapterView.OnItemClickListener lvClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CallRequest callRequest;
            switch (position) {
                case 0:
                    callRequest = (CallRequest) new CallRequest("get", "getUsersJson"){
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            startMyActivity(UsersActivity.class, s);
                        }
                    }.execute();
                    break;
                case 1:
                    callRequest = (CallRequest) new CallRequest("get", "getNotesJson"){
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            startMyActivity(NotesActivity.class, s);
                        }
                    }.execute();
                    break;
                case 2:
                    finish();
                    break;
            }
        }
    };

    private void startMyActivity(Class<?> activity, String json){
        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("response", json);
        startActivity(intent);
    }
}