package notif1ed;

import javafx.beans.property.*;

public class StudentEntry {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty surname = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty guardianName = new SimpleStringProperty();
    private final StringProperty guardianEmail = new SimpleStringProperty();

    public StudentEntry(String id, String surname, String firstName, String guardianName, String guardianEmail) {
        setId(id);
        setSurname(surname);
        setFirstName(firstName);
        setGuardianName(guardianName);
        setGuardianEmail(guardianEmail);
    }

    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean v) { selected.set(v); }
    public BooleanProperty selectedProperty() { return selected; }

    public String getId() { return id.get(); }
    public void setId(String v) { id.set(v); }
    public StringProperty idProperty() { return id; }

    public String getSurname() { return surname.get(); }
    public void setSurname(String v) { surname.set(v); }
    public StringProperty surnameProperty() { return surname; }

    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String v) { firstName.set(v); }
    public StringProperty firstNameProperty() { return firstName; }

    public String getGuardianName() { return guardianName.get(); }
    public void setGuardianName(String v) { guardianName.set(v); }
    public StringProperty guardianNameProperty() { return guardianName; }

    public String getGuardianEmail() { return guardianEmail.get(); }
    public void setGuardianEmail(String v) { guardianEmail.set(v); }
    public StringProperty guardianEmailProperty() { return guardianEmail; }
}