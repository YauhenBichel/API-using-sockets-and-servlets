package bichel.yauhen.core.comparator;

import bichel.yauhen.core.model.Review;

import java.util.Comparator;

/**
 * Review comparator by date and review id
 */
public class ReviewComparatorByDateAndReviewId implements Comparator<Review> {
    @Override
    public int compare(Review review1, Review review2) {
        int mostRecentFirstCoef = -1;

        int dateComparison = mostRecentFirstCoef * review1.getReviewSubmissionTime()
                .compareTo(review2.getReviewSubmissionTime());

        boolean isTheSame = dateComparison == 0;
        if(isTheSame) {
            return review1.getId().compareTo(review2.getId());
        } else {
            return dateComparison;
        }
    }
}
