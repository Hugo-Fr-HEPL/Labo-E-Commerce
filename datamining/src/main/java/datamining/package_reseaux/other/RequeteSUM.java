package datamining.package_reseaux.other;

import datamining.package_reseaux.Interface.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class RequeteSUM implements Requete, Serializable
{
    public static int REQUEST_E_MAIL = 1;
    public static int CONNEXION_RSERVE = 2;
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

    private int type;
    private String chargeUtile;
    private Socket socketClient;

    public RequeteSUM(int t, String chu)
    {
        type = t; setChargeUtile(chu);
    }

    public RequeteSUM(int t, String chu, Socket s)
    {
        type = t; setChargeUtile(chu); socketClient = s;
    }

    public Runnable createRunnable (final Socket s, final ConsoleServeur cs)
    {
        if (type==REQUEST_E_MAIL)
            return new Runnable()
            {
                public void run()
                {
                    traiteRequeteEMail(s, cs);
                }
            };
        else if (type==CONNEXION_RSERVE)
        return new Runnable()
        {
            public void run()
            {
                traiteConnexionRServe(s, cs);
            }
        };
        else return null;
    }

    private void traiteRequeteEMail(Socket sock, ConsoleServeur cs)
    {
    // Affichage des informations
            String adresseDistante = sock.getRemoteSocketAddress().toString();
            System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
    // la charge utile est le nom du client
            String eMail = (String)tableMails.get(getChargeUtile());
            if (eMail != null)
                System.out.println("E-Mail trouvé pour " + getChargeUtile());
            else
            {
                System.out.println("E-Mailnon trouvé pour " + getChargeUtile() + " : " + eMail);
                eMail="?@?";
            }
    // Construction d'une réponse
            ReponseSUM rep = new ReponseSUM(ReponseSUM.EMAIL_OK, getChargeUtile() + " : " + eMail);
            ObjectOutputStream oos;
            try
            {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(rep); oos.flush();
                oos.close();
            }
            catch (IOException e)
            {
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }
    }

    RConnection rConn = null;
    REXP x = null;
    private void traiteConnexionRServe(Socket sock, ConsoleServeur cs)
    {
        try {
            rConn = new RConnection("localhost");
            System.out.println("connexion réussie");
        } 
        catch (RserveException ex) {
            Logger.getLogger(RequeteSUM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String GetNomFichier(String nomf) {
        return System.getProperty("user.dir") + System.getProperty("file.separator") + "Files" + System.getProperty("file.separator") + nomf;
    }

    public String getChargeUtile() { return chargeUtile; }

    public void setChargeUtile(String chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }
}
