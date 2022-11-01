package com.example.movie_service.Repositories;

import com.example.movie_service.Entities.UserEntities.MoviePurchase;
import com.example.movie_service.Entities.UserEntities.Purchase;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface MoviePurchaseRepository extends JpaRepository<MoviePurchase, Long> {
    @NonNull
    List<MoviePurchase> findAll();

    @Transactional
    @Modifying
    @Query(value = """
            insert into movie_purchase(date, movies_id, users_id)
            values (?,?,?)""", nativeQuery = true)
    void addPurchase(LocalDate date, long movie, long user);


    List<MoviePurchase> findAllByDateBetween(LocalDate data1, LocalDate data2);

    @Transactional
    @Modifying
    @Query(value = """
            select * from movie_purchase
             where to_char(movie_purchase.date, 'YYYYMM') = ?;
                         """, nativeQuery = true)
    List<MoviePurchase> findAllByMonthAndYear(String date);

    @Transactional
    @Modifying
    @Query(value = """
            select * from movie_purchase
             where to_char(movie_purchase.date, 'YYYYMM') <= ?;
                         """, nativeQuery = true)
    List<MoviePurchase> findAllBeforeMonthAndYear(String date);

}
