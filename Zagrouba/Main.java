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
import SendSMS.Sendsms;
import Services.ReclamationService;
import Services.ReponseService;
import MyConnection.MyConnection;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
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
        // MyConnection mc = MyConnection.getInstance();
        //System.out.println(mc.hashCode());


        IReclamationService es = new ReclamationService();

        LocalDate datePublication = LocalDate.of(2020, 8, 5);
        LocalDateTime dateTimePublication = datePublication.atStartOfDay();
        Date datePub = Date.from(dateTimePublication.atZone(ZoneId.systemDefault()).toInstant());

        Reclamation r = new Reclamation(1, "hy", "ze", datePub, true);


        //es.ajouterReclamation(r);
        //es.supprimerReclamation(r);
//es.afficherReclamations();

        Reponse r1 = new Reponse(9, "rrrrrr", 25);
        IReponseService ee = new ReponseService();
        //ee.ajouterReponse(r1);
        //ee.supprimerReponse(r1);
        // ee.modifierReponse(r1);
        //ee.afficherReponses();




/*
            Tesseract tesseract = new Tesseract();
            try {

                tesseract.setDatapath("C:\\Users\\yassin\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");

                // the path of your tess data folder
                // inside the extracted file
                String text
                        = tesseract.doOCR(new File("C:\\Users\\yassin\\Desktop\\image2.png"));

                // path of your image file
                System.out.print(text);
            }
            catch (TesseractException e) {
                e.printStackTrace();
            }


*/


        Sendsms sm = new Sendsms();
        sm.sendSMS();





        }}



