package bichel.yauhen.web.app.servlet;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.core.model.Hotel;
import bichel.yauhen.web.Constants;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.client.WeatherApiSocketClient;
import bichel.yauhen.web.app.client.model.WeatherModel;
import bichel.yauhen.web.app.client.socket.SocketHttpsClient;
import bichel.yauhen.web.app.vo.HotelWeatherResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.web.app.mapper.WeatherMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * Servlet for endpoint weather
 */
public class WeatherServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(WeatherServlet.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final WeatherApiSocketClient weatherApiClient;
    private final WeatherMapper weatherMapper;

    public WeatherServlet(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        super();
        this.hotelReviewData = hotelReviewData;
        this.weatherApiClient = new WeatherApiSocketClient(new SocketHttpsClient());
        this.weatherMapper = new WeatherMapper();
    }

    /**
     * Action for HTTP request method GET
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Process resource /weather");

        response.setContentType(Constants.CONTENT_TYPE_JSON);
        PrintWriter out = response.getWriter();

        final String paramHotelId = "hotelId";

        if(request.getQueryString() == null || request.getQueryString().isEmpty()) {
            logger.warn("query params are missing");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
        } else {
            String hotelIdValue = request.getParameter(paramHotelId);

            if(hotelIdValue == null || hotelIdValue.isEmpty()) {
                logger.info("Hotel ID is missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
            } else {
                Optional<Hotel> optHotel = hotelReviewData.findHotel(hotelIdValue);
                if (optHotel.isPresent()) {
                    logger.info("Hotel: " + optHotel.get());
                    final String latitude = optHotel.get().getLocation().getLatitude();
                    final String longitude = optHotel.get().getLocation().getLongitude();
                    WeatherModel model = weatherApiClient.fetchByLocation(latitude, longitude);

                    HotelWeatherResponse respBody = weatherMapper.mapToResponse(optHotel.get().getId(), optHotel.get().getName(), model);
                    String json = JsonUtils.getJsonString(respBody);

                    response.setStatus(HttpServletResponse.SC_OK);
                    out.printf(json);
                } else {
                    logger.info("Hotel with ID: " + hotelIdValue + " is not found");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
                }
            }
        }
    }
}
