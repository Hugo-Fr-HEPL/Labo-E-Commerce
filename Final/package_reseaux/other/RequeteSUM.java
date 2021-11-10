package package_reseaux.other;

import application.jFreeChart;
import application.Client;
import package_reseaux.Interface.*;

import java.io.*;
import java.net.Socket;
//import java.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    public static Properties prop = null;
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


/*
    Requête de Connexion
*/
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


/*
    Regression Correlation 1
*/
    private void traiteRegCorr(Socket sock, ConsoleServeur cs) {
        System.out.println("reg corr 1");
        boolean ok = false;
        double pval = 0, Rsq = 0;
        List<Double> lpoids = new ArrayList<Double>();
        List<Double> ldist = new ArrayList<Double>();
        List<Double> histDouble1 = new ArrayList<Double>();
        List<Double> histDouble2 = new ArrayList<Double>();
        List<String> histString = new ArrayList<String>();
        int numgraph=0;
        
        try {
            ResultSet resultat = instruc.executeQuery(Request(1));

            resultat.next();
            String poids = resultat.getString(1);
            String distance = resultat.getString(2);

            //pour graphique
            lpoids.add(resultat.getDouble(1));
            ldist.add(resultat.getDouble(2));
            int cpt = 1;

            while(resultat.next()) {
                if(resultat.getString(1) != null) {
                    poids += ",";
                    poids += resultat.getString(1);
                }

                if(resultat.getString(2) != null) {
                    distance += ',';
                    distance += resultat.getString(2);
                }

                //pour graphique
                lpoids.add(resultat.getDouble(1));
                ldist.add(resultat.getDouble(2));
                cpt++;
            }
            
            if(mois==0)
            {
                numgraph=1;
                
                resultat = instruc.executeQuery("SELECT EXTRACT(YEAR FROM dateVol), distance FROM vols");
                ldist = new ArrayList<Double>();
                List<String> lyear = new ArrayList<String>();
                cpt = 0;
                while(resultat.next()) {
                    ldist.add(resultat.getDouble(2));
                    lyear.add(resultat.getString(1));
                    cpt++;
                }

                //tri par année
                Double tmp;
                int k=0;
                List<Double> histdist = new ArrayList<Double>();
                List<String> histyear = new ArrayList<String>();
                for(int i = 0, j; i < cpt - 1; i++) {
                    for(j = 0; j < histyear.size(); j++) {
                        if(lyear.get(i).equals(histyear.get(j)))
                        {
                            tmp=histdist.get(j);
                            tmp+=ldist.get(i);
                            histdist.set(j,tmp);
                            break;
                        }     
                    }
                    if(j == histyear.size()) {
                        histdist.add(ldist.get(i));
                        histyear.add(lyear.get(i));
                        k++;
                    }
                }
                
            }
            else
            {   
                numgraph=2;

                int moisSuivant;
                if(mois!=12)
                    moisSuivant=mois+1;
                else
                    moisSuivant=1;

                resultat = instruc.executeQuery("SELECT EXTRACT(YEAR FROM dateVol), EXTRACT(MONTH FROM dateVol), distance FROM vols WHERE EXTRACT(MONTH FROM dateVol) = " + mois + " OR EXTRACT(MONTH FROM dateVol) = " + moisSuivant);
                
                ldist = new ArrayList<Double>();
                ArrayList<String> lmois = new ArrayList<String>();
                ArrayList<String> histyear = new ArrayList<String>();
                cpt = 0;

                while(resultat.next()) {
                    ldist.add(resultat.getDouble(3));
                    lmois.add(resultat.getString(2));
                    histyear.add(resultat.getString(1));
                    cpt++;
                }

                //tri par année
                Double tmp;
                int k=0;
                
                for(int i = 0, j; i < cpt - 1; i++) {
                    for(j = 0; j < histString.size(); j++) {
                        if(histyear.get(i).equals(histString.get(j)))
                        {
                            if(lmois.get(i).equals(String.valueOf(mois)))
                            {
                                tmp=histDouble1.get(j);
                                tmp+=ldist.get(i);
                                histDouble1.set(j,tmp);
                                break;
                            }
                            else
                            {
                                tmp=histDouble2.get(j);
                                tmp+=ldist.get(i);
                                histDouble2.set(j,tmp);
                                break;
                            }
                        }     
                    }
                    if(j == histString.size()) {
                        if(lmois.get(i).equals(String.valueOf(mois)))
                        {
                            histDouble1.add(ldist.get(i));
                            histDouble2.add(0.0);
                        }
                        else
                        {
                            histDouble2.add(ldist.get(i));
                            histDouble1.add(0.0);
                        }
                        histString.add(histyear.get(i));
                        k++;
                    }
                }
            }

            rConn.voidEval("reg <- data.frame(poids = c("+ poids +"), distance = c("+ distance +"))");
            System.out.println("data reg corr créée");

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
            if(Rsq <= -0.5 || 0.5 <= Rsq)
                conclusion = pval <= 0.20 ? "Corrélation entre le poids et la distance" : "Pas de corrélation entre le poids et la distance";
            else
                conclusion = "Pas confiance aux régresseurs";
            
            if(numgraph==1)
                SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion, ReponseSUM.HISTOGRAM, lpoids, ldist, histDouble1, histDouble2, histString));
            else
                SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion, ReponseSUM.HISTOGRAMCOM, lpoids, ldist, histDouble1, histDouble2, histString));
        } else
            SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion));
    }

