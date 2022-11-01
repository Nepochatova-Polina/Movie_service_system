package com.example.movie_service.Entities.DTO;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MovieDTO {
    private String title;
    private Integer releaseYear;
    private String synopsis;
    private String genre;
    private String director;
    private String engagement;
    private float price = 0;

    public MovieDTO(String title, Integer releaseYear, String synopsis, String genre, String director, String engagement) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.synopsis = synopsis;
        this.genre = genre;
        this.director = director;
        this.engagement = engagement;
    }

    public MovieDTO(String title) {
        this.title = title;
    }
}
