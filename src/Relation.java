/**
 * Class represents a relation between two people
 * @author Duc Minh Le (s3651764)
 */
public class Relation {

    public final String name1;
    public final String name2;
    public final RelationType type;

    public Relation(String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
        this.type = null;
    }

    public Relation(String name1, String name2, String type) {
        this.name1 = name1;
        this.name2 = name2;
        this.type = RelationType.valueOf(type);
    }

    public Relation(String name1, String name2, RelationType type) {
        this.name1 = name1;
        this.name2 = name2;
        this.type = type;
    }

    /**
     * Determine if this relation contains a specified person's name
     */
    public boolean contains(String name) {
        return name1.equals(name) || name2.equals(name);
    }

    /**
     * Give a name of one person, this method returns other person's name in this relation
     */
    public String getOther(String name) {
        if (name1.equals(name)) return name2;
        else if (name2.equals(name)) return name1;
        else return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return relation.contains(name1) && relation.contains(name2);
    }

    @Override
    public int hashCode() {
        return name1.hashCode() + name2.hashCode();
    }
}
