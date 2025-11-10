package com.notif1ed.model;

import com.notif1ed.model.StudentEntry;

import javafx.beans.property.*;

public class StudentEntry {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty studentNumber = new SimpleStringProperty();
    private final StringProperty surname = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty section = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty guardianName = new SimpleStringProperty();
    private final StringProperty guardianEmail = new SimpleStringProperty();

    // Constructor for student form (5 params)
    public StudentEntry(String id, String surname, String firstName, String guardianName, String guardianEmail) {
        setId(id);
        setStudentNumber(id);
        setSurname(surname);
        setFirstName(firstName);
        setLastName(surname);
        setGuardianName(guardianName);
        setGuardianEmail(guardianEmail);
        setEmail(guardianEmail);
    }
    
    // Constructor for database (4 params - student_number, first_name, last_name, email)
    public StudentEntry(String studentNumber, String firstName, String lastName, String email) {
        setStudentNumber(studentNumber);
        setId(studentNumber);
        setFirstName(firstName);
        setLastName(lastName);
        setSurname(lastName);
        setEmail(email);
        setGuardianEmail(email);
    }

    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean v) { selected.set(v); }
    public BooleanProperty selectedProperty() { return selected; }

    public String getId() { return id.get(); }
    public void setId(String v) { id.set(v); }
    public StringProperty idProperty() { return id; }
    
    public String getStudentNumber() { return studentNumber.get(); }
    public void setStudentNumber(String v) { studentNumber.set(v); }
    public StringProperty studentNumberProperty() { return studentNumber; }

    public String getSurname() { return surname.get(); }
    public void setSurname(String v) { surname.set(v); }
    public StringProperty surnameProperty() { return surname; }

    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String v) { firstName.set(v); }
    public StringProperty firstNameProperty() { return firstName; }
    
    public String getLastName() { return lastName.get(); }
    public void setLastName(String v) { lastName.set(v); }
    public StringProperty lastNameProperty() { return lastName; }
    
    public String getEmail() { return email.get(); }
    public void setEmail(String v) { email.set(v); }
    public StringProperty emailProperty() { return email; }

    public String getGuardianName() { return guardianName.get(); }
    public void setGuardianName(String v) { guardianName.set(v); }
    public StringProperty guardianNameProperty() { return guardianName; }

    public String getGuardianEmail() { return guardianEmail.get(); }
    public void setGuardianEmail(String v) { guardianEmail.set(v); }
    public StringProperty guardianEmailProperty() { return guardianEmail; }
    
    public String getSection() { return section.get(); }
    public void setSection(String v) { section.set(v); }
    public StringProperty sectionProperty() { return section; }
}