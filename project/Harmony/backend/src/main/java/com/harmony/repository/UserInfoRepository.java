package com.harmony.repository;

import com.harmony.entity.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByAge(Integer age);

    Optional<UserInfo> findByGenres(String genre);

    Optional<UserInfo> findByInstrument(String instrument);
}