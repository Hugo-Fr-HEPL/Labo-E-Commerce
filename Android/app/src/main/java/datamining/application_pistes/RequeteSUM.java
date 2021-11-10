package datamining.application_pistes;

import java.io.*;
import java.net.Socket;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;



public class RequeteSUM implements Serializable {
    public static int CONNEXION_RSERVE = 1;
    public static int REG_CORR_LUG = 2;
    public static int REG_CORR_LUG_PLUS = 3;
    public static int ANOVA_1_LUG = 4;
    public static int ANOVA_2_LUG_HF = 5;
    public static int CONNEXION_ANDROID = 6;

    public static Connection con = null;
    public static Statement instruc = null;
    public static Properties prop;

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