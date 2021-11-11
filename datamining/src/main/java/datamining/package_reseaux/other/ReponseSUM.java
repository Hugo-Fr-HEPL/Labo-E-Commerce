package datamining.package_reseaux.other;

import datamining.package_reseaux.Interface.Reponse;

import java.io.*;
import java.util.*;



public class ReponseSUM implements Reponse, Serializable {
    public static int CONNECTION_OK = 201;
    public static int CONNECTION_NOK = 202;
    public static int STATISTIC_OK = 301;
    public static int STATISTIC_NOK = 302;
    public static int ANDROID_OK = 401;

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


    private Double[] poidsHist;
    private Double[] distance;
    private Double[] histDou1;
    private Double[] histDou2;
    private String[] histStr;

    private List<List> poidsAnov;
    private String[] destination;


    public ReponseSUM(int c) {
        codeRetour = c; chargeUtile = "";
    }
    public ReponseSUM(int c, String chu) {
        codeRetour = c; chargeUtile = chu;
    }


    public ReponseSUM(int c, String chu, int co, String[] dest, List<List> poi) {
        codeRetour = c; chargeUtile = chu; codegraph=co;
        poidsAnov = poi; destination = dest;
    }
    public ReponseSUM(int c, String chu, int co, Double[] poi, Double[] dist, Double[] hD1, Double[] hD2, String[] hS) {
        codeRetour = c; chargeUtile = chu; codegraph=co;
        poidsHist = poi; distance = dist; histDou1 = hD1; histDou2 = hD2; histStr = hS;
    }


    public ReponseSUM(int c, String chu, int co, List ar1, List ar2) {
        codeRetour = c; chargeUtile = chu; codegraph=co; donnees1 = ar1; donnees2 = ar2;
    }
    public ReponseSUM(int c, String chu, int co, List ar1, List ar2, List ar3, List ar4, List ar5) {
        codeRetour = c; chargeUtile = chu; codegraph=co; donnees1 = ar1; donnees2 = ar2; donnees3 = ar3; donnees4 = ar4; donnees5 = ar5;
    }


    public Double[] GetPoidsHist() { return poidsHist; }
    public Double[] GetDistance() { return distance; }
    public Double[] GetHistDou1() { return histDou1; }
    public Double[] GetHistDou2() { return histDou2; }
    public String[] GetHistStr() { return histStr; }

    public List<List> GetPoidsAnov() { return poidsAnov; }
    public String[] GetDestination() { return destination; }


    public int getCode() { return codeRetour; }
    public String getChargeUtile() { return chargeUtile; }
    public List getdonnees1() { return donnees1; }
    public List getdonnees2() { return donnees2; }
    public List getdonnees3() { return donnees3; }
    public List getdonnees4() { return donnees4; }
    public List getdonnees5() { return donnees5; }
    public int getcodegraph() { return codegraph; }
}
