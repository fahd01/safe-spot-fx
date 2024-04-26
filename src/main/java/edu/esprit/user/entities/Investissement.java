package edu.esprit.user.entities;

import java.sql.Date;

public class Investissement {
    private int id;
    private Date date;
    private int duree;
    private double prix_a;
    private String description;
    private String email;
    private String name;
    private String image;
    private String color;

    public Investissement() {
    }

    public Investissement(int id, Date date, int duree, double prix_a, String description, String email, String name, String image, String color) {
        this.id = id;
        this.date = date;
        this.duree = duree;
        this.prix_a = prix_a;
        this.description = description;
        this.email = email;
        this.name = name;
        this.image = image;
        this.color = color;
    }

    public Investissement(Date date, int duree, double prix_a, String description, String email, String name, String image, String color) {
        this.date = date;
        this.duree = duree;
        this.prix_a = prix_a;
        this.description = description;
        this.email = email;
        this.name = name;
        this.image = image;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public double getPrix_a() {
        return prix_a;
    }

    public void setPrix_a(double prix_a) {
        this.prix_a = prix_a;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Investissement{" +
                "date=" + date +
                ", duree=" + duree +
                ", prix_a=" + prix_a +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
