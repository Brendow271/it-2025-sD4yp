package com.harmony.repository;

import com.harmony.entity.Instruments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentsRepository extends JpaRepository<Instruments, String> {
}
