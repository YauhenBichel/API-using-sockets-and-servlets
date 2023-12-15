package bichel.yauhen.web.app.socket.handler;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.core.model.Review;
import bichel.yauhen.web.Constants;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.mapper.ReviewMapper;
import bichel.yauhen.web.app.vo.WordReviewsResponse;
import bichel.yauhen.web.socket.server.HttpHandler;
import bichel.yauhen.web.socket.server.HttpRequest;
import bichel.yauhen.web.socket.server.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.web.app.mapper.WordReviewsMapper;

import java.util.List;

/**
 * Http Handler for endpoint /index
 */
public final class WordHandler implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(WordHandler.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final WordReviewsMapper wordReviewsMapper;

    public WordHandler(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        this.hotelReviewData = hotelReviewData;
        wordReviewsMapper = new WordReviewsMapper(new ReviewMapper());
    }

    /**
     * Handles http get request from the client
     * @param request client's http request
     * @param response http response
     */
    public void processRequest(HttpRequest request, HttpResponse response) {
        logger.info("Process resource '/index' to find a word");

        final String paramWord = "word";
        final String paramNum = "num";

        if(request.getQueryParams() == null) {
            logger.warn("query params are missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramWord), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }

        String wordParam = request.getQueryParams().get(paramWord);
        String numParam = request.getQueryParams().get(paramNum);
        if(wordParam == null) {
            logger.warn("word is missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramWord), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }
        if(numParam == null) {
            logger.warn("num is missing");
            response.sendResponse(JsonUtils.wrapInvalidResponseToJson(paramNum), Constants.HTTP_STATUS_BAD_REQUEST);
            return;
        }

        int amount = Integer.parseInt(numParam, 10);
        List<Review> wordReviews = hotelReviewData.findReviewsByWord(wordParam, amount);
        WordReviewsResponse respBody = wordReviewsMapper.mapToResponse(wordParam, wordReviews);
        String json = JsonUtils.getJsonString(respBody);
        response.sendResponse(json, Constants.HTTP_STATUS_OK);
    }
}
