import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    /**
     * Reads the XML file and fills the instance variables locationMap, locations
     * and
     * trails.
     * 
     * @param filename
     */
    public void initializeMap(String filename) {
        try {
            File file = new File(filename);

            // instantiating DOM class in order to parse the XML inputs
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            // getting a NodeList of the given locations
            NodeList locations = document.getElementsByTagName("Location");

            // getting a NodeList of the given trails
            NodeList trails = document.getElementsByTagName("Trail");

            // iterating through the NodeLists and instantiating the Locations and Trails
            for (int i = 0; i < locations.getLength(); i++) {
                Element tempElement = (Element) locations.item(i);
                String locationName = tempElement.getElementsByTagName("Name").item(0).getTextContent();
                int locationID = Integer.parseInt(tempElement.getElementsByTagName("Id").item(0).getTextContent());
                Location newLocation = new Location(locationName, locationID);
                this.locations.add(newLocation);
                this.locationMap.put(locationID, newLocation);
            }
            for (int i = 0; i < trails.getLength(); i++) {
                Element tempElement = (Element) trails.item(i);
                int sourceID = Integer.parseInt(tempElement.getElementsByTagName("Source").item(0).getTextContent());
                int destinationID = Integer
                        .parseInt(tempElement.getElementsByTagName("Destination").item(0).getTextContent());
                int dangerLevel = Integer.parseInt(tempElement.getElementsByTagName("Danger").item(0).getTextContent());
                Trail newTrail = new Trail(this.locationMap.get(sourceID), this.locationMap.get(destinationID),
                        dangerLevel);
                this.trails.add(newTrail);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();
        // Fill the safestTrail list and return it.
        // Select the optimal Trails from the Trail list that you have read.
        // TODO: Your code here
        return safestTrails;
    }

    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.
        // TODO: Your code here
    }
}
