package bichel.yauhen.web.app.client;

import bichel.yauhen.web.app.client.model.WeatherModel;

public interface WeatherApiClient {
    WeatherModel fetchByLocation(String latitude, String longitude);
}
