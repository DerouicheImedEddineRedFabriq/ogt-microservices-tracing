package entities;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 *
 * @author m.mazigh
 */

public class Person implements Serializable {

    private String id;

    private String LastName;
    private String FirstName;
    private static final long serialVersionUID = 1L;

    public Person() {
        super();
    }

    public Person(String lastName, String firstName) {
        id = UUID.randomUUID().toString();
        LastName = lastName;
        FirstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

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