package com.harmony.service;

import com.harmony.dto.GenresResponse;
import com.harmony.entity.Genres;
import com.harmony.repository.GenresRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenresService {

    @Autowired
    private GenresRepository genresRepository;

    public List<GenresResponse> getGenres() {
        List<Genres> entities = genresRepository.findAll();
        return entities
                .stream()
                .map(entity -> new GenresResponse(entity.getGenresName()))
                .collect(Collectors.toList());
    }
}
