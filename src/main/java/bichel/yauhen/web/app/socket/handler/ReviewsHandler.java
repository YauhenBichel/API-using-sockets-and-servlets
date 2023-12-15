package bichel.yauhen.web.app.socket.handler;

import java.util.List;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.core.model.Review;
import bichel.yauhen.web.Constants;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.mapper.HotelReviewsMapper;
import bichel.yauhen.web.app.mapper.ReviewMapper;
import bichel.yauhen.web.app.vo.HotelReviewsResponse;
import bichel.yauhen.web.socket.server.HttpHandler;
import bichel.yauhen.web.socket.server.HttpRequest;
import bichel.yauhen.web.socket.server.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Http Handler for endpoint /reviews
 */
public final class ReviewsHandler implements HttpHandler {
    private static final Logger logger = LogManager.getLogger(ReviewsHandler.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final HotelReviewsMapper hotelReviewsMapper;

    public ReviewsHandler(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        this.hotelReviewData = hotelReviewData;
        hotelReviewsMapper = new HotelReviewsMapper(new ReviewMapper());
    }

    /**
     * Handles http get request from the client
     *
     * @param request  client's http request
     * @param response http response
     */
    public void processRequest(HttpRequest request, HttpResponse response) {
        logger.info("Process resource /reviews");

        final String paramHotelId = "hotelId";
        final String paramNum = "num";

        if(request.getQueryParams() == null) {
            logger.warn("query params are missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }

        String hotelIdParam = request.getQueryParams().get("hotelId");
        String numParam = request.getQueryParams().get(paramNum);
        if(hotelIdParam == null) {
            logger.info("Hotel Id or num is missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }
        if(numParam == null) {
            logger.info("num is missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramNum), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }

        int hotelId = Integer.parseInt(hotelIdParam, 10);
        int amount = Integer.parseInt(numParam, 10);
        List<Review> reviews = hotelReviewData.findReviews(hotelId, amount);

        if (reviews != null && !reviews.isEmpty()) {
            HotelReviewsResponse respBody = hotelReviewsMapper.mapToResponse(String.valueOf(hotelId), reviews);
            String json = JsonUtils.getJsonString(respBody);
            response.sendResponse(json, Constants.HTTP_STATUS_OK);
        } else {
            logger.info("Hotel with ID: " + hotelId + " is not found");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramHotelId), Constants.HTTP_STATUS_BAD_REQUEST);
        }
    }
}
