package datamining.application_pistes;

import java.io.*;



public class ReponseSUM implements Serializable {
    public static int CONNECTION_OK = 201;
    public static int CONNECTION_NOK = 202;
    public static int STATISTIC_OK = 301;
    public static int STATISTIC_NOK = 302;

    private int codeRetour;
    private String chargeUtile;

    public ReponseSUM(int c, String chu) {
        codeRetour = c; setChargeUtile(chu);
    }
    public ReponseSUM(int c) {
        codeRetour = c; setChargeUtile("");
    }

    public int getCode() { return codeRetour; }
    public String getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(String chargeU) { chargeUtile = chargeU; }
}