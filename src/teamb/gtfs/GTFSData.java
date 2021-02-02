/*
 * Course: SE2030 - 041
 * Fall 2020
 * Group Lab - GTFS Application
 * Lab members: Kian, Tommy, Amish, Nicholas
 * Created: 10/6/2020
 */
package teamb.gtfs;

import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class holds and manages the data structures that contain the
 * routes, trips, and stops used in the GTFS application.
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
public class GTFSData implements Subject {
    TextArea textArea;

    private List<Observer> routes;
    private List<Observer> stops;
    private HashMap<String, Observer> tripHashMap;

    GTFSData(TextArea textArea) {
        routes = new ArrayList<>();
        stops = new ArrayList<>();
        tripHashMap = new HashMap<>();

        this.textArea = textArea;
    }

    /**
     * Attaches the observer to the list of observers
     * @author Nicholas Dang
     * @param observer The observer to be attached
     * @param observers The list of observers
     */
    public void attach(Observer observer, List<Observer> observers) {
        observers.add(observer);
    }

    /**
     * Detaches the observer from the list of observers
     * @author Nicholas Dang
     * @param observer The observer to be detached
     * @param observers The list of observers
     */
    public void detach(Observer observer, List<Observer> observers) {
        observers.remove(observer);
    }

    /**
     * Notifies the observers of changes made to an object
     * @author Nicholas Dang
     * @param item The item to notify the observers of as a string
     * @param id The id of the observer as a string
     * @param observers the list of observers
     */
    public void notifyObserver(String item, String id,
                               List<Observer> observers) {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(item, id, textArea);
        }
    }

    /**
     * Notifies the observers of changes made to an object
     * @author Nicholas Dang
     * @param item The item as a string
     * @param id the id as a string
     * @param observers the list of observers as a hash map
     */
    public void notifyObserver(String item, String id, HashMap<String,
            Observer> observers) {
        Iterator iterator = observers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry map = (Map.Entry) iterator.next();
            Observer observer = (Observer) map.getValue();
            observer.update(item, id, textArea);
        }
    }

    /**
     * Gets a list of routes
     * @return a list of routes
     */
    public List<Observer> getRoutes() {
        return routes;
    }

    /**
     * Gets a list of stops
     * @return a list of stops
     */
    public List<Observer> getStops() {
        return stops;
    }

    /**
     * Gets a hashmap containing trips
     * @return a hashmap containing trips
     */
    public HashMap<String, Observer> getTrips() {
        return tripHashMap;
    }

    /**
     * Adds a new route to the list of routes
     * @param newRoute The new route
     */
    public void addRoute(Route newRoute) {
        attach(newRoute, routes);
    }

    /**
     * Adds a new trip to the list of trips
     * @param tripID The trip ID as an int
     * @param newTrip The new trip as a Trip object
     */
    public void addTrip(String tripID, Trip newTrip) {
        tripHashMap.put(tripID, newTrip);
    }

    /**
     * Adds a new stop to the list of stops
     * @param newStop The new stop as a Stop object
     */
    public void addStop(Stop newStop) {
        attach(newStop, stops);
    }

    /**
     * Gets a route based on routeID
     * @param routeID The routeID of the route
     * @return The route as a Route object
     */
    public Route getRoute(String routeID) {
        Route desiredRoute = null;
        for (int i = 0; i < routes.size(); i++) {
            Route route = (Route) routes.get(i);
            if (route.getRouteID().equals(routeID) || (" " + route.getRouteID()).equals(routeID)) {
                desiredRoute = route;
            }
        }
        return desiredRoute;
    }

    /**
     * Gets the trip from a trip ID
     * @param tripID The trip ID as a string
     * @return The trip as a Trip object
     */
    public Trip getTrip(String tripID) {
        Trip desiredTrip;
        desiredTrip = (Trip) tripHashMap.get(tripID);
        return desiredTrip;
    }

    /**
     * Gets the stop from a stop ID
     * @param stopID The stop ID as an int
     * @return The stop as a Stop object
     */
    public Stop getStop(String stopID) {
        Stop desiredStop = null;
        for (int i = 0; i < stops.size(); i++) {
            Stop stop = (Stop) stops.get(i);
            if (stop.getStopID().equals(stopID)) {
                desiredStop = stop;
            }
        }
        if (desiredStop == null) {
            desiredStop = getStopHex(stopID);
        }
        return desiredStop;
    }

    /**
     * Finds the stop using the stopID
     * allowing for stopIDs expressed in hex
     * @author Tommy Donahoe
     * @param stopID
     * @return The desired stop
     */
    public Stop getStopHex(String stopID) {
        final int hexSize = 16;
        stopID = Long.toHexString(Long.parseLong(stopID, hexSize));
        Stop desiredStop = null;
        for (int i = 0; i < stops.size(); i++) {
            Stop stop = (Stop) stops.get(i);
            if (Long.toHexString(Long.parseLong(stop.getStopID(), hexSize)).equals(stopID)) {
                desiredStop = stop;
            }
        }
        return desiredStop;
    }

    /**
     * Changes the stops latitudinal location.
     * @author Nicholas Dang
     * @param lat The stop's new latitude as a double
     * @param stopID The latitude of the stop as a double
     */
    public void setStopLat(String stopID, double lat) {
        getStop(stopID).setLat(lat);
        notifyObserver("stopLat", stopID, stops);
    }

    /**
     * Changes the route color
     * @author Nicholas Dang
     * @param routeID The routeID of the route as a String
     * @param color The color the the route as a String
     */
    public void setRouteColor(String routeID, String color) {
        getRoute(routeID).setColor(color);
        notifyObserver("color", routeID, routes);
    }

    /**
     * Set routeID for the given trip using tripID
     * @author Nicholas Dang
     * @param tripID The tripID as a String
     * @param routeID The routeID as a String
     */
    public void setTripRouteID(String tripID, String routeID) {
        getTrip(tripID).setRouteID(routeID);
        notifyObserver("routeID", tripID, tripHashMap);
    }


}
