package datamining.package_reseaux;

import java.io.*;
import java.net.*;

public class ThreadServeur extends Thread
{
    int port;
    ServerSocket SSocket = null;
    Socket CSocket = null;
    Requete req;

    private SourceTaches tachesAExecuter;
    private ConsoleServeur guiApplication;

    public ThreadServeur(int p, SourceTaches st)
    {
        port = p;
        tachesAExecuter = st;
    }

    public void run()
    {
        try {
            SSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Demarrage du pool de threads");

        for (int i = 0; i < 3; i++) {
            ThreadClient thr = new ThreadClient(tachesAExecuter, "Thread du pool n° " + i);
            thr.start();
        }

        while (!isInterrupted())
        {
            System.out.println("Serveur en attente");
            try {
                CSocket = SSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Le serveur est connecté");
            try {
                ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());

                //Reçus
                req = (Requete) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Runnable travail = req.createRunnable(CSocket, guiApplication);
            if (travail != null) {
                tachesAExecuter.recordTache(travail);
                System.out.println("Travail mis dans la file");
            }
        }
    }
}
