package com.santa.ThirdProject.dto;

import com.santa.ThirdProject.models.Sensor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MeasurementDTO {

    @NotNull(message = "Value should not be empty")
    @Min(value = -100, message = "Value should be greater -100")
    @Max(value = 100, message = "Value shouldn't be greater 100")
    private float value;

    @NotNull(message = "Value raining should not be empty")
    private boolean raining;
    private Sensor sensor;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
