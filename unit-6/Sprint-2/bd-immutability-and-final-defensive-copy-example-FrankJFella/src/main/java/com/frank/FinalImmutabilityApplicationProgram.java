package com.frank;

public class FinalImmutabilityApplicationProgram {

        private  static int nums[] = {1, 2, 3, 4};

        public static void main(String[] args) {
                System.out.println("-".repeat(80) + "\nHello from main()\n"+"-".repeat(80));

                // Instantiate an aClass object using this class' array
                aClass anObject = new aClass(nums);

                System.out.println("Here are the values in the Arrays");

                showArray();         // Display values in this class' array
                anObject.showClass();// Display the values in the aClass object array

                nums[0] = 9999;    // change the value in the first element of the array in this class
                showArray();       // display the array in this class

                anObject.showClass(); // display array in the aClass object
                                      // the array in the aCloss object  was also changed when we changed the array in main


                System.out.println("\nInstantiate an array using the data in the aClass array");
                int[] Charles = anObject.getAnArray();

                System.out.println("\nChange the value in the 2nd element of our new array");
                Charles[1] = 1776;

                System.out.println("\nDisplay the values in the aClass array");
                anObject.showClass();

                System.out.println("-".repeat(80));

        }  // end of main()

        /******************************************************************************************
         Helper methods
         ******************************************************************************************/
        public static void showArray() {
                System.out.println(("\nContents of array in main(): "));
                for (int i = 0; i < nums.length; i++) {
                        System.out.println("Element " + i + ": " + nums[i]);
                }
        }

}
