package com.jzv.nimbus;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String apiKey = "408edb444b564d43a76203814253007";
    String urlAPI = "https://api.weatherapi.com/v1/current.json?key="+apiKey+"&q=Alcañiz&aqi=no";
    TextView prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prueba = findViewById(R.id.pruebaApi);
        
        pruebaApi();
    }

    public void pruebaApi() {
        StringRequest request = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject location = jsonObject.getJSONObject("location");
                            JSONObject current = jsonObject.getJSONObject("current");
                            JSONObject condition = current.getJSONObject("condition");

                            String ciudad = location.getString("name");
                            String temp = current.getString("temp_c");
                            String estado = condition.getString("text");

                            String mensaje = "Ciudad: " + ciudad + "\nTemperatura: " + temp + "°C\nEstado: " + estado;
                            prueba.setText(mensaje);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            prueba.setText("Error al procesar la respuesta JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prueba.setText("Error de conexión o en la API");
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }
}