package bichel.yauhen.web.app.mapper;

import java.util.ArrayList;
import java.util.List;

import bichel.yauhen.core.model.Review;
import bichel.yauhen.web.app.vo.ReviewResponse;
import bichel.yauhen.web.app.vo.WordReviewsResponse;

/**
 * Mapper for Reviews with a word
 */
public class WordReviewsMapper {

    private final ReviewMapper reviewMapper;

    public WordReviewsMapper(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    /**
     * Maps list of reviews to response WordReviewsResponse
     * @param word String
     * @param reviews List<Review>
     * @return WordReviewsResponse
     */
    public WordReviewsResponse mapToResponse(String word, List<Review> reviews)  {
        WordReviewsResponse response = new WordReviewsResponse();

        response.setSuccess(true);
        response.setWord(word);

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        reviews.forEach(review -> reviewResponses.add(reviewMapper.mapToResponse(review)));
        response.setReviews(reviewResponses);

        return response;
    }
}
