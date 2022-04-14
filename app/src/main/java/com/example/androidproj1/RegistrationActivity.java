package com.example.androidproj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;

//окно регистрации
public class RegistrationActivity extends AppCompatActivity {
    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btnReg;
    TextInputLayout ilRegPass;
    TextInputLayout ilRegPassConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Регистрация");

        etUsername = findViewById(R.id.etUsernameReg);
        etPassword = findViewById(R.id.etPasswordReg);
        etConfirmPassword = findViewById(R.id.etConfirmPasswordReg);
        btnReg = findViewById(R.id.btnReg);
        ilRegPass = findViewById(R.id.ilRegPass);
        ilRegPassConfirm=findViewById(R.id.ilRegPassConfirm);

        etPassword.addTextChangedListener(twPassword);
        etConfirmPassword.addTextChangedListener(twPasswordConfirm);
        btnReg.setOnClickListener(btnRegListener);
    }

    View.OnClickListener btnRegListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String json = getJson();
                if (checkFields() == true) {
                    CallRequest callRequest = (CallRequest) new CallRequest("post", "registrationMob") {
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            if (s != null) {
                                switch (s) {
                                    case "User already exist":
                                        refreshForm();
                                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                        break;
                                    case "User saved":
                                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                                        startMyActivity(MainActivity.class);
                                        break;
                                }
                            }
                        }
                    }.execute(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void startMyActivity(Class<?> activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("response", responseRequest);
        startActivity(intent);
    }

    private boolean checkFields() {
        boolean isValid = true;
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String sError = "Поле должно быть заполнено";

        if (username.equals("")) {
            etUsername.setError(sError);
            isValid = false;
        }
        if (password.equals("")) {
            ilRegPass.setEndIconVisible(false);
            etPassword.setError(sError);
            isValid = false;
        }
        if (confirmPassword.equals("")) {
            ilRegPassConfirm.setEndIconVisible(false);
            etConfirmPassword.setError(sError);
            isValid = false;
        }
        if (!password.equals(confirmPassword) && !password.equals("") && !confirmPassword.equals("")) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    private void refreshForm(){
        etUsername.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
    }

    private String getJson(){
        try{
            UserTmp userTmp = new UserTmp();
            userTmp.setUsername(etUsername.getText().toString());
            userTmp.setPassword(etPassword.getText().toString());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(userTmp);

            return json;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    TextWatcher twPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ilRegPass.setEndIconVisible(true);
        }
    };

    TextWatcher twPasswordConfirm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ilRegPassConfirm.setEndIconVisible(true);
        }
    };

}