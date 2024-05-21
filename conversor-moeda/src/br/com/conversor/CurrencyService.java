package br.com.conversor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CurrencyService {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    private OkHttpClient client = new OkHttpClient();

    public Map<String, Currency> getExchangeRates(String baseCurrency) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + baseCurrency)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String jsonData = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonObject rates = jsonObject.getAsJsonObject("rates");

            Map<String, Currency> currencyMap = new HashMap<>();
            rates.entrySet().forEach(entry -> {
                String code = entry.getKey();
                double rate = entry.getValue().getAsDouble();
                currencyMap.put(code, new Currency(code, code, rate));
            });

            return currencyMap;
        }
    }
}