package com.example.movie_service.Repositories;

import com.example.movie_service.Entities.MovieEntities.Movie;
import com.example.movie_service.Entities.UserEntities.MoviePurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import javax.transaction.Transactional;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findById(long id);

    Movie findByTitle(String title);


    void removeMovieByTitle(String title);

    @Transactional
    void removeMovieById(long id);

    Movie findMovieByTitle(String title);

    @Transactional
    @Modifying
    @Query(value = """
            select * from movies
            inner join movie_purchase mp on movies.id = mp.movies_id
            and users_id = ?""", nativeQuery = true)
    List<Movie> findMoviesByMoviePurchases(long id);


    @Transactional
    @NonNull
    @Modifying
    @Query(value = "select * from movies", nativeQuery = true)
    List<Movie> findAll();

    @Transactional
    @NonNull
    @Modifying
    @Query(value = "select * from movies where engagement_id = ?", nativeQuery = true)
    List<Movie> findAllByEngagement(long id);


    @Transactional
    @Modifying
    @Query(value = """
            insert into movies(release_year, title, synopsis,engagement_id,director_id)
            values (?,?,?,?,?)""", nativeQuery = true)
    void addMovie(int releaseYear, String title, String synopsis, Long engagement_id, Long director_id);

    @Transactional
    @NonNull
    @Modifying
    @Query(value = "update movies set title = ? where id = ?", nativeQuery = true)
    void updateTitle(String title, long id);

    @Transactional
    @NonNull
    @Modifying
    @Query(value = "update movies set release_year = ? where id = ?", nativeQuery = true)
    void updateReleaseYear(int year, long id);

    @Transactional
    @NonNull
    @Modifying
    @Query(value = "update movies set synopsis = ? where id = ?", nativeQuery = true)
    void updateSynopsis(String synopsis, long id);

    @Transactional
    @NonNull
    @Modifying
    @Query(value = "update movies set engagement_id = ? where id = ?", nativeQuery = true)
    void updateEngagement(long engagement_id, long id);

    @Transactional
    @NonNull
    @Modifying
    @Query(value = "update movies set director_id = ? where id = ?", nativeQuery = true)
    void updateDirector(long director_id, long id);

    @Transactional
    @NonNull
    @Modifying
    @Query(value = "update movie_genre set genre_id = ? where movie_id = ?", nativeQuery = true)
    void updateGenre(long genre_id, long id);

}
