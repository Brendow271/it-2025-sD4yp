package com.harmony.repository;

import com.harmony.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    Optional<UserAuth> findByEmail(String email);
    
    boolean existsByEmail(String email);
   
    Optional<UserAuth> findByEmailAndIsActiveTrue(String email);
    
    List<UserAuth> findByIsActiveTrue();
}
