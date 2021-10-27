package datamining.package_reseaux;

import java.util.*;

public class ListeTaches implements SourceTaches
{
    private LinkedList<Runnable> listTaches;

    public ListeTaches()
    {
        listTaches = new LinkedList<Runnable>();
    }

    public synchronized Runnable getTache() throws  InterruptedException
    {
        System.out.println("attente d'une tache");
        while(!existTaches())wait();
        return (Runnable)listTaches.remove();
    }

    public synchronized boolean existTaches()
    {
        return !listTaches.isEmpty();
    }

    public synchronized void recordTache(Runnable r)
    {
        listTaches.addLast(r);
        System.out.println("Tache dans la file");
        notify();
    }
}
