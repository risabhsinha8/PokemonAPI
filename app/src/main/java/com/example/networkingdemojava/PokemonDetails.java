package com.example.networkingdemojava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PokemonDetails extends AppCompatActivity {
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);
        Intent intent = getIntent();
        String data = intent.getStringExtra("JSONString");
        try {
            JSONObject jsonObject = new JSONObject(data);
            HorizontalBarChart skillRatingChart = findViewById(R.id.skill_rating_chart);
            JSONObject sprites = (JSONObject) jsonObject.get("sprites");
            JSONArray statsJSON = (JSONArray) jsonObject.get("stats");
            String imageURL = sprites.get("front_default").toString();
            String name = jsonObject.getString("name");
            TextView nameView = (TextView)findViewById(R.id.pkmnName);
            ArrayList<Integer> stats = new ArrayList<>();
            for(int i = 0; i < statsJSON.length(); i++)
            {
                JSONObject stat = statsJSON.getJSONObject(i);
                stats.add(stat.getInt("base_stat"));
            }
            showChart(stats);
            nameView.setText(name);
            showImage(imageURL);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error : Can't parse JSON",Toast.LENGTH_SHORT).show();
        }

    }

    void showImage(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageView imageView = (ImageView)findViewById(R.id.pkmnId);
// Request a string response from the provided URL.
        ImageRequest stringRequest = new ImageRequest( url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }},300,300, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Failed to load image",Toast.LENGTH_LONG).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    void showChart(ArrayList<Integer> valueList){
        HorizontalBarChart chart = findViewById(R.id.skill_rating_chart);             //skill_rating_chart is the id of the XML layout

        chart.setDrawBarShadow(false);

        chart.setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawValueAboveBar(false);
         String[] labels = new String[]{"hp", "attack", "defence", "special-attack", "special-defence","speed"};
        int[] colors = new int[6];
        //Display the axis on the left (contains the labels 1*, 2* and so on)
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels[(int)value];
            }
        });


        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Points";

        //input data

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            if(valueList.get(i) <= 50)
                colors[i] = Color.RED;
            else if(valueList.get(i) <= 100)
                colors[i] = Color.YELLOW;
            else
                colors[i] = Color.GREEN;
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);
        barDataSet.setColors(colors);
        BarData data = new BarData(barDataSet);
        chart.setData(data);

//        chart.invalidate();

        xAxis.setLabelCount(6);





        //Add animation to the graph
        chart.animateY(2000);
    }
}



