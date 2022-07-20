package com.amazon.ata.immutabilityandfinal.classroom.primephoto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a PrimePhoto - contains dimensions, and a list of Pixels that make up the image.
 */
// Make the class immutable since it is used in a concurrent processing environment (threads)
// add final to the class and data members
//     and defensive copy references passed to the constructor
//     and defensive return in getters returning reference
public final class PrimePhoto {
    private final List<Pixel> pixels;
    private final int height;
    private final int width;
    // used when saving to a buffered image
    private final int type;

    // This constructor receives a List object as a parameter
    // List objects are references - defensive copy the value passed not simply assign it
    public PrimePhoto(List<Pixel> pixelList, int height, int width, int type) {
        if (pixelList.size() != (height * width)) {
            throw new IllegalArgumentException("Not enough pixels for the dimensions of the image.");
        }
        // this.pixels = pixelList;  // replaced by defensive copy
        this.pixels = new ArrayList<>(pixelList);  // copy the parameter ArrayList to a new one
        this.height = height;
        this.width  = width;
        this.type   = type;
    }

    // This method is returning a reference
    //      made defensive return to protect our data
    public List<Pixel> getPixels() {
        return new ArrayList<>(pixels);  // return a copy of the data in the reference
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pixels, height, width, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        PrimePhoto photo = (PrimePhoto) obj;

        return photo.height == this.height && photo.width == this.width &&
            photo.type == photo.type && Objects.equals(photo.pixels, this.pixels);
    }

}
