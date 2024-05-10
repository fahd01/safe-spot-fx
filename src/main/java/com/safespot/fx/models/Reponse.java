package com.safespot.fx.models;

public class Reponse {
  private int id;
  private String reponse;
  private int reclamation_id;




    public Reponse(int id, String reponse, int reclamation_id) {
        this.id = id;
        this.reponse = reponse;
        this.reclamation_id = reclamation_id;
    }


    public Reponse(String reponse, int reclamation_id) {
        this.reponse = reponse;
        this.reclamation_id = reclamation_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getReclamation_id() {
        return reclamation_id;
    }

    public void setReclamation_id(int reclamation_id) {
        this.reclamation_id = reclamation_id;
    }



    public Reponse() {
    }


    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", reponse='" + reponse + '\'' +
                ", reclamation_id=" + reclamation_id +
                '}';
    }
}
