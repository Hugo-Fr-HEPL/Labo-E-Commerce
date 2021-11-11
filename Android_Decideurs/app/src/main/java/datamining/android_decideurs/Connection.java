package datamining.android_decideurs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection extends Thread {
    private Socket sock = null;
    //private ObjectOutputStream dos = null;
    private ObjectInputStream dis = null;

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

/*
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
            sock = new Socket("192.168.0.58", 50000);
            //dos = new ObjectOutputStream(sock.getOutputStream());
            dis = new ObjectInputStream(sock.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() { return sock; }
    //public ObjectOutputStream getDos() { return dos; }
    //public ObjectInputStream getDis() { return dis; }

    public void SendMsg(String msg) {
        try {
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            dos.writeUTF(msg); dos.flush();
        } catch (IOException e) {
            System.err.println("Error network ? [" + e.getMessage() + "]");
        }
    }

    public String[] GetMsg() {
        byte b;
        String[] msg = new String[10];
        try {
            DataInputStream dis = new DataInputStream(sock.getInputStream());

            int i = 0;
            msg[i] = "";
            while((b = dis.readByte()) != (byte)'$') {
                if(b != '$' && b != '#')
                    msg[i] += (char)b;
                if(b == '#') {
                    i++;
                    msg[i] = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Recu " + msg[0] + " - "+ msg[1]);
        return msg;
    }
}