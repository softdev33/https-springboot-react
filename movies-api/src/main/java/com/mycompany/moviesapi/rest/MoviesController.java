package com.mycompany.moviesapi.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.mycompany.moviesapi.exception.MovieNotFoundException;
import com.mycompany.moviesapi.model.Movie;
import com.mycompany.moviesapi.rest.dto.CreateMovieDto;
import com.mycompany.moviesapi.rest.dto.MovieDto;
import com.mycompany.moviesapi.service.MovieService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFacade;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

  private final MovieService movieService;
  private final MapperFacade mapperFacade;

  public MoviesController(MovieService movieService, MapperFacade mapperFacade) {
    this.movieService = movieService;
    this.mapperFacade = mapperFacade;
  }

  @GetMapping
  public List<MovieDto> getMovies() {
    return movieService.getMovies().stream().map(movie -> mapperFacade.map(movie, MovieDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{imdbId}")
  public MovieDto getMovie(@PathVariable String imdbId) throws MovieNotFoundException {
    Movie movie = movieService.validateAndGetMovie(imdbId);
    return mapperFacade.map(movie, MovieDto.class);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public MovieDto createMovie(@Valid @RequestBody CreateMovieDto createMovieDto) {
    Movie movie = mapperFacade.map(createMovieDto, Movie.class);
    movie = movieService.saveMovie(movie);
    return mapperFacade.map(movie, MovieDto.class);
  }

  @DeleteMapping("/{imdbId}")
  public MovieDto deleteMovie(@PathVariable String imdbId) throws MovieNotFoundException {
    Movie movie = movieService.validateAndGetMovie(imdbId);
    movieService.deleteMovie(movie);
    return mapperFacade.map(movie, MovieDto.class);
  }

}