package com.harmony.service;

import com.harmony.dto.LocationsResponse;
import com.harmony.entity.Locations;
import com.harmony.repository.LocationsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationsService {

    @Autowired
    private LocationsRepository LocationsRepository;

    public List<LocationsResponse> getLocations() {
        List<Locations> entities = LocationsRepository.findAll();
        return entities
                .stream()
                .map(entity -> new LocationsResponse(entity.getLocationName()))
                .collect(Collectors.toList());
    }
}
