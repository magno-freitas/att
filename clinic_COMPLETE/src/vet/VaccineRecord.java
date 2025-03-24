package vet;

import java.sql.Date;

public class VaccineRecord {
    private int id;
    private int petId;
    private String vaccineName;
    private Date applicationDate;
    private Date nextDoseDate;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getNextDoseDate() {
        return nextDoseDate;
    }

    public void setNextDoseDate(Date nextDoseDate) {
        this.nextDoseDate = nextDoseDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}