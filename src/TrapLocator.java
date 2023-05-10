import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    /**
     * Trap (cycle) positions for each colony are identified with this method. An
     * empty
     * list is kept if the colony is safe.
     * 
     * @return the list of trap locations for each colony
     */
    public List<List<Integer>> revealTraps() {

        List<List<Integer>> traps = new ArrayList<>();

        for (Colony colony : this.colonies) {
            List<Integer> trapLocations = searchForTraps(colony);
            traps.add(trapLocations);
        }
        return traps;
    }

    /**
     * Utilizing BFS algorithm and starting a new traversal from each city, cycle
     * locations are detected and returned as an ArrayList.
     * 
     * @param cityID id of the city to start the traversal with
     * @param colony the colony in which our source city is in.
     * @return the trap locations as a list
     */
    public List<Integer> searchForTraps(Colony colony) {
        List<Integer> trapList = new ArrayList<>();
        // starting a new traversal at each city
        for (int city : colony.cities) {
            // keeping an ArrayList where we can reach indices of the visited elements.
            ArrayList<Integer> tempVisitedCities = new ArrayList<>();
            ArrayList<Integer> queue = new ArrayList<>();
            queue.add(0, city);
            while (!queue.isEmpty()) {
                int current = queue.remove(queue.size() - 1);
                tempVisitedCities.add(current);

                for (int neighbor : colony.roadNetwork.get(current)) {
                    if (!tempVisitedCities.contains(neighbor)) {
                        queue.add(0, neighbor);
                    } else {
                        int cycleStart = tempVisitedCities.indexOf(neighbor);
                        trapList = tempVisitedCities.subList(cycleStart, tempVisitedCities.size());
                        return trapList;
                    }
                }
            }
        }
        return trapList;
    }

    /**
     * For each colony, if a time trap was encountered, the cities
     * that create the trap are printed.
     * If a time trap was not encountered in this colony, then prints "Safe".
     * 
     * @param traps
     */
    public void printTraps(List<List<Integer>> traps) {
        System.out.println("Danger exploration conclusions:");
        for (int i = 0; i < colonies.size(); i++) {
            System.out.printf("Colony %d: ", i + 1);
            String lineToPrint = "Dangerous. Cities on the dangerous path: [";
            if (traps.get(i).size() == 0) {
                lineToPrint = "Safe";
                System.out.println(lineToPrint);
                continue;
            }
            Collections.sort(traps.get(i));
            for (int trapCity : traps.get(i)) {
                lineToPrint += (trapCity + 1) + ", ";
            }
            lineToPrint = lineToPrint.substring(0, lineToPrint.length() - 2);
            lineToPrint += "]";
            System.out.println(lineToPrint);
        }
    }

}
