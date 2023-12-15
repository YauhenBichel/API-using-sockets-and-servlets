package bichel.yauhen.web.app.client;

import bichel.yauhen.web.app.client.model.WeatherModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import bichel.yauhen.web.app.client.model.WeatherApiResponse;
import bichel.yauhen.web.app.client.socket.SocketHttpsClient;

public class WeatherApiSocketClient implements WeatherApiClient {
    private SocketHttpsClient socketHttpsClient;

    public WeatherApiSocketClient(SocketHttpsClient socketHttpsClient) {
        this.socketHttpsClient = socketHttpsClient;
    }

    public WeatherModel fetchByLocation(String latitude, String longitude) {
        WeatherModel model = new WeatherModel();
        Gson gson = new Gson();
        final String url = "https://api.open-meteo.com/v1/forecast?latitude=" +
                latitude + "&longitude=" +
                longitude +
                "&hourly=temperature_2m&daily=wind_speed_10m_max&forecast_days=1";
        String apiJson = socketHttpsClient.get(url);

        try {
            WeatherApiResponse apiResponse = gson.fromJson(apiJson, WeatherApiResponse.class);
            model.setTemperature(apiResponse.getHourly().getTemperature_2m().get(0));
            model.setWindSpeed(apiResponse.getDaily().getWind_speed_10m_max().get(0));
        } catch(JsonSyntaxException ex) {
            System.out.println(ex);
        } catch(Exception ex) {
            System.out.println(ex);
        }

        return model;
    }
}
