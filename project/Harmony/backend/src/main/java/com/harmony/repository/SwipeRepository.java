package com.harmony.repository;

import com.harmony.entity.Swipe;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    Optional<Swipe> findByUserId1AndUserId2(Long userId1, Long userId2);

    @Query("SELECT s FROM Swipe s WHERE (s.userId1 = :userId OR s.userId2 = :userId)")
    List<Swipe> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM Swipe s WHERE (s.userId1 = :userId1 AND s.userId2 = :userId2) OR (s.userId1 = :userId2 AND s.userId2 = :userId1)")
    Optional<Swipe> findSwipeBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT s FROM Swipe s WHERE ((s.userId1 = :userId1 AND s.userId2 = :userId2 AND s.decision1 = true AND s.decision2 = true) OR (s.userId1 = :userId2 AND s.userId2 = :userId1 AND s.decision1 = true AND s.decision2 = true))")
    List<Swipe> findMatchesForUser(@Param("userId1") Long userId1);
}
