import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<Integer> trapList = new ArrayList<>();

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
            trapList.clear();

            for (int city : colony.cities) {
                searchForTraps(colony, city, new ArrayList<Integer>(),
                        new ArrayList<Integer>());

                if (!trapList.isEmpty()) {
                    break;
                }
            }

            List<Integer> temp = new ArrayList<>(trapList);
            traps.add(temp);
        }
        return traps;
    }

    /**
     * Utilizing DFS algorithm recursively, cycle
     * locations are detected and stored in the global variable {@code trapList}
     * (which is later cleared for each colony).
     * 
     * @param colony
     * @param city     traversals source city id
     * @param visited
     * @param trapPath
     * @return whether the trapList is filled or not
     */
    public boolean searchForTraps(Colony colony, int city, List<Integer> visited, List<Integer> trapPath) {

        if (trapPath.contains(city)) {
            trapPath = trapPath.subList(trapPath.indexOf(city), trapPath.size());
            trapList = trapPath;
            return true;
        } else if (visited.contains(city)) {
            return false;
        }
        visited.add(city);
        trapPath.add(city);
        for (int neighbor : colony.roadNetwork.get(city)) {
            if (searchForTraps(colony, neighbor, visited, trapPath)) {
                return true;
            }
        }

        trapPath.remove(Integer.valueOf(city));
        return false;
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
