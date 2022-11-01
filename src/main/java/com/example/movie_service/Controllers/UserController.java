package com.example.movie_service.Controllers;

import com.example.movie_service.Entities.DTO.MovieDTO;
import com.example.movie_service.Entities.MovieEntities.Engagement;
import com.example.movie_service.Entities.MovieEntities.Movie;
import com.example.movie_service.Entities.UserEntities.User;
import com.example.movie_service.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MoviePurchaseRepository moviePurchaseRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private EngagementRepository engagementRepository;

    @Autowired
    private GenreRepository genreRepository;

    private final ModelAndView modelAndView = new ModelAndView();

    Cookie uiColorCookie = new Cookie("BlackJules12_7H", "BlackJules12_7H");

    @GetMapping("/account")
    public ModelAndView userPage(Map<String, Object> model) {
        List<MovieDTO> movies = new ArrayList<>();
        List<Movie> movieList;
        MovieDTO movieDTO;
        Movie movie;
        User user = userRepository.findUserByNickName(uiColorCookie.getName());
        Engagement engagementId = user.getEngagement();
        switch (engagementId.getId().intValue()) {
            case 1 -> movieList = movieRepository.findAllByEngagement(1);
            case 2 -> {
                movieList = movieRepository.findAllByEngagement(1);
                movieList.addAll(movieRepository.findAllByEngagement(2));
            }
            case 3 -> movieList = movieRepository.findAll();
            default -> {
                modelAndView.setViewName("Admin/home.html");
                return modelAndView;
            }
        }
        movieList.addAll(movieRepository.findMoviesByMoviePurchases(user.getId()));
        for (Movie value : movieList) {
            movie = value;
            String directorName = movie.getDirector().getFirstName() + " " + movie.getDirector().getLastName();
            Engagement engagement = movie.getEngagement();
            movieDTO = new MovieDTO(movie.getTitle(), movie.getReleaseYear(), movie.getSynopsis(), movie.getGenre().stream().toList().get(0).getGenre(),
                    directorName, engagement.getEngagement());
            movies.add(movieDTO);
        }
        model.put("movies", movies);
        modelAndView.setViewName("User/account.html");
        return modelAndView;
    }

    @GetMapping("/purchase")
    public ModelAndView purchasePage(Map<String, Object> model) {
        List<MovieDTO> movies = new ArrayList<>();
        List<Movie> movieList;
        MovieDTO movieDTO;
        Movie movie;
        Engagement engagementId = userRepository.findUserByNickName(uiColorCookie.getName()).getEngagement();
        switch (engagementId.getId().intValue()) {
            case 1 -> {
                movieList = movieRepository.findAllByEngagement(2);
                movieList.addAll(movieRepository.findAllByEngagement(3));
            }
            case 2 -> {
                movieList = movieRepository.findAllByEngagement(3);
            }
            case 3 -> {
                modelAndView.setViewName("User/account.html");
                return modelAndView;

            }
            default -> {
                modelAndView.setViewName("Admin/home.html");
                return modelAndView;
            }
        }
        for (Movie value : movieList) {
            movie = value;
            String directorName = movie.getDirector().getFirstName() + " " + movie.getDirector().getLastName();
            Engagement engagement = movie.getEngagement();
            movieDTO = new MovieDTO(movie.getTitle(), movie.getReleaseYear(), movie.getSynopsis(), movie.getGenre().stream().toList().get(0).getGenre(),
                    directorName, engagement.getEngagement(), movie.getPrice());
            movies.add(movieDTO);
        }
        model.put("movies", movies);
        modelAndView.setViewName("User/purchaseFilm.html");
        return modelAndView;
    }

    @PostMapping("/verify/{title}")
    public ModelAndView verifyPurchase(@PathVariable String title, Map<String, Object> model){
        MovieDTO movieDTO = new MovieDTO(title);
        model.put("movies",movieDTO);
        modelAndView.setViewName("User/purchasePopup.html");
        return modelAndView;

    }
    @PostMapping("/verify/createPurchase/{title}")
    public ModelAndView createPurchase(@PathVariable String title, Map<String, Object> model) throws ParseException {
        LocalDate l = LocalDate.now();
        Movie movie = movieRepository.findMovieByTitle(title);
        User user = userRepository.findUserByNickName(uiColorCookie.getName());
        moviePurchaseRepository.addPurchase(l,movie.getId(),user.getId());
        modelAndView.setViewName("User/purchaseFilm.html");
        return modelAndView;

    }
}
