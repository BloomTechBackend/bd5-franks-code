package com.frank.server.datastore;

import com.frank.server.model.Movie;
import com.frank.server.model.MovieDbResponse;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class MovieDataStore {

    //         movie-id, aMovie
    private Map<Integer, Movie> movieDataStore = new HashMap<>();


//   static {
//       System.out.println("static initializer for movieDataStore");
//        loadMovieDataStore();
//    }

    public MovieDataStore() {
        loadMovieDataStore();
    }

    public List<Movie> getAllMovies() {
       return (new ArrayList<>(movieDataStore.values()));
    }

    public Movie findMovie(int movieNumber) throws InterruptedException {
        Random aRandomObject = new Random();
        // This pause is purposely included to increase response time
        // pause anywhere from 1 second to 10 seconds
        Thread.sleep(new Random().nextInt(10000 - 1000) + 1000);
        return movieDataStore.get(movieNumber);
    }

    // Get the movie data from an external API
    private void loadMovieDataStore() {

        // RestTemplate is a framework for doing RESTful HTTP calls to a server
        // Define a RestTemplate object to call the API Server
        RestTemplate callApi = new RestTemplate();

        // Values used to retrieve movies from movie API
        final int NUM_MOVIES_TO_GET            = 100; // The number movies tol hold in the data store
        final int NUM_MOVIES_RETURNED_PER_PAGE = 20;  // API returns "pages" of movies - 20 movies per page
        final int NUM_PAGES                    = NUM_MOVIES_TO_GET / NUM_MOVIES_RETURNED_PER_PAGE;

        int movieNumber = 0;

        // RestTemplate can return an array of objects automatically for us, so here is an array to hold the returned objects
        Movie[] movieArray;   // Hold the Movie objects returned from the API the call

        MovieDbResponse movieDbResponse = null;

        // Call the API to get the number of pages of movies we want
        for(int pageNumber = 1; pageNumber <= NUM_PAGES; pageNumber++) {

            // Call the API using the RestTemplate getForObject() method
            // In this example the constructURL() helper methods is used to create the URL
            //    because the URL is a long String and we need to substitute values in it
            //    so rather than use String concatenation for String format method, we chose a helper method
            //                       .getForObject(URL-for-API             , type-of-data-to-return
            movieDbResponse = callApi.getForObject(constructURL(pageNumber), MovieDbResponse.class);
            movieNumber = movieDataStore.size() + 1;
            // Loop through the results array assigning each movie retrieved to our data store Map
            for (int i = 0; i < movieDbResponse.getResults().length; i++) {
                //movieDataStore.put(movieDbResponse.getResults()[i].getMovieId(),movieDbResponse.getResults()[i]);
                movieDbResponse.getResults()[i].setMovieNumber(movieNumber);
                movieDataStore.put(movieNumber, movieDbResponse.getResults()[i]);
                movieNumber++;
            }
        }
        return;
    } // end of loadMovieDataStore

    private String constructURL(int pageNumber) {
        // Define Base URL for API Server
        final String API_BASE_URL              = "https://api.themoviedb.org/3/search/movie";
        final String QUERY_PARMAMETER          = "query=";
        final String API_KEY_PARAMETER         = "api_key=";
        final String API_KEY_VALUE             = "7dc1a5d409c76b2f20e86d003b066614";
        final String PAGE_PARAMETER            = "page=";
        final String LANGUAGE_PARAMETER        = "language=";
        final String LANGUAGE_DESIRED          = "en-US";
        final String INCLUDE_ADULT_PARAMETER   = "include_adult=";

        String api_URL = API_BASE_URL
                + "?" + QUERY_PARMAMETER + "the"
                + "&" + API_KEY_PARAMETER + API_KEY_VALUE
                + "&" + LANGUAGE_PARAMETER + LANGUAGE_DESIRED
                + "&" + INCLUDE_ADULT_PARAMETER + "false"
                + "&" + PAGE_PARAMETER + Integer.toString(pageNumber);

        return api_URL;
    }

//************************************************************************************************
//   Old Code to be deleted sometime
//************************************************************************************************
//    private void loadMovieDataStore() {
//
//        // https://api.themoviedb.org/3/search/movie?query=The&api_key=7dc1a5d409c76b2f20e86d003b066614&language=en-US&page=1&include_adult=false
//
//        // Define a RestTemplate object to call the API Server
//        RestTemplate callApi = new RestTemplate();
//
//        // Define Base URL for API Server
//        final String API_BASE_URL            = "https://api.themoviedb.org/3/search/movie";
//        final String QUERY_PARMAMETER        = "query=";
//        final String API_KEY_PARAMETER       = "api_key=";
//        final String API_KEY                 = "7dc1a5d409c76b2f20e86d003b066614";
//        final String PAGE_PARAMETER          = "page=";
//        final String LANGUAGE_PARAMETER      = "language=";
//
//        final String INCLUDE_ADULT_PARAMETER = "include_adult=";
//
//        int movieNumber = 0;
//        String language_desired = "en-US";
//        int    pageNumber = 1;
//
//
//        // RestTemplate can return an array of objects automatically for us, so here is an array to hold the returned objects
//        Movie[] movieArray;   // Hold the Movie objects returned from the API the call
//        // There is no easy way to have a RestTemplate method return a Java List object
//
//        String api_URL = API_BASE_URL
//                        + "?" + QUERY_PARMAMETER + "the"
//                        + "&" +API_KEY_PARAMETER + API_KEY
//                        + "&" + LANGUAGE_PARAMETER + language_desired
//                        + "&" + INCLUDE_ADULT_PARAMETER + "false"
//                        + "&" + PAGE_PARAMETER + Integer.toString(pageNumber);
//
//        // https://api.themoviedb.org/3/search/movie?query=The&api_key=7dc1a5d409c76b2f20e86d003b066614&language=en-US&page=1&include_adult=false
//        //
//        // String movieDbURL= "https://api.themoviedb.org/3/search/movie?query=The&api_key=7dc1a5d409c76b2f20e86d003b066614&language=en-US&page=1&include_adult=false";
//        String movieDbURL = "https://627ea182b75a25d3f3bb6905.mockapi.io/movieapi/Movies";
//        // Call the API to get all Movies with the query= word in them and store them in our array
//        //  RestTemplate    getForObject(               URL                , data-type-to-return)
//        movieArray = callApi.getForObject(movieDbURL
//                                         , Movie[].class
//                                         );
//       movieNumber = movieDataStore.size();
//       for(int i = 0; i < movieArray.length; i++) {
//           movieDataStore.put(movieNumber,movieArray[i]);
//           movieNumber++;
//       }
//
//       return;
//
//
//    } // end of loadMovieDataStore

}
