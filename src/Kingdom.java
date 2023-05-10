import java.io.File;
import java.util.*;

public class Kingdom {

    // keeping an undirected adjacency map in order to find separate colonies easily
    public HashMap<Integer, ArrayList<Integer>> adjacencyMatrixUndirected = new HashMap<>();
    // keeping the original directions
    public HashMap<Integer, ArrayList<Integer>> adjacencyMatrixDirected = new HashMap<>();

    public int colonyCount = 0;

    // needed with the DFS algorithm
    public Set<Integer> visitedCities = new HashSet<>();

    /**
     * Reads the adjacency matrix from the kingdom.txt file and fills the instance
     * variables {@code adjacencyMatrixUndirected} and
     * {@code adjacencyMatrixDirected}.
     * 
     * @param filename
     */
    public void initializeKingdom(String filename) {
        File file = new File(filename);
        try {
            Scanner scan = new Scanner(file);

            int lineCounter = 0;
            while (scan.hasNextLine()) {
                String[] tempLine = scan.nextLine().split(" ");
                for (int i = 0; i < tempLine.length; i++) {
                    // for mapping the column elements to row elements WITH DIRECTIONS
                    ArrayList<Integer> tempDirectedList = new ArrayList<>();
                    if (adjacencyMatrixDirected.get(lineCounter) != null) {
                        tempDirectedList = (ArrayList<Integer>) adjacencyMatrixDirected.get(lineCounter).clone();
                    }
                    // for mapping column elements to row elements
                    ArrayList<Integer> tempList = new ArrayList<>();
                    if (adjacencyMatrixUndirected.get(lineCounter) != null) {
                        tempList = (ArrayList<Integer>) adjacencyMatrixUndirected.get(lineCounter).clone();
                    }
                    // for mapping row elements to column elements
                    ArrayList<Integer> tempList2 = new ArrayList<>();
                    if (adjacencyMatrixUndirected.get(i) != null) {
                        tempList2 = (ArrayList<Integer>) adjacencyMatrixUndirected.get(i).clone();
                    }

                    // adding the new connection to the lists
                    if (tempLine[i].equals("1")) {

                        tempDirectedList.add(i);

                        tempList.add(i);

                        tempList2.add(lineCounter);

                    }
                    adjacencyMatrixDirected.put(lineCounter, tempDirectedList);
                    adjacencyMatrixUndirected.put(lineCounter, tempList);
                    adjacencyMatrixUndirected.put(i, tempList2);
                }
                lineCounter++;
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Identifies the colonies using the instance variables defined as adjacency
     * matrices.
     * 
     * @return colonies ArrayList
     */
    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();
        for (int city : adjacencyMatrixUndirected.keySet()) {
            Colony newColony = foundNewColony(city);
            if (newColony != null) {
                for (int integer : newColony.cities) {
                    newColony.roadNetwork.put(integer,
                            (ArrayList<Integer>) adjacencyMatrixDirected.get(integer).clone());
                }
                colonies.add(newColony);
            }
        }
        return colonies;
    }

    /**
     * Using DFS algorithm, this method starts a new traversal if the given source
     * city is unvisited, marks each node as visited along the way and increments
     * the colony count when the traversal is finished.
     * 
     * @param cityID
     * @return the new Colony resulting from the traversal, null in case of no
     *         traversal
     */
    public Colony foundNewColony(int cityID) {
        if (!visitedCities.contains(cityID)) {
            Colony newColony = new Colony();
            colonyCount++;

            ArrayList<Integer> stack = new ArrayList<>();
            stack.add(0, cityID);

            while (!stack.isEmpty()) {
                int current = stack.get(0);
                visitedCities.add(current);
                stack.remove(0);
                if (!newColony.cities.contains(current)) {
                    newColony.cities.add(current);
                }

                ArrayList<Integer> neighborsList = adjacencyMatrixUndirected.get(current);
                for (int neighbor : neighborsList) {
                    if (!visitedCities.contains(neighbor)) {
                        stack.add(0, neighbor);
                    }
                }
            }
            return newColony;
        }
        return null;
    }

    /**
     * Prints the given list of discovered colonies conforming to the given output
     * format.
     * 
     * @param discoveredColonies
     */
    public void printColonies(List<Colony> discoveredColonies) {
        System.out.println("Discovered colonies are: ");
        for (int i = 0; i < colonyCount; i++) {
            String lineToPrint = "";
            Collections.sort(discoveredColonies.get(i).cities);
            System.out.printf("Colony %d: [", i + 1);
            for (int city : discoveredColonies.get(i).cities) {
                lineToPrint += (city + 1) + ", ";
            }
            lineToPrint = lineToPrint.substring(0, lineToPrint.length() - 2);
            lineToPrint += "]";
            System.out.println(lineToPrint);
        }
    }
}
