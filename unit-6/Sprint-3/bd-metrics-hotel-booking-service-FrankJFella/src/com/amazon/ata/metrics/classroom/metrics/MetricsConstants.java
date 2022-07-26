package com.amazon.ata.metrics.classroom.metrics;

/**
 * Contains constants related to metrics.
 *
 * These are used when logging metrics to AWS CloudWatch
 */
public class MetricsConstants {

    public static final String NAMESPACE = "Unit6MetricsGuidedProject-BD5";
    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "ATAHotelReservationService";
    public static final String MARKETPLACE = "Marketplace";
    public static final String US_MARKETPLACE = "US";

    // Constants added for Metric Guided Project
    public static final String BOOKED_RESERVATION_COUNT = "BookedReservationCount";
    public static final String CANCEL_COUNT             = "CanceledReservationCount";
    public static final String MODIFY_COUNT             = "ModifiedReservationCount";
    public static final String RESERVATION_REVENUE      = "ReservationRevenue";

}
