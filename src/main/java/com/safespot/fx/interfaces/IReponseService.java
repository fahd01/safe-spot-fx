package com.safespot.fx.interfaces;


import java.util.List;

public interface IReponseService<Reponse> {
    void ajouterReponse(Reponse var1);

    void supprimerReponse(Reponse var1);

    void modifierReponse(Reponse var1);

    List<Reponse> afficherReponses();


}
