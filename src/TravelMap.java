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

    public Map<Integer, ArrayList<Integer>> safeAdjacencyMatrix = new HashMap<>();

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
        for (Location location : locations) {
            safeAdjacencyMatrix.put(location.id, new ArrayList<>());
        }
    }

    /**
     * Utilizing Kruskal's algorithm, this method sorts the trails by their danger
     * levels and adds them to the spanning tree list in ascending order if the new
     * trail does not form
     * a cycle.
     * 
     * @return safest trails ArrayList
     */
    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();
        Collections.sort(this.trails);
        for (Trail trail : this.trails) {
            if (!areConnected(trail.source, trail.destination)) {
                safestTrails.add(trail);

                ArrayList<Integer> tempList1 = (ArrayList<Integer>) safeAdjacencyMatrix.get(trail.source.id).clone();
                ArrayList<Integer> tempList2 = (ArrayList<Integer>) safeAdjacencyMatrix.get(trail.destination.id)
                        .clone();

                if (!tempList1.contains(trail.destination.id)) {
                    tempList1.add(trail.destination.id);
                }
                if (!tempList2.contains(trail.source.id)) {
                    tempList2.add(trail.source.id);
                }
                this.safeAdjacencyMatrix.put(trail.destination.id, tempList2);
                this.safeAdjacencyMatrix.put(trail.source.id, tempList1);
            }
        }
        return safestTrails;
    }

    /**
     * This method checks if the given two locations are already connected in the
     * minimum spanning tree and if so returns true, else false.
     * 
     * @param source      location
     * @param destination location
     * @return areConnected
     */
    public boolean areConnected(Location source, Location destination) {
        // DFS to see if they are connected
        ArrayList<Location> stack = new ArrayList<>();
        stack.add(0, source);
        Set<Integer> visited = new HashSet<>();

        while (!stack.isEmpty()) {
            Location current = stack.remove(0);
            visited.add(current.id);

            if (current.id == destination.id) {
                return true;
            }
            ArrayList<Integer> neighborsList = safeAdjacencyMatrix.get(current.id);
            for (int neighbor : neighborsList) {
                if (!visited.contains(neighbor)) {
                    stack.add(0, this.locationMap.get(neighbor));
                }

            }
        }
        return false;
    }

    /**
     * Prints the given list of safest trails conforming to the given output format.
     * 
     * @param safestTrails
     */
    public void printSafestTrails(List<Trail> safestTrails) {
        int totalDanger = 0;
        System.out.println("Safest trails are:");
        for (Trail trail : safestTrails) {
            totalDanger += trail.danger;
            System.out.printf("The trail from %s to %s with danger %d\n", trail.source.toString(),
                    trail.destination.toString(), trail.danger);
        }
        System.out.printf("Total danger: %d\n", totalDanger);
    }
}
