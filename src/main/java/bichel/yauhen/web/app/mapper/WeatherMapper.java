package bichel.yauhen.web.app.mapper;

import bichel.yauhen.web.app.client.model.WeatherModel;
import bichel.yauhen.web.app.vo.HotelWeatherResponse;

public class WeatherMapper {
    public HotelWeatherResponse mapToResponse(int hotelId, String hotelName, WeatherModel model) {
        HotelWeatherResponse response = new HotelWeatherResponse();
        response.setHotelId(String.valueOf(hotelId));
        response.setName(hotelName);
        response.setTemperature(model.getTemperature());
        response.setWindSpeed(model.getWindSpeed());
        response.setSuccess(true);

        return response;
    }
}
