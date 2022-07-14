package com.amazon.ata.dynamodbquery.dao;

import com.amazon.ata.dynamodbquery.converter.ZonedDateTimeConverter;
import com.amazon.ata.dynamodbquery.dao.models.EventAnnouncement;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.checkerframework.checker.units.qual.A;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Manages access to EventAnnouncement items.
 */
public class EventAnnouncementDao {

    private DynamoDBMapper mapper;
    
    // Define a constant to represent the Date/time converter we are using
    // This is done to make it easier if we need to change the converter
    // NOT REQUIRED FOR DYNAMODB ACCESS
    private static final ZonedDateTimeConverter ZONED_DATE_TIME_CONVERTER = new ZonedDateTimeConverter();

    /**
     * Creates an EventDao with the given DDB mapper.
     * @param mapper DynamoDBMapper
     */
    @Inject
    public EventAnnouncementDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Gets all event announcements for a specific event.
     *
     * @param eventId The event to get announcements for.
     * @return the list of event announcements.
     */
    public List<EventAnnouncement> getEventAnnouncements(String eventId) {

        // Instantiate an EventAnouncement object for interaction with DynamoDB
        //    and set it's eventId to the eventId we are passed (eventId caller is looking for)
        EventAnnouncement eventAnnouncement = new EventAnnouncement();
        eventAnnouncement.setEventId(eventId);

        // Since we are making a condistion query to DynamoDB
        //   we need to define DynamoDBQueryExpression for an EventAnnouncement object
        //              using the object we defined to interact with DynamoDB.
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression<EventAnnouncement>()
                .withHashKeyValues(eventAnnouncement);

        // Go to DynamoDB to get the data that matches the query expression
        //    and return the list it gives us
        //                  class-of-objects-returned, queryExpression
        return mapper.query(EventAnnouncement.class  , queryExpression);

    }  // end of getEventAnnouncements()

    /**
     * Get all event announcements posted between the given dates for the given event.
     *
     * @param eventId The event to get announcements for.
     * @param startTime The start time to get announcements for.
     * @param endTime The end time to get announcements for.
     * @return The list of event announcements.
     */
    public List<EventAnnouncement> getEventAnnouncementsBetweenDates(String eventId, ZonedDateTime startTime,
                                                                     ZonedDateTime endTime) {
        // TODO: implement

        // To set up a conditional search for DynamoDB
        //    We need to define the conditions we want it to in the search
        //
        // Because we have more than one search condition (eventId and between dates)
        //    We need to store the conditions in a Map which send to DynamoDB
        //
        //  The Map key will be an identifier for the condition, usually a String
        //  The Map value will be the condition value as an AttributeValue object
        Map<String, AttributeValue> searchValues = new HashMap<>();

        // Add the condition values to the Map, each with a unique identifier
        //              identifier   , value-associate-with-identifier
        searchValues.put(":eventId"  , new AttributeValue().withS(eventId));
        // Add the startTime parameter to the Map as a String (data in DynamoDB attribute is a String)
        searchValues.put(":startDate", new AttributeValue().withS(ZONED_DATE_TIME_CONVERTER.convert(startTime)));
        // Add the endTime parameter to the Map as a String (data in DynamoDB attribute is a String)
        searchValues.put(":endDate"  , new AttributeValue().withS(ZONED_DATE_TIME_CONVERTER.convert(endTime)));

        // Define a DynamoDBQueryExpression for EventAnnouncements to give DynamoDB to use in searching the database
        //    including the conditional expression(s) to be used as a String using
        //    .withKeyConditionExpression("name-column-in-table relational-operator identifer-in-Map-for-value")
        //    .withExpressionAttributeValues(name-of-Map-with-values-to-be-used-in-search)
        DynamoDBQueryExpression querySearchExpression = new DynamoDBQueryExpression<EventAnnouncement>()
                .withKeyConditionExpression("eventId = :eventId and timePublished between :startDate and :endDate")
                .withExpressionAttributeValues(searchValues);

        // Tell DynamoDB to search for we want and return as a List of EventAnnounceMent objects
        //      using the DynamoDBQuesyExpression
        return mapper.query(EventAnnouncement.class, querySearchExpression);
    }

    /**
     * Creates a new event announcement.
     *
     * @param eventAnnouncement The event announcement to create.
     * @return The newly created event announcement.
     */
    public EventAnnouncement createEventAnnouncement(EventAnnouncement eventAnnouncement) {
        mapper.save(eventAnnouncement);
        return eventAnnouncement;
    }
}
