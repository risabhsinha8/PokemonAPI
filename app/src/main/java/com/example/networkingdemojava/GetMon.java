package com.example.networkingdemojava;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetMon extends AsyncTask<URL,Void,String> {
    OnDataSendToActivity activity;
    public GetMon(Activity activity){
        this.activity = (OnDataSendToActivity) activity;
    }
    @Override
    protected String doInBackground(URL... urls) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) urls[0].openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String cur;
                    while ((cur = in.readLine()) != null) {
                        sb.append(cur);
                    }
                    in.close();
                    connection.disconnect();

                    return sb.toString();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null)
            {
                connection.disconnect();
            }
        }


        return "FAILED";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.updateUI(s);
    }
}
