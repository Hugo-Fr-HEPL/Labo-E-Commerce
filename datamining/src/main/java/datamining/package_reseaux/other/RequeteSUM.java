package datamining.package_reseaux.other;

import datamining.application.Client;
import datamining.package_reseaux.Interface.*;

import java.io.*;
import java.net.Socket;
//import java.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;



public class RequeteSUM implements Requete, Serializable {
    public static int CONNEXION_RSERVE = 1;
    public static int REG_CORR_LUG = 2;
    public static int REG_CORR_LUG_PLUS = 3;
    public static int ANOVA_1_LUG = 4;
    public static int ANOVA_2_LUG_HF = 5;

    public static Connection con = null;
    public static Statement instruc = null;
    public static Properties prop;
    public static RConnection rConn = null;


    public Runnable createRunnable (final Socket s, final ConsoleServeur cs) {
        if (type == CONNEXION_RSERVE)
            return new Runnable() {
                public void run() {
                    traiteConnexionRServe(s, cs);
                }
            };
        else if (type == REG_CORR_LUG) {
            return new Runnable() {
                public void run() {
                    traiteRegCorr(s, cs);
                }
            };
        }
        else if (type == REG_CORR_LUG_PLUS) {
            return new Runnable() {
                public void run() {
                    traiteRegCorrPlus(s, cs);
                }
            };
        }
        else if (type == ANOVA_1_LUG) {
            return new Runnable() {
                public void run() {
                    traiteAnova(s, cs);
                }
            };
        }
        else if (type == ANOVA_2_LUG_HF) {
            return new Runnable() {
                public void run() {
                    traiteAnovaHf(s, cs);
                }
            };
        }
        else
            return null;
    }

    private void traiteConnexionRServe(Socket sock, ConsoleServeur cs) {
        try {
            rConn = new RConnection("localhost");
            System.out.println("connexion réussie");
            
            ReponseSUM rep = new ReponseSUM(ReponseSUM.CONNECTION_OK);
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(rep); oos.flush();
                oos.close();
            }
            catch (IOException e) {
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }

            prop = Client.Proper();

            con = MySQL.MySQL_Connexion("bd_airport", (String)prop.get("DB_port"), "localhost", (String)prop.get("DB"), (String)prop.get("DB_pwd"));
            instruc = con.createStatement();
            System.out.println("-- DB Connected --");
        }
        catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
            //Send CONNECTION NOT OK
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void traiteRegCorr(Socket sock, ConsoleServeur cs) {
        System.out.println("reg corr 1");
        try {
            String req  = "SELECT AVG(ba.poids), v.distance FROM bagages ba"+
            " INNER JOIN billets bi ON (ba.idBillet = bi.idBillet)"+
            " INNER JOIN vols v ON (bi.numVol = v.numVol)"+
            " INNER JOIN avions a ON (v.idAvion = a.idAvion)"+
            " INNER JOIN compagnies c ON (a.idCompagnie = c.idCompagnie)";

            if(mois != 0 && comp.equals("Toutes les compagnies") == false) {
                req += " WHERE nomCompagnie = '"+ comp +"'"+
                        " AND EXTRACT(MONTH FROM dateVol) = "+ mois;
            } else {
                if(comp.equals("Toutes les compagnies") == false)
                    req += " WHERE nomCompagnie = '"+ comp +"'";
                if(mois != 0)
                    req += " WHERE EXTRACT(MONTH FROM dateVol) = "+ mois;
            }
            req += " GROUP BY bi.idClient, v.distance";

            ResultSet resultat = instruc.executeQuery(req);

            resultat.next();
            String poids = resultat.getString(1);
            String distance = resultat.getString(2);

            while(resultat.next()) {
                if(resultat.getString(1) != null) {
                    poids += ",";
                    poids += resultat.getString(1);
                }

                if(resultat.getString(2) != null) {
                    distance += ',';
                    distance += resultat.getString(2);
                }
            }

            rConn.voidEval("reg <- data.frame(poids = c("+ poids +"), distance = c("+ distance +"))");
            System.out.println("data reg corr créée");

            rConn.voidEval("jpeg(file=\"" + GetDirectory.FileDir("reg1.jpeg") + "\",width=800, height=700)");
            rConn.voidEval("dev.off()");
            rConn.voidEval("dev.new()");
            rConn.voidEval("boxplot(reg)");

            REXP rExp = rConn.eval("summ <- summary(reg)");
            System.out.println("\tPoids\t\tDistance");
            RExpPrintSummary(rExp, 2);

            rConn.voidEval("model <- lm(formula = reg$poids ~ reg$distance)");

            rConn.voidEval("tmp <- summary(model)");
            rExp = rConn.eval("tmp$coefficients");
            System.out.println("\nfinal summary :");
            System.out.println("\tEstimate\t\tStd. Error\t\tt value\t\tPr(>|t|)");
            RExpPrintSummary(rExp, 4);

            rExp = rConn.eval("tmp$adj.r.squared");
            System.out.println("\tAdjusted R Squared : " + rExp.asString());

            // p-value to do
            //rExp = rConn.eval("tmp$");
            //System.out.println("\tp-value : " + "< " + rExp.asString());
        } 
        catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        }
        
