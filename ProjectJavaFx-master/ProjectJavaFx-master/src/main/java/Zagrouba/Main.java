package Zagrouba;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import Entities.Reclamation;

import Entities.Reponse;
import Interfaces.IReclamationService;
import Interfaces.IReponseService;
import MyConnection.MyConnection;
import Services.ReclamationService;
import Services.ReponseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author TECHN
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        System.out.println(mc.hashCode());



        IReclamationService es = new ReclamationService();

        LocalDate datePublication = LocalDate.of(2020, 8, 5);
        LocalDateTime dateTimePublication = datePublication.atStartOfDay();
        Date datePub = Date.from(dateTimePublication.atZone(ZoneId.systemDefault()).toInstant());

        Reclamation r = new Reclamation(1,"hy","ze",datePub,true);




        //es.ajouterReclamation(r);
         //es.supprimerReclamation(r);
//es.afficherReclamations();

        Reponse r1 = new Reponse(9,"rrrrrr",25);
        IReponseService ee = new ReponseService();
        //ee.ajouterReponse(r1);
        //ee.supprimerReponse(r1);
        ee.modifierReponse(r1);
         //ee.afficherReponses();








    }








   }
