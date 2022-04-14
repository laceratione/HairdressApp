package com.example.androidproj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;

//Ctrl Alt L = форматирование кода
//окно входа + регистрация
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sPref;

    TextInputLayout textInputLayout;
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscreen);

        textInputLayout = findViewById(R.id.textInputLayout);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        etPassword.addTextChangedListener(watcherPassword);
        btnLogin.setOnClickListener(this);
    }

    TextWatcher watcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            textInputLayout.setEndIconVisible(true);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login();
                break;
        }
    }

    private void login() {
        try {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if (checkError(username, password) == true) {
                UserTmp userTmp = new UserTmp();
                userTmp.setUsername(username);
                userTmp.setPassword(password);

                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(userTmp);
                sendRequest(username, json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(String username, String json){
        CallRequest callRequest = (CallRequest) new CallRequest("post", "loginMobile") {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("ROLE_USER")) {
                    saveDataUser();
                    Intent userPageIntent = new Intent(getApplicationContext(), UserPage.class);
                    userPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    userPageIntent.putExtra("user_name", username);
                    startActivity(userPageIntent);
                } else if (s.equals("ROLE_ADMIN")) {
                    Intent adminIntent = new Intent(getApplicationContext(), AdminActivity.class);
                    adminIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(adminIntent);
                } else
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }.execute(json);
    }

    @Override
    protected void onStart() {
        super.onStart();

        etUsername.setText("");
        etPassword.setText("");

        checkResponse();
    }

    public void getRegistrationActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
    }

    private void checkResponse() {
        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        if (response != null) {
            switch (response) {
                case "User saved":
                    Toast.makeText(getApplicationContext(), "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
                    intent.removeExtra("response");
                    break;
            }
        }
    }

    private boolean checkError(String username, String password) {
        boolean isValid = true;

        if (username.trim().equalsIgnoreCase("")) {
            isValid = false;
            etUsername.setError("This field can not be blank");
        }

        if (password.trim().equalsIgnoreCase("")) {
            isValid = false;
            textInputLayout.setEndIconVisible(false);
            etPassword.setError("This field can not be blank");
        }
        return isValid;
    }

    private void saveDataUser() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("user_name", etUsername.getText().toString());
        ed.commit();
    }
}
