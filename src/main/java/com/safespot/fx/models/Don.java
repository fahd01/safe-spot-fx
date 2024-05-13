package com.safespot.fx.models;

public class Don {
    private int id;
    private String fullname;
    private int investissementsId;
    private double taux;
    private double montant;
    private boolean etat;

    public Don() {
    }

    public Don(int id, String fullname, int investissementsId, double taux, double montant, boolean etat) {
        this.id = id;
        this.fullname = fullname;
        this.investissementsId = investissementsId;
        this.taux = taux;
        this.montant = montant;
        this.etat = etat;
    }

    public Don(String fullname, int investissementsId, double taux, double montant, boolean etat) {
        this.fullname = fullname;
        this.investissementsId = investissementsId;
        this.taux = taux;
        this.montant = montant;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getInvestissementsId() {
        return investissementsId;
    }

    public void setInvestissementsId(int investissementsId) {
        this.investissementsId = investissementsId;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean getEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Don{" +
                "fullname='" + fullname + '\'' +
                ", investissementsId=" + investissementsId +
                ", taux=" + taux +
                ", montant=" + montant +
                ", etat='" + etat + '\'' +
                '}';
    }
}
