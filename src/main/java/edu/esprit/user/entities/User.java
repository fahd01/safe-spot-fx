package edu.esprit.user.entities;

import java.sql.Date;

public class User {
    private int id;
    private String email;
    private String roles;
    private String password;
    private String nom;
    private String prenom;
    private Date dateDeNaissance;
    private String numTlph;
    private String adresse;
    private boolean isVerified;
    private String etat;
    private String imageName;
    private String updatedAt;

    public User() {
    }

    public User(int id, String email, String roles, String password, String nom, String prenom, Date dateDeNaissance, String numTlph, String adresse, boolean isVerified, String etat, String imageName, String updatedAt) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.numTlph = numTlph;
        this.adresse = adresse;
        this.isVerified = isVerified;
        this.etat = etat;
        this.imageName = imageName;
        this.updatedAt = updatedAt;
    }

    public User(String email, String password, String nom, String prenom, Date dateDeNaissance, String numTlph, String adresse) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.numTlph = numTlph;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getNumTlph() {
        return numTlph;
    }

    public void setNumTlph(String numTlph) {
        this.numTlph = numTlph;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "user{" +
                "email='" + email + '\'' +
                ", roles='" + roles + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateDeNaissance='" + dateDeNaissance + '\'' +
                ", numTlph='" + numTlph + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
