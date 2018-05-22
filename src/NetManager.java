import exceptions.TooYoungException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * Class that stores data and contains all operation methods
 * @author Duc Minh Le (s3651764)
 */
public class NetManager {

    // People data
    static final ObservableList<Person> people = FXCollections.observableArrayList();

    // Reltaion data
    static final ArrayList<Relation> relations = new ArrayList<>();

    public static ObservableList<Person> getAllPeople() {
        return people;
    }

    /**
     * Load the data files
     * @throws IOException when an I/O error occur
     */
    public static void loadFromFile(String peopleFile, String relationsFile) throws IOException {
        // Read the people file
        BufferedReader reader = new BufferedReader(new FileReader(peopleFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = Util.splitAndTrimTokens(line,",");
            if (tokens.length != 6) break;

            Person person = new Person(tokens[0], tokens[1], tokens[2], tokens[3].equalsIgnoreCase("F"), Integer.parseInt(tokens[4]), tokens[5]);
            people.add(person);
        }
        reader.close();

        // Read the relation file
        reader = new BufferedReader(new FileReader(relationsFile));
        while ((line = reader.readLine()) != null) {
            String[] tokens = Util.splitAndTrimTokens(line,",");
            if (tokens.length != 3) break;

            Relation relation = new Relation(tokens[0], tokens[1], tokens[2]);
            relations.add(relation);
        }
        reader.close();

        // TODO: check for constraints

    }

    /**
     * Add a person
     * @throws RuntimeException if the person's name is already exist
     */
    public static void addPerson(Person p) {
        if (!people.contains(p)) {
            people.add(p);
        }
        else throw new RuntimeException("The name is already exist");
    }

    /**
     * Remove a person by name and all of its relations
     * If the name does not exist, this methods does nothing
     */
    public static void removePersonByName(String name) {
        people.removeIf(p -> p.name.equals(name));
        relations.removeIf(r -> r.contains(name));
    }

    /**
     * @return number of selected people
     */
    public static int getSelectedCount() {
        int count = 0;
        for (Person p : people) {
            if (p.selected.getValue()) {
                count++;
            }
        }

        return count;
    }

    /**
     * @return all selected people
     */
    public static ArrayList<Person> getSelectedPeople() {
        ArrayList<Person> selected = new ArrayList<>();

        for (Person p : people) {
            if (p.selected.getValue()) {
                selected.add(p);
            }
        }

        return selected;
    }

    /**
     * @return a Relation object represents a connection between selected two people, if there is no connection,
     * the "type" field of the returned objects is null
     */
    public static Relation getConnectionBetweenSelectedPeople() {
        ArrayList<Person> selected = getSelectedPeople();

        Relation relation = new Relation(selected.get(0).name, selected.get(1).name);
        int index = relations.indexOf(relation);

        return index < 0? relation : relations.get(index);
    }

    /**
     * Retrieve a full Person object, find by its name
     */
    public static Person getByName(String name) {
        for (Person p : people) {
            if (p.name.equals(name)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Connect two people with a specified relation type
     */
    public static void connect(Person p1, Person p2, RelationType type) throws RuntimeException {
        if (type == null) {
            throw new IllegalArgumentException("Please select the relation type");
        }

        if (p1.isYoungChild() || p2.isYoungChild()) {
            throw new TooYoungException("One of the selected people is a YoungChild");
        }

        Relation relation = new Relation(p1.name, p2.name, type);
        relations.add(relation);
    }

    /**
     * @return list contains all people that has direct connection to the specified person
     */
    public static ArrayList<Person> getRelatedPeople(String name) {
        ArrayList<Person> relatedPeople = new ArrayList<>();
        for (Relation r : relations) {
            String otherName = r.getOther(name);
            if (otherName == null) continue;

            Person relatedPerson = getByName(otherName);
            relatedPeople.add(relatedPerson);
        }

        return relatedPeople;
    }

    /**
     * @return list contains all people that has direct connection to the specified person, along with the type of
     * the connections
     */
    public static ObservableList<SimpleEntry<Person, RelationType>> getRelatedPeople2(String name) {
        ObservableList<SimpleEntry<Person, RelationType>> relatedPeople = FXCollections.observableArrayList();
        for (Relation r : relations) {
            String otherName = r.getOther(name);
            if (otherName == null) continue;

            Person relatedPerson = getByName(otherName);
            relatedPeople.add(new SimpleEntry<>(relatedPerson, r.type));
        }

        return relatedPeople;
    }

    public static ObservableList<Person> getParents(String name) {
        ObservableList<Person> relatedPeople = FXCollections.observableArrayList();
        for (Relation r : relations) {
            String otherName = r.getOther(name);
            if (otherName == null || r.type != RelationType.parent) continue;

            Person relatedPerson = getByName(otherName);
            relatedPeople.add(relatedPerson);
        }

        return relatedPeople;
    }

    public static boolean hasSameFamily(String name1, String name2) {
        ObservableList<Person> parents1 = getParents(name1);
        HashSet<Person> parentSet1 = new HashSet<>(parents1);

        ObservableList<Person> parents2 = getParents(name2);
        HashSet<Person> parentSet2 = new HashSet<>(parents2);

        return parentSet1.equals(parentSet2);
    }

    /**
     * Calculate the shortest path between two selected people
     * @return the connection chain
     */
    public static ArrayList<Person> getConnectionChain() {
        ArrayList<Person> srcAndTar = getSelectedPeople();

        return shortestPath(srcAndTar.get(0), srcAndTar.get(1));
    }

    /**
     * Calculate the shortest path between two specified people
     * Using Dijkstra's algorithm for calculating shortest path in a graph
     * @return the connection chain
     */
    public static ArrayList<Person> shortestPath(Person source, Person target) {
        HashSet<Person> q = new HashSet<>();
        HashMap<Person, Integer> dist = new HashMap<>();
        HashMap<Person, Person> prev = new HashMap<>();

        for (Person v : people) {
            dist.put(v, Integer.MAX_VALUE);
            q.add(v);
        }

        dist.put(source, 0);

        while (!q.isEmpty()) {
            Person u = null;
            Integer minDist = Integer.MAX_VALUE;

            for (Person v : q) {
                Integer distance = dist.get(v);
                if (distance < minDist) {
                    u = v;
                    minDist = distance;
                }
            }

            if (u == null) break;

            if (target.equals(u)) {
                break;
            }
            q.remove(u);

            List<Person> neighbours = getRelatedPeople(u.name);
            for (Person v : neighbours) {
                int distance = 1;

                int alt = dist.get(u) + distance;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                }
            }
        }

        ArrayList<Person> path = new ArrayList<>();
        Person u = target;

        while (prev.get(u) != null) {
            path.add(u);
            u = prev.get(u);
        }
        path.add(u);

        Collections.reverse(path);

        return path;
    } // end of shortestPathDistance()

}
