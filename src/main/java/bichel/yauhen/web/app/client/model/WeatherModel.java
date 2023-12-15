package bichel.yauhen.web.app.client.model;

/**
 * Project 4 weather model
 */
public final class WeatherModel {
    private String temperature;
    private String windSpeed;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherModel)) return false;

        WeatherModel that = (WeatherModel) o;

        if (temperature != null ? !temperature.equals(that.temperature) : that.temperature != null) return false;
        return windSpeed != null ? windSpeed.equals(that.windSpeed) : that.windSpeed == null;
    }

    @Override
    public int hashCode() {
        int result = temperature != null ? temperature.hashCode() : 0;
        result = 31 * result + (windSpeed != null ? windSpeed.hashCode() : 0);
        return result;
    }
}
