package com.santa.ThirdProject.util.measurement;

import com.santa.ThirdProject.dto.MeasurementDTO;
import com.santa.ThirdProject.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(MeasurementDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        MeasurementDTO measurementDTO = (MeasurementDTO) target;

        if (sensorService.loadSensorByName(measurementDTO.getSensor().getName()).isEmpty())
            errors.rejectValue("sensor", "", "This name: " +
                    measurementDTO.getSensor().getName() + " not found");

    }
}
