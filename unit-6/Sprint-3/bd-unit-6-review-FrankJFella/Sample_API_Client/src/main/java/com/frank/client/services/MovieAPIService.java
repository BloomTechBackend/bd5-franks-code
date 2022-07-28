package com.frank.client.services;

import com.frank.client.services.model.Movie;

import com.frank.client.services.model.MovieAPICachingServiceKey;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MovieAPIService {

    // Instantiate a RestTemplate object to make RESTful calls to an server
    private RestTemplate callApi = new RestTemplate();

    private static final String BASE_API_UTL = "http://localhost:8080";

    public List<Movie> getAllMovies() {

        // RestTemplate can return an array of objects automatically for us, so here is an array to hold the returned objects
        Movie[] movieArray;   // Hold the Movie  objects returned from the API the call
        // There is no easy way to have a RestTemplate method return a Java List object

        // Call the API to get all the movies ans store them in our array
        //  RestTemplate    getForObject(               URL                , data-type-to-return)
        movieArray = callApi.getForObject("http://localhost:8080/movie", Movie[].class);

        return Arrays.asList(movieArray);   // convert the array we have to a List for the method
    }

    // Go to the server to retrieve a single movive
    public Movie getMovie(int movieNumber) {

        // RestTemplate can return an array of objects automatically for us, so here is an array to hold the returned objects
        Movie aMovie;   // Hold the Movie  objects returned from the API the call
        // There is no easy way to have a RestTemplate method return a Java List object

        // Call the API to get all the movies ans store them in our array
        //  RestTemplate    getForObject(               URL                               , data-type-to-return)
        aMovie = callApi.getForObject("http://localhost:8080/movie/find?id=" + movieNumber, Movie.class);

        return aMovie;   // return the Movie from the data store
    }
    // This method is required when caching is activated for the application
    public Movie getMovie(MovieAPICachingServiceKey cacheKey) {
        return getMovie(cacheKey.getMovieNumber());  // Call the original DAO method with the values from the cache key
    }
}