package datamining.application;

import java.io.*;
import java.net.*;
import java.util.Properties;

import datamining.package_reseaux.other.GetDirectory;
import datamining.package_reseaux.other.ReponseSUM;
import datamining.package_reseaux.other.RequeteSUM;

public class Client {
    static Properties prop;
    static int port;
    static String adresse;
    
    public static Properties Proper() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(GetDirectory.FileDir("properties.txt")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        port = Integer.parseInt(prop.getProperty("Port"));
        adresse = (String) prop.get("Adresse");

        return prop;
    }
    
    public static void Connection() {
        RequeteSUM req = null;

// Connexion au serveur
        req = new RequeteSUM(RequeteSUM.CONNEXION_RSERVE);

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Socket cliSock = null;

        try {
            cliSock = new Socket(adresse, port);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e) {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");
        } catch (IOException e) {
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
            if(rep.getCode() == ReponseSUM.CONNECTION_OK)
                System.out.println(" *** Reponse reçue : Connection ok");
            else
                System.out.println(" *** Reponse reçue : Connection echouée");
        }
        catch (ClassNotFoundException e) {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        } catch (IOException e) {
            System.out.println("--- erreur IO = " + e.getMessage());
        }
    }

    public static String Statistics(int mois, String comp, String request, boolean age, boolean nbAccomp) {
        RequeteSUM req = null;
        
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Socket cliSock = null;

        try {
            cliSock = new Socket(adresse, port);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e) {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");
        } catch (IOException e) {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");
        }
        
        if(request.equals("REG_CORR_LUG"))
            req = new RequeteSUM(RequeteSUM.REG_CORR_LUG, mois, comp);
        else if(request.equals("REG_CORR_LUG_PLUS"))
            req = new RequeteSUM(RequeteSUM.REG_CORR_LUG_PLUS, mois, comp, age, nbAccomp);
        else if(request.equals("ANOVA_1_LUG"))
            req = new RequeteSUM(RequeteSUM.ANOVA_1_LUG, mois, comp);
        else if(request.equals("ANOVA_2_LUG_HF"))
            req = new RequeteSUM(RequeteSUM.ANOVA_2_LUG_HF, mois, comp);
        
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
            if(rep.getCode() == ReponseSUM.STATISTIC_OK) {
                System.out.println(" *** Reponse reçue : Statistic ok");
                System.out.println(rep.getChargeUtile());
                
                if(rep.getcodegraph() == ReponseSUM.BOXPLOT_SECTORIEL) {
                    jFreeChart.ShowBoxPlot("Anova - BoxPlot", rep.getdonnees1(), rep.getdonnees2());
                    jFreeChart.ShowPieShart("Anova - Sectoriel", rep.getdonnees1(), rep.getdonnees2());
                } else {
                    //SCATER POINTS
                    
                    int cpt = rep.getdonnees1().size();
                    
                    //scatter points
                    double[][] histo = new double[cpt - 1][2];
                    for(int i = 0; i < cpt - 1; i++) {
                        histo[i][0] = (Double) rep.getdonnees1().get(i);
                        histo[i][1] = (Double) rep.getdonnees2().get(i);
                    }
                    jFreeChart.ShowScatterPoint("Reg - Scatter Points", "y", "x", histo);
                    
                    //HISTOGRAM
                    
                    if(rep.getcodegraph() == ReponseSUM.HISTOGRAM) {
                        int k = rep.getdonnees2().size();
                        //arrylist vers []
                        double[] histoDouble = new double[k];
                        String[] histoString = new String[k];
                        for(int i = 0; i < k; i++) {
                            histoDouble[i]= (Double) rep.getdonnees2().get(i);
                            histoString[i]= (String) rep.getdonnees5().get(i);
                        }

                        jFreeChart.ShowHistogram("Reg - Histogram", "Distance", "Années", histoDouble, rep.GetHistStr());
                    } else {
                        String[] vecmois = new String[2];
                
                        switch (mois) {
                            case 1: vecmois[0]="janvier";
                                    vecmois[1]="février";
                                break;
                            case 2: vecmois[0]="février";
                                    vecmois[1]="mars";
                                break;
                            case 3: vecmois[0]="mars";
                                    vecmois[1]="avril";
                                break;
                            case 4: vecmois[0]="avril";
                                    vecmois[1]="mai";
                                break;
                            case 5: vecmois[0]="mai";
                                    vecmois[1]="juin";
                                break;
                            case 6: vecmois[0]="juin";
                                    vecmois[1]="juillet";
                                break;
                            case 7: vecmois[0]="juillet";
                                    vecmois[1]="aout";
                                break;
                            case 8: vecmois[0]="aout";
                                    vecmois[1]="septembre";
                                break;
                            case 9: vecmois[0]="septembre";
                                    vecmois[1]="octobre";
                                break;
                            case 10: vecmois[0]="octobre";
                                    vecmois[1]="novembre";
                                break;
                            case 11: vecmois[0]="novembre";
                                    vecmois[1]="décembre";
                                break;
                            case 12: vecmois[0]="décembre";
                                    vecmois[1]="janvier";
                                break;
                        }
                        
                        int k = rep.getdonnees3().size();
                        //arrylist vers []
                        Double[][] values = new Double[k][2];
                        String[] histoString = new String[k];

                        for(int i=0;i<k;i++) {
                            values[i][0]= (Double) rep.getdonnees3().get(i);
                            values[i][1]= (Double) rep.getdonnees4().get(i);
                            histoString[i]= (String) rep.getdonnees5().get(i);
                        }
                        jFreeChart.ShowHistogramComp("Reg - Histogram", "Distance", "Années", rep.GetHistStr(), vecmois, values);
                    }
                }
            } else
                System.out.println(" *** Reponse reçue : Statistic echouée");
        }
        catch (ClassNotFoundException e) {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        } catch (IOException e) {
            System.out.println("--- erreur IO = " + e.getMessage());
        }
        return rep.getChargeUtile();
    }
}