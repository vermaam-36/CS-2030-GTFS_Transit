/*
 * Course: SE2030 - 041
 * Fall 2020
 * Group Lab - GTFS Application
 * Lab members: Kian, Tommy, Amish, Nicholas
 * Created: 10/6/2020
 *//*

package tests.teamb.gtfs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamb.gtfs.GTFSController;
import teamb.gtfs.Trip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;



class GTFSControllerTest {
    GTFSController gtfsController;
    @BeforeEach
    void init() {
        gtfsController = new GTFSController();
    }

    */
/**
     * Checks to make sure that verifyStopHeader throws no exceptions
     * and returns the correct list when given a valid header.
     * @author Amish Verma
     *//*

    @Test
    void verifyStopHeader() {
        String header = "stop_id,stop_name,stop_desc,stop_lat,stop_lon";
        List<String> target = Arrays.asList(header.split(","));
        List<String> returnedList = new ArrayList<>();
        try {
            returnedList = gtfsController.verifyStopHeader(header);
        } catch(GTFSController.IncorrectFileFormatException e){

        }
        assertEquals(target, returnedList);

    }

    @Test
    */
/**
     * Checks to make sure that verifyStopEnrty throws no exception
     * when given valid data
     * @author Amish Verma
     * *//*

    void verifyStopEntry() {
        final int stopIDIndex = 0;
        final int stopLatIndex = 3;
        final int stopLonIndex = 4;
        String[] entry = "6712,STATE & 5101 #6712,,43.0444475,-87.9779369".split(",");
        assertDoesNotThrow(()->gtfsController.verifyStopEntry(entry, stopIDIndex,
                stopLatIndex, stopLonIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyRouteHeader throws no exceptions
     * and returns the correct list when given a valid header.
     * @author Tommy Donahoe and Kian Dettlaff
     *//*

    void verifyRouteValidHeader() {
        String header = "route_id,route_color";
        List<String> goalList = Arrays.asList(header.split(","));
        assertDoesNotThrow(() ->gtfsController.verifyRouteHeader(header));
        List<String> returnedList = new ArrayList<>();
        try {
            returnedList = gtfsController.verifyRouteHeader(header);
        } catch(GTFSController.IncorrectFileFormatException e){

        }
        assertEquals(goalList, returnedList);
    }

    @Test
    */
/**
     * Checks to make sure that verifyRouteHeader throws the correct exception
     * when given a header with no route_id field.
     * @author Tommy Donahoe and Kian Dettlaff
     *//*

    void routesHeaderMissingRouteID() {
        String header = "route_color";
        assertThrows(GTFSController.IncorrectFileFormatException.class,
                () ->gtfsController.verifyRouteHeader(header));
    }

    @Test
    */
/**
     * Checks to make sure that verifyRouteHeader throws the correct exception
     * when given a header with no route_id field.
     * @author Kian Dettlaff
     *//*

    void routesHeaderMissingRouteColor() {
        String header = "route_id";
        assertThrows(GTFSController.IncorrectFileFormatException.class,
                () ->gtfsController.verifyRouteHeader(header));
    }

    @Test
    */
/**
     * Checks to make sure that verifyRouteEntry throws no exception
     * when given a valid header
     * @author Tommy Donahoe and Kian Dettlaff
     *//*

    void verifyRouteEntryValidEntry() {
        final int routeIDIndex = 0;
        final int routeColorIndex = 1;

        String[] entry = "12,008345".split(",");
        assertDoesNotThrow(()->
                gtfsController.verifyRouteEntry(entry, routeIDIndex, routeColorIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyRouteEntry throws the correct exception
     * when given a header that doesn't have enough fields
     * @author Tommy Donahoe and Kian Dettlaff
     *//*

    void verifyRoutesEntryNotEnoughFields() {
        final int routeIDIndex = 0;
        final int routeColorIndex = 1;

        String[] entry = "008345".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyRouteEntry(entry, routeIDIndex, routeColorIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyRouteEntry throws the correct exception
     * when given a header with an empty route_id field
     * @author Tommy Donahoe and Kian Dettlaff
     *//*

    void verifyRouteEntryEmptyRouteID() {
        final int routeIDIndex = 0;
        final int routeColorIndex = 1;

        String[] entry = ",008345".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyRouteEntry(entry, routeIDIndex, routeColorIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyRouteEntry throws the correct exception
     * when given a header with an empty route_color field
     * @author Tommy Donahoe and Kian Dettlaff
     *//*

    void verifyRouteEntryEmptyRouteColor() {
        final int routeIDIndex = 0;
        final int routeColorIndex = 1;

        String[] entry = "12,".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyRouteEntry(entry, routeIDIndex, routeColorIndex));
    }

    @Test
    */
/**
     * A test to make sure that the trip file header is correct and
     * will throw a IncorrectFileFormatException if the header does not
     * contain route ID or trip ID
     * @author Nicholas Dang
     *//*

    void verifyTripHeader() throws GTFSController.IncorrectFileFormatException {
        String fullTripHeader = "route_id,service_id,trip_id," +
                "trip_headsign,direction_id,block_id,shape_id";
        String fullTripHeaderWrong = "route_id,service_id," +
                "trip_headsign,direction_id,block_id,shape_id";

        List<String> header = Arrays.asList("route_id", "service_id", "trip_id",
                "trip_headsign", "direction_id", "block_id", "shape_id");

        assertEquals(header, gtfsController.verifyTripHeader(fullTripHeader));
        assertThrows(GTFSController.IncorrectFileFormatException.class,
                () -> gtfsController.verifyTripHeader(fullTripHeaderWrong));

    }

    */
