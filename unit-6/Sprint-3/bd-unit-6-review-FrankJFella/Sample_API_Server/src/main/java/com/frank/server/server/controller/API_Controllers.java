package com.frank.server.server.controller;

import com.frank.server.dao.MovieDao;
import com.frank.server.model.Movie;
import com.frank.server.exception.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



@RestController      // Tells the Server there are methods in here to process RESTful HTTP requests
@RequestMapping(path="/movie")  // Set the base path for all URLs handled in the class
public class API_Controllers {

    // DAO to get movies
    static final private MovieDao theMovieDao = new MovieDao();

    // Used in logging to remember the start and end of the request
    private long controllerStartTime;
    private long controllerEndTime;

    // Since we defined a base URL path for the class in @RequestMapping, we don't method mapping
    // This @GetMapping is actually for "/movie/"   or  "/movie"
    //
    // Adding and HTTPServeletRequest object as the first parameter to a controller method
    //        give you access to information about the request sent to the server

    /** Retrieve all teh movies from the Movie data store and return them
     *
     * @param theHttpRequest
     * @return a list of movies
     */
    @GetMapping (value={"/", ""})  // handle URLs with the base path and "/" or nothing
    public List<Movie> rootPathProcessor(HttpServletRequest theHttpRequest) {
        logHttpRequest(theHttpRequest);   // Pass the HTTP request to the logging method
        List<Movie> allMovies = theMovieDao.getAllMovies();
        long endTime   = System.currentTimeMillis();
        logEndOfProcessInformation();
        return allMovies;
    }

    /**
     *  Return a movie for the id passed in a query parameter
     *
     *  Handle URL: /movie/find?id=value
     *
     * @param theHttpRequest
     * @param id
     * @return a Movie object
     * @throws NotFoundException
     * @throws InterruptedException
     */
    @GetMapping (value="/find")  // handle URLs with the base path and "/find" or nothing
    public Movie findMoveById(HttpServletRequest theHttpRequest, @RequestParam int id) throws NotFoundException, InterruptedException {
        logHttpRequest(theHttpRequest);

        Movie aMovie = theMovieDao.findMovie(id);

        logMessage("Movie "+ id + " was" + ((aMovie == null)  ? " NOT, REPEAT NOT" : "") + " found");

        logEndOfProcessInformation();

        return aMovie;
    }


    // Log request with timestamp
    // This method receives the HTTP request and display information from it
    private void logHttpRequest(HttpServletRequest theRequest) {
        controllerStartTime = System.currentTimeMillis();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
        String timeNow = now.format(formatter);

        System.out.println("-".repeat(100));
        System.out.printf("%s --> %4s %4s request for URL: %s%s\n",
                timeNow
                , theRequest.getProtocol()    // Display the protocol of the request
                , theRequest.getMethod()      // Type of request
                , theRequest.getRequestURI()  // URL from the request
                // display any query parameters in the URL or "" if there weren't any
                , (theRequest.getQueryString() != null ? ("?" + theRequest.getQueryString()) : ""));
    }
    private void logEndOfProcessInformation() {
        controllerEndTime = System.currentTimeMillis();
        logMessage("Processing time for request: " + (controllerEndTime - controllerStartTime) + " milliseconds");
    }
    // log a message passed in as a parameter
    private void logMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
        String timeNow = now.format(formatter);

        System.out.printf("%s --> %s\n", timeNow, message);
    }


}
