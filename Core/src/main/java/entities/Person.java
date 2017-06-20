package entities;

import java.io.Serializable;
import java.util.UUID;

/**
 * The Person entity.
 *
 * @author m.mazigh
 */

public class Person implements Serializable {

    private String id;

    private String LastName;
    private String FirstName;
    private static final long serialVersionUID = 1L;

    /**
     * Empty constructor.
     */
    public Person() {
    }

    /**
     * Creates a new {@link Person}.
     *
     * @param lastName  the last name of person.
     * @param firstName the first name of person.
     */
    public Person(String lastName, String firstName) {
        id = UUID.randomUUID().toString();
        LastName = lastName;
        FirstName = firstName;
    }

    /**
     * @return The id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to pf person to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The lastName of person.
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName) {
        LastName = lastName;
    }

    /**
     * @return The firstName of person.
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     * @param firstName The firstName to set
     */
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", LastName='" + LastName + '\'' +
                ", FirstName='" + FirstName + '\'' +
                '}';
    }
}