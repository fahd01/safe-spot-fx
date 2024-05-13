package com.safespot.fx.models;

import java.util.Date;

public class Reclamation {
    private int id;
private String sujet;
private String description;
private Date dt;
private Boolean verified;

    public Reclamation(){}

    public Reclamation(int id, String sujet, String description, Date dt, Boolean verified) {
        this.id = id;
        this.sujet = sujet;
        this.description = description;
        this.dt = dt;
        this.verified = verified;
    }

    public Reclamation(String sujet, String description, Date dt, Boolean verified) {
        this.sujet = sujet;
        this.description = description;
        this.dt = dt;
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", sujet='" + sujet + '\'' +
                ", description='" + description + '\'' +
                ", dt=" + dt +
                ", verified=" + verified +
                '}';
    }

}
