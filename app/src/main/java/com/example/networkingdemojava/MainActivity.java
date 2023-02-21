package com.example.networkingdemojava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnDataSendToActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.button);
        b.setOnClickListener(view -> {
            try {
                fetchMon();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void fetchMon() throws IOException {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if(netinfo == null || !netinfo.isConnected()) {
            Toast.makeText(this, "Not connected to Network", Toast.LENGTH_SHORT).show();
        }
        else
        {
            EditText monName = findViewById(R.id.editText);
            URL url =  new URL("https://pokeapi.co/api/v2/pokemon/"+monName.getText().toString().toLowerCase());
            AsyncTask<URL, Void, String> task = new GetMon(this).execute(url);
        }
    }

    public void updateUI(String data){
        Intent i = new Intent(this,PokemonDetails.class);
        if(data.equals("FAILED"))
        {
            Toast.makeText(this,"Pokemon not found. Try entering Pikachu", Toast.LENGTH_LONG).show();
        }
        else {
            i.putExtra("JSONString", data);
            startActivity(i);
            Log.d("JSON", data);
        }

    }
}

