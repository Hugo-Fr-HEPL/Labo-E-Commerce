package datamining.package_reseaux;

import java.net.*;

public interface Requete {
    public Runnable createRunnable(Socket s, ConsoleServeur cs);
}