/*
    Regression Correlation 2
*/
    private void traiteRegCorrPlus(Socket sock, ConsoleServeur cs) {
        System.out.println("reg corr 2");
        boolean ok = false;
        double pval = 0, Rsq = 0;
        
        try {
            ResultSet resultat = instruc.executeQuery(Request(2));

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

            String req = "reg <- data.frame(poids = c("+ poids +"), distance = c("+ distance +")";
            
            if(nbAccomp)
                req += ", nbAccomp = c("+ nbAccompReq +")";
            if(age)
                req += ", age = c("+ ageReq +")";
            req += ")";
            rConn.voidEval(req);
            System.out.println("data reg corr plus créée");

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
            SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion));
        } else
            SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion));
    }


/*
    Anova 1
*/
    private void traiteAnova(Socket sock, ConsoleServeur cs) {
        System.out.println("anova 1");
        boolean ok = false;
        double pval = 0;
        
        List<List> lpoidsfin = new ArrayList<List>();
        List<String> ldestfin = new ArrayList<String>();
        
        try {
            ResultSet resultat = instruc.executeQuery(Request(3));

            List<Double> lpoids = new ArrayList<Double>();
            List<String> ldest = new ArrayList<String>();

            resultat.next();
            String poids = resultat.getString(1);
            String destination = ("\"" + resultat.getString(2) + "\"");
            int cpt = 1, k=0;
            
            while(resultat.next()) {
                if(resultat.getString(1) != null) {
                    poids += ",";
                    poids += resultat.getString(1);
                    lpoids.add(resultat.getDouble(1));
                }

                if(resultat.getString(2) != null) {
                    destination += ",";
                    destination += ("\"" + resultat.getString(2) + "\"");
                    ldest.add(resultat.getString(2));
                }
                cpt++;
            }
            
            for(int i = 0, j; i < cpt - 1; i++) {
                for(j = 0; j < ldestfin.size(); j++) {
                    if(ldest.get(i).equals(ldestfin.get(j)))
                    {
                        lpoidsfin.get(j).add(lpoids.get(i));
                        break;
                    }
                }
                if(j == ldestfin.size()) {
                    List tmp2 = new ArrayList<Double>();
                    tmp2.add(lpoids.get(i));
                    lpoidsfin.add(tmp2);
                    
                    ldestfin.add(ldest.get(i));
                    k++;
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
            SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion, ReponseSUM.BOXPLOT_SECTORIEL, ldestfin, lpoidsfin));
        } else
            SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion));
    }


/*
    Anova 2
*/
    private void traiteAnovaHf(Socket sock, ConsoleServeur cs) {
        System.out.println("reg anova 2");
        boolean ok = false;
        double pval = 0;
        
        try {
            ResultSet resultat = instruc.executeQuery(Request(4));
            
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
            SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_OK, conclusion));
        } else
            SendAnswer(sock, new ReponseSUM(ReponseSUM.STATISTIC_NOK, conclusion));
    }

    private String Request(int i) {
        String req  = "SELECT AVG(ba.poids)";
        if(i <= 2) {
            req += ", v.distance";
            if(i == 1)
                req += ", EXTRACT(YEAR FROM dateVol)";
            else
                req += ", nombreAccompagnant, age";
        } else {
            req += ", v.destination";
            if(i == 4) req += ", cl.sexe";
        }
        req += " FROM bagages ba"+
        " INNER JOIN billets bi USING(idBillet)"+
        " INNER JOIN vols v USING(numVol)"+
        " INNER JOIN avions a USING(idAvion)"+
        " INNER JOIN compagnies c USING(idCompagnie)";
        if(i == 2 || i == 4)
            req += " INNER JOIN clients cl USING(idclient)";

        if(mois != 0 && comp.equals("Toutes les compagnies") == false) {
            req += " WHERE nomCompagnie = '"+ comp +"'"+
                    " AND EXTRACT(MONTH FROM dateVol) = "+ mois;
        } else {
            if(comp.equals("Toutes les compagnies") == false)
                req += " WHERE nomCompagnie = '"+ comp +"'";
            if(mois != 0)
                req += " WHERE EXTRACT(MONTH FROM dateVol) = "+ mois;
        }
        req += " GROUP BY bi.idClient";
        if(i <= 2) {
            req += ", v.distance";
            if(i == 1)
                req += ", EXTRACT(YEAR FROM dateVol)";
            else
                req += ", nombreAccompagnant";
        } else {
            req += ", v.destination";
            if(i == 4) req += ", cl.sexe";
        }

        return req;
    }

    private void SendAnswer(Socket sock, ReponseSUM rep) {
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