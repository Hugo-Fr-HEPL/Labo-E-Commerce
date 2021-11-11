package datamining.android_decideurs;

import java.io.*;



public class ReponseSUM implements Serializable {
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


    public ReponseSUM(int c, String chu) {
        codeRetour = c; chargeUtile = chu;
    }
    public ReponseSUM(int c) {
        codeRetour = c; chargeUtile = "";
    }

    public int getCode() { return codeRetour; }
    public String getChargeUtile() { return chargeUtile; }
}
