package com.santa.ThirdProject.util.sensor;

import com.santa.ThirdProject.dto.SensorDTO;
import com.santa.ThirdProject.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(SensorDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SensorDTO sensor = (SensorDTO) target;

        if (sensorService.loadSensorByName(sensor.getName()).isPresent())
            errors.rejectValue("name", "", "This name already exist");

    }
}
