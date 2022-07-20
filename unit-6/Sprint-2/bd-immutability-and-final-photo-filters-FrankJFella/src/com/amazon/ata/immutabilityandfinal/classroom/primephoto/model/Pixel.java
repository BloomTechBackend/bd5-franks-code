package com.amazon.ata.immutabilityandfinal.classroom.primephoto.model;

import java.util.Objects;

/**
 * Represents a single point in an image. Each pixel has a location in the image, and an associated RGB color.
 */
// Make the class immutable since it is used in a concurrent processing environment (threads)
// add final to the class and data members
//     defensive copy ctor reference parameter(s)
//     defensive return of reference data member(s)
public final class Pixel {
    private final int x;
    private final int y;
    private final RGB rgb;

    // constructor is receiving a reference to an RGB object
    //       defensive copy the reference instead of simply assigning it
    public Pixel(int x, int y, RGB rgb) {
        this.x = x;
        this.y = y;
   //     this.rgb = rgb;  // replaced by defensive copy of the reference passed
        // assign a new RGB object with teh values passed to our data member data
        this.rgb = new RGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), rgb.getTransparency());
    }

    // Since this returns a reference to member data
    //       defensive return the data
    public RGB getRGB() {
        // return rgb;  // replaced by defensive return
        //  Note: use of this. is optional
        return new RGB(rgb.getRed(), this.rgb.getGreen(), this.rgb.getBlue(), rgb.getTransparency());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, rgb);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Pixel pixel = (Pixel) obj;

        return pixel.x == this.x && pixel.y == this.y &&
           Objects.equals(pixel.rgb, this.rgb);
    }
}
