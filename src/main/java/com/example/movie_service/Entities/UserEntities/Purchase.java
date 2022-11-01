package com.example.movie_service.Entities.UserEntities;


import com.example.movie_service.Entities.MovieEntities.Engagement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User users;

    @ManyToOne
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
