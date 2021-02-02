/*
 * Course: SE2030 - 041
 * Fall 2020
 * Group Lab - GTFS Application
 * Lab members: Kian, Tommy, Amish, Nicholas
 * Created: 10/6/2020
 */
package teamb.gtfs;


import javafx.scene.control.TextArea;

/**
 * An Observer class that will be implemented to make the Observer pattern
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
public interface Observer {

    /**
     * Updates the observer of the changes made
     * @author Nicholas Dang
     * @param item The item changed as a string
     * @param id The id of the observer
     * @param textArea the text area in the gui to be updated
     */
    void update(String item, String id, TextArea textArea);

}