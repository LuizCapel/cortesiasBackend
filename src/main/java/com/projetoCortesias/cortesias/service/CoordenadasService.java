package com.projetoCortesias.cortesias.service;

import com.projetoCortesias.cortesias.dto.Coordenada;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

@Service
public class CoordenadasService {

    public Coordenada buscarCoordenadaPorCidade(String cidade) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" +
                    URLEncoder.encode(cidade + ", Brasil", "UTF-8") +
                    "&format=json&limit=1";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "SeuSistemaDeFoodtrucks/1.0"); // Obrigatório pela política da Nominatim

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonBuilder.append(inputLine);
            }
            in.close();

            JSONArray array = new JSONArray(jsonBuilder.toString());
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                return new Coordenada(lat, lon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // não encontrado
    }

}
