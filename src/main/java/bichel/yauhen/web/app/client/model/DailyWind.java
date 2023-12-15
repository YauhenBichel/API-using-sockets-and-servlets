package bichel.yauhen.web.app.client.model;

import java.util.List;

/**
 * Item daily in weather response model
 */
public final class DailyWind {
    private List<String> time;
    private List<String> wind_speed_10m_max;

    public DailyWind(List<String> time, List<String> wind_speed_10m_max) {
        this.time = time;
        this.wind_speed_10m_max = wind_speed_10m_max;
    }

    public List<String> getTime() {
        return time;
    }
    public List<String> getWind_speed_10m_max() {
        return wind_speed_10m_max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyWind)) return false;

        DailyWind dailyWind = (DailyWind) o;

        if (time != null ? !time.equals(dailyWind.time) : dailyWind.time != null) return false;
        return wind_speed_10m_max != null ? wind_speed_10m_max.equals(dailyWind.wind_speed_10m_max) : dailyWind.wind_speed_10m_max == null;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (wind_speed_10m_max != null ? wind_speed_10m_max.hashCode() : 0);
        return result;
    }
}
