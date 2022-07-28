package com.frank.server.dao;

import com.frank.server.model.Movie;
import com.frank.server.datastore.MovieDataStore;

import java.util.List;

public class MovieDao {

    // Hold movies retrieved from the external API
    static final MovieDataStore movieDataBase;

    // Because the movieDataStore object is static, we need a static initializer to set it up
    // Due to static data having the ability to exist without any instantiated objects of class.
    //     we don't want to initialize the static data in a constructor
    //        because a constructor only runs when an object of the class is instantiated
    //     so we need a way to initialize static data before any objects are instantiated
    //       that's why static initializers exist

    static {  // a static initializer has no name, no parameters and no return type
        movieDataBase = new MovieDataStore();  // Initialize the static reference
    }

    public List<Movie> getAllMovies() {
        return movieDataBase.getAllMovies();
    }

    public Movie findMovie(int movieId) throws InterruptedException {
        return movieDataBase.findMovie(movieId);
    }
} // End of MovieDao class

