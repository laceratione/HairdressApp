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

//окно списка пользователей приложения
public class UsersActivity extends AppCompatActivity {
    ListView lvUsers;
    UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setTitle("Пользователи");

        try{
            Intent intent = getIntent();
            String json = intent.getStringExtra("response");
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>(){});
            ArrayList<User> arrayUsers = new ArrayList<User>(users);

            usersAdapter = new UsersAdapter(this, this, arrayUsers);
            lvUsers = (ListView) findViewById(R.id.lvUsers);
            lvUsers.setAdapter(usersAdapter);


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}