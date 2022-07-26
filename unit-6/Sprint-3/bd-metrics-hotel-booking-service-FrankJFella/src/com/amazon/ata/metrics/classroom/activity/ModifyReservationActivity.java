package com.amazon.ata.metrics.classroom.activity;

import com.amazon.ata.metrics.classroom.dao.ReservationDao;
import com.amazon.ata.metrics.classroom.dao.models.UpdatedReservation;
import com.amazon.ata.metrics.classroom.metrics.MetricsConstants;
import com.amazon.ata.metrics.classroom.metrics.MetricsPublisher;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import java.time.ZonedDateTime;
import javax.inject.Inject;

/**
 * Handles requests to modify a reservation
 */
public class ModifyReservationActivity {

    private ReservationDao   reservationDao;
    private MetricsPublisher metricsPublisher;

    /**
     * Construct ModifyReservationActivity.
     * @param reservationDao Dao used for modify reservations.
     */
    @Inject
    public ModifyReservationActivity(ReservationDao reservationDao, MetricsPublisher metricsPublisher) {
        this.reservationDao = reservationDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Modifies the given reservation.
     *     and update the ModifiedReservationCount metric
     *                and ReservationRevenue metric
     *
     * @param reservationId Id to modify reservations for
     * @param checkInDate modified check in date
     * @param numberOfNights modified number of nights
     * @return UpdatedReservation that includes the old reservation and the updated reservation details.
     */
    public UpdatedReservation handleRequest(final String reservationId, final ZonedDateTime checkInDate,
                                            final Integer numberOfNights) {

        UpdatedReservation updatedReservation = reservationDao.modifyReservation(reservationId, checkInDate,
            numberOfNights);

        // After the Reservation has been updated
        //       update the ModifiedReservationMetric to count it
        //                               class-name.enun-name
        metricsPublisher.addMetric(MetricsConstants.MODIFY_COUNT,1, StandardUnit.Count);

        // Subtract the total cost from the original revervation in the response
        //        from  total cost of the modified reservation in the response
        //        giving us the difference in revenue for the reservation
        //     and update the ReservationRevenue metric

        double revenueDifference = updatedReservation.getModifiedReservation().getTotalCost()
                         .subtract(updatedReservation.getOriginalReservation().getTotalCost())
                         .doubleValue();

        metricsPublisher.addMetric(MetricsConstants.RESERVATION_REVENUE, revenueDifference, StandardUnit.None );




        return updatedReservation;
    }
}
