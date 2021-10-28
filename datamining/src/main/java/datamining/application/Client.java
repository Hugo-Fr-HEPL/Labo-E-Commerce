package datamining.application;

import java.io.*;
import java.net.*;
import java.util.Properties;

import datamining.package_reseaux.other.GetDirectory;
import datamining.package_reseaux.other.ReponseSUM;
import datamining.package_reseaux.other.RequeteSUM;



public class Client {
    public static void Connection(String mois, String comp, String request) {
        RequeteSUM req = null;


// Connexion au serveur
        req = new RequeteSUM(RequeteSUM.CONNEXION_RSERVE);

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Socket cliSock = null;

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(GetDirectory.FileDir("properties.txt")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        int port = Integer.parseInt(prop.getProperty("Port"));
        String adresse = (String) prop.get("Adresse");

        try {
            cliSock = new Socket(adresse, port);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e) {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");
        }
        catch (IOException e) {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");
        }

        // Envoie de la requête
        try {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }

        // Lecture de la réponse
        ReponseSUM rep = null;
        try {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseSUM)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getChargeUtile());
        }
        catch (ClassNotFoundException e) {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("--- erreur IO = " + e.getMessage());
        }

        
// Requête SQL
        if(request.equals("REG_CORR_LUG"))
            req = new RequeteSUM(1, mois, comp);
        else if(request.equals("REG_CORR_LUG_PLUS"))
            req = new RequeteSUM(2, mois, comp);
        else if(request.equals("ANOVA_1_LUG"))
            req = new RequeteSUM(3, mois, comp);
        else if(request.equals("ANOVA_2_LUG_HF"))
            req = new RequeteSUM(4, mois, comp);
    }
}
