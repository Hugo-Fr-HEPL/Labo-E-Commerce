package datamining.application_pistes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection extends Thread {
    private Socket sock = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    @Override
    public void run() {
/*
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(GetDirectory.FileDir("properties.txt")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Connection con = MySQL.MySQL_Connexion("bd_airport", (String)prop.get("DB_port"), "localhost", (String)prop.get("DB"), (String)prop.get("DB_pwd"));
            Statement instruc = con.createStatement();

            FillUI(con, instruc);
        }
        catch (SQLException e) {
            System.out.println("Erreur JDBC-OBCD : " + e.getMessage() + " ** " + e.getSQLState() + "--\n\n");
        }
*/
        try {
            sock = new Socket("192.168.1.98", 50000);
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() { return sock; }
    public ObjectOutputStream getOos() { return oos; }
    public ObjectInputStream getOis() { return ois; }
}