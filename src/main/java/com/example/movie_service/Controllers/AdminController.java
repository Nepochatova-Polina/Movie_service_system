package com.example.movie_service.Controllers;

import com.example.movie_service.Entities.DTO.MovieDTO;
import com.example.movie_service.Entities.MovieEntities.Director;
import com.example.movie_service.Entities.MovieEntities.Engagement;
import com.example.movie_service.Entities.MovieEntities.Genre;
import com.example.movie_service.Entities.MovieEntities.Movie;
import com.example.movie_service.Entities.UserEntities.MoviePurchase;
import com.example.movie_service.Entities.UserEntities.Purchase;
import com.example.movie_service.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@Controller

public class AdminController {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private EngagementRepository engagementRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private MoviePurchaseRepository moviePurchaseRepository;

    private final ModelAndView modelAndView = new ModelAndView();

    @GetMapping("/admin")
    public ModelAndView adminPage() {
        modelAndView.setViewName("Admin/home.html");
        return modelAndView;
    }

    // CREATE
    @GetMapping("/admin/createMovie")
    @ResponseBody
    public ModelAndView addMovie() {
        modelAndView.setViewName("Admin/create.html");
        return modelAndView;
    }

    @PostMapping("/admin/createMovie")
    @ResponseBody
    public ModelAndView addMovieFromForm(MovieDTO movieDTO) {
        long director_id = 0;
        List<String> director = List.of(movieDTO.getDirector().split(" "));
        Director newDirector = directorRepository.findDirectorByFirstNameAndLastName(director.get(0), director.get(1));
        if (newDirector != null) {
            director_id = newDirector.getId();
        } else {
            directorRepository.insertNewDirector(director.get(0), director.get(1));
            director_id = directorRepository.findDirectorByFirstNameAndLastName(director.get(0), director.get(1)).getId();
        }
        long engagement_id = engagementRepository.findEngagementByEngagement(movieDTO.getEngagement()).getId();
        movieRepository.addMovie(movieDTO.getReleaseYear(), movieDTO.getTitle(), movieDTO.getSynopsis(), engagement_id, director_id);
        long id = movieRepository.findMovieByTitle(movieDTO.getTitle()).getId();
        genreRepository.addMovieGenre(id, genreRepository.findGenreByGenre(movieDTO.getGenre()).getId());
        directorRepository.insertNewDirector(director.get(0), director.get(1));
        modelAndView.setViewName("Admin/home.html");
        return modelAndView;
    }

    // DELETE
    @GetMapping("/admin/deleteMovie")
    public ModelAndView deleteMovie() {
        modelAndView.setViewName("Admin/delete.html");
        return modelAndView;
    }

    @PostMapping("/admin/deleteMovie")
    @ResponseBody
    public ModelAndView findMovieToRemove(String title, Map<String, Object> model) {
        Movie movie = movieRepository.findMovieByTitle(title);
        String directorName = movie.getDirector().getFirstName() + " " + movie.getDirector().getLastName();
        Engagement engagement = movie.getEngagement();
        MovieDTO movieDTO = new MovieDTO(movie.getTitle(), movie.getReleaseYear(), movie.getSynopsis(), movie.getGenre().stream().toList().get(0).getGenre(),
                directorName, engagement.getEngagement());
        model.put("movies", movieDTO);
        modelAndView.setViewName("Admin/delete.html");
        return modelAndView;
    }

    @PostMapping("/admin/removeMovie/{title}")
    @ResponseBody
    public ModelAndView removeMovie(@PathVariable String title) {
        long id = movieRepository.findMovieByTitle(title).getId();
        movieRepository.removeMovieById(id);
        genreRepository.removeMovieGenreById(id);
        modelAndView.setViewName("Admin/delete.html");
        return modelAndView;
    }

    //  STATISTIC FUNCTIONAL
    @GetMapping("/admin/statistic")
    public ModelAndView statisticPage() {
        modelAndView.setViewName("Admin/purchaseStatistic.html");
        return modelAndView;
    }

    @PostMapping("/admin/statistic")
    public ModelAndView showStatistic(String firstDate, String lastDate, Map<String, Object> model) throws ParseException {
        List<Purchase> purchases = purchaseRepository.findAllByDateBetween(LocalDate.parse(firstDate),LocalDate.parse(lastDate));
        double income = 0.0;
        for (Purchase purchase : purchases) {
            income += purchase.getEngagement().getPrice();
        }
        model.put("income",String.format("%.2f", income));
        model.put("purchases", purchases);
        model.put("first_date", firstDate);
        model.put("last_date", lastDate);
        modelAndView.setViewName("Admin/purchaseStatistic.html");
        return modelAndView;
    }
    @PostMapping("/admin/MonthStatistic")
    public ModelAndView showMonthStatistic(String month, Map<String, Object> model) {
        String date = month.replace("-","");
        List<Purchase> purchases = purchaseRepository.findAllByMonthAndYear(date);
        double income = 0.0;
        for (Purchase purchase : purchases) {
            income += purchase.getEngagement().getPrice();
        }
        model.put("income", String.format("%.2f", income));
        model.put("purchases", purchases);
        model.put("Month_Year", month);
        modelAndView.setViewName("Admin/purchaseStatistic.html");
        return modelAndView;
    }
    @PostMapping("/admin/BeforeMonthStatistic")
    public ModelAndView showBeforeMonthStatistic(String before_month, Map<String, Object> model) {
        String date = before_month.replace("-","");
        List<Purchase> purchases = purchaseRepository.findAllBeforeMonthAndYear(date);
        double income = 0.0;
        for (Purchase purchase : purchases) {
            income += purchase.getEngagement().getPrice();
        }
        model.put("income", String.format("%.2f", income));
        model.put("purchases", purchases);
        model.put("Month_Year", before_month);
        modelAndView.setViewName("Admin/purchaseStatistic.html");
        return modelAndView;
    }


