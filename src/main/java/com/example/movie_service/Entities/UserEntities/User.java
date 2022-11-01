package com.example.movie_service.Entities.UserEntities;


import com.example.movie_service.Entities.MovieEntities.Engagement;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickName;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    @Column(unique = true)
    private String password;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> role;

    @ManyToOne
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;

    @OneToMany(mappedBy = "user")
    private List<MoviePurchase> moviePurchases;

}