package com.harmony.repository;

import com.harmony.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenresRepository extends JpaRepository<Genres, String> {

    List<Genres> findAll();
}
