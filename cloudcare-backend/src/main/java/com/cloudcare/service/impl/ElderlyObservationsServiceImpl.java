package com.cloudcare.service.impl;

import com.cloudcare.entity.ElderlyObservations;
import com.cloudcare.mapper.ElderlyObservationsMapper;
import com.cloudcare.service.ElderlyObservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElderlyObservationsServiceImpl implements ElderlyObservationsService {

    @Autowired
    private ElderlyObservationsMapper observationsMapper;

    @Override
    public List<ElderlyObservations> getAllObservations() {
        return observationsMapper.selectAllObservations();
    }

    @Override
    public List<ElderlyObservations> getObservationsByElderlyIdOrderByTimeDesc(int elderlyId) {
        return observationsMapper.selectObservationsByElderlyIdOrderByTimeDesc(elderlyId);
    }

    @Override
    public List<ElderlyObservations> getObservationsByElderlyIdAndTimeRange(int elderlyId, String startTime, String endTime) {
        return observationsMapper.selectObservationsByElderlyIdAndTimeRange(elderlyId, startTime, endTime);
    }

    @Override
    public boolean addObservationWithLocation(ElderlyObservations observation) {
        return observationsMapper.insertObservationWithLocation(observation) > 0;
    }

    @Override
    public boolean updateObservationWithLocation(ElderlyObservations observation) {
        return observationsMapper.updateObservationWithLocation(observation) > 0;
    }

    @Override
    public boolean deleteObservation(int id) {
        return observationsMapper.deleteObservation(id) > 0;
    }
}