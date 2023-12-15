package bichel.yauhen.web.app.vo;

import java.util.List;

/**
 * Response model for reviews of a hotel
 */
public final class HotelReviewsResponse {
    private boolean success;
    private String hotelId;
    private List<ReviewResponse> reviews;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
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
        if (!(o instanceof HotelReviewsResponse)) return false;

        HotelReviewsResponse that = (HotelReviewsResponse) o;

        if (success != that.success) return false;
        if (hotelId != null ? !hotelId.equals(that.hotelId) : that.hotelId != null) return false;
        return reviews != null ? reviews.equals(that.reviews) : that.reviews == null;
    }

    @Override
    public int hashCode() {
        int result = (success ? 1 : 0);
        result = 31 * result + (hotelId != null ? hotelId.hashCode() : 0);
        result = 31 * result + (reviews != null ? reviews.hashCode() : 0);
        return result;
    }
}
