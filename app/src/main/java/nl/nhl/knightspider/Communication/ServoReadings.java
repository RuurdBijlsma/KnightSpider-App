package nl.nhl.knightspider.Communication;

import com.google.gson.Gson;

/**
 * Created by jorn on 6/2/17.
 */

public class ServoReadings {
    private int id;
    private float voltage;
    private int temperature;
    private float load;
    private float position;

    public int getId() {
        return id;
    }

    public float getVoltage() {
        return voltage;
    }

    public int getTemperature() {
        return temperature;
    }

    public float getLoad() {
        return load;
    }

    public float getPosition() {
        return position;
    }

    public static ServoReadings fromJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, ServoReadings.class);
        }
        catch(Exception e) {
            return null;
        }
    }

}
