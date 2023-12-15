package bichel.yauhen.web.app.servlet;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.core.model.Review;
import bichel.yauhen.web.Constants;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.mapper.HotelReviewsMapper;
import bichel.yauhen.web.app.mapper.ReviewMapper;
import bichel.yauhen.web.app.vo.HotelReviewsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet for endpoint reviews
 */
public class ReviewsServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ReviewsServlet.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final HotelReviewsMapper hotelReviewsMapper;

    public ReviewsServlet(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        super();
        this.hotelReviewData = hotelReviewData;
        hotelReviewsMapper = new HotelReviewsMapper(new ReviewMapper());
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
        logger.info("Process resource /reviews");

        response.setContentType(Constants.CONTENT_TYPE_JSON);
        PrintWriter out = response.getWriter();

        final String paramHotelId = "hotelId";
        final String paramNum = "num";

        if(request.getQueryString() == null || request.getQueryString().isEmpty()) {
            logger.warn("query params are missing");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
        } else {
            String hotelIdParamValue = request.getParameter(paramHotelId);
            String numParamValue = request.getParameter(paramNum);

            if(hotelIdParamValue == null || hotelIdParamValue.isEmpty()) {
                logger.info("Hotel ID is missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
            } else if(numParamValue == null) {
                logger.info("num is missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.printf(JsonUtils.wrapInvalidResponseToJson(paramNum));
            } else {
                int hotelId = Integer.parseInt(hotelIdParamValue, 10);
                int amount = Integer.parseInt(numParamValue, 10);
                List<Review> reviews = hotelReviewData.findReviews(hotelId, amount);

                if (reviews != null && !reviews.isEmpty()) {
                    HotelReviewsResponse respBody = hotelReviewsMapper.mapToResponse(String.valueOf(hotelId), reviews);
                    String json = JsonUtils.getJsonString(respBody);

                    response.setStatus(HttpServletResponse.SC_OK);
                    out.printf(json);
                } else {
                    logger.info("Hotel with ID '" + hotelId + "' is not found");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
                }
            }
        }
    }
}
