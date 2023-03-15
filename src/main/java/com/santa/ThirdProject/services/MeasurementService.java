package com.santa.ThirdProject.services;

import com.santa.ThirdProject.models.Measurement;
import com.santa.ThirdProject.models.Sensor;
import com.santa.ThirdProject.repositories.MeasurementRepository;
import com.santa.ThirdProject.util.measurement.RainingNotFoundException;
import com.santa.ThirdProject.util.measurement.ValuesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    @Transactional
    public void add(Measurement measurement) {

        Optional<Sensor> sensor = sensorService.loadSensorByName(measurement.getSensor().getName());
        measurement.setSensor(sensor.get());
        measurementRepository.save(measurement);

    }

    @Transactional(readOnly = true)
    public List<Float> getValue() {

        Optional<List<Float>> findValues = measurementRepository.findAllValue();

        if (findValues.get().isEmpty()) {
            throw new ValuesNotFoundException("Values do not exist");
        }

        return findValues.get();

    }

    @Transactional(readOnly = true)
    public Integer getRaining() {

        Optional<Integer> findRaining = measurementRepository.findAllRaining();

        if (findRaining.isEmpty()) {
            throw new RainingNotFoundException("Raining do not exist");
        }

        return findRaining.get();

    }

}
