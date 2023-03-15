package com.santa.ThirdProject.repositories;

import com.santa.ThirdProject.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    @Query("select m.value from Measurement m")
    Optional<List<Float>> findAllValue();

    @Query("select count(m.raining) from Measurement m where m.raining = true")
    Optional<Integer> findAllRaining();

}
