package com.example.movie_service.Repositories;

import com.example.movie_service.Entities.MovieEntities.Director;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Director findDirectorById(Long id);

    Director findDirectorByFirstNameAndLastName(String firstName, String lastName);



    void removeDirectorById(long id);

    void removeDirectorByFirstNameAndLastName(String firstName, String lastName);

    @Transactional
    @Modifying
    @Query(value = "insert into directors(first_name, last_name) VALUES (?,?)",nativeQuery = true)
    void insertNewDirector(String firstName, String secondName);

}
