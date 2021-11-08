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

    
/* Requête de Connexion */
    private void traiteConnexionRServe(Socket sock, ConsoleServeur cs) {
        try {
            if(rConn == null) {
                rConn = new RConnection("localhost");
                System.out.println("connexion réussie");
            }
            
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
            
            ReponseSUM rep = new ReponseSUM(ReponseSUM.CONNECTION_NOK);
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(rep); oos.flush();
                oos.close();
            }
            catch (IOException e) {
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
/* Requêtes Rserve */
    private void traiteRegCorr(Socket sock, ConsoleServeur cs) {
        System.out.println("reg corr 1");
        boolean ok = false;
        ReponseSUM rep;
        double pval = 0, Rsq = 0;
        
        try {
            String req  = "SELECT AVG(ba.poids), v.distance FROM bagages ba"+
            " INNER JOIN billets bi USING(idBillet)"+
            " INNER JOIN vols v USING(numVol)"+
            " INNER JOIN avions a USING(idAvion)"+
            " INNER JOIN compagnies c USING(idCompagnie)";

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

            /*
            var dataset = new HistogramDataset();
            dataset.addSeries("key", double[] tmp = new Double[0.0, 0.0], 50);
            JFreeChart histogram = ChartFactory.createHistogram("Normal distribution", "y values", "x values", dataset);
            ChartUtils.saveChartAsPNG(new File("\"" + GetDirectory.FileDir("reg1.jpeg") + "\""), histogram, 800, 700);
            */
/*
            rConn.voidEval("jpeg(file=\"" + GetDirectory.FileDir("reg1.jpeg") + "\",width=800, height=700)");
            rConn.voidEval("dev.off()");
            rConn.voidEval("dev.new()");
            rConn.voidEval("boxplot(reg)");
*/

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
            System.out.println("\n\tAdjusted R Squared : " + rExp.asString());
            Rsq = rExp.asDouble();

            rExp = rConn.eval("tmp$coefficients[8]");
            System.out.println("\n\tP-value : " + rExp.asString());
            pval = rExp.asDouble();
            
            ok = true;
        } 
        catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        }

        String conclusion = "";
        if(ok == true) {
            if( Rsq <= -0.5 || 0.5 <= Rsq)
                conclusion = pval <= 0.20 ? "Corrélation entre le poids et la distance" : "Pas de corrélation entre le poids et la distance";
            else
                conclusion = "Pas confiance aux régresseurs";
            
            rep = new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion);
        }
        else
            rep = new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion);

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
        boolean ok = false;
        ReponseSUM rep;
        double pval = 0, Rsq = 0;
        
        try {
            String req  = "SELECT AVG(ba.poids), v.distance, nombreAccompagnant, age FROM bagages ba"+
            " INNER JOIN billets bi USING(idBillet)"+
            " INNER JOIN vols v USING(numVol)"+
            " INNER JOIN avions a USING(idAvion)"+
            " INNER JOIN compagnies c USING(idCompagnie)"+
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
/*
            while (resultat.next()) {
                if(age)
                    System.out.println(resultat.getDouble(1) + "\t" + resultat.getInt(2) + "\t" + resultat.getInt(3) + "\t" + resultat.getInt(4));
                else
                    System.out.println(resultat.getDouble(1) + "\t" + resultat.getInt(2) + "\t" + resultat.getInt(3));
            }
*/
            resultat.next();
            String poids = resultat.getString(1);
            String distance = resultat.getString(2);
            String nbAccompReq = resultat.getString(3);
            String ageReq = resultat.getString(4);

            while(resultat.next()) {
                if(resultat.getString(1) != null) {
                    poids += ",";
                    poids += resultat.getString(1);
                }

                if(resultat.getString(2) != null) {
                    distance += ',';
                    distance += resultat.getString(2);
                }

                if(resultat.getString(3) != null) {
                    nbAccompReq += ',';
                    nbAccompReq += resultat.getString(3);
                }

                if(resultat.getString(4) != null) {
                    ageReq += ',';
                    ageReq += resultat.getString(4);
                }
            }

            req = "reg <- data.frame(poids = c("+ poids +"), distance = c("+ distance +")";
            
            if(nbAccomp)
                req += ", nbAccomp = c("+ nbAccompReq +")";
            if(age)
                req += ", age = c("+ ageReq +")";
            req += ")";
            rConn.voidEval(req);
            System.out.println("data reg corr plus créée");

/*
            rConn.voidEval("jpeg(file=\"" + GetDirectory.FileDir("reg2.jpeg") + "\",width=800, height=700)");
            rConn.voidEval("dev.off()");
            rConn.voidEval("dev.new()");
            rConn.voidEval("boxplot(reg)");
*/

            REXP rExp = rConn.eval("summ <- summary(reg)");
            int i = 2;
            
            System.out.print("\tPoids\t\tDistance");
            if(nbAccomp) {
                System.out.print("\t\tNb Accomp");
                i++;
            }
            if(age) {
                System.out.print("\t\t Age");
                i++;
            }
            System.out.print("\n");
            RExpPrintSummary(rExp, i);
            
            String reqmodel = "model <- lm(formula = reg$poids ~ reg$distance";
            if(nbAccomp)
                reqmodel += " + reg$nbAccomp";
            if(age)
                reqmodel += " + reg$age";
            reqmodel+=")";
            
            rConn.voidEval(reqmodel);

            rConn.voidEval("tmp <- summary(model)");
            rExp = rConn.eval("tmp$coefficients");
            System.out.println("\nfinal summary :");
            System.out.println("\tEstimate\t\tStd. Error\t\tt value\t\tPr(>|t|)");
            RExpPrintSummary(rExp, 4);

            rExp = rConn.eval("tmp$adj.r.squared");
            System.out.println("\tAdjusted R Squared : " + rExp.asString());
            Rsq = rExp.asDouble();

            rExp = rConn.eval("tmp$df");
            double[] degreLib = rExp.asDoubles();

            rExp = rConn.eval("tmp$fstatistic");
            double fStat = rExp.asDouble();

            rExp = rConn.eval("pf("+ fStat +", "+ degreLib[0] +", "+ degreLib[1] +", lower.tail = F)");
            System.out.println("\n\tP-value : " + rExp.asString());
            pval = rExp.asDouble();

            ok = true;            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (REXPMismatchException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        }

        String conclusion = "";
        if(ok == true) {
            if(Rsq <= -0.5 || 0.5 <= Rsq)
                conclusion = pval <= 0.20 ? "Corrélation entre le poids et la distance" : "Pas de corrélation entre le poids et la distance";
            else
                conclusion = "Pas confiance aux régresseurs";
            
            rep = new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion);
        }
        else
            rep = new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion);

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
        boolean ok = false;
        ReponseSUM rep;
        double pval = 0;
        
        try {
            String req  = "SELECT AVG(ba.poids), destination FROM bagages ba"+
            " INNER JOIN billets bi USING(idBillet)"+
            " INNER JOIN vols v USING(numVol)"+
            " INNER JOIN avions a USING(idAvion)"+
            " INNER JOIN compagnies c USING(idCompagnie)";

            if(mois != 0 && comp.equals("Toutes les compagnies") == false) {
                req += " WHERE nomCompagnie = '"+ comp +"'"+
                        " AND EXTRACT(MONTH FROM dateVol) = "+ mois;
            } else {
                if(comp.equals("Toutes les compagnies") == false)
                    req += " WHERE nomCompagnie = '"+ comp +"'";
                if(mois != 0)
                    req += " WHERE EXTRACT(MONTH FROM dateVol) = "+ mois;
            }
            req += " GROUP BY bi.idClient, v.destination";

            ResultSet resultat = instruc.executeQuery(req);
            
            resultat.next();
            String poids = resultat.getString(1);
            String destination = ("\"" + resultat.getString(2) + "\"");

            while(resultat.next()) {
                if(resultat.getString(1) != null) {
                    poids += ",";
                    poids += resultat.getString(1);
                }

                if(resultat.getString(2) != null) {
                    destination += ",";
                    destination += ("\"" + resultat.getString(2) + "\"");
                }
            }
            
            rConn.voidEval("anova <- data.frame(poids = c("+ poids +"), destination = c("+ destination +"))");
            System.out.println("data anova1 créée");
            
            REXP rExp = rConn.eval("summ <- summary(anova)");
            System.out.println("\tPoids\t\tDestination");
            RExpPrintSummary(rExp, 2);
            
            rConn.voidEval("model <- lm(anova$poids ~ anova$destination)");
            System.out.println("model créé");
            
            rConn.voidEval("tmp <- anova(model)");
// SHOW

            rExp = rConn.eval("tmp$Pr");
            System.out.println("\tP-value : " + rExp.asString());
            pval = rExp.asDouble();

            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (REXPMismatchException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String conclusion = "";
        if(ok == true) {
            conclusion = pval <= 0.20 ? "Lien entre le poids et la destination" : "Pas de lien entre le poids et la destination";
            rep = new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion);
        }
        else
            rep = new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion);
            
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
        boolean ok = false;
        ReponseSUM rep;
        double pval = 0;
        
        try {
            String req  = "SELECT AVG(ba.poids), destination, cl.sexe FROM bagages ba"+
            " INNER JOIN billets bi USING(idBillet)"+
            " INNER JOIN vols v USING(numVol)"+
            " INNER JOIN avions a USING(idAvion)"+
            " INNER JOIN compagnies c USING(idCompagnie)"+
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
            req += " GROUP BY bi.idClient, v.destination, cl.sexe";

            ResultSet resultat = instruc.executeQuery(req);
            
            resultat.next();
            String poids = resultat.getString(1);
            String destination = ("\"" + resultat.getString(2) + "\"");
            String sexe = ("\"" + resultat.getString(3) + "\"");

            while(resultat.next()) {
                if(resultat.getString(1) != null) {
                    poids += ",";
                    poids += resultat.getString(1);
                }

                if(resultat.getString(2) != null) {
                    destination += ",";
                    destination += ("\"" + resultat.getString(2) + "\"");
                }

                if(resultat.getString(3) != null) {
                    sexe += ",";
                    sexe += ("\"" + resultat.getString(3) + "\"");
                }
            }
            System.out.println("SEXE " + sexe);
            
            rConn.voidEval("anova <- data.frame(poids = c("+ poids +"), destination = c("+ destination +"), sexe = c("+ sexe +"))");
            System.out.println("data anova2 créée");
            
            REXP rExp = rConn.eval("summ <- summary(anova)");
            System.out.println("\tPoids\t\tDestination\t\tSexe");
            RExpPrintSummary(rExp, 3);
            
            rConn.voidEval("model <- lm(anova$poids ~ anova$destination + anova$sexe)");
            System.out.println("model créé");
            
            rExp = rConn.eval("tmp <- summary(model)");
            rExp = rConn.eval("tmp$coefficients");
            System.out.println("\nfinal summary :");
            System.out.println("\tEstimate\t\tStd. Error\t\tt value\t\tPr(>|t|)");
            RExpPrintSummary(rExp, 4);

            rExp = rConn.eval("tmp$df");
            double[] degreLib = rExp.asDoubles();

            rExp = rConn.eval("tmp$fstatistic");
            double fStat = rExp.asDouble();

            rExp = rConn.eval("pf("+ fStat +", "+ degreLib[0] +", "+ degreLib[1] +", lower.tail = F)");
            System.out.println("\n\tP-value : " + rExp.asString());
            pval = rExp.asDouble();

            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        }

        String conclusion = "";
        if(ok == true) {
            conclusion = pval <= 0.20 ? "Lien entre le poids et la destination" : "Pas de lien entre le poids et la destination";
            rep = new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion);
        }
        else
            rep = new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion);

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
    private boolean nbAccomp;
    //private Socket socketClient;

    public RequeteSUM(int t) {
        type = t;
    }

    public RequeteSUM(int t, int m, String c) {
        type = t; mois = m; comp = c;
    }

    public RequeteSUM(int t, int m, String c, boolean a, boolean n) {
        type = t; mois = m; comp = c; age = a; nbAccomp = n;
    }
}