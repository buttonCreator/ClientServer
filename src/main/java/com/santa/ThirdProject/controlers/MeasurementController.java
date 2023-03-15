package com.santa.ThirdProject.controlers;

import com.santa.ThirdProject.dto.MeasurementDTO;
import com.santa.ThirdProject.models.Measurement;
import com.santa.ThirdProject.services.MeasurementService;
import com.santa.ThirdProject.util.TemplateError;
import com.santa.ThirdProject.util.measurement.*;
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
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<Float>> getValue() {

        List<Float> listValues = measurementService.getValue();

        return new ResponseEntity<>(listValues, HttpStatus.OK);

    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Integer> getRaining() {

        Integer listRaining = measurementService.getRaining();

        return new ResponseEntity<>(listRaining, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult) {

        measurementValidator.validate(measurementDTO, bindingResult);

        if (bindingResult.hasErrors()) {

            StringBuilder stringErrors = new StringBuilder();

            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrorList) {

                stringErrors.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");

            }

            throw new SensorNotFoundException(stringErrors.toString());

        }

        measurementService.add(convertToMeasurement(measurementDTO));

        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<TemplateError> responseNotFoundName (SensorNotFoundException e) {

        TemplateError measurementError = new TemplateError();
        measurementError.setMessage(e.getMessage());
        measurementError.setTime(LocalDateTime.now());

        return new ResponseEntity<>(measurementError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<TemplateError> responseValuesError (ValuesNotFoundException e) {

        TemplateError valuesError = new TemplateError();

        valuesError.setMessage(e.getMessage());
        valuesError.setTime(LocalDateTime.now());

        return new ResponseEntity<>(valuesError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<TemplateError> responseRainingError (RainingNotFoundException e) {


        TemplateError rainingError = new TemplateError();

        rainingError.setMessage(e.getMessage());
        rainingError.setTime(LocalDateTime.now());

        return new ResponseEntity<>(rainingError, HttpStatus.BAD_REQUEST);

    }

    public Measurement convertToMeasurement (MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

}
