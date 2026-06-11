package com.harmony.repository;

import com.harmony.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByAge(Integer age);

    @Query(value = "SELECT * FROM user_info WHERE :genre = ANY(genres)", nativeQuery = true)
    List<UserInfo> findByGenres(@Param("genre") String genre);

    @Query(value = "SELECT * FROM user_info WHERE :instrument = ANY(instrument)", nativeQuery = true)
    List<UserInfo> findByInstrument(@Param("instrument") String instrument);
}