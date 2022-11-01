package com.example.movie_service.Repositories;

import com.example.movie_service.Entities.UserEntities.Purchase;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @NonNull
    List<Purchase> findAll();

    List<Purchase> findAllByDateBetween(LocalDate data1, LocalDate data2);

    @Transactional
    @Modifying
    @Query(value = """
            select * from purchase
             where to_char(purchase.date, 'YYYYMM') = ?;
                         """, nativeQuery = true)
    List<Purchase> findAllByMonthAndYear(String date);
}
