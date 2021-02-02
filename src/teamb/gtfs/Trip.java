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
 * different trips in the GTFS application.
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
public class Trip implements Observer {
    private String routeID;
    private List<StopTimes> stopTimes;
    private String tripID;

    /**
     * Creates a new Trip object with the given details
     * @param routeID The ID of the route in which the trip exists
     * @param tripID The ID of the trip
     */
    public Trip(String routeID, String tripID) {
        this.routeID = routeID;
        this.tripID = tripID;
        stopTimes = new ArrayList<>();
    }

    /**
     * Adds a new stop to the trip with the given details
     * @param arrival The time when the trip will arrive at the stop
     * @param departure The time when the trip will leave the stop
     * @param stop The Stop which the trip must visit
     * @param sequence An int indicating what order different stops are visited
     */
    public void addStopTime(String arrival, String departure, Stop stop, int sequence) {
        stopTimes.add(new StopTimes(arrival, departure, stop, sequence));
        stop.addTrip(this);
    }

    /**
     * Retrieves a stop with a given ID
     * @param stopID retrieve a stop with this ID
     * @return The Stop object on the trip corresponding with the given stop ID
     */
    public Stop getStop(int stopID) {
        Stop desiredStop = null;
        for (int i = 0; i < stopTimes.size(); i++) {
            Stop currentStop = stopTimes.get(i).getStop();
            if (currentStop.getStopID().equals(stopID)) {
                desiredStop = currentStop;
            }
        }
        return desiredStop;
    }

    /**
     * Removes a stop from the trip
     * @param stopID The ID of the stop to be removed from the trip
     * @return The stoptime object that was removed
     */
    public StopTimes removeStop(int stopID) {
        StopTimes stopTime = null;
        for (int i = 0; i < stopTimes.size(); i++) {
            Stop currentStop = stopTimes.get(i).getStop();
            if (currentStop.getStopID().equals(stopID)) {
                stopTime = stopTimes.get(i);
            }
        }
        stopTimes.remove(stopTime);
        return stopTime;
    }

    /**
     * Finds the closest time that the trip will arrive at a stop.
     * @author Kian Dettlaff
     * @param stop The stop to arrive at
     * @return A LocalTime describing when the trip will next arrive at the stop
     */
    public LocalTime nextStopArrivalTime(Stop stop) {
        final int secondsInDay = 86400;
        LocalTime currentTime = LocalTime.now();
        LocalTime closestTime = null;
        long timeTillBest = Long.MAX_VALUE;
        for (StopTimes stopTime : stopTimes) {
            if (stopTime.getStop().equals(stop)) {
                long timeTillArival = currentTime.until(stopTime.getArrivalTime(), SECONDS);
                if (timeTillArival < 0) {
                    timeTillArival = secondsInDay + timeTillArival;
                }
                if (timeTillArival < timeTillBest) {
                    closestTime = stopTime.getArrivalTime();
                    timeTillBest = timeTillArival;
                }
            }
        }
        return closestTime;
    }


    public List<StopTimes> getStopTimes() {
        return stopTimes;
    }

    public String getRouteID() {
        return routeID;
    }

    public String getTripID() {
        return tripID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    @Override
    public void update(String item, String id, TextArea textArea) {
        String[] params = new String[] {"tripID", "routeID", "stopTimes"};
        if (Arrays.asList(params).contains(item) && tripID.equals(id)) {
            String changed = "Trip " + item + " changed(tripID, routeID): "
                    + tripID + ", " + routeID + "\n";
            textArea.setText(textArea.getText() + changed);
        }
    }

    /**
     * Finds the latest time that the trip arrives at a stop during the day.
     * @author Kian Dettlaff
     * @return The last arrival time during the day
     */
    public LocalTime getLatestStopTime() {
        LocalTime latestTime = LocalTime.MIN;
        for (StopTimes stopTime : stopTimes) {
            LocalTime stopTimeArrival = stopTime.getArrivalTime();
            if (latestTime.until(stopTimeArrival, SECONDS) > 0) {
                latestTime = stopTimeArrival;
            }
        }
        return latestTime;
    }

}