package kz.taxikz.ui.widget.item;

public class City {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCrewGroupId() {
        return crewGroupId;
    }

    public void setCrewGroupId(int crewGroupId) {
        this.crewGroupId = crewGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUdsId() {
        return udsId;
    }

    public void setUdsId(int udsId) {
        this.udsId = udsId;
    }

    private int id;
    private int crewGroupId;
    private int udsId;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (id != city.id) return false;
        if (crewGroupId != city.crewGroupId) return false;
        if (udsId != city.udsId) return false;
        return name != null ? name.equals(city.name) : city.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + crewGroupId;
        result = 31 * result + udsId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", crewGroupId=" + crewGroupId +
                ", udsId=" + udsId +
                ", name='" + name + '\'' +
                '}';
    }
}
