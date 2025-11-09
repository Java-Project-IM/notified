package com.notif1ed.model;

import com.notif1ed.model.SubjectEntry;

import javafx.beans.property.*;

public class SubjectEntry {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty subjectCode = new SimpleStringProperty();
    private final StringProperty subjectName = new SimpleStringProperty();
    private final IntegerProperty yearLevel = new SimpleIntegerProperty();
    private final StringProperty section = new SimpleStringProperty();

    public SubjectEntry(int id, String subjectCode, String subjectName, int yearLevel, String section) {
        setId(id);
        setSubjectCode(subjectCode);
        setSubjectName(subjectName);
        setYearLevel(yearLevel);
        setSection(section);
    }

    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean v) { selected.set(v); }
    public BooleanProperty selectedProperty() { return selected; }

    public int getId() { return id.get(); }
    public void setId(int v) { id.set(v); }
    public IntegerProperty idProperty() { return id; }

    public String getSubjectCode() { return subjectCode.get(); }
    public void setSubjectCode(String v) { subjectCode.set(v); }
    public StringProperty subjectCodeProperty() { return subjectCode; }

    public String getSubjectName() { return subjectName.get(); }
    public void setSubjectName(String v) { subjectName.set(v); }
    public StringProperty subjectNameProperty() { return subjectName; }

    public int getYearLevel() { return yearLevel.get(); }
    public void setYearLevel(int v) { yearLevel.set(v); }
    public IntegerProperty yearLevelProperty() { return yearLevel; }

    public String getSection() { return section.get(); }
    public void setSection(String v) { section.set(v); }
    public StringProperty sectionProperty() { return section; }
}
