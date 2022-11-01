package com.example.movie_service.Repositories;

import com.example.movie_service.Entities.MovieEntities.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import javax.transaction.Transactional;
import java.util.List;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {
    Engagement findEngagementById(Long id);
    Engagement findEngagementByEngagement(String engagement);
    @NonNull
    @Modifying
    @Query(value = "select * from engagement", nativeQuery = true)
    List<Engagement> findAll();

    @Transactional
    @Modifying
    @Query(value = "insert into engagement(engagement, price) values (?,?)",nativeQuery = true)
    void addEngagement(String engagement, int price);

}