/**
     * A test that will verify that a Trip object will be added into the gtfsData
     * data structure and will make sure that the entry from the file has the correct
     * parameters needed to add a trip.
     * @author Nicholas Dang
     *//*

    @Test
    void verifyTripEntry() {
        GTFSController gtfsController = new GTFSController();
        String[] tripInfo = new String[] {"64", "17-SEP_SUN,21736564_2535",
                "60TH-VLIET", "0", "64102", "17-SEP_64_0_23"};
        int tripIDIndex = 0;
        int routeIDIndex = 2;
        Trip trip = new Trip(tripInfo[tripIDIndex], tripInfo[routeIDIndex]);
        gtfsController.addTrip(tripInfo[tripIDIndex], trip);
        gtfsController.verifyTripEntry(tripInfo, tripIDIndex, routeIDIndex);
        assertEquals(trip, gtfsController.getTrip(tripInfo[tripIDIndex]));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesHeader throws no exceptions
     * and returns the correct list when given a valid header.
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesHeaderValidHeader() {
        String header = "trip_id,arrival_time,departure_time,stop_id,stop_sequence";
        List<String> goalList = Arrays.asList(header.split(","));
        assertDoesNotThrow(() ->gtfsController.verifyStopTimesHeader(header));
        List<String> returnedList = new ArrayList<>();
        try {
            returnedList = gtfsController.verifyStopTimesHeader(header);
        } catch (GTFSController.IncorrectFileFormatException e){

        }
        assertEquals(goalList, returnedList);
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesHeader throws the correct exception
     * when given a header with no trip_id field.
     * @author Kian Dettlaff
     *//*

    void stopTimesHeaderMissingTripID() {
        String header = "arrival_time,departure_time,stop_id,stop_sequence";
        assertThrows(GTFSController.IncorrectFileFormatException.class,
                () ->gtfsController.verifyStopTimesHeader(header));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesHeader throws the correct exception
     * when given a header with no arrival_time field.
     * @author Kian Dettlaff
     *//*

    void stopTimesHeaderMissingArrivalTime() {
        String header = "trip_id,departure_time,stop_id,stop_sequence";
        assertThrows(GTFSController.IncorrectFileFormatException.class,
                () ->gtfsController.verifyStopTimesHeader(header));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesHeader throws the correct exception
     * when given a header with no departure_time field.
     * @author Kian Dettlaff
     *//*

    void stopTimesHeaderMissingDepartureTime() {
        String header = "trip_id,arrival_time,stop_id,stop_sequence";
        assertThrows(GTFSController.IncorrectFileFormatException.class,
                () ->gtfsController.verifyStopTimesHeader(header));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesHeader throws the correct exception
     * when given a header with no stop_id field.
     * @author Kian Dettlaff
     *//*

    void stopTimesHeaderMissingStopID() {
        String header = "trip_id,arrival_time,departure_time,stop_sequence";
        Assertions.assertThrows(GTFSController.IncorrectFileFormatException.class,
                () ->gtfsController.verifyStopTimesHeader(header));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesHeader throws the correct exception
     * when given a header with no stop_sequence field.
     * @author Kian Dettlaff
     *//*

    void stopTimesHeaderMissingStopSequence() {
        String header = "trip_id,arrival_time,departure_time,stop_id";
        Assertions.assertThrows(GTFSController.IncorrectFileFormatException.class,
                () ->gtfsController.verifyStopTimesHeader(header));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws no exception
     * when given a valid header
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryValidEntry() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,08:51:00,08:51:00,9113,1".split(",");
        assertDoesNotThrow(()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex,
                arrivalIndex, departureIndex, stopIDIndex, sequenceIDIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header that doesn't have enough fields
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryNotEnoughFields() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,08:51:00,08:51:00,9113".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header with an empty tripID field
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryEmptyTripID() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = ",08:51:00,08:51:00,9113,1".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header with an empty Arrival Time field
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryEmptyArrivalTime() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,,08:51:00,9113,1".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }

    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header with an empty departure time field
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryEmptyDepartureTime() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,08:51:00,,9113,1".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }
    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header with an empty stop ID field
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryEmptyStopID() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,08:51:00,08:51:00,,1".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }
    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header with a stop ID field containing a non integer value
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryNonIntegerStopID() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,08:51:00,08:51:00,one,1".split(",");
        assertThrows(IllegalArgumentException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }
    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header with an empty sequence ID field
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryEmptySequenceID() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,08:51:00,08:51:00,9113,,".split(",");
        assertThrows(IllegalStateException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }
    @Test
    */
/**
     * Checks to make sure that verifyStopTimesEntry throws the correct exception
     * when given a header with a sequence ID field containing a non integer value
     * @author Kian Dettlaff
     *//*

    void verifyStopTimesEntryNonIntegerSequenceID() {
        final int tripIDIndex = 0;
        final int arrivalIndex = 1;
        final int departureIndex = 2;
        final int stopIDIndex = 3;
        final int sequenceIDIndex = 4;
        String[] entry = "21736564_2535,08:51:00,08:51:00,9113,one".split(",");
        assertThrows(IllegalArgumentException.class,
                ()->gtfsController.verifyStopTimesEntry(entry, tripIDIndex, arrivalIndex,
                        departureIndex, stopIDIndex, sequenceIDIndex));
    }
}*/
