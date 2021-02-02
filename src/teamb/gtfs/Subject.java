/*
 * Course: SE2030 - 041
 * Fall 2020
 * Group Lab - GTFS Application
 * Lab members: Kian, Tommy, Amish, Nicholas
 * Created: 10/6/2020
 */
package teamb.gtfs;


import java.util.HashMap;
import java.util.List;

/**
 * A Subject interface which will be implemented to make an observer pattern
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
public interface Subject {

    /**
     * Attaches the observer to the list of observers
     * @author Nicholas Dang
     * @param observer The observer to be attached
     * @param observers The list of observers
     */
    void attach(Observer observer, List<Observer> observers);

    /**
     * Detaches the observer from the list of observers
     * @author Nicholas Dang
     * @param observer The observer to be detached
     * @param observers The list of observers
     */
    void detach(Observer observer, List<Observer> observers);

    /**
     * Notifies the observers of changes made to an object
     * @author Nicholas Dang
     * @param item The item to notify the observers of as a string
     * @param id The id of the observer as a string
     * @param observers the list of observers
     *
     */
    void notifyObserver(String item, String id, List<Observer> observers);

    /**
     * Notifies the observers of changes made to an object
     * @author Nicholas Dang
     * @param item The item as a string
     * @param id the id as a string
     * @param observers the list of observers as a hash map
     */
    void notifyObserver(String item, String id, HashMap<String, Observer> observers);

}