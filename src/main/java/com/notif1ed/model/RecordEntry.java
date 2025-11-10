package com.notif1ed.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecordEntry {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty studentNumber = new SimpleStringProperty();
    private final StringProperty surname = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty guardianEmail = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private final StringProperty type = new SimpleStringProperty();
    
    // Computed properties for FXML bindings
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty createdAt = new SimpleStringProperty();
    private final StringProperty recordType = new SimpleStringProperty();

    public RecordEntry(int id, String studentNumber, String surname, String firstName, 
                      String guardianEmail, LocalDate date, LocalTime time, String type) {
        setId(id);
        setStudentNumber(studentNumber);
        setSurname(surname);
        setFirstName(firstName);
        setGuardianEmail(guardianEmail);
        setDate(date);
        setTime(time);
        setType(type);
        
        // Populate computed properties
        setEmail(guardianEmail);
        setLastName(surname);
        setRecordType(type);
        
        // Format date and time nicely
        if (date != null && time != null) {
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
            setCreatedAt(dateTime.format(formatter));
        } else {
            setCreatedAt("N/A");
        }
    }

    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public String getStudentNumber() { return studentNumber.get(); }
    public void setStudentNumber(String v) { studentNumber.set(v); }
    public StringProperty studentNumberProperty() { return studentNumber; }

    public String getSurname() { return surname.get(); }
    public void setSurname(String v) { surname.set(v); }
    public StringProperty surnameProperty() { return surname; }

    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String v) { firstName.set(v); }
    public StringProperty firstNameProperty() { return firstName; }

    public String getGuardianEmail() { return guardianEmail.get(); }
    public void setGuardianEmail(String v) { guardianEmail.set(v); }
    public StringProperty guardianEmailProperty() { return guardianEmail; }

    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate v) { date.set(v); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    public LocalTime getTime() { return time.get(); }
    public void setTime(LocalTime v) { time.set(v); }
    public ObjectProperty<LocalTime> timeProperty() { return time; }

    public String getType() { return type.get(); }
    public void setType(String v) { type.set(v); }
    public StringProperty typeProperty() { return type; }
    
    // Alias getters for FXML property binding
    public String getEmail() { return email.get(); }
    public void setEmail(String v) { email.set(v); }
    public StringProperty emailProperty() { return email; }
    
    public String getLastName() { return lastName.get(); }
    public void setLastName(String v) { lastName.set(v); }
    public StringProperty lastNameProperty() { return lastName; }
    
    public String getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(String v) { createdAt.set(v); }
    public StringProperty createdAtProperty() { return createdAt; }
    
    public String getRecordType() { return recordType.get(); }
    public void setRecordType(String v) { recordType.set(v); }
    public StringProperty recordTypeProperty() { return recordType; }
}
