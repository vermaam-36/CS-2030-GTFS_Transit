/*
 * Course: SE2030 - 041
 * Fall 2020
 * Group Lab - GTFS Application
 * Lab members: Kian, Tommy, Amish, Nicholas
 * Created: 10/6/2020
 */
package teamb.gtfs;


import javafx.scene.control.TextArea;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * This class holds and manages information describing
 * different stops in the GTFS application.
 *
 * This file is part of Team B GTFS Application.
 *
 *     Team B GTFS Application is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Team B GTFS Application is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Team B GTFS Application.  If not, see <https://www.gnu.org/licenses/>.
 */
public class Stop implements Observer {
    private String stopID;
    private double stopLat;
    private double stopLon;
    private List<Trip> trips;

    /**
     * Creates a stop object that stores the ID, Name, and location of a stop.
     *
     * @param stopID   The ID of the stop as an int
     * @param stopLat  The stop's latitude as a double
     * @param stopLon  The stop's longitude as a double.
     */
    public Stop(String stopID, double stopLat, double stopLon) {
        this.stopID = stopID;
        this.stopLat = stopLat;
        this.stopLon = stopLon;
        trips = new ArrayList<>();
    }

    public double getLat() {
        return stopLat;
    }

    public double getLon() {
        return stopLon;
    }

    public String getStopID() {
        return stopID;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    /**
     * Changes the stops latitudinal location.
     *
     * @param lat The stop's new latitude as a double
     */
    public void setLat(double lat) {
        stopLat = lat;
    }

    /**
     * Changes the stops longitudinal location.
     *
     * @param lon The stop's new longitude as a double
     */
    public void setLon(double lon) {
        stopLon = lon;
    }

    /**
     * Adds adds a new trip to the list of trips that include the stop.
     * @author Kian Dettlaff
     * @param trip The new trip that includes the stop
     */
    public void addTrip(Trip trip) {
        if (!trips.contains(trip)){
            trips.add(trip);
        }
    }

    /**
     * Retrieves the trip that arrives at the stop closest to the current time.
     * @author Kian Dettlaff
     * @return The trip that will arrive at the stop next
     */
    public List<Trip> getNextTrips() {
        final int secondsInDay = 86400;
        LocalTime currentTime = LocalTime.now();
        List<Trip> nextTrips = new ArrayList<>();
        long timeTillBest = Long.MAX_VALUE;
        for (Trip trip : trips) {
            LocalTime tripArrivalTime = trip.nextStopArrivalTime(this);
            long timeTillTrip = currentTime.until(tripArrivalTime, SECONDS);
            if(timeTillTrip < 0){
                timeTillTrip = secondsInDay + timeTillTrip;
            }
            if (timeTillTrip < timeTillBest) {
                nextTrips.clear();
                nextTrips.add(trip);
                timeTillBest = timeTillTrip;
            } else if (timeTillTrip == timeTillBest) {
                nextTrips.add(trip);
            }
        }
        return nextTrips;
    }


    @Override
    public void update(String item, String id, TextArea textArea) {
        String[] params = new String[] {"stopID", "stopLat", "stopLon"};
        if (Arrays.asList(params).contains(item) && stopID.equals(id)) {
            String changed = "Stop " + item + " changed(stopID, stopLat, stopLon): "
                    + stopID + ", " + stopLat + "," + stopLon + "\n";
            textArea.setText(textArea.getText() + changed);
        }
    }
}