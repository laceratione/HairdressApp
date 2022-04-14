package com.example.androidproj1;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

//отвечает за выполнение запросов к серверву
public class CallRequest extends AsyncTask<String, String, String> {
    String method;
    String page;
    String responseRequest;

    public CallRequest(String method, String page) {
        this.method = method;
        this.page = page;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (method == "get") {
            getRequest(page);
        } else {
            String json = strings[0];
            postRequest(json);
        }
        return responseRequest;
    }

    private void getRequest(String page) {
        try {
            URL url = new URL("http://10.0.2.2:8080/" + page);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            readResponse(con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postRequest(String json) {
        try {
            URL url = new URL("http://10.0.2.2:8080/" + page);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/text");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            readResponse(con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResponse(HttpURLConnection connection){
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            responseRequest = response.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}