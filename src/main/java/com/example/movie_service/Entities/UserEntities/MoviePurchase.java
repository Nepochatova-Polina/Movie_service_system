package com.example.movie_service.Entities.UserEntities;

import com.example.movie_service.Entities.MovieEntities.Engagement;
import com.example.movie_service.Entities.MovieEntities.Movie;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter

@Entity
@Table(name = "movie_purchase")
public class MoviePurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movies_id")
    private Movie movie;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
