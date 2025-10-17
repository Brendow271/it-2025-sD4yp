package com.harmony.service;

import com.harmony.dto.InstrumentsResponse;
import com.harmony.entity.Instruments;
import com.harmony.repository.InstrumentsRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstrumentsService {

    @Autowired
    private InstrumentsRepository instrumentsRepository;

    public List<InstrumentsResponse> getInstruments(){
        List<Instruments> entities = instrumentsRepository.findAll();
        return entities.stream()
                .map(i -> new InstrumentsResponse(i.getInstrumentName(), i.getInstrumentType()))
                .collect(Collectors.toList());
    }
}
