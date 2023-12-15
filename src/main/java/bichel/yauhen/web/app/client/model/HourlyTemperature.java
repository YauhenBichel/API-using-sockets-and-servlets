package bichel.yauhen.web.app.client.model;

import java.util.List;

/**
 * Item hourly in weather response model
 */
public final class HourlyTemperature {
    private List<String> time;
    private List<String> temperature_2m;

    public HourlyTemperature(List<String> time, List<String> temperature_2m) {
        this.time = time;
        this.temperature_2m = temperature_2m;
    }

    public List<String> getTime() {
        return time;
    }
    public List<String> getTemperature_2m() {
        return temperature_2m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HourlyTemperature)) return false;

        HourlyTemperature that = (HourlyTemperature) o;

        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        return temperature_2m != null ? temperature_2m.equals(that.temperature_2m) : that.temperature_2m == null;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (temperature_2m != null ? temperature_2m.hashCode() : 0);
        return result;
    }
}
