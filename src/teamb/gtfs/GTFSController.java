/*
 * Course: SE2030 - 041
 * Fall 2020
 * Group Lab - GTFS Application
 * Lab members: Kian, Tommy, Amish, Nicholas
 * Created: 10/6/2020
 */
package teamb.gtfs;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;


/**
 * This class acts as a controller for managing and reading the information
 * stored in the GTFS application.
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
public class GTFSController {
    @FXML
    MenuItem exportButton;
    @FXML
    Menu fileMenu;
    @FXML
    MenuItem importButton;
    @FXML
    TextArea textArea;
    private GTFSData gtfsData;
    private final int windowSize = 400;
    private final int maxLat = 90;
    private final int minLat = -90;
    private final int maxLon = 180;
    private final int minLon = -180;
    private boolean isInitialized = false;

    @FXML
    private void importItem() {
        textArea.setText("");
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(null);
        if (file != null) {
            gtfsData = new GTFSData(textArea);
            exportButton.setDisable(false);
            String path = file.getAbsolutePath();
            File stops = new File(path + File.separator + "stops.txt");
            File routes = new File(path + File.separator + "routes.txt");
            File trips = new File(path + File.separator + "trips.txt");
            File stopTimes = new File(path + File.separator + "stop_times.txt");
            try {
                readStops(stops);
                System.out.println("Stops done.");
                readRoutes(routes);
                System.out.println("Routes done.");
                readTrips(trips);
                System.out.println("Trips done.");
                readStopTimes(stopTimes);
                System.out.println("StopTimes done.");
                isInitialized = true;
                //Testing if Observer Pattern works by have it print the changed stop object.
//                gtfsData.setStopLat("6712", 0000000000);
//                gtfsData.setRouteColor("12", "BLACK");
//                gtfsData.setTripRouteID("21736564_2535", "000000");

            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("File Error");
                alert.setContentText("Unable to open a file.");
                alert.show();
            } catch (IncorrectFileFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("File Format Error");
                alert.setContentText(e.getMessage());
                alert.show();
            } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Entry Contents");
                alert.setContentText(e.getMessage());
                alert.show();
            } catch (GPSValuesOutOfRangeException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid GPS Stop Values");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }

    @FXML
    private void exportGTFS() {
        if (isInitialized) {
            FileChooser chooser = new FileChooser();
            File dir = chooser.showSaveDialog(null);
            if (dir != null) {
                dir.mkdir();
                try {
                    saveStopsFile(dir);
                    saveRoutesFile(dir);
                    saveTripsFile(dir);
                    saveStopTimesFile(dir);
                    System.out.println("Successfully saved.");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Cannot export without valid files.");
            alert.show();
        }
    }

    private void saveStopsFile(File dir) throws IOException {
        FileWriter writer = new FileWriter(dir.getPath() + File.separator + "stops.txt");
        String header = "stop_id,stop_lat,stop_lon";
        List<Observer> stops = gtfsData.getStops();
        writer.write(header);
        writer.write(System.lineSeparator());
        for (int i = 0; i < stops.size(); i++) {
            Stop current = (Stop) stops.get(i);
            writer.write(current.getStopID() + ",");
            writer.write(current.getLat() + ",");
            writer.write(current.getLon() + "");
            writer.write(System.lineSeparator());
        }
        writer.close();
    }

    private void saveRoutesFile(File dir) throws IOException {
        FileWriter writer = new FileWriter(dir.getPath() + File.separator + "routes.txt");
        String header = "route_id,route_color";
        List<Observer> routes = gtfsData.getRoutes();
        writer.write(header);
        writer.write(System.lineSeparator());
        for (int i = 0; i < routes.size(); i++) {
            Route current = (Route) routes.get(i);
            writer.write(current.getRouteID() + ",");
            writer.write(current.getColor());
            writer.write(System.lineSeparator());
        }
        writer.close();
    }

    private void saveTripsFile(File dir) throws IOException {
        FileWriter writer = new FileWriter(dir.getPath() + File.separator + "trips.txt");
        String header = "route_id,trip_id";
        HashMap<String, Observer> trips = gtfsData.getTrips();
        writer.write(header);
        writer.write(System.lineSeparator());
        for (Map.Entry mapElement : trips.entrySet()) {
            Trip current = (Trip) mapElement.getValue();
            writer.write(current.getRouteID() + ",");
            writer.write(current.getTripID() + "");
            writer.write(System.lineSeparator());
        }
        writer.close();
    }

    private void saveStopTimesFile(File dir) throws IOException {
        FileWriter writer = new FileWriter(dir.getPath() + File.separator + "stop_times.txt");
        String header = "trip_id,arrival_time,departure_time,stop_id,stop_sequence";
        HashMap<String, Observer> trips = gtfsData.getTrips();
        writer.write(header);
        writer.write(System.lineSeparator());
        for (Map.Entry mapElement : trips.entrySet()) {
            Trip currentTrip = (Trip) mapElement.getValue();
            List<StopTimes> stopTimes = currentTrip.getStopTimes();
            for (StopTimes current : stopTimes) {
                writer.write(currentTrip.getTripID() + ",");
                writer.write(current.getArrivalString() + ",");
                writer.write(current.getDepartureString() + ",");
                writer.write(current.getStop().getStopID() + ",");
                writer.write(current.getSequence() + "");
                writer.write(System.lineSeparator());
            }
        }
        writer.close();
    }

    private void displayItems(String type, String[] display) {
        String output = textArea.getText() + type + ": ";
        for (String item : display) {
            output += item + ", ";
        }
        output = output.substring(0, output.length() - 2) + "\n";
        textArea.setText(output);
    }

    /**
     * Displays all Trips on all Stops
     */
    @FXML
    private void displayTripsEachStop() {
        if (isInitialized) {
            try {
                TextArea stopItems = new TextArea();
                VBox vBox = new VBox(stopItems);
                Scene scene = new Scene(vBox, windowSize, windowSize);
                Stage displayStops = new Stage();
                displayStops.setScene(scene);
                String items = "";
                List<Observer> stops = gtfsData.getStops();
                for (int i = 0; i < gtfsData.getStops().size(); i++) {
                    Stop stop = (Stop) stops.get(i);
                    String stopID = stop.getStopID();
                    int numberOfTrips = getNumberOfTrips(stopID);
                    String numberOfTripsLine = "Stop ID: " + stopID +
                            " has " + numberOfTrips + " trips\n";
                    items = items + numberOfTripsLine;
                }
                stopItems.setText(items);
                stopItems.setEditable(false);
                displayStops.show();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Must import files before displaying anything");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Must import files before displaying anything");
            alert.show();
        }
    }

    /**
     * Displays all Routes on a Given StopID
     * @author Amish Verma
     * @throws NullPointerException if the list of routes is empty
     */
    @FXML
    private void displayRoutesForStopID() throws NullPointerException {
        if (isInitialized) {
            try {
                TextField searchBar = new TextField("Enter a stopId");

                TextArea stopItems = new TextArea();
                searchBar.setOnKeyReleased(k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        stopItems.setText(onRouteSearchStopID(searchBar.getText()));
                    }
                    stopItems.setEditable(false);
                });
                VBox vBox = new VBox(searchBar, stopItems);
                Scene scene = new Scene(vBox, windowSize, windowSize);
                Stage displayRoutes = new Stage();
                displayRoutes.setScene(scene);
                displayRoutes.show();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Must import files before displaying anything");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Must import files before displaying anything");
            alert.show();
        }
    }

    /**
     * Returns (correctly formatted) all Routes for a given StopID
     * @param searchID The Stop ID to be searched
     * @return The correctly formatted string for the TextArea
     * @author Amish Verma
     */
    @FXML
    private String onRouteSearchStopID(String searchID) {
        String items = "";
        try {
            List<Route> routes = getRoutesFromStopID(searchID);
            for (Route route : routes) {
                String allRoutes = "Route ID: " + route.getRouteID() +
                        "\n";
                items = items + allRoutes;
            }
        } catch (NullPointerException e) {
            items = "Stop does not exist";
        }
        return items;
    }
    /**
     * Displays all Stops on a Given RouteID
     * @author Amish Verma
     * @throws NullPointerException if the list of stops is empty
     */
    @FXML
    private void displayStopsForRouteID() throws NullPointerException {
        if (isInitialized) {
            try {
                TextField searchBar = new TextField("Enter a RouteId");

                TextArea stopItems = new TextArea();
                searchBar.setOnKeyReleased(k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        stopItems.setText(onStopSearchRouteID(searchBar.getText()));
                    }
                    stopItems.setEditable(false);
                });
                VBox vBox = new VBox(searchBar, stopItems);
                Scene scene = new Scene(vBox, windowSize, windowSize);
                Stage displayStops = new Stage();
                displayStops.setScene(scene);
                displayStops.show();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Must import valid files before displaying anything");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Must import files before displaying anything");
            alert.show();
        }
    }
    /**
     * Returns (correctly formatted) all Stops for a given RouteID
     * @param searchID The Route ID to be searched
     * @return The correctly formatted string for the TextArea
     * @author Amish Verma
     */
    @FXML
    private String onStopSearchRouteID(String searchID) {
        String items = "";
        try {
            List<Stop> stops = getStopsFromRouteID(searchID);
            for (Stop stop : stops) {
                String allStops = "Stop ID: " + stop.getStopID() +
                        "\n";
                items = items + allStops;
            }
        } catch (NullPointerException e) {
            items = "Route does not exist";
        }
        return items;
    }
    /**
     * Displays the closest Trip for a StopID
     * @throws NullPointerException if list of trips empty
     */
    @FXML
    private void displayNextTripStopID() throws NullPointerException {
        if (isInitialized) {
            try {
                TextField searchBar = new TextField("Enter a stopId");
                TextArea tripItems = new TextArea();
                tripItems.setEditable(false);
                searchBar.setOnKeyReleased(k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        tripItems.setText(onNextTripSearchStopID(searchBar.getText()));
                    }
                });
                VBox vBox = new VBox(searchBar, tripItems);
                Scene scene = new Scene(vBox, windowSize, windowSize);
                Stage displayNextTrip = new Stage();
                displayNextTrip.setScene(scene);
                displayNextTrip.show();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Must import files before displaying anything");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Must import files before displaying anything");
            alert.show();
        }
    }

    /**
     * Returns (correctly formatted) the next trip for the given Stop
     * @param searchID the StopID to be searched for
     * @return The correctly formatted string to be displayed
     * @throws NullPointerException if list of trips empty
     */
    @FXML
    private String onNextTripSearchStopID(String searchID) throws NullPointerException {
        String items = "";
        try {
            List<Trip> trips = getStop(searchID).getNextTrips();
            String nextTrips = "";
            for (Trip trip : trips) {
                nextTrips += "Trip ID: " + trip.getTripID() +
                        "\n";
            }
            items = items + nextTrips;
        } catch (NullPointerException e) {
            items = "Stop does not exist";
        }
        return items;
    }
    /**
     * Displays the remaining Trips in a Day
     * @throws NullPointerException if list of trips empty
     */
    @FXML
    private void displayTripsLeftInDay() throws NullPointerException {
        if (isInitialized) {
            try {
                TextField searchBar = new TextField("Enter a routeId");

                TextArea tripItems = new TextArea();
                tripItems.setEditable(false);
                searchBar.setOnKeyReleased(k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        tripItems.setText(onTripsLeftSearchRouteID(searchBar.getText()));
                    }
                });
                VBox vBox = new VBox(searchBar, tripItems);
                Scene scene = new Scene(vBox, windowSize, windowSize);
                Stage displayNextTrip = new Stage();
                displayNextTrip.setScene(scene);
                displayNextTrip.show();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Must import files before displaying anything");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Must import files before displaying anything");
            alert.show();
        }
    }
    /**
     * Returns (correctly formatted) the next trip for the given Stop
     * @param searchID the RouteId to be searched for
     * @return The correctly formatted string to be displayed
     * @throws NullPointerException if list of trips empty
     */
    @FXML
    private String onTripsLeftSearchRouteID(String searchID) throws NullPointerException {
        String items = "";
        try {
            List<Trip> trips = getRoute(searchID).getTripsLeftInDay();
            String nextTrips = "";
            for (Trip trip : trips) {
                nextTrips += "Trip ID: " + trip.getTripID() +
                        "\n";
            }
            items = items + nextTrips;
            if (items.isEmpty()){
                items = "No trips left in day";
            }
        } catch (NullPointerException e) {
            items = "Route does not exist";
        }
        return items;
    }

    /**
     * Gets the number of trips given a stop ID
     * @param stopID The stop ID linked to the trip
     * @return An int that will represent how many trips
     * contain the stop ID.
     */
    public int getNumberOfTrips(String stopID) {
        return gtfsData.getStop(stopID).getTrips().size();
    }

    /**
     * Adds all stops from a given stops.txt GTFS file
     *
     * @param stops The stops.txt file holding the stop details
     * @throws FileNotFoundException        If the file cannot be accessed
     * @throws IncorrectFileFormatException If there is a problem with the file
     * @throws GPSValuesOutOfRangeException If the GPS values are out of range
     */
    public void readStops(File stops) throws FileNotFoundException,
            IncorrectFileFormatException, GPSValuesOutOfRangeException {
        Scanner reader = new Scanner(stops);
        String header = reader.nextLine();
        int i = 0;
        List<String> headerList = verifyStopHeader(header);
        int stopIDIndex = headerList.indexOf("stop_id");
        int stopLatIndex = headerList.indexOf("stop_lat");
        int stopLonIndex = headerList.indexOf("stop_lon");

        while (reader.hasNextLine()) {
            String[] stopInfo = reader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            verifyStopEntry(stopInfo, stopIDIndex, stopLatIndex, stopLonIndex);
            String stopID = stopInfo[stopIDIndex];
            double stopLat = Double.parseDouble(stopInfo[stopLatIndex]);
            double stopLon = Double.parseDouble(stopInfo[stopLonIndex]);
            addStop(stopID, stopLat, stopLon);
            if (i < 3) {
                displayItems("Stop", new String[] {"Stop ID: " + stopID, "Longitude: " + stopLon,
                        "Latitude: " + stopLat});
                i++;
            }
        }
    }

    /**
     * Checks to make sure stop header is valid.
     *
     * @param header The header line of the file in a string format
     * @return headerList A list containing all header fields of the file
     * @throws IncorrectFileFormatException If a required header is missing
     */
    public List<String> verifyStopHeader(String header) throws IncorrectFileFormatException {
        List<String> headerList = listFromHeader(header);
        if (!headerList.contains("stop_id") || !headerList.contains("stop_lat") ||
                !headerList.contains("stop_lon")) {
            throw new IncorrectFileFormatException("stops.txt is missing an indexing header");
        }
        return headerList;
    }

    /**
     * Makes sure that the data fields of a stop entry are valid
     *
     * @param stopInfo     An array containing the string fields of the entry
     * @param stopIDIndex  The index of the stop ID in the array
     * @param stopLatIndex The index of the stop latitude in the array
     * @param stopLonIndex The index of the stop longitude in the array
     * @throws IllegalStateException    If one of the fields is empty or there aren't enough fields
     * @throws IllegalArgumentException If one of the fields contains the wrong type of data
     * @throws GPSValuesOutOfRangeException If one of the GPS values is out of range
     */
    public void verifyStopEntry(String[] stopInfo, int stopIDIndex, int stopLatIndex,
                                int stopLonIndex) throws IllegalStateException,
            IllegalArgumentException, GPSValuesOutOfRangeException {
        final int requiredNumberOfFields = 3;
        if (stopInfo.length < requiredNumberOfFields) {
            throw new IllegalStateException("Entry in stops.txt does not have enough fields");
        }
        if (stopInfo[stopIDIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stops.txt is missing a stopID");
        }
        if (stopInfo[stopLatIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stops.txt is missing a latitude");
        }
        try {
            Double.parseDouble(stopInfo[stopLatIndex]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Entry in stops.txt has a non numeric latitude");
        }
        if (stopInfo[stopLonIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stops.txt is missing a longitude");
        }
        try {
            Double.parseDouble(stopInfo[stopLonIndex]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Entry in stops.txt has a non numeric longitude");
        }
        double lat = Double.parseDouble(stopInfo[stopLatIndex]);
        if (lat < minLat || lat > maxLat) {
            throw new GPSValuesOutOfRangeException("Latitude value of a stop is out of range");
        }
        double lon = Double.parseDouble(stopInfo[stopLonIndex]);
        if (lon < minLon || lon > maxLon) {
            throw new GPSValuesOutOfRangeException("Longitude value of a stop is out of range");
        }

    }

    /**
     * Adds all routes from a given routes.txt GTFS file
     *
     * @param routes The routes.txt file holding the route details
     * @throws FileNotFoundException        If the file cannot be accessed
     * @throws IncorrectFileFormatException If there is a problem with the file
     */
    public void readRoutes(File routes) throws FileNotFoundException, IncorrectFileFormatException {
        Scanner reader = new Scanner(routes);
        String header = reader.nextLine();
        int i = 0;
        List<String> headerList = verifyRouteHeader(header);
        int routeIDIndex = headerList.indexOf("route_id");
        int routeColorIndex = headerList.indexOf("route_color");

        while (reader.hasNextLine()) {
            String[] routeInfo = reader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            verifyRouteEntry(routeInfo, routeIDIndex, routeColorIndex);
            String routeID = routeInfo[routeIDIndex];
            String routeColor = routeInfo[routeColorIndex];
            addRoute(routeID, routeColor);
            if (i < 3) {
                displayItems("Route", new String[] {"Route ID: " + routeID,
                        "Color: " + routeColor});
                i++;
            }
        }
    }

    /**
     * Checks to make sure route header is valid.
     *
     * @param header The header line of the file in a string format
     * @return headerList A list containing all header fields of the file
     * @throws IncorrectFileFormatException If a required header is missing
     */
    public List<String> verifyRouteHeader(String header) throws IncorrectFileFormatException {
        List<String> headerList = listFromHeader(header);
        if (!headerList.contains("route_id") || !headerList.contains("route_color")) {
            throw new IncorrectFileFormatException("routes.txt is missing an indexing header");
        }
        return headerList;
    }

    /**
     * Makes sure that the data fields of a route entry are valid
     *
     * @param routeInfo       An array containing the strings from the data fields
     * @param routeIDIndex    The index of the route ID in the array
     * @param routeColorIndex The index of the route color in the array
     * @throws IllegalStateException If one of the fields is empty or there aren't enough fields
     */
    public void verifyRouteEntry(String[] routeInfo, int routeIDIndex, int routeColorIndex)
            throws IllegalStateException {
        final int requiredNumberOfFields = 2;
        if (routeInfo.length < requiredNumberOfFields) {
            throw new IllegalStateException("Entry in routes.txt does not have enough fields");
        }
        if (routeInfo[routeIDIndex].isEmpty()) {
            throw new IllegalStateException("Entry in routes.txt is missing a route ID");
        }
        if (routeInfo[routeColorIndex].isEmpty()) {
            throw new IllegalStateException("Entry in routes.txt is missing a route color");
        }
    }

    /**
     * Adds all routes from a given routes.txt GTFS file
     *
     * @param trips The routes.txt file holding the trip details
     * @throws FileNotFoundException        If the file cannot be accessed
     * @throws IncorrectFileFormatException If there is a problem with the file
     */
    public void readTrips(File trips) throws FileNotFoundException, IncorrectFileFormatException {
        Scanner reader = new Scanner(trips);
        String header = reader.nextLine();
        int i = 0;
        List<String> headerList = verifyTripHeader(header);
        int routeIDIndex = headerList.indexOf("route_id");
        int tripIDIndex = headerList.indexOf("trip_id");

        while (reader.hasNextLine()) {
            String[] tripInfo = reader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            verifyTripEntry(tripInfo, tripIDIndex, routeIDIndex);
            String routeID = tripInfo[routeIDIndex];
            String tripID = tripInfo[tripIDIndex];
            Route accessedRoute = gtfsData.getRoute(routeID);
            accessedRoute.addTrip(routeID, tripID);
            gtfsData.addTrip(tripID, accessedRoute.getTrip(routeID, tripID));
            if (i < 3) {
                displayItems("Trip", new String[] {"Route ID: " + routeID, "Trip ID: " + tripID});
                i++;
            }
        }
    }

    /**
     * Checks to make sure trip header is valid.
     *
     * @param header The header line of the file in a string format
     * @return headerList A list containing all header fields of the file
     * @throws IncorrectFileFormatException If a required header is missing
     */
    public List<String> verifyTripHeader(String header) throws IncorrectFileFormatException {
        List<String> headerList = listFromHeader(header);
        if (!headerList.contains("route_id") || !headerList.contains("trip_id")) {
            throw new IncorrectFileFormatException("trips.txt is missing an indexing header");
        }
        return headerList;
    }

    /**
     * Makes sure that the data fields of a trip entry are valid
     *
     * @param tripInfo     An array containing the strings from the data fields
     * @param tripIDIndex  The index of the trip ID in the array
     * @param routeIDIndex The index of the route ID in the array
     * @throws IllegalStateException If one of the fields is empty or there aren't enough fields
     */
    public void verifyTripEntry(String[] tripInfo, int tripIDIndex, int routeIDIndex)
            throws IllegalStateException {
        final int requiredNumberOfFields = 2;
        if (tripInfo.length < requiredNumberOfFields) {
            throw new IllegalStateException("Entry in trips.txt does not have enough fields");
        }
        if (tripInfo[tripIDIndex].isEmpty()) {
            throw new IllegalStateException("Entry in trps.txt is missing a trip ID");
        }
        if (tripInfo[routeIDIndex].isEmpty()) {
            throw new IllegalStateException("Entry in trips.txt is missing a route ID");
        }

    }

    /**
     * Adds all stop times from the given stop_times.txt GTFS file
     *
     * @param stopTimes The stop_times.txt file holding the stop time details
     * @throws FileNotFoundException        If the file cannot be accessed
     * @throws IncorrectFileFormatException If there is a problem with the file
     */
    public void readStopTimes(File stopTimes)
            throws FileNotFoundException, IncorrectFileFormatException {
        Scanner reader = new Scanner(stopTimes);
        String header = reader.nextLine();
        int i = 0;
        List<String> headerList = verifyStopTimesHeader(header);
        int tripIDIndex = headerList.indexOf("trip_id");
        int arrivalIndex = headerList.indexOf("arrival_time");
        int departureIndex = headerList.indexOf("departure_time");
        int stopIDIndex = headerList.indexOf("stop_id");
        int sequenceIDIndex = headerList.indexOf("stop_sequence");
        while (reader.hasNextLine()) {
            String[] stopTimesInfo = reader.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            String tripID = stopTimesInfo[tripIDIndex];
            String arrival = stopTimesInfo[arrivalIndex];
            String departure = stopTimesInfo[departureIndex];
            String stopID = stopTimesInfo[stopIDIndex];
            int sequence = Integer.parseInt(stopTimesInfo[sequenceIDIndex]);
            Trip accessedTrip = gtfsData.getTrip(tripID);
            Stop accessedStop = gtfsData.getStop(stopID);
            accessedTrip.addStopTime(arrival, departure, accessedStop, sequence);
            if (i < 3) {
                displayItems("Stop Times", new String[] {"Arrival Time: " + arrival,
                        "Departure Time: " + departure, "Stop ID: " + stopID,
                        "Sequence: " + sequence});
                i++;
            }
        }
    }

    /**
     * Checks to make sure stop time header is valid.
     *
     * @param header The header line of the file in a string format
     * @return headerList A list containing all header fields of the file
     * @throws IncorrectFileFormatException If a required header is missing
     */
    public List<String> verifyStopTimesHeader(String header) throws IncorrectFileFormatException {
        List<String> headerList = listFromHeader(header);
        if (!headerList.contains("trip_id") || !headerList.contains("arrival_time") ||
                !headerList.contains("departure_time") || !headerList.contains("stop_id") ||
                !headerList.contains("stop_sequence")) {
            throw new IncorrectFileFormatException("stops_times.txt is missing an indexing header");
        }
        return headerList;
    }

    /**
     * Checks to make sure that the data fields of a stop time entry are valid
     *
     * @param stopTimesInfo   An array containing the fields in a string format
     * @param tripIDIndex     The index of the trip ID in the array
     * @param arrivalIndex    The index of the arrival time in the array
     * @param departureIndex  The index of the departure time in the array
     * @param stopIDIndex     The index of the stop ID in the array
     * @param sequenceIDIndex The index of the sequence ID in the array
     * @throws IllegalStateException    If one of the fields are empty or there aren't enough fields
     * @throws IllegalArgumentException If one of the fields contains an invalid data type
     */
    public void verifyStopTimesEntry(String[] stopTimesInfo, int tripIDIndex, int arrivalIndex,
                                     int departureIndex, int stopIDIndex, int sequenceIDIndex)
            throws IllegalStateException, IllegalArgumentException {
        final int requiredNumberOfFields = 5;
        if (stopTimesInfo.length < requiredNumberOfFields) {
            throw new IllegalStateException("Entry in stops_times.txt does not have enough fields");
        }
        if (stopTimesInfo[tripIDIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stops_times.txt is missing a trip ID");
        }
        if (stopTimesInfo[arrivalIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stop_times.txt is missing an arrival time");
        }
        if (stopTimesInfo[departureIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stop_times.txt is missing a departure time");
        }
        if (stopTimesInfo[stopIDIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stops_times.txt is missing a stop ID");
        }
        if (!stopTimesInfo[stopIDIndex].matches("\\d+")) {
            throw new IllegalArgumentException("Entry in stops_times.txt has a non integer " +
                    "stop ID");
        }
        if (stopTimesInfo[sequenceIDIndex].isEmpty()) {
            throw new IllegalStateException("Entry in stops_times.txt is missing a sequence ID");
        }
        if (!stopTimesInfo[sequenceIDIndex].matches("\\d+")) {
            throw new IllegalArgumentException("Entry in stops_times.txt has a non integer" +
                    " sequence ID");
        }
    }

    private List<String> listFromHeader(String header) {
        String[] headerSplit = header.split(",");
        return Arrays.asList(headerSplit);
    }

    private List<Route> getRoutesFromStopID(String stopID) {
        List<Route> routes = new ArrayList<>();
        Stop stop = gtfsData.getStop(stopID);
        List<Trip> trips = stop.getTrips();
        for (Trip trip : trips) {
            Route route = gtfsData.getRoute(trip.getRouteID());
            if (!routes.contains(route)) {
                routes.add(gtfsData.getRoute(trip.getRouteID()));
            }
        }
        return routes;
    }

    private List<Stop> getStopsFromRouteID(String routeID) {
        Route route = gtfsData.getRoute(routeID);
        List<Trip> trips = route.getTrips();
        List<Stop> stops = new ArrayList<>();
        for(Trip trip : trips) {
            List<StopTimes> stopTimes = trip.getStopTimes();
            for(StopTimes stopTime : stopTimes) {
                Stop stop = stopTime.getStop();
                if(!stops.contains(stop)) {
                    stops.add(stop);
                }
            }
        }
        return stops;
    }


    /**
     * Adds a new route to the application
     *
     * @param routeID The ID of the new route
     * @param color   The color of the route
     * @return True if the addition was successful
     */
    public boolean addRoute(String routeID, String color) {
        Route route = new Route(routeID, color);
        gtfsData.addRoute(route);
        return true;
    }

    /**
     * Adds a new stop to the application
     *
     * @param stopID  The ID of the new stop
     * @param stopLat The latitudinal location of the new stop
     * @param stopLon The longitudinal location of the new stop
     * @return A boolean true when adding the stop
     */
    public boolean addStop(String stopID, Double stopLat, Double stopLon) {
        Stop stop = new Stop(stopID, stopLat, stopLon);
        gtfsData.addStop(stop);
        return true;
    }

    /**
     * Adds a new trip to the application
     *
     * @param tripID  The ID of the new trip
     * @param trip The Trip object of the new trip
     */
    public void addTrip(String tripID, Trip trip) {
        gtfsData.addTrip(tripID, trip);
    }

    /**
     * Gets a trip based on tripID
     *
     * @param tripID The id of the trip
     * @return The trip requested
     */
    public Trip getTrip(String tripID) {
        return gtfsData.getTrip(tripID);
    }


    /**
     * Edits a current route in the application
     *
     * @param routeID   The route ID to be edited
     * @param agencyID  The desired agency ID of the route
     * @param shortName The desired name of the route
     * @param desc      The desired description of the route
     * @return True if the edit is successful
     */
    public boolean editRoute(int routeID, int agencyID, String shortName, String desc) {
        return false;
    }

    /**
     * Edits a current stop in the application
     *
     * @param stopID   The ID of the stop to be modified
     * @param stopName The desired name of the route
     * @param stopLat  The desired latitudinal location of the stop
     * @param stopLon  The desired longitudinal location of the stop
     * @return A boolean true when editing stop
     */
    public boolean editStop(int stopID, String stopName, double stopLat, double stopLon) {
        return false;
    }

    /**
     * Edits a current trip in the application
     *
     * @param tripID    The ID of the trip to be modified
     * @param routeID   The ID of the route on which the trip should exist
     * @param serviceID The desired service ID of the trip
     * @return A boolean true when editing trip
     */
    public boolean editTrip(int tripID, int routeID, String serviceID) {
        return false;
    }

    /**
     * Gets the route based on routeID
     *
     * @param routeID The ID of the route as an int
     * @return The route object
     */
    public Route getRoute(String routeID) {
        return gtfsData.getRoute(routeID);
    }

    /**
     * Gets the stop based on stopID
     *
     * @param stopID The ID of the stop
     * @return The stop object
     */
    public Stop getStop(String stopID) {
        return gtfsData.getStop(stopID);
    }

    /**
     * A class to make an exception which will be thrown for incorrect file
     * format whenever the application imports and reads invalid text files.
     */
    public class IncorrectFileFormatException extends Exception {
        /**
         * An exception that will be thrown from the verifyHeaders and
         * verifyEntries methods whenever the file does not contain
         * the required parts needed to import files into this application
         * @param errorMessage The message to be displayed whenever exception thrown
         */
        public IncorrectFileFormatException(String errorMessage) {
            super(errorMessage);
        }
    }

    /**
     * A class to make an exception which will be thrown for incorrect file
     * format whenever the application imports and reads invalid latitude and longitudes.
     */
    public class GPSValuesOutOfRangeException extends Exception {
        /**
         * An exception that will be thrown from the verifyHeaders and
         * verifyEntries methods whenever the file does not contain
         * the required parts needed to import files into this application
         * @param errorMessage The message to be displayed whenever exception thrown
         */
        public GPSValuesOutOfRangeException(String errorMessage) {
            super(errorMessage);
        }
    }
}
