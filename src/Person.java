import exceptions.NoSuchAgeException;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Class represents a person
 * @author Duc Minh Le (s3651764)
 */
public class Person {

    public final String name;
    public final String photo;
    public final String status;
    public final boolean gender;
    public final int age;
    public final String state;

    // Field for JavaFX to determine if the person is currently selected
    public final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public Person(String name, String photo, String status, boolean gender, int age, String state) {
        if (age < 0 || age > 150) {
            throw new NoSuchAgeException("Age must be a positive number up to 150");
        }

        this.name = name;
        this.photo = photo;
        this.status = status;
        this.gender = gender;
        this.age = age;
        this.state = state;
    }

    public boolean isAdult() {
        return age > 16;
    }

    public boolean isChild() {
        return age >= 3 && age <= 16;
    }

    public boolean isYoungChild() {
        return age < 3;
    }

    public String getGenderText() {
        return gender? "Female" : "Male";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", status='" + status + '\'' +
                ", genderToggleGroup=" + gender +
                ", age=" + age +
                ", state='" + state + '\'' +
                '}';
    }
}