    //  PURCHASE STATISTIC FUNCTIONAL
    @GetMapping("/admin/MovieStatistic")
    public ModelAndView movieStatisticPage() {
        modelAndView.setViewName("Admin/MoviePurchaseStatistic.html");
        return modelAndView;
    }

    @PostMapping("/admin/MovieStatistic")
    public ModelAndView showMovieStatistic(String firstDate, String lastDate, Map<String, Object> model) throws ParseException {
        List<MoviePurchase> purchases = moviePurchaseRepository.findAllByDateBetween(LocalDate.parse(firstDate),LocalDate.parse(lastDate));
        double income = 0.0;
        for (MoviePurchase purchase : purchases) {
            income += purchase.getMovie().getPrice();
        }
        model.put("income", String.format("%.2f", income));
        model.put("purchases", purchases);
        model.put("first_date", firstDate);
        model.put("last_date", lastDate);
        modelAndView.setViewName("Admin/MoviePurchaseStatistic.html");
        return modelAndView;
    }

    @PostMapping("/admin/MonthMovieStatistic")
    public ModelAndView showMonthMovieStatistic(String month, Map<String, Object> model) {
        String date = month.replace("-","");
        List<MoviePurchase> purchases = moviePurchaseRepository.findAllByMonthAndYear(date);
        double income = 0;
        for (MoviePurchase purchase : purchases) {
            income += purchase.getMovie().getPrice();
        }
        model.put("income", String.format("%.2f", income));
        model.put("purchases", purchases);
        model.put("Month_Year", month);
        modelAndView.setViewName("Admin/MoviePurchaseStatistic.html");
        return modelAndView;
    }
    @PostMapping("/admin/BeforeMonthMovieStatistic")
    public ModelAndView showMBeforeonthMovieStatistic(String before_month, Map<String, Object> model) {
        String date = before_month.replace("-","");
        List<MoviePurchase> purchases = moviePurchaseRepository.findAllBeforeMonthAndYear(date);
        double income = 0;
        for (MoviePurchase purchase : purchases) {
            income += purchase.getMovie().getPrice();
        }
        model.put("income", String.format("%.2f", income));
        model.put("purchases", purchases);
        model.put("Month_Year", before_month);
        modelAndView.setViewName("Admin/MoviePurchaseStatistic.html");
        return modelAndView;
    }


    //  UPDATE FUNCTIONAL

    @GetMapping("/admin/updateMovie")
    public ModelAndView updatePage() {
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }

    @PostMapping("/admin/updateMovie")
    @ResponseBody
    public ModelAndView findMovieToUpdate(String title, Map<String, Object> model) {
        Movie movie = movieRepository.findMovieByTitle(title);
        String directorName = movie.getDirector().getFirstName() + " " + movie.getDirector().getLastName();
        Engagement engagement = movie.getEngagement();
        MovieDTO movieDTO = new MovieDTO(movie.getTitle(), movie.getReleaseYear(), movie.getSynopsis(), movie.getGenre().stream().toList().get(0).getGenre(),
                directorName, engagement.getEngagement());
        model.put("movies", movieDTO);
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }

    @GetMapping("/admin/updateTitle/{title}")
    public ModelAndView updateTitle(@PathVariable String title, String newTitle, Map<String, Object> model) {
        movieRepository.updateTitle(newTitle, movieRepository.findByTitle(title).getId());
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }

    @GetMapping("/admin/updateYear/{title}")
    public ModelAndView updateReleaseYear(@PathVariable String title, int newYear, Map<String, Object> model) {
        movieRepository.updateReleaseYear(newYear, movieRepository.findByTitle(title).getId());
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }

    @GetMapping("/admin/updateSynopsis/{title}")
    public ModelAndView updateSynopsis(@PathVariable String title, String newSynopsis, Map<String, Object> model) {
        movieRepository.updateSynopsis(newSynopsis, movieRepository.findByTitle(title).getId());
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }

    @GetMapping("/admin/updateDirector/{title}")
    public ModelAndView updateDirector(@PathVariable String title, String newDirector, Map<String, Object> model) {
        String[] directorNames = newDirector.split(" ");
        Director director = directorRepository.findDirectorByFirstNameAndLastName(directorNames[0], directorNames[1]);
        if (director == null) {
            directorRepository.insertNewDirector(directorNames[0], directorNames[1]);
            director = directorRepository.findDirectorByFirstNameAndLastName(directorNames[0], directorNames[1]);

        }
        movieRepository.updateDirector(director.getId(), movieRepository.findByTitle(title).getId());
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }

    @GetMapping("/admin/updateGenre/{title}")
    public ModelAndView updateGenre(@PathVariable String title, String newGenre, Map<String, Object> model) {
        Genre genre = genreRepository.findGenreByGenre(newGenre);
        movieRepository.updateGenre(genre.getId(), movieRepository.findByTitle(title).getId());
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }

    @GetMapping("/admin/updateEngagement/{title}")
    public ModelAndView updateEngagement(@PathVariable String title, String newEngagement, Map<String, Object> model) {
        Engagement engagement = engagementRepository.findEngagementByEngagement(newEngagement.toUpperCase());
        movieRepository.updateEngagement(engagement.getId(), movieRepository.findByTitle(title).getId());
        modelAndView.setViewName("Admin/update.html");
        return modelAndView;
    }
}
