package com.example.movie_service.Entities.MovieEntities;

import com.example.movie_service.Entities.UserEntities.Purchase;
import com.example.movie_service.Entities.UserEntities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "engagement")
public class Engagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String engagement;
    private Float price;


    @OneToMany(mappedBy = "engagement")
    private List<Purchase> purchases;

}
