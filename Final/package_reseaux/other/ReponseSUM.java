package package_reseaux.other;

import java.io.*;
import java.util.*;

import package_reseaux.Interface.Reponse;

public class ReponseSUM implements Reponse, Serializable {
    public static int CONNECTION_OK = 201;
    public static int CONNECTION_NOK = 202;
    public static int STATISTIC_OK = 301;
    public static int STATISTIC_NOK = 302;

    public static int HISTOGRAM = 1;
    public static int HISTOGRAMCOM = 2;
    public static int BOXPLOT_SECTORIEL = 3;

    private int codeRetour;
    private String chargeUtile;
    private int codegraph;
    private List donnees1;
    private List donnees2;
    private List donnees3;
    private List donnees4;
    private List donnees5;

    public ReponseSUM(int c, String chu) {
        codeRetour = c; setChargeUtile(chu);
    }
    public ReponseSUM(int c) {
        codeRetour = c; setChargeUtile("");
    }
    public ReponseSUM(int c, String chu, int co, List ar1, List ar2) {
        codeRetour = c; setChargeUtile(chu); codegraph=co; setdonnees1(ar1); setdonnees2(ar2);
    }
    public ReponseSUM(int c, String chu, int co, List ar1, List ar2, List ar3, List ar4, List ar5) {
        codeRetour = c; setChargeUtile(chu); codegraph=co; setdonnees1(ar1); setdonnees2(ar2); setdonnees3(ar3); setdonnees4(ar4); setdonnees5(ar5);
    }

    public int getCode() { return codeRetour; }
    public String getChargeUtile() { return chargeUtile; }
    public List getdonnees1() { return donnees1; }
    public List getdonnees2() { return donnees2; }
    public List getdonnees3() { return donnees3; }
    public List getdonnees4() { return donnees4; }
    public List getdonnees5() { return donnees5; }
    public int getcodegraph() { return codegraph; }
    
    public void setChargeUtile(String chargeU) { chargeUtile = chargeU; }
    public void setdonnees1(List d) { donnees1 = d; }
    public void setdonnees2(List d) { donnees2 = d; }
    public void setdonnees3(List d) { donnees3 = d; }
    public void setdonnees4(List d) { donnees4 = d; }
    public void setdonnees5(List d) { donnees5 = d; }
    
}
