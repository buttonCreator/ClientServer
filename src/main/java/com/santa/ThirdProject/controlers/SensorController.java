package com.santa.ThirdProject.controlers;

import com.santa.ThirdProject.dto.SensorDTO;
import com.santa.ThirdProject.models.Sensor;
import com.santa.ThirdProject.services.SensorService;
import com.santa.ThirdProject.util.TemplateError;
import com.santa.ThirdProject.util.sensor.SensorNotCreatedException;
import com.santa.ThirdProject.util.sensor.SensorValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;

    private final ModelMapper modelMapper;


    @Autowired
    public SensorController(SensorService sensorService, SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> getDataOfRain(@RequestBody @Valid SensorDTO sensorDTO,
                                                BindingResult bindingResult) {

        sensorValidator.validate(sensorDTO, bindingResult);

        if (bindingResult.hasErrors()) {

            StringBuilder stringErrors = new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrorList) {

                stringErrors.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");

            }

            throw new SensorNotCreatedException(stringErrors.toString());

        }

        sensorService.save(convertToSensor(sensorDTO));

        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<TemplateError> sensorNameErrorResponseEntity (SensorNotCreatedException e) {

        TemplateError sensorNameError = new TemplateError();
        sensorNameError.setMessage(e.getMessage());
        sensorNameError.setTime(LocalDateTime.now());

        return new ResponseEntity<>(sensorNameError, HttpStatus.BAD_REQUEST);

    }

    public Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

}
