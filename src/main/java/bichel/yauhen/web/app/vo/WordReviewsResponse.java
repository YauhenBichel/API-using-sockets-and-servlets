package bichel.yauhen.web.app.vo;

import java.util.List;

/**
 * Response model for reviews with some word
 */
public final class WordReviewsResponse {
    private boolean success;
    private String word;
    private List<ReviewResponse> reviews;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<ReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordReviewsResponse)) return false;

        WordReviewsResponse that = (WordReviewsResponse) o;

        if (success != that.success) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;
        return reviews != null ? reviews.equals(that.reviews) : that.reviews == null;
    }

    @Override
    public int hashCode() {
        int result = (success ? 1 : 0);
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (reviews != null ? reviews.hashCode() : 0);
        return result;
    }
}
