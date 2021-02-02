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
 * Route class that creates a route object with agencyID, routeID,
 * longName, routeType, and list of trips
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
public class Route implements Observer {

    private String routeID;
    private String color;
    private List<Trip> trips;

    /**
     * Creates a Route object that stores agencyID, routeDesc,
     * routeID, routeShortName, and list of trips
     * @param color the color as a String
     * @param routeID the routeID as an int
     */
    public Route(String routeID, String color) {
        this.color = color;
        this.routeID = routeID;
        trips = new ArrayList<>();
    }

    /**
     * Adds a trip to the trip list contained in the Route object
     * @param routeID the route ID as an int
     * @param tripID the tripID as an int
     */
    public void addTrip(String routeID, String tripID) {
        Trip trip = new Trip(routeID, tripID);
        trips.add(trip);
    }

    /**
     * Gets a trip from route based on routeID, serviceID, and tripID
     * @param routeID the routeID of the trip as an int
     * @param tripID the tripID of the trip as an int
     * @return Returns a trip object that contains the parameters
     */
    public Trip getTrip(String routeID, String tripID) {
        Trip validTrip = null;
        for (Trip trip : trips) {
            if (trip.getRouteID().equals(routeID) && trip.getTripID().equals(tripID)) {
                validTrip = trip;
            }
        }
        return validTrip;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public String getColor(){
        return color;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void update(String item, String id, TextArea textArea) {
        String[] params = new String[] {"routeID", "color", "trips"};
        if (Arrays.asList(params).contains(item) && routeID.equals(id)) {
            String changed = "Route " + item + " changed(routeID, color): "
                    + routeID + ", " + color + "\n";
            textArea.setText(textArea.getText() + changed);
        }
    }

    /**
     * Finds what trips on the route still have stops during the day.
     * @author Kian Dettlaff
     * @return A list of trips which all still have a stop during the current day.
     */
    public List<Trip> getTripsLeftInDay() {
        List<Trip> futureTrips = new ArrayList<>();
        for(Trip trip : trips){
            LocalTime lastTripStopTime = trip.getLatestStopTime();
            if(LocalTime.now().until(lastTripStopTime, SECONDS) > 0){
                futureTrips.add(trip);
            }
        }
        return futureTrips;
    }
}