package bichel.yauhen.web.app.socket.handler;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.web.Constants;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.mapper.HotelMapper;
import bichel.yauhen.web.app.vo.HotelResponse;
import bichel.yauhen.web.socket.server.HttpRequest;
import bichel.yauhen.web.socket.server.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.core.model.Hotel;
import bichel.yauhen.web.socket.server.HttpHandler;

import java.util.Optional;

/**
 * Http Handler for endpoint /hotelInfo
 */
public final class HotelHandler implements HttpHandler {
    private static final Logger logger = LogManager.getLogger(HotelHandler.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final HotelMapper hotelMapper;

    public HotelHandler(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        this.hotelReviewData = hotelReviewData;
        this.hotelMapper = new HotelMapper();
    }

    /**
     * Handles http get request from the client
     *
     * @param request  client's http request
     * @param response http response
     */
    public void processRequest(HttpRequest request, HttpResponse response) {
        logger.info("Process resource hotelInfo");

        final String paramHotelId = "hotelId";

        if(request.getQueryParams() == null) {
            logger.warn("query params are missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }

        String hotelId = request.getQueryParams().get(paramHotelId);
        if(hotelId == null) {
            logger.info("Hotel ID is missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }

        Optional<Hotel> optHotel = hotelReviewData.findHotel(hotelId);
        if (optHotel.isPresent()) {
            logger.info("Hotel: " + optHotel.get());
            HotelResponse respBody = hotelMapper.mapToResponse(optHotel.get());
            String json = JsonUtils.getJsonString(respBody);

            response.sendResponse(json, Constants.HTTP_STATUS_OK);
        } else {
            logger.info("Hotel with ID '" + hotelId + "' is not found");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
        }
    }
}
