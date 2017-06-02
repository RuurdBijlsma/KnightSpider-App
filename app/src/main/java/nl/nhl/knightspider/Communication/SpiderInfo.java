package nl.nhl.knightspider.Communication;

import com.google.gson.Gson;

/**
 * Created by jorn on 6/2/17.
 */

public final class SpiderInfo {
    private int battery;
    private float slope;
    private float cpuUsage;
    private float cpuTemperature;

    public int getBattery() {
        return battery;
    }

    public float getSlope() {
        return slope;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }

    @Override
    public String toString() {
        return String.format("battery: %s\nslope: %s\ncpu usage: %s\ncpu temperature: %s\n",
                getBattery(),
                getSlope(),
                getCpuUsage(),
                getCpuTemperature());
    }

    public float getCpuTemperature() {
        return cpuTemperature;
    }

    public static SpiderInfo fromJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, SpiderInfo.class);
        }
        catch(Exception e) {
            return null;
        }
    }
}
