package bichel.yauhen.web.app.servlet;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.core.model.Review;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.mapper.ReviewMapper;
import bichel.yauhen.web.app.mapper.WordReviewsMapper;
import bichel.yauhen.web.app.vo.WordReviewsResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static bichel.yauhen.web.Constants.CONTENT_TYPE_JSON;

/**
 * Servlet for endpoint word
 */
public class WordServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(WordServlet.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final WordReviewsMapper wordReviewsMapper;

    public WordServlet(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        super();
        this.hotelReviewData = hotelReviewData;
        wordReviewsMapper = new WordReviewsMapper(new ReviewMapper());
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
        logger.info("Process resource '/index' to find a word");

        response.setContentType(CONTENT_TYPE_JSON);
        PrintWriter out = response.getWriter();

        final String paramWord = "word";
        final String paramNum = "num";

        if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
            logger.warn("query params are missing");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.printf(JsonUtils.wrapInvalidResponseToJson(paramWord));
        } else {
            String wordParamValue = request.getParameter(paramWord);
            String numParamValue = request.getParameter(paramNum);

            if (wordParamValue == null) {
                logger.warn("word is missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.printf(JsonUtils.wrapInvalidResponseToJson(paramWord));
            } else if (numParamValue == null) {
                logger.warn("num is missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.printf(JsonUtils.wrapInvalidResponseToJson(paramNum));
            } else {
                int amount = Integer.parseInt(numParamValue, 10);
                List<Review> wordReviews = hotelReviewData.findReviewsByWord(wordParamValue, amount);

                WordReviewsResponse respBody = wordReviewsMapper.mapToResponse(wordParamValue, wordReviews);
                String json = JsonUtils.getJsonString(respBody);

                response.setStatus(HttpServletResponse.SC_OK);
                out.printf(json);
            }
        }
    }
}
