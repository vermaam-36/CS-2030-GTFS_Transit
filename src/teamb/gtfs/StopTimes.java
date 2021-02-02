/*
 * Course: SE2030 - 041
 * Fall 2020
 * Group Lab - GTFS Application
 * Lab members: Kian, Tommy, Amish, Nicholas
 * Created: 10/6/2020
 */
package teamb.gtfs;


import java.time.LocalTime;

/**
 * This class holds and manages information describing
 * different stopTimes in the GTFS application.
 */
public class StopTimes {
    private LocalTime arrival;
    private String arrivalTimeString;
    private LocalTime departure;
    private String departureTimeString;
    private Stop stop;
    private int sequence;

    /**
     * Creates a new stopTime object with the provided details
     *
     * @author Kian Dettlaff
     * @param arrivalString The time when the trip will arrive at the stop
     * @param departureString The time when the trip will leave the stop
     * @param stop The Stop which the trip must visit
     * @param sequence An int indicating what order different stops are visited
     */
    public StopTimes(String arrivalString, String departureString, Stop stop, int sequence) {
        setArrival(arrivalString);
        setDeparture(departureString);
        this.stop = stop;
        this.sequence = sequence;
    }

    /**
     * Retrieves a formatted string describing the arrival time.
     *
     * @author Kian Dettlaff
     * @return A HH:MM:SS formatted string describing the arrival time
     */
    public String getArrivalString() {
        return arrivalTimeString;
    }

    /**
     * Retrieves the arrival time object of the stop.
     *
     * @author Kian Dettlaff
     * @return The LocalTime object describing the arrival time
     */

    public LocalTime getArrivalTime() {
        return arrival;
    }

    /**
     * Retrieves a formatted string describing the departure time.
     *
     * @author Kian Dettlaff
     * @return A HH:MM:SS formatted string describing the departure time
     */
    public String getDepartureString() {
        return departureTimeString;
    }

    /**
     * Retrieves the departure time object of the stop.
     *
     * @author Kian Dettlaff
     * @return The LocalTime object describing the departure time
     */
    public LocalTime getDepartureTime() {
        return departure;
    }

    public int getSequence() {
        return sequence;
    }

    public Stop getStop() {
        return stop;
    }

    /**
     * Sets the arrival of the StopTimes object
     * @param arrivalString time of arrival at the stop
     */
    public void setArrival(String arrivalString) {
        final int localTimeMaxHours = 24;
        arrivalTimeString = arrivalString;
        int hours = Integer.parseInt(arrivalString.substring(0, 2));
        if (hours >= localTimeMaxHours) {
            arrivalString = "0" + (hours - localTimeMaxHours) + arrivalString.substring(2);
        }
        arrival = LocalTime.parse(arrivalString);
    }

    /**
     * Sets the departure of the StopTimes object
     * @param departureString time of departure at the stop
     */
    public void setDeparture(String departureString) {
        final int localTimeMaxHours = 24;
        departureTimeString = departureString;
        int hours = Integer.parseInt(departureString.substring(0, 2));
        if (hours >= localTimeMaxHours) {
            departureString = "0" + (hours - localTimeMaxHours) + departureString.substring(2);
        }
        departure = LocalTime.parse(departureString);
    }

}