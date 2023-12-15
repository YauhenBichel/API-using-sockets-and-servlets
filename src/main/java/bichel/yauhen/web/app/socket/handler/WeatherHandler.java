package bichel.yauhen.web.app.socket.handler;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.core.model.Hotel;
import bichel.yauhen.web.Constants;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.client.WeatherApiSocketClient;
import bichel.yauhen.web.app.client.model.WeatherModel;
import bichel.yauhen.web.app.client.socket.SocketHttpsClient;
import bichel.yauhen.web.app.vo.HotelWeatherResponse;
import bichel.yauhen.web.socket.server.HttpHandler;
import bichel.yauhen.web.socket.server.HttpRequest;
import bichel.yauhen.web.socket.server.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.web.app.mapper.WeatherMapper;

import java.util.Optional;

/**
 * Http Handler for endpoint /weather
 */
public final class WeatherHandler implements HttpHandler {
    private static final Logger logger = LogManager.getLogger(WeatherHandler.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final WeatherApiSocketClient weatherApiClient;
    private final WeatherMapper weatherMapper;

    public WeatherHandler(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        this.hotelReviewData = hotelReviewData;
        this.weatherApiClient = new WeatherApiSocketClient(new SocketHttpsClient());
        this.weatherMapper = new WeatherMapper();
    }

    /**
     * Handles http get request from the client
     *
     * @param request  client's http request
     * @param response http response
     */
    public void processRequest(HttpRequest request, HttpResponse response) {
        logger.info("Process resource /weather");

        final String paramHotelId = "hotelId";

        if(request.getQueryParams() == null) {
            logger.warn("query params are missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }

        String hotelId = request.getQueryParams().get(paramHotelId);
        Optional<Hotel> optHotel = hotelReviewData.findHotel(hotelId);
        if (optHotel.isPresent()) {
            logger.info("Hotel: " + optHotel.get());

            final String latitude = optHotel.get().getLocation().getLatitude();
            final String longitude = optHotel.get().getLocation().getLongitude();
            WeatherModel model = weatherApiClient.fetchByLocation(latitude, longitude);

            HotelWeatherResponse respBody = weatherMapper.mapToResponse(optHotel.get().getId(), optHotel.get().getName(), model);
            String json = JsonUtils.getJsonString(respBody);

            response.sendResponse(json, Constants.HTTP_STATUS_OK);
        } else {
            logger.info("Hotel with ID: " + hotelId + " is not found");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
        }
    }
}
