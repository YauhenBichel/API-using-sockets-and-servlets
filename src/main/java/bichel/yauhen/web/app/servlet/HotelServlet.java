package bichel.yauhen.web.app.servlet;

import bichel.yauhen.core.data.ThreadSafeHotelReviewDataReadonly;
import bichel.yauhen.web.Constants;
import bichel.yauhen.web.app.JsonUtils;
import bichel.yauhen.web.app.mapper.HotelMapper;
import bichel.yauhen.web.app.vo.HotelResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bichel.yauhen.core.model.Hotel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * Servlet for endpoint hotel
 */
public class HotelServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(HotelServlet.class);

    private final ThreadSafeHotelReviewDataReadonly hotelReviewData;
    private final HotelMapper hotelMapper;

    public HotelServlet(ThreadSafeHotelReviewDataReadonly hotelReviewData) {
        super();
        this.hotelReviewData = hotelReviewData;
        this.hotelMapper = new HotelMapper();
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

        logger.info("Process resource hotelInfo");

        response.setContentType(Constants.CONTENT_TYPE_JSON);
        PrintWriter out = response.getWriter();

        final String paramHotelId = "hotelId";

        if(request.getQueryString() == null || request.getQueryString().isEmpty()) {
            logger.warn("query params are missing");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
        } else {
            String hotelId = request.getParameter(paramHotelId);
            if(hotelId == null || hotelId.isEmpty()) {
                logger.info("Hotel ID is missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.printf(JsonUtils.wrapInvalidResponseToJson(paramHotelId));
            } else {
                Optional<Hotel> optHotel = hotelReviewData.findHotel(hotelId);
                if (optHotel.isPresent()) {
                    logger.info("Hotel: " + optHotel.get());
                    HotelResponse respBody = hotelMapper.mapToResponse(optHotel.get());
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
