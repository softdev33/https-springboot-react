package com.ivanfranchin.moviesshell.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.moviesshell.dto.AddMovieRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MovieApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MovieApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${movies-api.url}")
    private String movieApiUrl;

    public ResponseEntity<String> getMovies() {
        return restTemplate.getForEntity(movieApiUrl, String.class);
    }

    public ResponseEntity<String> getMovie(String imdbId) {
        String url = String.format("%s/%s", movieApiUrl, imdbId);
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> addMovie(String imdbId, String title, String director, int year) throws JsonProcessingException {
        AddMovieRequest addMovieRequest = new AddMovieRequest(imdbId, title, director, year);
        String addMovieRequestStr = objectMapper.writeValueAsString(addMovieRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(addMovieRequestStr, headers);
        return restTemplate.postForEntity(movieApiUrl, request, String.class);
    }

    public ResponseEntity<String> deleteMovie(String imdbId) {
        String url = String.format("%s/%s", movieApiUrl, imdbId);
        return restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}