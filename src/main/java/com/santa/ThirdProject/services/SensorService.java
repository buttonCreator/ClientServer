package com.santa.ThirdProject.services;

import com.santa.ThirdProject.models.Sensor;
import com.santa.ThirdProject.repositories.SensorRepository;
import com.santa.ThirdProject.util.sensor.SensorNameExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void save(Sensor sensor) {

        sensor.setCreatedAt(LocalDateTime.now());
        sensorRepository.save(sensor);

    }

    public Optional<Sensor> loadSensorByName(String name) throws SensorNameExistException {

        return sensorRepository.findByName(name);

    }

}
