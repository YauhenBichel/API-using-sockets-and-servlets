package bichel.yauhen.web.app.client.model;

/**
 * Weather response model
 */
public final class WeatherApiResponse {
    private String latitude;
    private String longitude;
    private DailyWind daily;
    private HourlyTemperature hourly;

    public WeatherApiResponse(String latitude, String longitude, DailyWind daily, HourlyTemperature hourly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.daily = daily;
        this.hourly = hourly;
    }

    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public DailyWind getDaily() {
        return daily;
    }
    public HourlyTemperature getHourly() {
        return hourly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherApiResponse)) return false;

        WeatherApiResponse that = (WeatherApiResponse) o;

        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (daily != null ? !daily.equals(that.daily) : that.daily != null) return false;
        return hourly != null ? hourly.equals(that.hourly) : that.hourly == null;
    }

    @Override
    public int hashCode() {
        int result = latitude != null ? latitude.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (daily != null ? daily.hashCode() : 0);
        result = 31 * result + (hourly != null ? hourly.hashCode() : 0);
        return result;
    }
}
