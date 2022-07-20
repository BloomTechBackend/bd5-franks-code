package com.frank;

import java.util.Arrays;

public class aClass {

        private int[] anArray;

        public aClass(int[] intArray) {
                // anArray = intArray;  // replaced by a defensive copy

                // Copy the reference passed in to the reference in this class
                //        Arrays.copyOf(target-array, length-of-source-array)
                anArray = Arrays.copyOf(intArray    , intArray.length);
        }

        public int[] getAnArray() {
                // defensive return of the reference to our data member
                return Arrays.copyOf(anArray, anArray.length);
        }

        public void showClass() {
                System.out.println(("\nContents of array in aClass: "));
                for (int i = 0; i < anArray.length; i++) {
                        System.out.println("Element " + i + ": " + anArray[i]);
                }
        }
}
