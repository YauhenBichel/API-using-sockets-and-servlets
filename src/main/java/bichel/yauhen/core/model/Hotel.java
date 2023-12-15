package bichel.yauhen.core.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model Hotel for json file with hotels
 */
public final class Hotel {
    @SerializedName(value = "id")
    private int id;
    @SerializedName(value = "f")
    private String name;
    @SerializedName(value = "ad")
    private String address;
    @SerializedName(value = "ci")
    private String city;
    @SerializedName(value = "pr")
    private String state;
    @SerializedName(value = "ll")
    private Location location;

    public Hotel(int id, String name, String address, String city, String state, Location location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("\n********************");
        sb.append("\n").append(name).append(": ").append(id);
        sb.append("\n").append(address);
        sb.append("\n").append(city).append(", ").append(state);
        sb.append("\n").append(", location=").append(location);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;

        Hotel hotel = (Hotel) o;

        if (id != hotel.id) return false;
        if (name != null ? !name.equals(hotel.name) : hotel.name != null) return false;
        if (address != null ? !address.equals(hotel.address) : hotel.address != null) return false;
        if (city != null ? !city.equals(hotel.city) : hotel.city != null) return false;
        if (location != null ? !location.equals(hotel.location) : hotel.location != null) return false;
        return state != null ? state.equals(hotel.state) : hotel.state == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
