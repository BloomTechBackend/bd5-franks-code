package com.amazon.ata.immutabilityandfinal.classroom.primephoto;

import com.amazon.ata.immutabilityandfinal.classroom.primephoto.activity.ConvertPrimePhotoActivity;
import com.amazon.ata.immutabilityandfinal.classroom.primephoto.dependency.DaggerServiceComponent;
import com.amazon.ata.immutabilityandfinal.classroom.primephoto.dependency.ServiceComponent;
import com.amazon.ata.immutabilityandfinal.classroom.primephoto.model.ConversionType;

import com.google.common.collect.ImmutableList;

/**
 * A class provided for interacting with the PrimePhotoConverterService
 */
public class PrimePhotoConverterManualTester {

    private static final ServiceComponent DAGGER = DaggerServiceComponent.create();

    /**
     * If you're having issues running the main method, check the "Before starting" steps in the README.
     */
    public static void main(String[] args) {
        // ImmutableList is simply an immutable version of a List
        //               used when you need a list in a concurrent processing environment (threads)
        runTest("src/resources/dalmatian.jpg", ImmutableList.of(ConversionType.INVERSION,
            ConversionType.GREYSCALE, ConversionType.SEPIA));

        // PARTICIPANTS: uncomment the below line to run a test that converts an image to a single filter type.
//         runTest("src/resources/dalmatian.jpg", ImmutableList.of(ConversionType.SEPIA));
    }
// Call the application manager method "handleRequest" to manage request
// Send the picture to be converted and the conversion we want to happen
    private static void runTest(String filePath, ImmutableList<ConversionType> conversions) {
        ConvertPrimePhotoActivity activity = DAGGER.provideConvertPhotoActivity();
        activity.handleRequest(filePath, conversions);
    }
}
