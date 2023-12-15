package bichel.yauhen.web.app.mapper;

import bichel.yauhen.web.app.vo.ReviewResponse;
import bichel.yauhen.core.model.Review;

/**
 * Mapper for Review
 */
public class ReviewMapper {
    /**
     * Maps model Review to response ReviewResponse
     * @param model Review
     * @return ReviewResponse
     */
    public ReviewResponse mapToResponse(Review model) {
        ReviewResponse response = new ReviewResponse();

        response.setReviewId(model.getId());
        response.setReviewText(model.getReviewText());
        response.setTitle(model.getTitle());
        String user = model.getUserNickname().isEmpty() ? "Anonymous" : model.getUserNickname();
        response.setUser(user);
        response.setDate(model.getReviewSubmissionTime().toString());

        return response;
    }
}
