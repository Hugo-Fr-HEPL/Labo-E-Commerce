package datamining.package_reseaux.other;

import datamining.package_reseaux.Interface.*;

import java.io.*;
import java.net.Socket;
//import java.util.*;

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
/*
    public static Hashtable<String, String> tableMails = new Hashtable<String, String>();

    static
    {
        tableMails.put("Vilvens", "claude.vilvens@prov-liege.be");
        tableMails.put("Charlet", "christophe.charlet@prov-liege.be");
        tableMails.put("Madani", "mounawar.madani@prov-liege.be");
        tableMails.put("Wagner", "jean-marc.wagner@prov-liege.be");
    }
    public static Hashtable<String, String> tablePwdNoms = new Hashtable<String, String>();

    static
    {
        tablePwdNoms.put("GrosZZ", "Vilvens");
        tablePwdNoms.put("GrosRouteur", "Charlet");
        tablePwdNoms.put("GrosseVoiture", "Madani");
        tablePwdNoms.put("GrosCerveau", "Wagner");
    }
*/

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
                    //traiteRegCorrPlus(s, cs);
                }
            };
        }
        else if (type == ANOVA_1_LUG) {
            return new Runnable() {
                public void run() {
                    //traiteAnova(s, cs);
                }
            };
        }
        else if (type == ANOVA_2_LUG_HF) {
            return new Runnable() {
                public void run() {
                    //traiteAnovaHf(s, cs);
                }
            };
        }
        else
            return null;
    }

    RConnection rConn = null;
    REXP x = null;
    private void traiteConnexionRServe(Socket sock, ConsoleServeur cs) {
        try {
            rConn = new RConnection("localhost");
            System.out.println("connexion réussie");
        }
        catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void traiteRegCorr(Socket sock, ConsoleServeur cs) {
        try {
            rConn.voidEval("demenagements <-read.table (\"" + GetDirectory.FileDir("demenagements.csv") + "\", sep=\";\", header = TRUE)");
            System.out.println("data demenagements récupérées");
            
            rConn.voidEval("jpeg(file=\"" + GetDirectory.FileDir("demenagements.jpeg") + "\",width=800, height=700)");
            rConn.voidEval("dev.off()");
            rConn.voidEval("dev.new()");

            rConn.voidEval("boxplot(demenagements)");

            REXP rExp = rConn.eval("summary(demenagements)");
            System.out.println("summary :");
            System.out.println("\tTemps\t\tVolume\t\t\tNombre de grandes pieces");
            RExpPrintSummary(rExp, 3);

            rConn.voidEval("model<-lm(formula = demenagements$Temps ~ demenagements$Volume + demenagements$Nombre.de.grandes.pieces)");

            rConn.voidEval("tmp <- summary(model)");
            rExp = rConn.eval("tmp$coefficients");
            System.out.println("final summary :");
            System.out.println("\tEstimate\t\tStd. Error\t\tt value\t\tPr(>|t|)");
            RExpPrintSummary(rExp, 4);

            rExp = rConn.eval("tmp$adj.r.squared");
            System.out.println("\tAdjusted R Squared : " + rExp.asString());

// p-value to do
            rExp = rConn.eval("tmp$");
            System.out.println("\tp-value : " + "< " + rExp.asString());
        } 
        catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (REXPMismatchException e) {
            e.printStackTrace();
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



/*
    private void traiteRequeteEMail(Socket sock, ConsoleServeur cs) {
    // Affichage des informations
            String adresseDistante = sock.getRemoteSocketAddress().toString();
            System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
    // la charge utile est le nom du client
            String eMail = (String)tableMails.get(getChargeUtile());
            if (eMail != null)
                System.out.println("E-Mail trouvé pour " + getChargeUtile());
            else {
                System.out.println("E-Mailnon trouvé pour " + getChargeUtile() + " : " + eMail);
                eMail="?@?";
            }
    // Construction d'une réponse
            ReponseSUM rep = new ReponseSUM(ReponseSUM.EMAIL_OK, getChargeUtile() + " : " + eMail);
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(rep); oos.flush();
                oos.close();
            }
            catch (IOException e) {
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }
    }
*/

    private int type;
    private String mois;
    private String comp;
    private Socket socketClient;

    public RequeteSUM(int t) {
        type = t;
    }

    public RequeteSUM(int t, String m, String c) {
        type = t; mois = m; comp = c;
    }
}
