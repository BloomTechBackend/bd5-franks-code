package com.amazon.ata.metrics.classroom.activity;

import com.amazon.ata.metrics.classroom.dao.ReservationDao;
import com.amazon.ata.metrics.classroom.dao.models.Reservation;
import com.amazon.ata.metrics.classroom.metrics.MetricsConstants;
import com.amazon.ata.metrics.classroom.metrics.MetricsPublisher;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import javax.inject.Inject;

/**
 * Handles requests to cancel a reservation.
 */
public class CancelReservationActivity {

    private ReservationDao   reservationDao;
    private MetricsPublisher metricsPublisher;

    /**
     * Constructs a CancelReservationActivity
     * @param reservationDao Dao used to update reservations.
     */
    @Inject
    public CancelReservationActivity(ReservationDao reservationDao, MetricsPublisher metricsPublisher) {
        this.reservationDao = reservationDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Cancels the given reservation.
     *      and update the CanceledReservationCount metric
     *                 and ReservationRevenue metric
     * @param reservationId of the reservation to cancel.
     * @return canceled reservation
     */
    public Reservation handleRequest(final String reservationId) {

        // Cancel a reservation
        Reservation response = reservationDao.cancelReservation(reservationId);

        // After the Reservation has been canceled
        //       update the CanceledReservationMetric to count it
        //                               class-name.enun-name
        metricsPublisher.addMetric(MetricsConstants.CANCEL_COUNT,1, StandardUnit.Count);

        // Retrieve the total cost (which is a negative value) from the response when we created a new Reservation
        //     and update the ReservationRevenue metric
        metricsPublisher.addMetric(MetricsConstants.RESERVATION_REVENUE,
                response.getTotalCost().doubleValue(), // convert the total cost from BigDecimal to a double
                StandardUnit.None );

        return response;
    }
}