        ReponseSUM rep = new ReponseSUM(ReponseSUM.CONNECTION_OK);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }

    private void traiteRegCorrPlus(Socket sock, ConsoleServeur cs) {
        System.out.println("reg corr 2");
        try {
            String req  = "SELECT AVG(ba.poids), v.distance, nombreAccompagnant, age FROM bagages ba"+
            " INNER JOIN billets bi ON (ba.idBillet = bi.idBillet)"+
            " INNER JOIN vols v ON (bi.numVol = v.numVol)"+
            " INNER JOIN avions a ON (v.idAvion = a.idAvion)"+
            " INNER JOIN compagnies c ON (a.idCompagnie = c.idCompagnie)"+
            " INNER JOIN clients cl USING(idclient)";

            if(mois != 0 && comp.equals("Toutes les compagnies") == false) {
                req += " WHERE nomCompagnie = '"+ comp +"'"+
                        " AND EXTRACT(MONTH FROM dateVol) = "+ mois;
            } else {
                if(comp.equals("Toutes les compagnies") == false)
                    req += " WHERE nomCompagnie = '"+ comp +"'";
                if(mois != 0)
                    req += " WHERE EXTRACT(MONTH FROM dateVol) = "+ mois;
            }
            req += " GROUP BY bi.idClient, v.distance, nombreAccompagnant";

            ResultSet resultat = instruc.executeQuery(req);
            while (resultat.next()) {
                if(age)
                    System.out.println(resultat.getDouble(1) + "\t" + resultat.getInt(2) + "\t" + resultat.getInt(3) + "\t" + resultat.getInt(4));
                else
                    System.out.println(resultat.getDouble(1) + "\t" + resultat.getInt(2) + "\t" + resultat.getInt(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        ReponseSUM rep = new ReponseSUM(ReponseSUM.CONNECTION_OK);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }

    private void traiteAnova(Socket sock, ConsoleServeur cs) {
        System.out.println("reg anova 1");
        
        ReponseSUM rep = new ReponseSUM(ReponseSUM.CONNECTION_OK);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }

    private void traiteAnovaHf(Socket sock, ConsoleServeur cs) {
        System.out.println("reg anova 2");
        
        ReponseSUM rep = new ReponseSUM(ReponseSUM.CONNECTION_OK);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }

    private void RExpPrintSummary(REXP r, int row) {
        try {
            int size = r.length() / row;
            for(int i = 0; i < size; i++) {
                for(int j = 0; j < row; j++)
                    if(r.asStrings()[i + (j*size)] != null)
                        System.out.print("\t" + r.asStrings()[i + (j*size)]);
                System.out.println("");
            }
        }
        catch (REXPMismatchException e) {
            e.printStackTrace();
        }
    }

    private int type;
    private int mois;
    private String comp;
    private boolean age;
    private Socket socketClient;

    public RequeteSUM(int t) {
        type = t;
    }

    public RequeteSUM(int t, int m, String c) {
        type = t; mois = m; comp = c;
    }

    public RequeteSUM(int t, int m, String c, boolean a) {
        type = t; mois = m; comp = c; age = a;
    }
}
