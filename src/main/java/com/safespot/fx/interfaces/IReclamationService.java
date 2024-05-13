package com.safespot.fx.interfaces;


import java.util.List;

public interface IReclamationService<Reclamation> {
    void ajouterReclamation(Reclamation var1);

    void supprimerReclamation(Reclamation var1);

    void modifierReclamation(Reclamation var1);

    List<Reclamation> afficherReclamations();


    Reclamation getReclamationById(int id);

}