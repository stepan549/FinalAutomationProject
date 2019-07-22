package model;

import java.util.List;
import java.util.Map;

public class SettingsData {
    private String name;
    private Map<String, Object> data;

    public String getName() {
        return name;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SettingsData{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
