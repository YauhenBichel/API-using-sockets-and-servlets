package bichel.yauhen.web.app.mapper;

import java.util.ArrayList;
import java.util.List;

import bichel.yauhen.web.app.vo.HotelReviewsResponse;
import bichel.yauhen.web.app.vo.ReviewResponse;
import bichel.yauhen.core.model.Review;

/**
 * Mapper for Reviews of hotel
 */
public class HotelReviewsMapper {
    private final ReviewMapper reviewMapper;

    public HotelReviewsMapper(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    /**
     * Maps list of reviews to response HotelReviewsResponse
     * @param hotelId String
     * @param reviews List<Review>
     * @return HotelReviewsResponse
     */
    public HotelReviewsResponse mapToResponse(String hotelId, List<Review> reviews) {
        HotelReviewsResponse response = new HotelReviewsResponse();

        response.setSuccess(true);
        response.setHotelId(hotelId);

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        reviews.forEach(review -> reviewResponses.add(reviewMapper.mapToResponse(review)));

        response.setReviews(reviewResponses);

        return response;
    }
}
