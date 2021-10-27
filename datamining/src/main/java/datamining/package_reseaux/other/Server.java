package datamining.package_reseaux.other;

import java.io.*;
import java.util.Properties;

public class Server {
    public static void main(String[] args)
    {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(GetNomFichier("properties.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int port = Integer.parseInt(prop.getProperty("Port"));

        ThreadServeur ts = new ThreadServeur(port, new ListeTaches());
        ts.start();
    }

    public static String GetNomFichier(String nomf)
    {
        String chemin = System.getProperty("user.dir")+ System.getProperty("file.separator") + "Files" + System.getProperty("file.separator") + nomf;

        return chemin;
    }
}
