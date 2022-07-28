package com.frank.server.model;

import java.time.LocalDate;
import java.util.Objects;

// Hold teh Movie information for one Movie in the data store

public class Movie {
    // This data will come from the server as JSON and be converted automatically
    // the data member names must match exactly the JSON attribute names
    // It is not necessary to have all JSON attributes defined in the object
    private int       movieNumber;  // Generated when the Movie is instantiated
    private int       id;
    private Boolean   adult;
    private String    title;
    private LocalDate release_date;
    private String    overview;

    // Be sure you have a default ctor for the class as this is what used to instantiate an object
    public Movie() {}

    // Be you have standard names for getters/setters - this is what is used to instantiate an object
    //  (IntelliJ generates getters/setters with standard name)
    public int getMovieNumber() {
        return movieNumber;
    }

    public void setMovieNumber(int movieNumber) {
        this.movieNumber = movieNumber;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return getMovieNumber() == movie.getMovieNumber() && getId() == movie.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMovieNumber(), getId());
    }
} // End of Movie class
